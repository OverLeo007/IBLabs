import os
import time
import json
import random as ra
from math import sqrt
import cProfile
from typing import Union

from gmpy2 import powmod
from rsa.utils import Testing, Utils


def timing(func):
    """
    Декоратор, измеряющий время выполнения функции и выводящий результаты.

    :param func: Целевая функция, время выполнения которой измеряется.
    :return: Обёртка вокруг целевой функции, включающая измерение времени выполнения.
    """

    def wrapper(*args, **kwargs):
        """
        Обёртка вокруг целевой функции для измерения времени выполнения.

        :param args: Позиционные аргументы целевой функции.
        :param kwargs: Именованные аргументы целевой функции.
        :return: Результат выполнения целевой функции.
        """

        start_time = time.time()
        result = func(*args, **kwargs)
        end_time = time.time()
        execution_time = end_time - start_time
        print(f"Функция {func.__name__} выполнилась за {execution_time:.4f} секунд")
        if args:
            print(f"{args=}")
        return result

    return wrapper


def profile(sort_by='tottime'):
    """
    Декоратор, использующий cProfile для профилирования выполнения функции
    и вывода статистики.

    :param sort_by: Критерий сортировки статистики. По умолчанию 'tottime'.
    :return: Декоратор для использования вокруг целевой функции.
    """

    def decorator(func):
        def wrapper(*args, **kwargs):
            profiler = cProfile.Profile()
            profiler.enable()

            result = func(*args, **kwargs)

            profiler.disable()
            profiler.print_stats(sort=sort_by)

            return result

        return wrapper

    return decorator


