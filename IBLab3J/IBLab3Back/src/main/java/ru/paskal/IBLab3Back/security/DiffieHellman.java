package ru.paskal.IBLab3Back.security;

import static ru.paskal.IBLab3Back.utils.MathUtils.generateRandomBigInteger;
import static ru.paskal.IBLab3Back.utils.MathUtils.getHash;
import static ru.paskal.IBLab3Back.utils.MathUtils.primitiveRoot;

import java.math.BigInteger;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.paskal.IBLab3Back.security.tests.TestRunner;

@Getter
@Setter
@ToString
public class DiffieHellman {

  private final BigInteger p;
  private final BigInteger g;
  private final BigInteger bLow;

  private BigInteger A;
  private BigInteger B;
  private byte[] K;

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

  private BigInteger findPrime(BigInteger lb, BigInteger rb) {
    BigInteger num;
    do {
      num = generateRandomBigInteger(lb, rb);

    } while (!TestRunner.run(num));

    return num;
  }

  public void setFromA(BigInteger A) {
    setA(A);
    setB(g.modPow(bLow, p));
    setK(getHash(A.modPow(bLow, p)));
  }


}
