package fmv1992.scala_cli_parser

import fmv1992.fmv1992_scala_utilities.util.Reader

object Main extends CLIConfigTestableMain {

  // ???: Grab those from somewhere else.
  lazy val programName = "scala_cli_parser"
  lazy val CLIConfigPath = "src/main/resources/scala_cli_parser_config.conf"

  lazy val version =
    Reader.readLines("./src/main/resources/version").mkString("")

  def testableMain(args: Seq[Argument]): Seq[String] = {
    Seq()
  }

}
