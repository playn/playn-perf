//
// PlayN Performance Tests
// http://github.com/threerings/playn-perf/blob/master/LICENSE

package com.threerings.perf.core;

import pythagoras.d.Point;
import pythagoras.d.Vector;

import playn.core.Image;

import react.IntValue;
import react.Value;

import tripleplay.ui.Label;
import tripleplay.util.DestroyableList;
import tripleplay.util.Hud;
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

    @Override public void onTap () {
        final Bodies bods = _bods.add(Bodies.bounded(BATCH, width(), height()));
        _pea.addCallback(new CB<Image>() { public void onSuccess (Image image) {
            bods.init(layer, Bodies.singleImageViz(image),
                      Bodies.random(width(), height(), 0.1f));
        }});
        _count.update(_bods.size()*BATCH);
    }

    @Override public void wasShown () {
        super.wasShown();
        onTap();
    }

    @Override public void update (float delta) {
        super.update(delta);
        for (int ii = 0, ll = _bods.size(); ii < ll; ii++) _bods.get(ii).update(delta);
    }

    @Override public void paint (float alpha) {
        super.paint(alpha);
        for (int ii = 0, ll = _bods.size(); ii < ll; ii++) _bods.get(ii).paint(alpha);
    }

    @Override protected void addHudBits (Hud hud) {
        hud.add("ManyQuadsOneTex:", true);
        hud.add("Bodies:", _count);
        hud.add("Tap HUD to add bodies", false);
    }

    protected final Image _pea = getImage("pea.png");
    protected final IntValue _count = new IntValue(0);
    protected final DestroyableList<Bodies> _bods = DestroyableList.create();

    protected static final int BATCH = 1000;
}
