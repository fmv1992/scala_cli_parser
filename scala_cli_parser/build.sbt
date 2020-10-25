// See:
// `comm8deec70`:

// ThisBuild / scalaVersion := "2.12.8"
organization := "fmv1992"
name := "scala_cli_parser"
// ThisBuild / organizationName := "example"

inThisBuild(
  List(
    scalaVersion := "2.12.12", // 2.11.12, or 2.13.3
    semanticdbEnabled := true, // enable SemanticDB
    semanticdbVersion := scalafixSemanticdb.revision // use Scalafix compatible version
  )
)

lazy val myproject = project.settings(
  scalacOptions += "-Ywarn-unused-import" // required by `RemoveUnused` rule
)

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5"
// libraryDependencies += "io.github.fmv1992" %% "util" % "2.+"
libraryDependencies += "io.github.fmv1992" %% "util" % "1.10.0"
// libraryDependencies += "fmv1992" %% "fmv1992_scala_utilities" % "2.+"
// libraryDependencies += "fmv1992" %% "util" % "2.+"

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
