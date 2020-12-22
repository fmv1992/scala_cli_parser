package fmv1992.scala_cli_parser

/** General (non GNU as of now) argument.
  *
  * Requirements are assured by the parser.
  */
case class Arg(longName: String, value: Seq[String]) extends Argument {}

/** Companion object for Arg. */
object Arg {}
