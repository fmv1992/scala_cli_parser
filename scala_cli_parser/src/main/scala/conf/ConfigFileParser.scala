package fmv1992.scala_cli_parser.conf

import java.nio.file.Path

import fmv1992.scala_cli_parser._
import fmv1992.scala_cli_parser.cli.ParserCLI

object ParserConfigFile extends Parser[Path, ParserCLI] {

  def parse(input: Path): ParserCLI = {
    // fullConfigParser.parse(
    ???
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

// `ParserConfUtils` -> `ParserConfUtils`.
