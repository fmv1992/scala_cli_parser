package fmv1992.scala_cli_parser.util.test

import fmv1992.fmv1992_scala_utilities.util.S
import fmv1992.scala_cli_parser.cli.ArgumentCLI
import fmv1992.scala_cli_parser.util.MainTestableConfBased

private object TestSum extends MainTestableConfBased {

  val version = "0.0.0"

  val programName = "TestSum"

  val CLIConfigContents =
    S.putfile("./src/test/resources/test_cli_example_02_gnu.txt")

  def testableMain(args: Set[ArgumentCLI]): Seq[String] = {
    args
      .find(_.name == "help")
      .orElse(args.find(_.name == "version"))
      .orElse(args.find(_.name == "sum"))
      .getOrElse(throw new Exception()) match {
      case a: ArgumentCLI if (a.name == "help")    => getHelp
      case a: ArgumentCLI if (a.name == "version") => getVersion
      case a: ArgumentCLI if (a.name == "sum") =>
        Seq(a.values.map(_.toInt).sum.toString)
    }
  }
}
