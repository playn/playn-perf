//
// PlayN Performance Tests
// http://github.com/threerings/playn-perf/blob/master/LICENSE

package com.threerings.perf.core;

import react.UnitSlot;

import tripleplay.game.Screen;
import tripleplay.game.UIScreen;
import tripleplay.ui.Background;
import tripleplay.ui.Button;
import tripleplay.ui.Label;
import tripleplay.ui.Root;
import tripleplay.ui.SimpleStyles;
import tripleplay.ui.Style;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.util.Ref;

/**
 * Displays a menu from which performance tests can be selected.
 */
public class TestMenu extends UIScreen
{
    @Override public void wasShown () {
        super.wasShown();
        Root root = _root.set(iface.createRoot(AxisLayout.vertical(),
                                               SimpleStyles.newSheet(), layer));
        root.addStyles(Style.BACKGROUND.is(Background.solid(0xFF99CCFF)));
        root.setSize(width(), height());

        root.add(new Label("PlayN Performance Tests"));
        root.add(testButton("Many Quads One Texture", ManyQuadsOneTex.THUNK));
    }

    @Override public void wasHidden () {
        super.wasHidden();
        _root.clear();
    }

    protected Button testButton (String label, final AbstractTest.Thunk thunk) {
        Button button = new Button(label);
        button.clicked().connect(new UnitSlot() {
            public void onEmit () { PerfTest.stack.push(thunk.create()); }
        });
        return button;
    }

    protected final Ref<Root> _root = Ref.<Root>create(null);
}
