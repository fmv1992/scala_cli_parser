/** Notice the "semi" import here. */
package fmv1992

/** Provide utilities to parse, check and manipulate CLI arguments. */
package object scala_cli_parser {
  val emptyMapSS: Map[String, String] = Map.empty

  implicit val combiner: (
      scala.util.Try[
        fmv1992.scala_cli_parser.ParsedResult[Seq[Char], Map[String, String]]
      ],
      scala.util.Try[
        fmv1992.scala_cli_parser.ParsedResult[Seq[Char], Map[String, String]]
      ]
  ) => scala.util.Try[
    fmv1992.scala_cli_parser.ParsedResult[Seq[Char], Map[String, String]]
  ] = (a, b) =>
    // a match {
    //   case Success(x) =>
    //     b match {
    //       case Success(y) =>
    //         Success(ParsedResult(x.data ++ y.data, x.result ++ y.result))
    //       case Failure(_) => throw new Exception()
    //     }
    //   case Failure(_) => throw new Exception()
    // }
    a.flatMap(
      a_ =>
        b.map(
          b_ => ParsedResult(a_.data ++ b_.data, a_.result ++ b_.result)
        )
    )
}
