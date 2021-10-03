package fmv1992.scala_cli_parser

import fmv1992.fmv1992_scala_utilities.util.S
import fmv1992.scala_cli_parser.cli.ArgumentCLI
import fmv1992.scala_cli_parser.conf.ParserConfigFile
import fmv1992.scala_cli_parser.util.MainTestableConfBased

private object Main extends MainTestableConfBased {

  val programName: String = "scala_cli_parser"

  val version: String = S.putfile("./src/main/resources/version")

  val CLIConfigContents =
    S.putfile("./src/main/resources/scala_cli_parser_config.conf")

  def testableMain(args: Set[ArgumentCLI]): Iterable[String] = {
    ParserConfigFile.parse(
      LazyList
        .continually(scala.io.StdIn.readLine())
        .takeWhile(_ != null)
        .mkString("\n")
    )
    Seq.empty
  }
}
