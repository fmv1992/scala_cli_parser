package fmv1992.scala_cli_parser

import java.nio.file.Path

case class StandardConfParser(p: Path)
    extends ParserWithIntermediateState[String, Seq, ParsedConfigStructure] {

  def parse(): Either[Seq[Throwable], ParsedConfigStructure] = {
    parse(scala.io.Source.fromFile(p.toFile).getLines().toSeq)
  }

  def isPossibleInput(s: String): Boolean = ???

  def parse(
      lines: Seq[String]
  ): Either[Seq[Throwable], ParsedConfigStructure] = {
    Right(ConfCLIParser.parseConf(lines.mkString("\n")))
  }

  def parse(
      content: String
  ): Either[Seq[Throwable], ParsedConfigStructure] = {
    parse(content)
  }

}
