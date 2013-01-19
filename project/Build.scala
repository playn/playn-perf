import sbt._
import Keys._
import samskivert.ProjectBuilder
import net.virtualvoid.sbt.graph.Plugin.graphSettings

object PerfTestBuild extends Build {
  val builder = new ProjectBuilder("pom.xml") {
    override val globalSettings = graphSettings ++ seq(
      crossPaths    := false,
      scalaVersion  := "2.9.2",
      javacOptions  ++= Seq("-Xlint", "-Xlint:-serial", "-source", "1.6", "-target", "1.6"),
      scalacOptions ++= Seq("-unchecked", "-deprecation"),
      javaOptions   ++= Seq("-ea"),
      fork in Compile := true,
      autoScalaLibrary := false // no scala-library dependency
    )
    override def projectSettings (name :String, pom :pomutil.POM) = name match {
      case "core" => seq(
        libraryDependencies ++= Seq(
          // scala test dependencies
          "com.novocode" % "junit-interface" % "0.7" % "test->default"
        )
      )
      case "java" => LWJGLPlugin.lwjglSettings ++ seq(
        javaOptions   ++= Seq("-verbose:gc", "-XX:+PrintGCDetails", "-XX:+PrintGCTimeStamps")
      )
      case _ => Nil
    }
  }

  lazy val assets = builder("assets")
  lazy val core = builder("core")
  lazy val java = builder("java")
  lazy val android = builder("android")

  // one giant fruit roll-up to bring them all together
  lazy val meta = Project("meta", file(".")) aggregate(assets, core, java, android)
}
