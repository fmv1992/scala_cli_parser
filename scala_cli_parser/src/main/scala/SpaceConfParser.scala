package fmv1992.scala_cli_parser

case class SpaceConfParser(data: Seq[Char])
    extends ParserDataHolder[Seq[Char], String] {

  def parse: Either[Seq[Throwable], String] = {
    if (isValid) {
      Right(data.mkString)
    } else {
      Left(Seq(new Exception()))
    }
  }

  def isValid = {
    data.forall(_.isWhitespace)
  }

}

object SpaceConfParser extends Parser[Seq[Char], String] {

  def parse(input: Seq[Char]): Either[Seq[Throwable], String] = {
    SpaceConfParser(input).parse
  }

}
