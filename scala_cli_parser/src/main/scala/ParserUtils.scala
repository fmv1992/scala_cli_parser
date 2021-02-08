package fmv1992.scala_cli_parser

object ParserUtils {

  def or[A, B](
      p1: ParserWithEither[A, B],
      p2: ParserWithEither[A, B]
  ): ParserWithEither[A, B] = {
    (x: A) =>
      p1.parse(x) match {
        case Left(_)  => p2.parse(x)
        case Right(r) => Right(r)
      }
  }

}
