import xerial.sbt.Sonatype._

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val scala213 = "2.13.8"

val versionsJVM = Seq(scala213)
val versionsNative = Seq(scala213)

// https://github.com/sbt/sbt-ghpages
enablePlugins(SiteScaladocPlugin)
enablePlugins(GhpagesPlugin)

inThisBuild(
  List(
    scalaVersion := scala213,
    scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.4.3",
    // https://index.scala-lang.org/ohze/scala-rewrites/scala-rewrites/0.1.10-sd?target=_2.13
    semanticdbEnabled := true,
    semanticdbOptions += "-P:semanticdb:synthetics:on", // make sure to add this
    semanticdbVersion := scalafixSemanticdb.revision,
    libraryDependencies += "org.scalameta" % "semanticdb-scalac-core" % "4.5.0" cross CrossVersion.full,
    scalafixScalaBinaryVersion := CrossVersion.binaryScalaVersion(
      scalaVersion.value
    ),
    // fork in Test := false,
    // fork in test := false,
    // fork in run := false
    git.remoteRepo := "https://github.com/fmv1992/scala_cli_parser"
  )
)

lazy val commonSettings = Seq(
  homepage := Some(url("https://github.com/fmv1992/scala_cli_parser")),
  organization := "io.github.fmv1992",
  name := "scala_cli_parser",
  scalaVersion := scala213,
  //
  // coverageMinimum := 70
  // coverageFailOnMinimum := true
  //
  // resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
  resolvers += Resolver.mavenLocal,
  //
  scalacOptions ++= (Seq("-feature", "-deprecation")
    ++
      Seq(
        "-P:semanticdb:synthetics:on",
        "-Yrangepos",
        "-Ywarn-dead-code",
        "-deprecation",
        "-feature"
        // "-Xfatal-warnings",
        // "-Ywarn-unuse"
      )
      ++ sys.env.get("SCALAC_OPTS").getOrElse("").split(" ").toSeq),
  licenses += "GPLv2" -> url("https://www.gnu.org/licenses/gpl-2.0.html"),
  version := IO
    .readLines(new File("./src/main/resources/version"))
    .mkString(""),
  // Ship resource files with each jar.
  resourceDirectory in Compile := file(".") / "./src/main/resources",
  resourceDirectory in Runtime := file(".") / "./src/main/resources",
  resourceDirectory in Test := file(".") / "./src/test/resources",
  // fmv1992_scala_utilities:33e41c7:fmv1992_scala_utilities/build.sbt:29
  test in assembly := {},
  assemblyMergeStrategy in assembly := {
    case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.rename
    case x                                   => MergeStrategy.first
  },
  //
  sonatypeProfileName := "io.github.fmv1992",
  publishMavenStyle := true,
  sonatypeProjectHosting := Some(
    GitHubHosting("fmv1992", "scala_cli_parser", "fmv1992@gmail.com")
  ),
  // or if you want to set these fields manually.
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/fmv1992/scala_cli_parser"),
      "scm:git@github.com:fmv1992/scala_cli_parser.git"
    )
  ),
  developers := List(
    Developer(
      id = "fmv1992",
      name = "Felipe Martins Vieira",
      email = "fmv1992@gmail.com",
      url = url("https://github.com/fmv1992/")
    )
  ),
  publishConfiguration := publishConfiguration.value.withOverwrite(true),
  publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(
    true
  ),
  publishTo in ThisBuild := sonatypePublishTo.value,
  credentials += Credentials(
    file(
      sys.env
        .get("SBT_CREDENTIALS_PATH")
        .getOrElse("")
    )
  ),
  usePgpKeyHex(
    sys.env
      .get("SBT_PGP_KEY")
      .getOrElse("B145230D09E5330C9A0ED5BC1FEB8CD8FBFDC1CB")
  ),
  //
  Compile / scalacOptions ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      // ???: -Ywarn-unused-import, -Xlint:unused
      case Some((2, n)) if n == 11 => List("-Ywarn-unused-import")
      case Some((2, n)) if n == 12 => List("-Ywarn-unused")
      case Some((2, n)) if n == 13 => List("-Ywarn-unused")
    }
  },
  //
  libraryDependencies ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, n)) if n == 11 => List()
      case Some((2, n)) if n == 12 =>
        List("com.sandinh" %% "scala-rewrites" % "1.0.0")
      case Some((2, n)) if n == 13 =>
        List("com.sandinh" %% "scala-rewrites" % "0.1.10-sd")
      case _ => Nil
    }
  },
  //
  // <https://scalacenter.github.io/scalafix/docs/users/installation.html>.
  libraryDependencies += "org.scalameta" %% "scalameta" % "4.3.24",
  scalafixScalaBinaryVersion := CrossVersion.binaryScalaVersion(
    scalaVersion.value
  )
)

lazy val commonDependencies = Seq(
  libraryDependencies += "io.github.fmv1992" %%% "util" % "2.6.1",
  libraryDependencies += "org.scala-lang.modules" %%% "scala-collection-compat" % "2.4.0",
    // libraryDependencies += "org.scala-native" %%% "nscplugin" % "0.4.3",
  libraryDependencies += "org.scalatest" %%% "scalatest" % "3.2.4-M1" % Test,
)

lazy val commonSettingsAndDependencies = commonSettings ++ commonDependencies

lazy val scalaNativeSettings = Seq(
  crossScalaVersions := versionsNative,
  scalaVersion := scala213, // allows to compile if scalaVersion set not 2.11
  nativeLinkStubs := true,
  nativeLinkStubs in runMain := true,
  nativeLinkStubs in Test := true,
  Test / nativeLinkStubs := true,
  sources in (Compile, doc) := Seq.empty,
)

lazy val scala_cli_parserCrossProject: sbtcrossproject.CrossProject =
  crossProject(JVMPlatform, NativePlatform)
    .crossType(CrossType.Pure)
    .in(file("."))
    .settings(
      commonSettingsAndDependencies
    )
    .jvmSettings(
      crossScalaVersions := versionsJVM,
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
