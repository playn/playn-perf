//
// PlayN Performance Tests
// http://github.com/threerings/playn-perf/blob/master/LICENSE

package com.threerings.perf.core;

import playn.core.Game;
import playn.core.util.Clock;
import tripleplay.game.ScreenStack;

public class PerfTest extends Game.Default
{
    public static final ScreenStack stack = new ScreenStack();

    public PerfTest () {
        super(50);
    }

    @Override public void init () {
        stack.push(new TestMenu());
    }

    @Override public void update (int delta) {
        _clock.update(delta);
        stack.update(delta);
    }

    @Override public void paint (float alpha) {
        _clock.paint(alpha);
        stack.paint(_clock);
    }

    protected final Clock.Source _clock = new Clock.Source(50);
}
