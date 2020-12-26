/** Notice the "semi" import here. */
package fmv1992

/** Provide utilities to parse, check and manipulate CLI arguments. */
package object scala_cli_parser {
  type OldParserType = String => Option[Map[String, String]]
  type ParsedConfigStructure = Map[String, Map[String, String]]
  val emptyMapSS: Map[String, String] = Map.empty

  def getOrElseEitherShim[L, R](e: Either[L, R], or: => R): R = {
    e match {
      case Right(b) => b
      case _        => or
    }
  }

  def mapEitherShim[L, R, A](e: Either[L, R], f: R => A): Either[L, A] = {
    e match {
      case Right(b) => Right(f(b))
      case Left(l)  => Left(l)
    }
  }

  def orElseEitherShim[L, R](
      x: Either[L, R],
      or: => Either[L, R]
  ): Either[L, R] =
    x match {
      case Right(_) => x
      case _        => or
    }

}
