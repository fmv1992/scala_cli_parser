package fmv1992.scala_cli_parser

trait ParserDataHolder[A, B] { self: Parser[A, B] =>

  def data: A

  def isValid: Boolean

}
