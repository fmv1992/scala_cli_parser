package fmv1992.scala_cli_parser

/** GNU style argument. Requirements are assured by the parser. */
case class GNUArg(longName: String, value: Seq[String]) extends Argument {

  // ???: Enforce gnu argument names and coherence inside package.

}

/** Companion object for GNUArg. */
object GNUArg {}
