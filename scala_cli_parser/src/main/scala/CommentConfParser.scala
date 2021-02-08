package fmv1992.scala_cli_parser

case class CommentConfParser(data: Seq[Char]) {

  def parse: Either[Throwable, String] = {
    if (isValid) {
      Right(data.mkString)
    } else {
      Left(ParseException())
    }
  }

  def isValid = {
    @scala.annotation.tailrec
    def go(da: Seq[Char]): Boolean = {
      if (da.isEmpty) {
        true
      } else if (da.head == '#') {
        val spaceIndex = da.indexOf('\n')
        if (spaceIndex == -1) {
          true
        } else {
          go(da.drop(spaceIndex + 1))
        }
      } else {
        false
      }
    }
    go(data)
  }

}

object CommentConfParser extends ParserWithEither[Seq[Char], String] {

  def parse(input: Seq[Char]): Either[Throwable, String] = {
    CommentConfParser(input).parse
  }

}
