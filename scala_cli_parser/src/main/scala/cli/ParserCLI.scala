package fmv1992.scala_cli_parser.cli

import fmv1992.scala_cli_parser.Parser

trait ArgumentCLI

trait ArgumentConf

trait ParserCLI extends Parser[Seq[String], Set[ArgumentCLI]] {

  def arguments: Set[ArgumentConf]

  def parse(input: Seq[String]): Set[ArgumentCLI] = ???

}

object ParserCLI {

  def apply(input: Map[String, Map[String, String]]): ParserCLI = {
    ???
  }

}
