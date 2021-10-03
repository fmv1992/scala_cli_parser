package fmv1992.scala_cli_parser.conf

import java.nio.file.Path

import fmv1992.scala_cli_parser._
import fmv1992.scala_cli_parser.cli.ParserCLI

/** Get a [[fmv1992.scala_cli_parser.cli.ParserCLI CLI parser]] from a config
  * file.
  *
  * The config file is comprised of:
  *
  *   1. Comments.
  *   1. CLI option sections.
  *      i. name ('''required'''): the long name of the cli parameter.
  *      i. n ('''required'''): how many positions it should consume from the
  *         command line [[scala.collections.immutable.Seq]].
  *      i. description ('''required'''): the description of the parameter.
  *      i. default (optional): a comma separated list of default options.
  *      i. type (not implemented): the type of this parameter (e.g. list of
  *         ints, int, float, etc). This is not implemented because I think a
  *         lot of complexity will come out of this.
  *
  * An example of such a config file can be found
  * [[https://github.com/fmv1992/scala_cli_parser/tree/dev/scala_cli_parser/src/test/resources/test_cli_example_02_gnu.txt here]].
  *
  * One of the few publicly accessible objects in this package.
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
