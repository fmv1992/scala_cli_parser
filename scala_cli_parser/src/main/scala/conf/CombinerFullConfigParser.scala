package fmv1992.scala_cli_parser.conf

import scala.util.Try

import fmv1992.scala_cli_parser._

/** Combine Map[String, Map[String, String]] into Map[String, Map[String,
  * Map[String, String]]] where the new key is given by `name`, which stands for
  * the CLI name.
  */
private object CombinerFullConfigParser
    extends Function2[Try[
      ParsedResult[Seq[Char], Map[String, Map[String, String]]]
    ], Try[
      ParsedResult[Seq[Char], Map[String, Map[String, String]]]
    ], Try[ParsedResult[Seq[Char], Map[String, Map[String, String]]]]] {

  def apply(
      v1: Try[ParsedResult[Seq[Char], Map[String, Map[String, String]]]],
      v2: Try[ParsedResult[Seq[Char], Map[String, Map[String, String]]]]
  ): Try[ParsedResult[Seq[Char], Map[String, Map[String, String]]]] = {

    v1.flatMap(a_ =>
      v2.map(b_ => {
        val intersection = a_.result.keySet.intersect(b_.result.keySet)
        if (intersection.isEmpty) {
          ParsedResult(
            a_.data ++ b_.data,
            (a_.result ++ b_.result).removed("")
          )
        } else {
          throw new RuntimeException(
            s"Intersection of dictionary keys is not empty: s'${intersection.toList.sorted}'. "
          )
        }
      })
    )
  }

}
