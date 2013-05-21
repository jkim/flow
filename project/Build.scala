import sbt._
import Keys._
import NativeBuild._
import JNIBuild._

object FlowBuild extends Build {
  val Organization = "com.github.jodersky"
  val Version = "1.0-SNAPSHOT"
  val ScalaVersion = "2.10.1"

  lazy val root = Project(
    id = "flow",
    base = file("."),
    settings = buildSettings ++ jniSettings ++ runSettings)

  lazy val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := Organization,
    version := Version,
    scalaVersion := ScalaVersion,
    resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/",
    scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature"),
    compileOrder in Compile := CompileOrder.JavaThenScala)
   
  lazy val jniSettings = JNIBuild.defaults ++ Seq(
    jdkHome := file(System.getProperty("java.home")) / "..",
    javaClass := "com.github.jodersky.flow.NativeSerial",
    NativeBuild.compiler := "gcc",
    options := Seq("-fPIC"),
    NativeBuild.includeDirectories <<= jdkHome apply (jdk => Seq(jdk / "include", jdk / "include" / "linux")),
    linker := "gcc",
    linkerOptions := Seq("-shared", "-Wl,-soname,libflow.so.1"),
    linkerOutput <<= NativeBuild.outputDirectory(_ / "libflow.so")
  )
  
  lazy val runSettings = Seq(
    fork := true,
    connectInput in run := true,
    javaOptions in run += "-Djava.library.path=.")
}

object Dependencies {
  lazy val all = Seq()

  //lazy val io = "com.github.scala-incubator.io" %% "scala-io-core" % "0.4.2"
  //lazy val file = "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.2"

}