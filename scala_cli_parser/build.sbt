// See:
// `comm8deec70`:

// https://github.com/SemanticSugar/sconfig/blob/9623f8401321fe847a49aecb7cfd92be73872ff6/build.sbt#L52
lazy val scala211 = "2.11.12"
lazy val scala212 = "2.12.12"
lazy val scala213 = "2.13.3"

// val versionsJVM = Seq(scala211, scala212, scala213)
val versionsJVM = Seq(scala211)
val versionsNative = Seq(scala211)

// inThisBuild(
//   Seq(
//   )
// )

lazy val commonSettings = Seq(
  organization := "fmv1992",
  // scalaVersion := scala211,
  name := "scala_cli_parser",
  //
  // coverageMinimum := 70
  // coverageFailOnMinimum := true
  //
  // resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
  // resolvers += Resolver.mavenLocal,
  //
  scalacOptions ++= (Seq("-feature", "-deprecation", "-Xfatal-warnings")
    ++ sys.env.get("SCALAC_OPTS").getOrElse("").split(" ").toSeq),
  licenses += "GPLv2" -> url("https://www.gnu.org/licenses/gpl-2.0.html"),
  version := IO
    .readLines(new File("./src/main/resources/version"))
    .mkString(""),
  // Ship resource files with each jar.
  resourceDirectory in Compile := file(".") / "./src/main/resources",
  resourceDirectory in Runtime := file(".") / "./src/main/resources",
  // fmv1992_scala_utilities:33e41c7:fmv1992_scala_utilities/build.sbt:29
  test in assembly := {},
  assemblyMergeStrategy in assembly := {
    case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.rename
    case x                                   => MergeStrategy.first
  }
)

lazy val commonDependencies = Seq(
  libraryDependencies += "org.scalatest" %%% "scalatest" % "3.1.0" % Test,
  // libraryDependencies += "io.github.fmv1992" %% "fmv1992_scala_utilities" % "1.11.4"
  libraryDependencies += "io.github.fmv1992" %%% "util" % "1.11.4"
)
lazy val commonSettingsAndDependencies = commonSettings ++ commonDependencies

lazy val scalaNativeSettings = Seq(
  crossScalaVersions := versionsNative,
  scalaVersion := scala211, // allows to compile if scalaVersion set not 2.11
  nativeLinkStubs := true,
  nativeLinkStubs in runMain := true,
  nativeLinkStubs in Test := true,
  Test / nativeLinkStubs := true,
  sources in (Compile, doc) := Seq.empty
)

lazy val scala_cli_parserCrossProject: sbtcrossproject.CrossProject =
  crossProject(JVMPlatform, NativePlatform)
    .crossType(CrossType.Pure)
    .in(file("."))
    .settings(
      commonSettingsAndDependencies
    )
    .jvmSettings(
      crossScalaVersions := versionsJVM
    )
    .nativeSettings(
      scalaNativeSettings
    )

lazy val scala_cli_parserJVM: sbt.Project = scala_cli_parserCrossProject.jvm

lazy val scala_cli_parserNative: sbt.Project =
  scala_cli_parserCrossProject.native

lazy val root: sbt.Project = (project in file("."))
  .settings(
    commonSettingsAndDependencies
  )
  .settings(
    publish / skip := true,
    doc / aggregate := false,
    crossScalaVersions := Nil,
    packageDoc / aggregate := false
  )
  .aggregate(
    scala_cli_parserJVM,
    scala_cli_parserNative
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
