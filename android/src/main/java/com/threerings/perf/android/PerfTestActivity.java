package com.threerings.perf.android;

import playn.android.GameActivity;
import playn.core.PlayN;

import com.threerings.perf.core.PerfTest;

public class PerfTestActivity extends GameActivity {

  @Override
  public void main(){
    PlayN.run(new PerfTest());
  }
}
