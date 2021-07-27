package fmv1992.scala_cli_parser.conf

import java.nio.file.Path

import fmv1992.scala_cli_parser._
import fmv1992.scala_cli_parser.cli.ParserCLI

/** Config file parser.
  *
  *  One of the few publicly accessible objects in this package.
  */
object ParserConfigFile extends Parser[Path, ParserCLI] {

  def parse(input: Path): ParserCLI = {
    parse(scala.io.Source.fromFile(input.toFile).getLines().mkString("\n"))
  }

  def parse(input: String): ParserCLI = {
    ParserCLI(fullConfigParser.parse(input).get.result)
  }

  private def fullConfigParser =
    ParserConfUtils.many(
      ParserConfUtils.or(
        ParserConfUtils.newLines,
        ParserConfUtils.or(
          CommentConfParser,
          ParserConfUtils.or(
            SolidLineConfParser,
            ParserConfUtils.or(MultiLineConfParser, SpaceConfParser)
          )
        )
      )
    )(MapperFullConfigParser, CombinerFullConfigParser)

}