class RSA:
    """
    Класс реализующий алгоритм RSA для шифрования и дешифрования данных.
    """
    file_folder = "files\\"
    key_folder = file_folder + "keys\\"
    enc_folder = file_folder + "encrypted\\"
    dec_folder = file_folder + "decrypted\\"

    def __init__(self, n_len: int = 32, tests_k: int = 10) -> None:
        """
        Инициализация объекта RSA с заданными параметрами.

        :param n_len: Длина числа N в битах.
        :param tests_k: Количество тестов для проверки простоты чисел.
        """
        self.N, self.s, self.e = self.calc_RSA_params(n_len, tests_k)
        self.n_byte_len = (len(format(self.N, 'b'))) // 8 - 1
        RSA.create_directories()

    @staticmethod
    def create_directories() -> None:
        """
        Создание необходимых директорий для хранения файлов.

        :return: None
        """
        if not os.path.exists(RSA.file_folder):
            os.makedirs(RSA.file_folder)
        if not os.path.exists(RSA.key_folder):
            os.makedirs(RSA.key_folder)
        if not os.path.exists(RSA.enc_folder):
            os.makedirs(RSA.enc_folder)
        if not os.path.exists(RSA.dec_folder):
            os.makedirs(RSA.dec_folder)

    @staticmethod
    def calc_PQ_powers(n_len) -> tuple[tuple, ...]:
        """
        Метод вычисления оптимальных степеней для границ для генерации простых чисел.

        :param n_len: Длина числа N, где N = P * Q
        :return: Кортеж с двумя кортежами степеней -
        левая и правая границы степеней для P и Q.
        """
        middle_third_range = list(range(lb := n_len // 3, n_len - lb))
        variants = []
        count = 0
        for i in range(1, n_len):
            for j in range(n_len, 1, -1):
                if (
                        j + i + 2 == n_len or j + i == n_len
                ) and i in middle_third_range and j in middle_third_range and j > i:
                    if count == 2:
                        count = 0
                    if count == 0:
                        variants.append([])
                    variants[-1].append([i, j])
                    count += 1
        for variant in variants:
            variant[0][-1], variant[1][0] = variant[1][0], variant[0][-1]

        optimal = max(
            list(map(lambda var: tuple(map(tuple, var)), variants)),
            key=lambda pair: sqrt(
                (pair[0][0] - pair[1][0]) ** 2 + (pair[0][1] - pair[1][1]) ** 2
            )
        )
        return optimal

    @staticmethod
    def calc_bounded_prime(lb: int, rb: int, tests_k: int) -> int:
        """
        Метод вычисления псевдопростого числа в заданных границах
         с использованием тестов простоты.

        :param lb: Нижняя граница.
        :param rb: Верхняя граница.
        :param tests_k: Количество тестов простоты.
        :return: Простое число в заданных границах.
        """
        while True:
            num = ra.randint(lb, rb)
            bpsw_result = Testing.bpsw(num, tests_k)
            fermat_result = Testing.fermat(num, tests_k)
            if bpsw_result and fermat_result:
                return num

    @staticmethod
    def calc_RSA_params(n_len: int, tests_k: int) -> tuple[int, int, int]:
        """
       Метод вычисления параметров RSA, таких как N, s, e.

       :param n_len: Длина числа N в битах.
       :param tests_k: Количество тестов простоты.
       :return: Кортеж с параметрами N, s, e.
        """
        (p_lbound, p_rbound), (q_lbound, q_rbound) = RSA.calc_PQ_powers(n_len)
        while True:
            p = RSA.calc_bounded_prime(10 ** p_lbound, 10 ** p_rbound, tests_k)
            q = RSA.calc_bounded_prime(10 ** q_lbound, 10 ** q_rbound, tests_k)
            n = p * q
            if p != q and 10 ** (n_len - 1) <= n < 10 ** n_len:
                d = (p - 1) * (q - 1)
                break

        while True:
            s = ra.randint(2, d)
            if Utils.gcd(s, d) == 1:
                break

        e = Utils.mod_inverse(s, d)
        return n, s, e

    @staticmethod
    def to_bytes(int_val: int, byte_len: int) -> bytes:
        """
        Преобразование целого числа в байты.

        :param int_val: Целое число.
        :param byte_len: Длина в байтах.
        :return: Байты, представляющие целое число.
        """
        return int_val.to_bytes(byte_len, byteorder="big")

    @staticmethod
    def from_bytes(byte_val: bytes) -> int:
        """
        Преобразование байтов в целое число.

        :param byte_val: Байты для преобразования.
        :return: Целое число.
        """
        return int.from_bytes(byte_val, byteorder="big")

    @staticmethod
    def get_blocks_from_seq(data: bytes, header_size: int) -> list[bytes]:
        """
        Получение блоков из последовательности байтов.

        :param data: Последовательность байтов.
        :param header_size: Размер заголовка с информацией о блоках.
        :return: Список блоков.
        """
        block_lengths = RSA.from_bytes(data[:header_size])
        remaining_data = data[header_size:]
        blocks = [
            remaining_data[i:i + block_lengths]
            for i in range(0, len(remaining_data), block_lengths)
        ]

        return blocks

    @staticmethod
    def remove_last_bytes(data: bytes) -> bytes:
        """
        Удаление лишних байтов из данных.

        :param data: Данные для обработки.
        :return: Данные без лишних байтов.
        """
        return data[:-data[-1]]

    def encrypt(self, _input: Union[bytes, str]) -> bytes:
        """
        Шифрование данных.

        :param _input: Входные данные для шифрования (байты или путь к файлу).
        :return: Зашифрованные данные.
        """
        if isinstance(_input, bytes):
            return self._encrypt(_input)
        elif isinstance(_input, str) and '.' in _input:
            with open(_input, "rb") as file:
                inp_bytes = file.read()
            return self._encrypt(inp_bytes)
        else:
            raise TypeError("Unknown input type")

    def _encrypt(self, inp_bytes: bytes) -> bytes:
        """
        Внутренний метод для шифрования данных.

        :param inp_bytes: Входные данные в виде байтов.
        :return: Зашифрованные данные.
        """
        padding_len = self.n_byte_len - (
                len(inp_bytes) % self.n_byte_len)
        padded_inp = inp_bytes + bytes([padding_len]) * padding_len

        blocks = [
            int(powmod(
                self.from_bytes(padded_inp[i:i + self.n_byte_len]),
                self.s,
                self.N
            ))
            for i in range(0, len(padded_inp), self.n_byte_len)
        ]

        block_byte_len = (len(format(max(blocks), 'b'))) // 8 + 1
        return b''.join(
            [self.to_bytes(block_byte_len, 4)] + list(
                map(
                    lambda x: self.to_bytes(x, block_byte_len),
                    blocks
                )
            ))

    def decrypt(self, _input: Union[bytes, str]) -> bytes:
        """
        Дешифрование данных.

        :param _input: Зашифрованные данные (байты или путь к файлу).
        :return: Расшифрованные данные.
        """
        if isinstance(_input, bytes):
            return self._decrypt(_input)
        elif isinstance(_input, str) and '.' in _input:
            with open(_input, "rb") as file:
                inp_bytes = file.read()
            return self._decrypt(inp_bytes)
        else:
            raise TypeError("Unknown input type")

    def _decrypt(self, enc_seq: bytes) -> bytes:
        """
        Внутренний метод для дешифрования данных.

        :param enc_seq: Зашифрованные данные.
        :return: Расшифрованные данные.
        """
        enc_blocks = self.get_blocks_from_seq(enc_seq, 4)
        res = []
        for block in enc_blocks:
            dec_block = self.to_bytes(
                int(powmod(self.from_bytes(block), self.e, self.N)),
                self.n_byte_len
            )
            res.append(dec_block)
        res[-1] = self.remove_last_bytes(res[-1])
        return b''.join(res)

    def save_keys(self, filename: str) -> None:
        """
        Сохранение ключей в файл.

        :param filename: Имя файла для сохранения ключей.
        :return: None
        """
        key_data = {
            "N": self.N,
            "e": self.e,
            "s": self.s
        }

        with open(RSA.key_folder + filename, 'w') as key_file:
            json.dump(key_data, key_file, indent=4)

    def load_keys(self, filename: str) -> None:
        """
        Загрузка ключей из файла.

        :param filename: Имя файла с ключами.
        :return: None
        """
        with open(filename, 'r') as key_file:
            key_data = json.load(key_file)
        self.N = key_data["N"]
        self.e = key_data["e"]
        self.s = key_data["s"]

    def __repr__(self) -> str:
        """
        Строковое представление объекта.

        :return: Строковое представление объекта.
        """
        return f"RSA(" \
               f"N={self.N} ({len(str(self.N))} chars), " \
               f"s={self.s} ({len(str(self.s))} chars), " \
               f"e={self.e} ({len(str(self.e))} chars), " \
               f")"

    def __str__(self) -> str:
        """
        Строковое представление объекта для вывода.

        :return: Строковое представление объекта.
        """
        return self.__repr__().replace(",", "\n")


# @profile()
@timing
def test_mult(runs):
    with open("files\\test.txt", "rb") as file:
        inp_bytes = file.read()
    fail = 0
    rsa = RSA()
    # rsa = RSA(n_len=200)
    for i in range(runs):
        enc = rsa.encrypt(inp_bytes)
        dec = rsa.decrypt(enc)
        if dec != inp_bytes:
            fail += 1
    # if fail != 0:
    print(f"{fail / runs * 100:.2f}% fails")


@timing
def test1():
    filename = "test.csv"
    inp_file_path = RSA.file_folder + filename
    enc_file_path = RSA.file_folder + filename + '.rsa'
    dec_file_path = RSA.file_folder + filename.replace('.', '.dec.')

    with open(inp_file_path, "rb") as file:
        inp_bytes = file.read()

    rsa = RSA(n_len=28)
    print(rsa.N)
    enc = rsa.encrypt(RSA.file_folder + filename)
    with open(enc_file_path, "wb") as file:
        file.write(enc)

    dec = rsa.decrypt(enc_file_path)

    if dec != inp_bytes:
        print("fail")
    else:
        print("success")
    with open(dec_file_path, "wb") as file:
        file.write(dec)


if __name__ == '__main__':
    runs = 10000
    test_mult(runs)
    # test1()
