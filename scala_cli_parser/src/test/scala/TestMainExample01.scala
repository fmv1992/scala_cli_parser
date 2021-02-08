package fmv1992.scala_cli_parser

import fmv1992.fmv1992_scala_utilities.util.S

object TestMainExample01 extends CLIConfigTestableMain {

  val version = "0.0.0"

  val programName = "TestMainExample01"

  val CLIConfigContents =
    S.putfile("./src/test/resources/test_cli_example_01.txt")

  /** Testable interface for main program. */
  def testableMain(args: Seq[Argument]): List[String] = {

    val res = args
      .foldLeft(Nil: List[String])((l, x) => {
        x match {
          case ArgImpl("debug", _)   => "Got debug flag." +: l
          case ArgImpl("verbose", _) => "Got verbose flag." +: l
          case _                     => throw new Exception()
        }
      })
      .reverse

    res
  }
}
