package ru.paskal.IBLab3Back.security.tests;

import static ru.paskal.IBLab3Back.utils.MathUtils.generateRandomBigInteger;

import java.math.BigInteger;

/**
 * Класс, представляющий тест Ферма для проверки числа на простоту.
 */
public class FermatTest {

  /**
   * Метод для выполнения теста Ферма.
   *
   * @param n Число, которое требуется проверить на простоту.
   * @param k Количество итераций теста.
   * @return {@code true}, если число вероятно простое, {@code false} в противном случае.
   */
  public static boolean run(BigInteger n, int k) {
    if (n.equals(BigInteger.ONE) || n.equals(BigInteger.valueOf(4))) {
      return false;
    } else if (n.equals(BigInteger.valueOf(2)) || n.equals(BigInteger.valueOf(3))) {
      return true;
    } else {
      for (int i = 0; i < k; i++) {
        BigInteger a = generateRandomBigInteger(BigInteger.valueOf(2),
            n.subtract(BigInteger.valueOf(2)));
        BigInteger result = a.modPow(n.subtract(BigInteger.ONE), n);

        if (!result.equals(BigInteger.ONE)) {
          return false;
        }
      }
      return true;
    }
  }
}
