package fmv1992.scala_cli_parser.util

import scala.Iterable

import fmv1992.scala_cli_parser.cli.Argument
import fmv1992.scala_cli_parser.cli.ArgumentCLI

// https://en.wikipedia.org/wiki/Standard_streams
// object StandardStreamMain {
//
// ???
//
// }

/** Provide a testable main interface: Read lines, process and output lines. */
trait TestableMain {

  def main(args: Array[String]): Unit

  // Need the command line arguments.
  // Does not need to specify the input stream (i.e. file or stdin). These
  // should be encoded by the parsed arguments.
  /** Testable interface for main program. */
  def testableMain(args: Set[ArgumentCLI]): Iterable[String]

  /** Split input [[Argument Arguments]] from other arguments. */
  def splitInputArgumentFromOthers(
      args: Seq[Argument]
  ): (Seq[Argument], Seq[Argument]) =
    splitArgumentFromOthers(args, "input")

  /** Split input [[Argument Arguments]] from other arguments. */
  def splitArgumentFromOthers(
      args: Seq[Argument],
      name: String
  ): Tuple2[Seq[Argument], Seq[Argument]] = {

    def isXArg(x: Argument): Boolean = x.name == name

    val inputArg: Seq[Argument] = args.filter(isXArg)
    val otherArgs: Seq[Argument] = args.filterNot(isXArg(_))
    require(inputArg.length <= 1)

    (inputArg, otherArgs)

  }

  // /** Read input argument (defaults to stdin). */
  // def readInputArgument(a: Seq[Argument]): Seq[String] = {
  //   if (!a.isEmpty && a(0).value(0) != "null") {
  //     Reader.readLines(a(0).value(0))
  //   } else {
  //     scala.io.Source.stdin.getLines().toSeq
  //   }
  // }

}
