//
// PlayN Performance Tests
// http://github.com/threerings/playn-perf/blob/master/LICENSE

package com.threerings.perf.core;

import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.Layer;
import static playn.core.PlayN.graphics;

/**
 * Displays a bunch of bodies as individual layers.
 */
public class LayerBodies extends Bodies
{
    /** Creates the visualization for a body and adds it to the appropriate parent. */
    public interface Viz {
        /** Creates the visualization for the {@code index}th body and adds it to the appropriate
         * parent layer, positioned at {@code x, y}. */
        Layer createViz (int index, float x, float y);
    }

    /** All bodies use the same image for visualization. */
    public static Viz singleImageViz (final Image image, final GroupLayer parent) {
        return new Viz() {
            public Layer createViz (int index, float x, float y) {
                Layer layer = graphics().createImageLayer(image).
                    setOrigin(image.width()/2, image.height()/2);
                parent.addAt(layer, x, y);
                return layer;
            }
        };
    }

    public LayerBodies (int count, float width, float height) {
        super(count, width, height);
        _layers = new Layer[count];
    }

    /**
     * Creates visualizations for all of the bodies, places them at their initial positions, and
     * adds their visualization layers to {@code parent}.
     */
    public void init (Viz viz, Init init) {
        super.init(init);
        float[] data = _data;
        for (int ii = 0, oo = 0, ll = _layers.length; ii < ll; ii++, oo += FIELDS) {
            _layers[ii] = viz.createViz(ii, data[oo+CX], data[oo+CY]);
        }
    }

    /**
     * Destroys all of the bodies' layers.
     */
    public void destroy () {
        for (int ii = 0, ll = _layers.length; ii < ll; ii++) {
            _layers[ii].destroy();
            _layers[ii] = null;
        }
    }

    @Override public void paint (float alpha) {
        if (_layers[0] == null) return; // not yet initialized
        super.paint(alpha);
    }

    @Override protected void paintBody (int idx, float cx, float cy) {
        _layers[idx].setTranslation(cx, cy);
    }

    /** The layers that represent each body. */
    protected final Layer[] _layers;
}
