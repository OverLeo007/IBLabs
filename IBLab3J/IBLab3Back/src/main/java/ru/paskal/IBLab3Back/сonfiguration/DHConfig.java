package ru.paskal.IBLab3Back.сonfiguration;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DHConfig {

  @Bean
  public List<BigInteger> basis() {
    BigInteger n = BigInteger.valueOf(1000);
    List<Boolean> basis = new ArrayList<>(n.intValue() + 1);
    for (int i = 0; i <= n.intValue(); i++) {
      basis.add(true);
    }

    List<BigInteger> result = new ArrayList<>();
    result.add(BigInteger.valueOf(2));

    BigInteger sqrtN = n.sqrt();

    for (BigInteger i = BigInteger.valueOf(3); i.compareTo(sqrtN) <= 0; i = i.add(BigInteger.valueOf(2))) {
      if (basis.get(i.intValue())) {
        for (BigInteger j = i.multiply(i); j.compareTo(n) < 0; j = j.add(i.multiply(BigInteger.valueOf(2)))) {
          basis.set(j.intValue(), false);
        }
      }
    }

    for (BigInteger i = BigInteger.valueOf(3); i.compareTo(n) < 0; i = i.add(BigInteger.valueOf(2))) {
      if (basis.get(i.intValue())) {
        result.add(i);
      }
    }

    return result;
  }

}
