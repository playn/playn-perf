//
// PlayN Performance Tests
// http://github.com/threerings/playn-perf/blob/master/LICENSE

package com.threerings.perf.core;

import playn.core.Game;
import static playn.core.PlayN.*;

import tripleplay.game.ScreenStack;

public class PerfTest implements Game
{
    public static final ScreenStack stack = new ScreenStack();

    @Override public void init () {
        // create and show our main menu
        stack.push(new TestMenu());
    }

    @Override public void paint (float alpha) {
        stack.paint(alpha);
    }

    @Override public void update (float delta) {
        stack.update(delta);
    }

    @Override public int updateRate () {
        return 30;
    }
}
