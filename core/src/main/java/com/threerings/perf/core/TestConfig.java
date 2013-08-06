//
// PlayN Performance Tests
// http://github.com/threerings/playn-perf/blob/master/LICENSE

package com.threerings.perf.core;

import react.Functions;
import react.UnitSlot;

import tripleplay.ui.Button;
import tripleplay.ui.Constraints;
import tripleplay.ui.Element;
import tripleplay.ui.Group;
import tripleplay.ui.Label;
import tripleplay.ui.Slider;
import tripleplay.ui.ValueLabel;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.ui.layout.TableLayout;

/**
 * A class that handles configuring a test before launching it.
 */
public abstract class TestConfig extends Group
{
    protected TestConfig () {
        super(new TableLayout(TableLayout.COL.alignLeft(),
                              TableLayout.COL.alignLeft()).gaps(5, 5));
    }

    protected void add (String label, Element<?> elem) {
        add(new Label(label), elem);
    }

    protected void addHeader (String header) {
        add(TableLayout.colspan(new Label(header), 2));
    }

    protected void addIntSlider (String label, Slider slider, String minFmt) {
        ValueLabel value = new ValueLabel(slider.value.map(Functions.INT_VALUE)).
            setConstraint(Constraints.minSize(minFmt));
        add(label, new Group(AxisLayout.horizontal()).add(slider, value));
    }

    protected void addStartButton () {
        Button start = new Button("Start");
        start.clicked().connect(new UnitSlot() { @Override public void onEmit () {
            PerfTest.stack.push(create());
        }});
        add(TableLayout.colspan(start, 2));
    }

    protected abstract AbstractTest create ();
}
