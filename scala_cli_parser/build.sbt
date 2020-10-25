// See:
// `comm8deec70`:

lazy val scala211 = "2.11.12"
lazy val scala212 = "2.12.8"
lazy val scala213 = "2.13.3"

lazy val supportedScalaVersions = List(
  scala211,
  scala212,
  scala213
)
ThisBuild / scalaVersion := scala212
scalaVersion := scala212

organization := "fmv1992"
name := "scala_cli_parser"
// ThisBuild / organizationName := "example"

inThisBuild(
  List(
    libraryDependencies += "org.scalameta" %% "scalameta" % "4.3.24",
    semanticdbEnabled := true,
    semanticdbOptions += "-P:semanticdb:synthetics:on",
    semanticdbVersion := scalafixSemanticdb.revision,
    scalafixScalaBinaryVersion := CrossVersion.binaryScalaVersion(
      scalaVersion.value
    ),
    addCompilerPlugin(
      "org.scalameta" % "semanticdb-scalac" % "4.3.24" cross CrossVersion.full
    ),
    addCompilerPlugin(scalafixSemanticdb)
  )
)

lazy val myproject = project.settings(
  scalacOptions += "-Ywarn-unused-import" // required by `RemoveUnused` rule
)

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0" % Test
// libraryDependencies += "io.github.fmv1992" %% "util" % "2.+"
libraryDependencies += "io.github.fmv1992" %% "util" % "1.+"
// libraryDependencies += "fmv1992" %% "fmv1992_scala_utilities" % "2.+"
// libraryDependencies += "fmv1992" %% "util" % "2.+"
libraryDependencies += "com.sandinh" %% "scala-rewrites" % "1.0.0"

// https://scalacenter.github.io/scalafix/
libraryDependencies += "org.scala-lang.modules" %% "scala-collection-compat" % "2.2.0"
scalafixDependencies in ThisBuild += "org.scala-lang.modules" %% "scala-collection-migrations" % "2.2.0"
scalacOptions ++= List("-Yrangepos", "-P:semanticdb:synthetics:on")

// resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

scalacOptions ++= (Seq("-feature", "-deprecation", "-Xfatal-warnings")
  ++ sys.env.get("SCALAC_OPTS").getOrElse("").split(" ").toSeq)

licenses += "GPLv2" -> url("https://www.gnu.org/licenses/gpl-2.0.html")
version := IO.readLines(new File("./src/main/resources/version")).mkString("")

// Ship resource files with each jar.
resourceDirectory in Compile := file(".") / "./src/main/resources"
resourceDirectory in Runtime := file(".") / "./src/main/resources"

coverageMinimum := 70
coverageFailOnMinimum := true

test in assembly := {}
assemblyMergeStrategy in assembly := {
  case "version" ⇒ MergeStrategy.first
  case x ⇒ {
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
  }
}

lazy val scala_cli_parser = (project in file("."))

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
