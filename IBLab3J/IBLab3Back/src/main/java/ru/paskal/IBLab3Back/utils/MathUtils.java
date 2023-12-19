package ru.paskal.IBLab3Back.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Утилитарный класс для математических операций.
 */
public class MathUtils {

  /**
   * Метод для генерации случайного BigInteger в указанном диапазоне.
   *
   * @param min Минимальное значение.
   * @param max Максимальное значение.
   * @return Сгенерированный случайный BigInteger.
   */
  public static BigInteger generateRandomBigInteger(BigInteger min, BigInteger max) {
    return new BigInteger(max.bitLength(), ThreadLocalRandom.current()).add(min);
  }

  /**
   * Метод для проверки, является ли число простым.
   *
   * @param n Проверяемое число.
   * @return true, если число простое, иначе false.
   */
  public static boolean isPrime(BigInteger n) {
      if (n.compareTo(BigInteger.ONE) <= 0) {
        return false;
      }

      if (n.compareTo(BigInteger.valueOf(3)) <= 0) {
        return true;
      }

      if (n.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO) || n.mod(BigInteger.valueOf(3)).equals(BigInteger.ZERO)) {
        return false;
      }

      BigInteger i = BigInteger.valueOf(5);

      while (i.multiply(i).compareTo(n) <= 0) {
        if (n.mod(i).equals(BigInteger.ZERO) || n.mod(i.add(BigInteger.valueOf(2))).equals(BigInteger.ZERO)) {
          return false;
        }

        i = i.add(BigInteger.valueOf(6));
      }

      return true;
    }

  /**
   * Метод для нахождения простых множителей числа и добавления их в множество.
   *
   * @param s Множество для хранения простых множителей.
   * @param n Число, для которого находятся простые множители.
   */
    public static void findPrimeFactors(Set<BigInteger> s, BigInteger n) {
      while (n.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) {
        s.add(BigInteger.valueOf(2));
        n = n.divide(BigInteger.valueOf(2));
      }

      for (BigInteger i = BigInteger.valueOf(3); i.multiply(i).compareTo(n) <= 0; i = i.add(BigInteger.valueOf(2))) {
        while (n.mod(i).equals(BigInteger.ZERO)) {
          s.add(i);
          n = n.divide(i);
        }
      }

      if (n.compareTo(BigInteger.valueOf(2)) > 0) {
        s.add(n);
      }
    }

  /**
   * Метод для нахождения первообразного корня по модулю простого числа.
   *
   * @param n Простое число, для которого находится первообразный корень.
   * @return Первообразный корень по модулю простого числа.
   */
    public static BigInteger primitiveRoot(BigInteger n) {
      Set<BigInteger> s = new HashSet<>();

      if (!isPrime(n)) {
        return BigInteger.valueOf(-1);
      }

      BigInteger phi = n.subtract(BigInteger.ONE);

      findPrimeFactors(s, phi);

      for (BigInteger r = BigInteger.valueOf(2); r.compareTo(phi.add(BigInteger.ONE)) < 0; r = r.add(BigInteger.ONE)) {
        boolean flag = false;
        for (BigInteger it : s) {
          if (r.modPow(phi.divide(it), n).equals(BigInteger.ONE)) {
            flag = true;
            break;
          }
        }
        if (!flag) {
          return r;
        }
      }

      return BigInteger.valueOf(-1);
    }

  /**
   * Метод для получения хэш-значения BigInteger.
   *
   * @param number Число, для которого получается хэш-значение.
   * @return Хэш-значение числа в виде массива байт.
   */
  public static byte[] getHash(BigInteger number) {
    String numberStr = number.toString();
    return getHash(numberStr);
  }

  /**
   * Метод для получения хэш-значения строки.
   *
   * @param value Строка, для которой получается хэш-значение.
   * @return Хэш-значение строки в виде массива байт.
   */
  public static byte[] getHash(String value) {
    byte[] numberBytes = value.getBytes();

    try {
      MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
      sha256.update(numberBytes);
      return sha256.digest();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Метод для преобразования массива байт в шестнадцатеричное представление.
   *
   * @param bytes Массив байт.
   * @return Шестнадцатеричное представление массива байт в виде строки.
   */
  public static String bytesToHex(byte[] bytes) {
    BigInteger bigInt = new BigInteger(1, bytes);
    return bigInt.toString(16);
  }
}
