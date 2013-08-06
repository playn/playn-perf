//
// PlayN Performance Tests
// http://github.com/threerings/playn-perf/blob/master/LICENSE

package com.threerings.perf.core;

import playn.core.Image;
import playn.core.Surface;
import playn.core.util.Clock;

/**
 * Displays a bunch of bodies via {@code Surface.drawImage} calls.
 */
public class SurfaceBodies extends LayerBodies
{
    /** Creates the visualization for a body. */
    public interface Viz {
        /** Creates the visualization for the {@code index}th body. */
        Image createViz (int index);
    }

    /** All bodies use the same image for visualization. */
    public static Viz singleImageViz (final Image image) {
        return new Viz() {
            public Image createViz (int index) { return image; }
        };
    }

    public SurfaceBodies (int count, float width, float height) {
        super(count, width, height);
        _images = new Image[count];
    }

    /**
     * Creates visualizations for all of the bodies, and places them at their initial positions.
     */
    public void init (Viz viz, Init init) {
        super.init(init);
        for (int ii = 0, ll = _images.length; ii < ll; ii++) {
            _images[ii] = viz.createViz(ii);
        }
    }

    /**
     * Handles painting of the bodies (details depend on concrete implementation).
     */
    @Override public void paint (Clock clock) {
        _alpha = clock.alpha();
    }

    /**
     * Interpolates between current and next coordinates (using {@code alpha} provided to prior
     * call to {@link #paint(float)}) and draws all the bodies to {@code surf}.
     */
    public void paint (Surface surf) {
        if (_images[0] == null) return; // not yet initialized
        float alpha = _alpha;
        float[] data = _data;
        for (int ii = 0, oo = 0, ll = count(); ii < ll; ii++, oo += FIELDS) {
            float cx = data[oo+CX] + alpha * data[oo+DX];
            float cy = data[oo+CY] + alpha * data[oo+DY];
            Image image = _images[ii];
            surf.drawImage(image, cx - image.width()/2, cy - image.height()/2);
        }
    }

    /** The images that represent each body. */
    protected final Image[] _images;

    /** The alpha for this frame (passed from {@link #paint(float)} to {@link #paint(Surface)}). */
    protected float _alpha;
}
