package ru.paskal.IBLab3Back.security.tests;

import java.math.BigInteger;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BPSWTest {

  public final List<BigInteger> basis1k;

  @Autowired
  public BPSWTest(List<BigInteger> basis) {
    this.basis1k = basis;
  }

  public static boolean millerRabin(BigInteger n, BigInteger b) {
    BigInteger[] basis = {b};
    if (n.equals(BigInteger.valueOf(2)) || n.equals(BigInteger.valueOf(3))) {
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

    for (BigInteger base : basis) {
      BigInteger x = base.modPow(s, n);
      if (x.equals(BigInteger.ONE) || x.equals(n.subtract(BigInteger.ONE))) {
        continue;
      }

      for (int i = 0; i < r - 1; i++) {
        x = x.modPow(BigInteger.valueOf(2), n);
        if (x.equals(n.subtract(BigInteger.ONE))) {
          break;
        }
      }

      return false;
    }

    return true;
  }

  public int jacobiSymbol(BigInteger d, BigInteger n) {
    d = d.mod(n);
    int result = 1;

    while (!d.equals(BigInteger.ZERO)) {
      while (d.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) {
        d = d.divide(BigInteger.valueOf(2));
        BigInteger r = n.mod(BigInteger.valueOf(8));
        if (r.equals(BigInteger.valueOf(3)) || r.equals(BigInteger.valueOf(5))) {
          result = -result;
        }
      }

      BigInteger temp = d;
      d = n;
      n = temp;

      if (d.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3)) &&
          n.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3))) {
        result = -result;
      }

      d = d.mod(n);
    }

    if (n.equals(BigInteger.ONE)) {
      return result;
    }
    return 0;
  }

  public BigInteger[] lucas(BigInteger k, BigInteger D, BigInteger P, BigInteger n) {
    assert !D.equals(BigInteger.ZERO);

    BigInteger U = BigInteger.ONE;
    BigInteger V = P;
    BigInteger kObj = k;
    k = BigInteger.ONE;

    for (char c : kObj.toString(2).substring(1).toCharArray()) {
      U = U.multiply(V);
      V = V.pow(2).add(D.multiply(U.pow(2))).multiply(BigInteger.valueOf(2).modInverse(n)).mod(n);
      k = k.multiply(BigInteger.valueOf(2));

      if (c == '1') {
        U = U.multiply(V);
        V = V.pow(2).add(D.multiply(U.pow(2))).multiply(BigInteger.valueOf(2).modInverse(n)).mod(n);
        k = k.multiply(BigInteger.valueOf(2));

        U = P.multiply(U).add(V).multiply(BigInteger.valueOf(2).modInverse(n)).mod(n);
        V = D.multiply(U).add(P.multiply(V)).multiply(BigInteger.valueOf(2).modInverse(n)).mod(n);
        k = k.add(BigInteger.ONE);
      }
    }

    assert kObj.equals(k);

    return new BigInteger[]{U, V};
  }

  public boolean strongLucasTest(BigInteger n, BigInteger D, BigInteger P) {
    assert n.gcd(D).equals(BigInteger.ONE);

    int s = 1;
    BigInteger d;
    while (true) {
      d = n.add(BigInteger.ONE).divide(BigInteger.valueOf(2).pow(s));
      if (d.mod(BigInteger.valueOf(2)).equals(BigInteger.ONE)) {
        break;
      }
      s++;
    }

    BigInteger u_d = lucas(d, D, P, n)[0];

    if (u_d.mod(n).equals(BigInteger.ZERO)) {
      return true;
    } else {
      for (int r = 0; r < s; r++) {
        BigInteger v_d = lucas(d.multiply(BigInteger.valueOf(2).pow(r)), D, P, n)[1];
        if (v_d.mod(n).equals(BigInteger.ZERO)) {
          return true;
        }
      }
    }

    return false;
  }

  public boolean strongLucasSelfridge(BigInteger n) {
    BigInteger root = n.sqrt();

    if (root.pow(2).equals(n)) {
      return false;
    }

    BigInteger dAbs = BigInteger.valueOf(5);
    BigInteger sign = BigInteger.ONE;
    BigInteger D = dAbs.multiply(sign);

    while (true) {
      if (n.gcd(dAbs).compareTo(BigInteger.ONE) > 0) {
        return false;
      }

      if (jacobiSymbol(D, n) == -1) {
        break;
      }

      dAbs = dAbs.add(BigInteger.valueOf(2));
      sign = sign.negate();
      D = dAbs.multiply(sign);
    }

    BigInteger P = BigInteger.ONE;
    if (D.compareTo(BigInteger.ZERO) < 0) {
      D = D.modInverse(n);
    }

    return strongLucasTest(n, D, P);
  }


  public boolean run(BigInteger n, int k) {
    for (int i = 0; i < k; i++) {
      if (n.compareTo(BigInteger.ONE) <= 0) {
        return false;
      }

      if (n.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) {
        return false;
      }

      for (BigInteger p : basis1k) {
        if (n.mod(p).equals(BigInteger.ZERO) && !n.equals(p)) {
          return false;
        }
      }

      if (!millerRabin(n, BigInteger.valueOf(2))) {
        return false;
      }

      if (!strongLucasSelfridge(n)) {
        return false;
      }
    }

    return true;
  }


}
