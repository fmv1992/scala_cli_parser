package fmv1992.scala_cli_parser

import java.nio.file.Path

case class StandardConfParser(p: Path)
    extends ParserWithIntermediateState[String, Seq, ParsedConfigStructure] {

  def parse(): ParsedConfigStructure = {
    parse(scala.io.Source.fromFile(p.toFile).getLines().toSeq)
  }

  def isPossibleInput(s: String): Boolean = ???

  def parse(lines: Seq[String]): ParsedConfigStructure = {
    ???
  }

}
