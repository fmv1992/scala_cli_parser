// See:
// `comm8deec70`:

ThisBuild / scalaVersion := "2.11.12"
organization := "fmv1992"
name := "scala_cli_parser"
// ThisBuild / organizationName := "example"

libraryDependencies += "org.scalatest" %%% "scalatest" % "3.1.0" % Test

// libraryDependencies += "io.github.fmv1992" %% "util" % "2.+"
libraryDependencies += "fmv1992" %%% "fmv1992_scala_utilities" % "1.8.5.dev.2.11.12"
/* libraryDependencies += "fmv1992" %% "util" % "1.+"*/
// libraryDependencies += "io.github.fmv1992" %% "util" % "1.9.3"
// libraryDependencies += "fmv1992" %% "util" % "2.+"

enablePlugins(ScalaNativePlugin)
nativeLinkStubs := true
nativeLinkStubs in runMain := true
Test / nativeLinkStubs := true
// fmv1992_scala_utilities:33e41c7:fmv1992_scala_utilities/build.sbt:26
sources in (Compile, doc) := Seq.empty

// resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
resolvers += Resolver.mavenLocal

scalacOptions ++= (Seq("-feature", "-deprecation", "-Xfatal-warnings")
  ++ sys.env.get("SCALAC_OPTS").getOrElse("").split(" ").toSeq)

licenses += "GPLv2" -> url("https://www.gnu.org/licenses/gpl-2.0.html")
version := IO.readLines(new File("./src/main/resources/version")).mkString("")

// Ship resource files with each jar.
resourceDirectory in Compile := file(".") / "./src/main/resources"
resourceDirectory in Runtime := file(".") / "./src/main/resources"

coverageMinimum := 70
coverageFailOnMinimum := true

// fmv1992_scala_utilities:33e41c7:fmv1992_scala_utilities/build.sbt:29
test in assembly := {}
assemblyMergeStrategy in assembly := {
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.rename
  case x                                   => MergeStrategy.first
}

lazy val scala_cli_parser = (project in file("."))

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
