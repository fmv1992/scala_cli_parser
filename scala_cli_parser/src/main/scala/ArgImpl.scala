package fmv1992.scala_cli_parser

/** General (non GNU as of now) argument.
  *
  * Requirements are assured by the parser.
  */
case class ArgImpl(longName: String, value: Seq[String]) extends Argument {}

/** Companion object for ArgImpl. */
object ArgImpl {}
