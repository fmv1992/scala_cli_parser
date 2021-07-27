package fmv1992.scala_cli_parser.util.test

import fmv1992.fmv1992_scala_utilities.util.S
import fmv1992.scala_cli_parser.cli.ArgumentCLI
import fmv1992.scala_cli_parser.util.MainTestableConfBased

object TestSum extends MainTestableConfBased {

  val version = "0.0.0"

  val programName = "TestSum"

  val CLIConfigContents =
    S.putfile("./src/test/resources/test_cli_example_02_gnu.txt")

  def testableMain(args: Set[ArgumentCLI]): List[String] = {

    val res = args.foldLeft(0)((l, x) => {
      x match {
        case y: ArgumentCLI if y.name == "sum" => x.values.map(_.toInt).sum + l
        case _                                 => throw new Exception()
      }
    })

    List(res.toString)
  }
}
