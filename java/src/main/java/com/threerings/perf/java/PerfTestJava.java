//
// PlayN Performance Tests
// http://github.com/threerings/playn-perf/blob/master/LICENSE

package com.threerings.perf.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import com.threerings.perf.core.PerfTest;

public class PerfTestJava
{
    public static void main (String[] args) {
        JavaPlatform.Config config = new JavaPlatform.Config();
        config.width = 1024;
        config.height = 768;
        JavaPlatform pf = JavaPlatform.register(config);
        pf.setTitle("PlayN Performance Tests");
        PlayN.run(new PerfTest());
    }
}
