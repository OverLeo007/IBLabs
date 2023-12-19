package ru.paskal.IBLab3Back.security.tests;

import java.math.BigInteger;
import java.util.Random;

/**
 * Класс, представляющий тест Миллера-Рабина для проверки числа на простоту.
 */
public class MillerRabinTest {

  /**
   * Метод для выполнения теста Миллера-Рабина.
   *
   * @param n           Число, которое требуется проверить на простоту.
   * @param testCount   Количество итераций теста.
   * @return {@code true}, если число вероятно простое, {@code false} в противном случае.
   */
  public static boolean run(BigInteger n, int testCount) {
    if (n.equals(BigInteger.valueOf(2))) {
      return true;
    }

    if (n.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) {
      return false;
    }

    int r = 0;
    BigInteger s = n.subtract(BigInteger.ONE);
    while (s.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) {
      r++;
      s = s.divide(BigInteger.valueOf(2));
    }

    Random random = new Random();
    for (int i = 0; i < testCount; i++) {
      BigInteger a = new BigInteger(n.bitLength(), random);
      a = a.mod(n.subtract(BigInteger.valueOf(2))).add(BigInteger.valueOf(2));

      BigInteger x = a.modPow(s, n);
      if (x.equals(BigInteger.ONE) || x.equals(n.subtract(BigInteger.ONE))) {
        continue;
      }

      for (int j = 0; j < r - 1; j++) {
        x = x.modPow(BigInteger.valueOf(2), n);
        if (x.equals(n.subtract(BigInteger.ONE))) {
          break;
        }
      }

      if (!x.equals(n.subtract(BigInteger.ONE))) {
        return false;
      }
    }

    return true;
  }

}
