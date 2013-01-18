package com.threerings.perf.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import com.threerings.perf.core.PerfTest;

public class PerfTestJava {

  public static void main(String[] args) {
    JavaPlatform.register();
    PlayN.run(new PerfTest());
  }
}
