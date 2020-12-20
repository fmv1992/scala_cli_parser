package fmv1992.scala_cli_parser

import fmv1992.fmv1992_scala_utilities.util.S

object TestSum extends CLIConfigTestableMain {

  val version = "0.0.0"

  val programName = "TestSum"

  val CLIConfigContents =
    S.putfile("./src/test/resources/test_cli_example_02_gnu.txt")

  def testableMain(args: Seq[Argument]): List[String] = {

    val res = args.foldLeft(0)((l, x) => {
      x match {
        case y: Argument if y.longName == "sum" => x.value.map(_.toInt).sum + l
        case _                                  => println(x); throw new Exception()
      }
    })

    List(res.toString)
  }
}
