//
// PlayN Performance Tests
// http://github.com/threerings/playn-perf/blob/master/LICENSE

package com.threerings.perf.core;

import java.util.Random;

import playn.core.CanvasImage;
import playn.core.PlayN;
import playn.core.util.Clock;
import react.IntValue;
import react.ValueView.Listener;
import tripleplay.particle.Emitter;
import tripleplay.particle.Generator;
import tripleplay.particle.Particles;
import tripleplay.particle.effect.Alpha;
import tripleplay.particle.effect.Gravity;
import tripleplay.particle.effect.Move;
import tripleplay.particle.init.Color;
import tripleplay.particle.init.Lifespan;
import tripleplay.particle.init.Transform;
import tripleplay.particle.init.Velocity;
import tripleplay.util.Colors;
import tripleplay.util.Hud;
import tripleplay.util.Interpolator;
import tripleplay.util.Randoms;

public class ParticleBurst extends AbstractTest
{
    public static final int BATCH = 5000;

    public static TestConfig config () {
        return new TestConfig() {
            @Override protected AbstractTest create () {
                return new ParticleBurst();
            }

            /* init */ {
                addHeader("Particle Burst");
                // TODO: any config params?
                addStartButton();
            }
        };
    }

    @Override public void wasShown () {
        super.wasShown();

        final CanvasImage image = PlayN.graphics().createImage(2, 2);
        image.canvas().setFillColor(0xFFFFFFFF);
        image.canvas().fillRect(0, 0, 2, 2);

        _count.connectNotify(new Listener<Integer>() {
            @Override
            public void onChange (Integer value, Integer oldValue) {
                if (_emitter != null) {
                    _emitter.destroy();
                }

                _emitter = _particles.createEmitter(value, image, layer);
                _emitter.generator = Generator.constant(value);
                _emitter.initters.add(Lifespan.random(_rando, 1, 1.5f));
                _emitter.initters.add(Color.constant(Colors.RED));
                _emitter.initters.add(Transform.layer(_emitter.layer));
                _emitter.initters.add(Velocity.randomNormal(_rando, 50, 200));
                _emitter.effectors.add(Alpha.byAge(Interpolator.EASE_IN));
                _emitter.effectors.add(new Gravity(30));
                _emitter.effectors.add(new Move());
                _emitter.layer.setTranslation(
                    PlayN.graphics().width()/2, PlayN.graphics().height()/2);
            }
        });
    }

    @Override public void wasHidden () {
        super.wasHidden();

        if (_emitter != null) {
            _emitter.destroy();
            _emitter = null;
        }
    }

    @Override public void onTap () {
        _count.increment(BATCH);
    }

    @Override protected void addHudBits (Hud hud) {
        hud.add("Particle count", _count);
    }

    @Override public void paint (Clock clock) {
        super.paint(clock);
        _particles.paint(clock);
    }

    protected Emitter _emitter;
    protected final Particles _particles = new Particles();
    protected final Randoms _rando = Randoms.with(new Random());
    protected final IntValue _count = new IntValue(BATCH);
}
