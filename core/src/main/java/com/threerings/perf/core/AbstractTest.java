//
// PlayN Performance Tests
// http://github.com/threerings/playn-perf/blob/master/LICENSE

package com.threerings.perf.core;

import react.UnitSlot;
import react.Value;

import playn.core.Image;
import static playn.core.PlayN.assets;

import tripleplay.game.UIScreen;
import tripleplay.ui.Background;
import tripleplay.ui.Button;
import tripleplay.ui.Label;
import tripleplay.ui.Root;
import tripleplay.ui.SimpleStyles;
import tripleplay.ui.Style;
import tripleplay.ui.TextWidget;
import tripleplay.ui.layout.TableLayout;
import tripleplay.util.Ref;

/**
 * The base class for a performance test.
 */
public abstract class AbstractTest extends UIScreen
{
    /** Simplifies creating and pushing test screens. */
    public interface Thunk {
        AbstractTest create ();
    }

    @Override public void wasShown () {
        super.wasShown();
        _hud.set(new Hud()).add(new Label("HUD"), button("Back", new UnitSlot() {
            public void onEmit () { PerfTest.stack.remove(AbstractTest.this); }
        }));
        _hud.get().addLabel("FPS:", _fps);
        addHudBits(_hud.get());
        updateHud();
    }

    @Override public void wasHidden () {
        super.wasHidden();
        _hud.clear();
    }

    @Override public void update (float delta) {
        super.update(delta);
        _elapsed += delta;
        if (_elapsed > 1000) {
            updateHud();
            _elapsed -= 1000;
        }
    }

    @Override public void paint (float alpha) {
        super.paint(alpha);
    }

    protected Image getImage (String path) {
        return assets().getImage("images/" + path);
    }

    /** Override this and add UI elements to the HUD as needed. */
    protected void addHudBits (Hud hud) {
    }

    protected void updateHud () {
        _hud.get().pack();
    }

    protected Button button (String label, UnitSlot onClick) {
        Button button = new Button(label);
        button.clicked().connect(onClick);
        return button;
    }

    protected class Hud extends Root {
        public Hud () {
            super(iface, new TableLayout(2).gaps(5, 5), SimpleStyles.newSheet());
            iface.addRoot(this);
            layer.setDepth(Short.MAX_VALUE); // render above test stuffs
            AbstractTest.this.layer.add(layer);
            addStyles(Style.BACKGROUND.is(Background.solid(0xFF99CCFF).inset(10)));
        }

        public Value<String> addLabel (String label) {
            Label value = new Label();
            add(new Label(label), value);
            return value.text;
        }

        public void addLabel (String label, final Value<Integer> value) {
            add(new Label(label), new IntLabel(value));
        }
    }

    protected static class IntLabel extends TextWidget<IntLabel> {
        public final Value<Integer> value;
        public IntLabel (Value<Integer> value) {
            this.value = value;
            value.connect(textDidChange());
        }
        @Override protected Image icon () { return null; }
        @Override protected String text () { return value.get().toString(); }
        @Override protected Class<?> getStyleClass () { return Label.class; }
    }

    private final Ref<Hud> _hud = Ref.<Hud>create(null);
    private final Value<Integer> _fps = Value.create(0);
    private float _elapsed;
}
