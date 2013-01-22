PlayN Performance Tests
-----------------------

This is a framework for writing performance tests for [PlayN] and investigating
whether they perform well or not.

Building and Running
--------------------

You will need the latest snapshot version of [OOO PlayN] and [TriplePlay] built
and installed into your local Maven repository. Then you can build and run on
the Java backend thusly:

    mvn clean test -Pjava

Building and running for other backends follows the [standard PlayN pattern].

Licensing
---------

Unless otherwise stated, all source files are licensed under the [BSD license].

[BSD license]: https://github.com/threerings/playn-perf/blob/master/LICENSE
[OOO PlayN]: http://github.com/threerings/playn/
[PlayN]: http://code.google.com/p/playn/
[TriplePlay]: http://github.com/threerings/tripleplay/
[standard PlayN pattern]: http://code.google.com/p/playn/wiki/GettingStarted
