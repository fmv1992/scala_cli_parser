package fmv1992.scala_cli_parser

case class SpaceConfParser(data: Seq[Char]) {

  def parse: Either[Throwable, String] = {
    if (isValid) {
      Right(data.mkString)
    } else {
      Left(ParseException())
    }
  }

  def isValid = {
    data.forall(_.isWhitespace)
  }

}

object SpaceConfParser extends ParserWithEither[Seq[Char], String] {

  def parse(input: Seq[Char]): Either[Throwable, String] = {
    SpaceConfParser(input).parse
  }

}
