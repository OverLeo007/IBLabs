package ru.paskal.IBLab3Back.security.tests;

import java.math.BigInteger;


/**
 * Класс, представляющий исполнителя тестов для проверки числа на простоту.
 */
public class TestRunner {

  /**
   * Метод для выполнения тестов Миллера-Рабина и Ферма.
   *
   * @param num Число, которое требуется проверить на простоту.
   * @return {@code true}, если число вероятно простое по обоим тестам, {@code false} в противном случае.
   */
  public static boolean run(BigInteger num) {
    return MillerRabinTest.run(num, 10) && FermatTest.run(num, 10);
  }
}
