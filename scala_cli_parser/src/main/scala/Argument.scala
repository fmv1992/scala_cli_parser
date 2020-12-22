package fmv1992.scala_cli_parser

/** General trait for an argument. */
trait Argument {

  def longName: String

  def value: Seq[String]

}
