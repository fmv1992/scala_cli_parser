package fmv1992.scala_cli_parser

/** CLI parser most general trait.
  *
  * @define parseDoc Parse a sequence of strings into a sequence of
  * [[Argument Arguments]].
  */
trait CLIParser extends Parser[Seq[String], Seq[Argument]] {

  /** $parseDoc */
  def parse(args: Seq[String]): Seq[Argument]

}
