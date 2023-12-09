import random as ra
from math import sqrt
from typing import Tuple


class Testing:
    """
    Класс, предоставляющий методы для тестирования простоты чисел.
    """
    class MillerRabin:
        """
        Класс, предоставляющий методы для генерации базиса
        и выполнения теста Миллера-Рабина для проверки простоты числа.
        """
        @staticmethod
        def generate_basis(n: int) -> list:
            """
            Генерирует базис простых чисел до заданного числа n.

            :param n: Верхний предел для генерации базиса.
            :return: Список простых чисел в базисе.
            """
            basis = [True] * n
            for i in range(3, int(n ** 0.5) + 1, 2):
                if basis[i]:
                    basis[i * i::2 * i] = [False] * ((n - i * i - 1) // (2 * i) + 1)
            return [2] + [i for i in range(3, n, 2) if basis[i]]

        @staticmethod
        def miller_rabin(n: int, b: int) -> bool:
            """
            Выполняет тест Миллера-Рабина для проверки простоты числа n.

            :param b: Число-базис.
            :param n: Проверяемое число.
            :return: True, если число, вероятно, простое; False, если составное.
            """
            basis = [b]
            if n == 2 or n == 3:
                return True

            if n % 2 == 0:
                return False

            r, s = 0, n - 1
            while s % 2 == 0:
                r += 1
                s //= 2
            for base in basis:
                x = pow(base, s, n)
                if x == 1 or x == n - 1:
                    continue
                for _ in range(r - 1):
                    x = pow(x, 2, n)
                    if x == n - 1:
                        break
                else:
                    return False

            return True

    class BPSW:
        """
         Класс, предоставляющий методы для реализации теста простоты
         Бейли — Померанца — Селфриджа — Уогстаффа (BPSW).
         """

        @staticmethod
        def jacobi_symbol(d: int, n: int) -> int:
            """
            Вычисляет символ Якоби (Jacobi symbol) для пары (d, n).

            :param d: Первый аргумент символа Якоби.
            :param n: Второй аргумент символа Якоби.
            :return: Символ Якоби (1, 0 или -1).
            """
            d %= n
            result = 1
            while d != 0:
                while d % 2 == 0:
                    d //= 2
                    r = n % 8
                    if r == 3 or r == 5:
                        result = -result

                d, n = n, d
                if d % 4 == 3 and n % 4 == 3:
                    result = -result
                d %= n

            if n == 1:
                return result
            return 0

        @staticmethod
        def lucas(k: int, D: int, P: int, n: int) -> Tuple[int, int]:
            """
            Выполняет вычисления для последовательности Лукаса.

            :param k: Порядок последовательности Лукаса.
            :param D: Параметр D в последовательности Лукаса.
            :param P: Параметр P в последовательности Лукаса.
            :param n: Модуль, по которому производятся вычисления.
            :return: Кортеж (U, V), где U и V - значения последовательности Лукаса.
            """
            assert D != 0

            U = 1
            V = P
            k_obj = k
            k = 1
            for i in bin(k_obj)[3:]:
                if i == '0':
                    U, V = U * V, (V ** 2 + D * U ** 2) * pow(2, -1, n) % n
                    k *= 2
                else:
                    U, V = U * V, (V ** 2 + D * U ** 2) * pow(2, -1, n) % n
                    k *= 2
                    U, V = (P * U + V) * pow(2, -1, n) % n, \
                           (D * U + P * V) * pow(2, -1, n) % n
                    k += 1

            assert k_obj == k
            return U, V

        @staticmethod
        def strong_lucas_test(n: int, D: int, P: int) -> bool:
            """
            Выполняет сильное тестирование Лукаса для проверки простоты числа n.

            :param n: Проверяемое число.
            :param D: Параметр D в последовательности Лукаса.
            :param P: Параметр P в последовательности Лукаса.
            :return: True, если число, вероятно, простое; False, если составное.
            """
            assert Utils.gcd(n, D) == 1
            s = 1
            while True:
                d = (n + 1) // pow(2, s)
                if d % 2 == 1:
                    break
                s += 1

            u_d = Testing.BPSW.lucas(d, D, P, n)[0]

            if u_d % n == 0:
                return True
            else:
                for r in range(s):
                    v_d = Testing.BPSW.lucas(d * pow(2, r), D, P, n)[1]
                    if v_d % n == 0:
                        return True
            return False

        @staticmethod
        def strong_lucas_selfridge(n: int) -> bool:
            """
            Выполняет сильное тестирование Лукаса-Селфриджа
            для проверки простоты числа n.

            :param n: Проверяемое число.
            :return: True, если число, вероятно, простое; False, если составное.
            """
            root = int(sqrt(n))
            if root * root == n:
                return False

            d_abs = 5
            sign = 1
            D = d_abs * sign
            while True:
                if Utils.gcd(n, d_abs) > 1:
                    return False

                if Testing.BPSW.jacobi_symbol(D, n) == -1:
                    break

                d_abs += 2
                sign = -sign
                D = d_abs * sign

            P = 1
            if D < 0:
                D = pow(D, -1, n)
            return Testing.BPSW.strong_lucas_test(n, D, P)

        @staticmethod
        def bpsw(n: int) -> bool:
            """
            Выполняет тест на простоту
            Бейли — Померанца — Селфриджа — Уогстаффа (BPSW).

            :param n: Проверяемое число.
            :return: True, если число, вероятно, простое; False, если составное.
            """
            if n <= 1:
                return False

            if n % 2 == 0:
                return False

            for p in PrimesSingleton.primes:
                if n % p == 0 and n != p:
                    return False

            if not Testing.MillerRabin.miller_rabin(n, 2):
                return False

            return Testing.BPSW.strong_lucas_selfridge(n)

    @staticmethod
    def fermat(n: int, k: int) -> bool:
        """
        Выполняет тест Ферма для проверки простоты числа n.

        :param n: Проверяемое число.
        :param k: Количество итераций теста.
        :return: True, если число, вероятно, простое; False, если составное.
        """
        if n == 1 or n == 4:
            return False
        elif n == 2 or n == 3:
            return True

        else:
            for i in range(k):
                a = ra.randint(2, n - 2)

                if pow(a, n - 1, n) != 1:
                    return False

        return True

    @staticmethod
    def bpsw(n: int, k: int) -> bool:
        """
        Выполняет тест простоты Бейли — Померанца — Селфриджа — Уогстаффа (BPSW).

        :param n: Проверяемое число.
        :param k: Количество итераций теста.
        :return: True, если число, вероятно, простое; False, если составное.
        """
        for _ in range(k):
            if not Testing.BPSW.bpsw(n):
                return False
        return True


class PrimesSingleton:
    """
    Класс, предоставляющий единственное значение для базиса, для каждого вызова.
    """
    primes = Testing.MillerRabin.generate_basis(1000)


class Utils:
    """
    Класс с утилитами,
    предоставляющий математические функции для криптографических операций.
    """

    @staticmethod
    def gcd(a: int, b: int) -> int:
        """
        Рассчитать наибольший общий делитель (НОД) двух целых чисел.

        :param a: Первое целое число.
        :param b: Второе целое число.
        :return: НОД a и b.
        """
        while b:
            a, b = b, a % b
        return a

    @staticmethod
    def egcd(a: int, b: int) -> Tuple[int, int, int]:
        """
        Расширенный алгоритм Евклида для нахождения НОД
        и коэффициентов для тождества Безу.

        :param a: Первое целое число.
        :param b: Второе целое число.
        :return: Кортеж (g, x, y), где g - НОД, а x,
        y - коэффициенты такие, что ax + by = g.
        """
        if a == 0:
            return b, 0, 1
        else:
            g, y, x = Utils.egcd(b % a, a)
            return g, x - (b // a) * y, y

    @staticmethod
    def mod_inverse(a: int, m: int) -> int:
        """
        Рассчитать модульное мультипликативное обратное целого числа по модулю m.

        :param a: Целое число, для которого нужно найти модульный обратный элемент.
        :param m: Модуль.
        :return: Модульное обратное число a по модулю m.
        :raises Exception: Если модульное обратное не существует.
        """
        g, x, y = Utils.egcd(a, m)
        if g != 1:
            raise Exception('Модульное обратное не существует')
        else:
            return x % m
