// pom-util POM helpers
libraryDependencies += "com.samskivert" % "sbt-pom-util" % "0.4"

// this is needed to wire up LWJGL when running the java version
addSbtPlugin("com.github.philcali" % "sbt-lwjgl-plugin" % "3.1.4")

// used to debug dependencies
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.6.0")
