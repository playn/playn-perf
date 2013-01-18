//
// PlayN Performance Tests
// http://github.com/threerings/playn-perf/blob/master/LICENSE

package com.threerings.perf.core;

import playn.core.util.Callback;
import static playn.core.PlayN.log;

public abstract class CB<T> implements Callback<T>
{
    @Override public void onFailure (Throwable cause) {
        log().warn("Ack", cause);
    }
}
