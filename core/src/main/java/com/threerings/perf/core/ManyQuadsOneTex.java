//
// PlayN Performance Tests
// http://github.com/threerings/playn-perf/blob/master/LICENSE

package com.threerings.perf.core;

import pythagoras.d.Point;
import pythagoras.d.Vector;

import playn.core.Image;

import react.Functions;
import react.IntValue;
import react.Slot;
import react.UnitSlot;
import react.Value;

import tripleplay.ui.Label;
import tripleplay.util.Ref;

/**
 * Displays a bunch of quads bouncing around the screen, all using the same texture. So we have
 * essentially no texture changes, and instead focus on matrix operations and the basic render
 * pipeline. Not at all realistic, but allows us to isolate and inspect certain things.
 */
public class ManyQuadsOneTex extends AbstractTest
{
    public static final Thunk THUNK = new Thunk() {
        public AbstractTest create() {  return new ManyQuadsOneTex(); }
    };

    public ManyQuadsOneTex () {
        _count.connectNotify(new Slot<Integer>() { public void onEmit (Integer count) {
            _bodies.set(Bodies.bounded(count, width(), height()));
            getImage("pea.png").addCallback(new CB<Image>() {
                public void onSuccess (Image image) {
                    _bodies.get().init(layer, Bodies.singleImageViz(image),
                                       Bodies.random(width(), height(), 0.1f));
                }
            });
        }});
    }

    @Override public void update (float delta) {
        super.update(delta);
        _bodies.get().update(delta);
    }

    @Override public void paint (float alpha) {
        super.paint(alpha);
        _bodies.get().paint(alpha);
    }

    @Override protected void addHudBits (Hud hud) {
        hud.addLabel("Bodies:", _count);
        hud.add(button("Less", new UnitSlot() {
            public void onEmit () { _count.decrementClamp(500, 500); }
        }), button("More", new UnitSlot() {
            public void onEmit () { _count.increment(500); }
        }));
    }

    protected final IntValue _count = new IntValue(500);
    protected final Ref<Bodies> _bodies = Ref.<Bodies>create(null);
}
