package fmv1992.scala_cli_parser.util

import scala.Iterable

import fmv1992.scala_cli_parser.cli.ArgumentCLI
import fmv1992.fmv1992_scala_utilities.util.Reader

/** Provide a testable main interface: Read lines, process and output lines. */
trait TestableMain {

  def main(args: Array[String]): Unit

  // Need the command line arguments.
  // Does not need to specify the input stream (i.e. file or stdin). These
  // should be encoded by the parsed arguments.
  /** Testable interface for main program. */
  def testableMain(args: Set[ArgumentCLI]): Iterable[String]

  /** Split input [[ArgumentCLI ArgumentCLIs]] from other arguments. */
  def splitInputArgumentCLIFromOthers(
      args: Seq[ArgumentCLI]
  ): (Seq[ArgumentCLI], Seq[ArgumentCLI]) =
    splitArgumentCLIFromOthers(args, "input")

  /** Split input [[ArgumentCLI ArgumentCLIs]] from other arguments. */
  def splitArgumentCLIFromOthers(
      args: Seq[ArgumentCLI],
      name: String
  ): Tuple2[Seq[ArgumentCLI], Seq[ArgumentCLI]] = {

    def isXArg(x: ArgumentCLI): Boolean = x.name == name

    val inputArg: Seq[ArgumentCLI] = args.filter(isXArg)
    val otherArgs: Seq[ArgumentCLI] = args.filterNot(isXArg(_))
    require(inputArg.length <= 1)

    (inputArg, otherArgs)

  }

  /** Read input argument (defaults to stdin). */
  def readInputArgument(a: Seq[ArgumentCLI]): Seq[String] = {
    if (!a.isEmpty && a(0).values(0) != "null") {
      Reader.readLines(a(0).values(0))
    } else {
      scala.io.Source.stdin.getLines().toSeq
    }
  }

}
