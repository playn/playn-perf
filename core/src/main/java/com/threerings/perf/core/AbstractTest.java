//
// PlayN Performance Tests
// http://github.com/threerings/playn-perf/blob/master/LICENSE

package com.threerings.perf.core;

import java.util.HashSet;
import java.util.Set;

import react.Value;

import playn.core.Image;
import playn.core.Keyboard;
import playn.core.Mouse;
import playn.core.Pointer;
import playn.core.gl.GLContext;
import static playn.core.PlayN.*;

import tripleplay.game.Screen;
import tripleplay.util.Hud;
import tripleplay.util.Ref;

/**
 * The base class for a performance test.
 */
public abstract class AbstractTest extends Screen
{
    /** Called when the user taps/clicks once. */
    public void onTap () {
    }

    @Override public void wasShown () {
        super.wasShown();
        addHudBits(_hud);
        _hud.layer.setDepth(Short.MAX_VALUE);
        layer.add(_hud.layer);

        // wire up listeners for tapping and going back to the menu
        _hud.layer.addListener(new Mouse.LayerAdapter() {
            @Override public void onMouseDown(Mouse.ButtonEvent event) {
                switch (event.button()) {
                case Mouse.BUTTON_RIGHT: pop(); break;
                default: break;
                }
            }
        });
        _hud.layer.addListener(new Pointer.Adapter() {
            @Override public void onPointerStart(Pointer.Event event) {
                _tapStart = currentTime();
            }
            @Override public void onPointerEnd(Pointer.Event event) {
                double duration = currentTime() - _tapStart;
                if (duration > 1000) pop();
                else onTap();
            }
            @Override public void onPointerCancel(Pointer.Event event) {
            }
            protected double _tapStart;
        });
        keyboard().setListener(new Keyboard.Adapter() {
            @Override public void onKeyDown(Keyboard.Event event) {
                switch (event.key()) {
                case ESCAPE:
                case BACK: pop(); break;
                case SPACE: onTap(); break;
                case H: _hud.layer.setVisible(_hudActive = !_hudActive); break;
                default: break;
                }
            }
        });
    }

    @Override public void wasHidden () {
        super.wasHidden();
        keyboard().setListener(null);
    }

    @Override public void update (float delta) {
        super.update(delta);
        if (_hudActive) _hud.update(delta);
    }

    @Override public void paint (float alpha) {
        super.paint(alpha);
        if (_hudActive) _hud.paint(alpha);
    }

    protected void pop () {
        PerfTest.stack.remove(this);
    }

    protected Image getImage (String path) {
        return assets().getImage("images/" + path);
    }

    /** Override this and add UI elements to the HUD as needed. */
    protected void addHudBits (Hud hud) {
    }

    private final Hud.Stock _hud = new Hud.Stock();
    private boolean _hudActive = true;
}
