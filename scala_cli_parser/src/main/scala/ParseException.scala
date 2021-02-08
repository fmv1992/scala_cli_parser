package fmv1992.scala_cli_parser

case class ParseException(
    val message: String = ""
) extends java.lang.Exception(message)
