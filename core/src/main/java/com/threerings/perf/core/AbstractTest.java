//
// PlayN Performance Tests
// http://github.com/threerings/playn-perf/blob/master/LICENSE

package com.threerings.perf.core;

import java.util.HashSet;
import java.util.Set;

import react.Value;

import playn.core.Image;
import playn.core.Key;
import playn.core.Keyboard;
import playn.core.Mouse;
import playn.core.Touch;
import playn.core.gl.GLContext;
import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.keyboard;

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
        _hud.add("Shader info:", true);
        _hud.add(_quadShader);
        _hud.add(_trisShader);
        _hud.add("Per second:", true);
        _hud.add("Frames:", _frames);
        _hud.add("Shader creates:", _shaderCreates);
        _hud.add("FB creates:", _fbCreates);
        _hud.add("Tex creates:", _texCreates);
        _hud.add("Per frame:", true);
        _hud.add("Shader binds:", _shaderBinds);
        _hud.add("FB binds:", _fbBinds);
        _hud.add("Tex binds:", _texBinds);
        _hud.add("Quads drawn:", _rQuads);
        _hud.add("Tris drawn:", _rTris);
        _hud.add("Shader flushes:", _shaderFlushes);
        addHudBits(_hud);
        _hud.layer.setDepth(Short.MAX_VALUE);
        layer.add(_hud.layer);

        // wire up listeners for tapping and going back to the menu
        _hud.layer.addListener(new Mouse.LayerAdapter() {
            @Override public void onMouseDown(Mouse.ButtonEvent event) {
                switch (event.button()) {
                case Mouse.BUTTON_RIGHT: pop(); break;
                case Mouse.BUTTON_LEFT: onTap(); break;
                default: break;
                }
            }
        });
        _hud.layer.addListener(new Touch.LayerAdapter() {
            @Override public void onTouchStart(Touch.Event event) {
                // Android and iOS handle touch events rather differently, so we need to do this
                // finagling to determine whether there is an active two finger touch
                _active.add(event.id());
                if (_active.size() > 1) pop();
                else onTap();
            }
            @Override public void onTouchEnd(Touch.Event event) {
                _active.remove(event.id());
            }
            @Override public void onTouchCancel(Touch.Event event) {
                _active.remove(event.id());
            }
            protected Set<Integer> _active = new HashSet<Integer>();
        });
        keyboard().setListener(new Keyboard.Adapter() {
            @Override public void onKeyDown(Keyboard.Event event) {
                if (event.key() == Key.ESCAPE || event.key() == Key.BACK) pop();
            }
        });
    }

    @Override public void wasHidden () {
        super.wasHidden();
        keyboard().setListener(null);
    }

    @Override public void update (float delta) {
        super.update(delta);
        long now = System.currentTimeMillis();
        if (now > _nextSec) {
            GLContext.Stats stats = graphics().ctx().stats();
            int frames = Math.max(stats.frames, 1);
            _frames.update(frames);
            _shaderCreates.update(stats.shaderCreates);
            _fbCreates.update(stats.frameBufferCreates);
            _texCreates.update(stats.texCreates);
            _shaderBinds.update(stats.shaderBinds/frames);
            _fbBinds.update(stats.frameBufferBinds/frames);
            _texBinds.update(stats.texBinds/frames);
            _rQuads.update(stats.quadsRendered/frames);
            _rTris.update(stats.trisRendered/frames);
            _shaderFlushes.update(stats.shaderFlushes/frames);
            stats.reset();
            _quadShader.update("Quad: " + graphics().ctx().quadShaderInfo());
            _trisShader.update("Tris: " + graphics().ctx().trisShaderInfo());
            _hud.update();
            _nextSec = now + 1000;
        }
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

    private final Hud _hud = new Hud();
    private long _nextSec;

    private final Value<Integer> _frames = Value.create(0);
    private final Value<Integer> _shaderCreates = Value.create(0);
    private final Value<Integer> _fbCreates = Value.create(0);
    private final Value<Integer> _texCreates = Value.create(0);
    private final Value<Integer> _shaderBinds = Value.create(0);
    private final Value<Integer> _fbBinds = Value.create(0);
    private final Value<Integer> _texBinds = Value.create(0);
    private final Value<Integer> _rQuads = Value.create(0);
    private final Value<Integer> _rTris = Value.create(0);
    private final Value<Integer> _shaderFlushes = Value.create(0);

    private final Value<String> _quadShader = Value.create("");
    private final Value<String> _trisShader = Value.create("");
}
