//
// PlayN Performance Tests
// http://github.com/threerings/playn-perf/blob/master/LICENSE

package com.threerings.perf.core;

import java.util.Random;

import playn.core.Image;
import playn.core.GroupLayer;
import playn.core.Layer;
import static playn.core.PlayN.graphics;

import tripleplay.util.Destroyable;

/**
 * Manages a collection of bodies with position and velocity.
 */
public abstract class Bodies
    implements Destroyable
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

    /** Handles initialization for a body. */
    public interface Init {
        /** Initializes the {@code index}th body. */
        void init (int index, float[] data, int offset);
    }

    /** Randomizes initial position in {@code 0,0..width,height}, and velocity in
     * {@code -maxVel..maxVel}. */
    public static Init random (final float width, final float height, final float maxVel) {
        return new Init() {
            public void init (int index, float[] data, int offset) {
                data[offset+CX] = _rando.nextFloat() * width;
                data[offset+CY] = _rando.nextFloat() * height;
                data[offset+VX] = -maxVel + (_rando.nextFloat() * 2 * maxVel);
                data[offset+VY] = -maxVel + (_rando.nextFloat() * 2 * maxVel);
            }
            protected Random _rando = new Random();
        };
    }

    /** A bodies instance that bounds its bodies in a region, bouncing them off the walls. */
    public static Bodies bounded (int count, final float width, final float height) {
        return new Bodies(count) {
            protected void willUpdateDelta (float delta) {
                // if anyone is out of bounds, reverse their velocity
                float bwidth = width, bheight = height;
                float[] data = _data;
                for (int ii = 0, oo = 0, ll = _layers.length; ii < ll; ii++, oo += FIELDS) {
                    float cx = data[oo+CX], cy = data[oo+CY];
                    if (cx < 0 || cx > bwidth) data[oo+VX] *= -1;
                    if (cy < 0 || cy > bheight) data[oo+VY] *= -1;
                }
            }
        };
    }

    /**
     * Creates visualizations for all of the bodies, places them at their initial positions, and
     * adds their visualization layers to {@code parent}.
     */
    public void init (Viz viz, Init init) {
        float[] data = _data;
        for (int ii = 0, oo = 0, ll = _layers.length; ii < ll; ii++, oo += FIELDS) {
            init.init(ii, data, oo);
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

    /**
     * Updates the coordinates of all the bodies, moving nx/ny into cx/cy and computing new nx/ny
     * based on vx/vy.
     */
    public void update (float delta) {
        float[] data = _data;
        willUpdate(delta);
        for (int ii = 0, oo = 0, ll = _layers.length; ii < ll; ii++, oo += FIELDS) {
            data[oo+CX] += data[oo+DX];
            data[oo+CY] += data[oo+DY];
        }
        willUpdateDelta(delta);
        for (int ii = 0, oo = 0, ll = _layers.length; ii < ll; ii++, oo += FIELDS) {
            data[oo+DX] = delta * data[oo+VX];
            data[oo+DY] = delta * data[oo+VY];
        }
        didUpdate(delta);
    }

    /**
     * Interpolates between current and next coordinates (using {@code alpha}) and updates the
     * position of all bodies' layers to their appropriate value.
     */
    public void paint (float alpha) {
        if (_layers[0] == null) return; // not yet initialized
        float[] data = _data;
        for (int ii = 0, oo = 0, ll = _layers.length; ii < ll; ii++, oo += FIELDS) {
            float cx = data[oo+CX] + alpha * data[oo+DX];
            float cy = data[oo+CY] + alpha * data[oo+DY];
            _layers[ii].setTranslation(cx, cy);
        }
    }

    protected Bodies (int count) {
        _data = new float[count*FIELDS];
        _layers = new Layer[count];
    }

    /** Called before bodies' current position is updated for this frame. */
    protected void willUpdate (float delta) {
    }

    /** Called before bodies' position delta is computed, but after their current position has been
     * updated for this frame. */
    protected void willUpdateDelta (float delta) {
    }

    /** Called after bodies' position and delta have been updated for this frame. */
    protected void didUpdate (float delta) {
    }

    /** The layers that represent each body. */
    protected final Layer[] _layers;

    /** The data for each body, as {@code {{cx, cy, dx, dy, vx, vy}, ...}}. */
    protected final float[] _data;

    protected static final int CX = 0, CY = 1, DX = 2, DY = 3, VX = 4, VY = 5;
    protected static final int FIELDS = 6; // cx/cy, dx/dy, vx/vy
}
