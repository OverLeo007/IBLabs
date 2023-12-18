package ru.paskal.IBLab3Back.security.tests;

import java.math.BigInteger;


public class TestRunner {
//  private final BPSWTest bpswTest;
//  private final FermatTest fermatTest;
//  @Autowired
//  public TestRunner(BPSWTest bpswTest, FermatTest fermatTest) {
//    this.bpswTest = bpswTest;
//    this.fermatTest = fermatTest;
//  }

  public static boolean run(BigInteger num) {
    return MillerRabinTest.run(num, 10) && FermatTest.run(num, 10);
  }
}
