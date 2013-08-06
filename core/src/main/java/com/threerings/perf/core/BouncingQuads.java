//
// PlayN Performance Tests
// http://github.com/threerings/playn-perf/blob/master/LICENSE

package com.threerings.perf.core;

import java.util.Random;

import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImmediateLayer;
import playn.core.Layer;
import playn.core.Surface;
import playn.core.util.Clock;
import static playn.core.PlayN.graphics;

import react.IntValue;
import react.Value;

import tripleplay.ui.CheckBox;
import tripleplay.ui.Slider;
import tripleplay.util.DestroyableList;
import tripleplay.util.Hud;

/**
 * Displays a bunch of quads bouncing around the screen, using a range of up to four textures and
 * up to four sub-images from those textures, and optionally being sorted (so that all renders for
 * a particular texture take place in sequence) or not.
 */
public class BouncingQuads extends AbstractTest
{
    public static TestConfig config () {
        return new TestConfig() {
            @Override protected AbstractTest create () {
                return new BouncingQuads(_images.value.get().intValue(),
                                         _subImages.value.get().intValue(),
                                         _sorted.checked.get(),
                                         _useLayers.checked.get());
            }

            protected Slider _images = new Slider(1, 1, IMGS).setIncrement(1);
            protected Slider _subImages = new Slider(1, 1, SUBIMGS).setIncrement(1);
            protected CheckBox _sorted = new CheckBox();
            protected CheckBox _useLayers = new CheckBox();

            /* init */ {
                addHeader("Bouncing Quads");
                addIntSlider("Images", _images, "0");
                addIntSlider("Sub-Images", _subImages, "0");
                add("Sorted", _sorted);
                add("Use layers", _useLayers);
                addStartButton();
            }
        };
    }

    @Override public void onTap () {
        // if any image is not ready, wait for it to load and try again
        for (Image img : _atlases) {
            if (!img.isReady()) {
                img.addCallback(new CB<Image>() { public void onSuccess (Image image) { onTap(); }});
                return;
            }
        }

        Bodies.Init init = Bodies.random(width(), height(), 0.1f);
        if (_useLayers) {
            LayerBodies bods = new LayerBodies(BATCH, width(), height());
            bods.init(new LayerBodies.Viz() {
                public Layer createViz (int index, float x, float y) {
                    int image = _rando.nextInt(_images);
                    Image pea = getPea(image, _rando.nextInt(_subImages));
                    Layer layer = graphics().createImageLayer(pea).
                        setOrigin(pea.width()/2, pea.height()/2);
                    _layers[image].addAt(layer, x, y);
                    return layer;
                }
                protected Random _rando = new Random();
            }, init);
            _bods.add(bods);
        } else {
            final SurfaceBodies bods = new SurfaceBodies(BATCH, width(), height());
            bods.init(new SurfaceBodies.Viz() {
                public Image createViz (int index) {
                    int image = _sorted ? (index / (BATCH/_images)) : _rando.nextInt(_images);
                    return getPea(image, _rando.nextInt(_subImages));
                }
                protected Random _rando = new Random();
            }, init);
            _bods.add(bods);
        }
        _count.update(_bods.size()*BATCH);
    }

    @Override public void wasShown () {
        super.wasShown();
        onTap();
    }

    @Override public void update (int delta) {
        super.update(delta);
        for (int ii = 0, ll = _bods.size(); ii < ll; ii++) _bods.get(ii).update(delta);
    }

    @Override public void paint (Clock clock) {
        super.paint(clock);
        for (int ii = 0, ll = _bods.size(); ii < ll; ii++) _bods.get(ii).paint(clock);
    }

    protected BouncingQuads (int images, int subImages, boolean sorted, boolean useLayers) {
        _images = images;
        _subImages = subImages;
        _sorted = sorted;
        _useLayers = useLayers;

        if (_useLayers) {
            // if we are sorting by texture, we create a group layer for each texture, otherwise we
            // combine everything into the same (top-level) group layer
            _layers = new GroupLayer[_images];
            for (int ii = 0; ii < _images; ii++) {
                if (sorted) layer.add(_layers[ii] = graphics().createGroupLayer());
                else _layers[ii] = layer;
            }
        } else {
            _layers = new GroupLayer[0]; // unused
            layer.add(graphics().createImmediateLayer(new ImmediateLayer.Renderer() {
                public void render (Surface surf) {
                    for (int ii = 0, ll = _bods.size(); ii < ll; ii++) {
                        SurfaceBodies bods = (SurfaceBodies)_bods.get(ii);
                        bods.paint(surf);
                    }
                }
            }));
        }
    }

    @Override protected void addHudBits (Hud hud) {
        hud.add("BouncingQuads:", true);
        hud.add("Images:", Value.create(_images));
        hud.add("Sub-images:", Value.create(_subImages));
        hud.add("Using layers:", Value.create(_useLayers));
        hud.add("Bodies:", _count);
        hud.add("Tap HUD to add bodies", false);
    }

    protected Image getPea (int image, int subImage) {
        Image pea = _peas[image][subImage];
        if (pea == null) {
            float size = _atlases[image].height();
            pea = _atlases[image].subImage(size*subImage, 0, size, size);
            // CanvasImage npea = graphics().createImage(size, size);
            // npea.canvas().drawImage(pea, 0, 0);
            // pea = npea;
            _peas[image][subImage] = pea;
        }
        return pea;
    }

    protected final int _images, _subImages;
    protected final boolean _sorted, _useLayers;
    protected final GroupLayer[] _layers;

    protected final Image[] _atlases = {
        getImage("peas0.png"),
        getImage("peas1.png"),
        getImage("peas2.png"),
        getImage("peas3.png"),
    };
    protected final Image[][] _peas = new Image[IMGS][SUBIMGS];

    protected final IntValue _count = new IntValue(0);
    protected final DestroyableList<Bodies> _bods = DestroyableList.create();

    protected static final int BATCH = 300;
    protected static final int IMGS = 4, SUBIMGS = 4;
}
