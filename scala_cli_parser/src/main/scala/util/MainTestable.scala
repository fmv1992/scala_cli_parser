package fmv1992.scala_cli_parser.util

import scala.Iterable

import fmv1992.scala_cli_parser.cli.Argument
import fmv1992.scala_cli_parser.cli.ArgumentCLI

/** Provide a testable main interface: Read lines, process and output lines. */
trait TestableMain {

  def main(args: Array[String]): Unit

  // Need the command line arguments.
  // Does not need to specify the input stream (i.e. file or stdin). These
  // should be encoded by the parsed arguments.
  /** Testable interface for main program. */
  def testableMain(args: Set[ArgumentCLI]): Iterable[String]

}
