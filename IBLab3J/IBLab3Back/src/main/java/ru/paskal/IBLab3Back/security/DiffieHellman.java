package ru.paskal.IBLab3Back.security;

import static ru.paskal.IBLab3Back.utils.MathUtils.generateRandomBigInteger;
import static ru.paskal.IBLab3Back.utils.MathUtils.getHash;
import static ru.paskal.IBLab3Back.utils.MathUtils.primitiveRoot;

import java.math.BigInteger;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.paskal.IBLab3Back.security.tests.TestRunner;

/**
 * Класс, представляющий реализацию протокола Диффи-Хеллмана для обмена ключами.
 */
@Getter
@Setter
@ToString
public class DiffieHellman {

  /**
   * Простое число p.
   */
  private final BigInteger p;

  /**
   * Первообразный корень по модулю p.
   */
  private final BigInteger g;

  /**
   * Секретный ключ bLow.
   */
  private final BigInteger bLow;

  /**
   * Открытый ключ A.
   */
  private BigInteger A;

  /**
   * Открытый ключ B.
   */
  private BigInteger B;

  /**
   * Общий секретный ключ K.
   */
  private byte[] K;

  /**
   * Конструктор класса для инициализации параметров протокола Диффи-Хеллмана.
   */
  public DiffieHellman() {
    this.p = findPrime(
        BigInteger.TEN.pow(11),
        BigInteger.TEN.pow(12).subtract(BigInteger.ONE)
    );
    this.bLow = findPrime(
        BigInteger.TEN.pow(11),
        BigInteger.TEN.pow(12).subtract(BigInteger.ONE)
    );
    this.g = primitiveRoot(p);

  }

  /**
   * Метод для нахождения простого числа в заданном диапазоне.
   *
   * @param lb Нижняя граница диапазона.
   * @param rb Верхняя граница диапазона.
   * @return Найденное простое число.
   */
  private BigInteger findPrime(BigInteger lb, BigInteger rb) {
    BigInteger num;
    do {
      num = generateRandomBigInteger(lb, rb);

    } while (!TestRunner.run(num));

    return num;
  }

  /**
   * Метод для установки значения открытого ключа A,
   * вычисления открытого ключа B и общего секретного ключа K.
   *
   * @param A Значение открытого ключа A.
   */
  public void setFromA(BigInteger A) {
    setA(A);
    setB(g.modPow(bLow, p));
    setK(getHash(A.modPow(bLow, p)));
  }
}
