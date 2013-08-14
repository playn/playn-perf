//
// PlayN Performance Tests
// http://github.com/threerings/playn-perf/blob/master/LICENSE

package com.threerings.perf.core;

import pythagoras.d.MathUtil;

import playn.core.Font;
import static playn.core.PlayN.graphics;

import tripleplay.game.UIScreen;
import tripleplay.ui.Background;
import tripleplay.ui.Label;
import tripleplay.ui.Root;
import tripleplay.ui.SimpleStyles;
import tripleplay.ui.Style;
import tripleplay.ui.layout.TableLayout;
import tripleplay.util.Ref;

/**
 * Displays a menu from which performance tests can be selected.
 */
public class TestMenu extends UIScreen
{
    @Override public void wasAdded () {
        super.wasAdded();
        int cols = Math.max(1, MathUtil.ifloor(width() / 200));
        Root root = _root.set(iface.createRoot(new TableLayout(cols).gaps(10, 10),
                                               SimpleStyles.newSheet(), layer));
        root.addStyles(Style.BACKGROUND.is(Background.solid(0xFF99CCFF).inset(10)),
                       Style.VALIGN.top);
        root.setSize(width(), height());

        Background configBG = Background.solid(0xFFCCCCCC).inset(10);
        root.add(TableLayout.colspan(new Label("PlayN Performance Tests").addStyles(
                                         Style.FONT.is(HEADER_FONT)), cols));
        root.add(BouncingQuads.config().addStyles(Style.BACKGROUND.is(configBG)));
        root.add(ScrollingQuads.config().addStyles(Style.BACKGROUND.is(configBG)));
        root.add(Particles.config().addStyles(Style.BACKGROUND.is(configBG)));
    }

    @Override public void wasRemoved () {
        super.wasRemoved();
        _root.clear();
    }

    protected final Ref<Root> _root = Ref.<Root>create(null);

    protected static final Font HEADER_FONT = graphics().createFont(
        "Helvetica", Font.Style.BOLD, 24);
}
