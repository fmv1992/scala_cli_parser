package fmv1992.scala_cli_parser

import scala.util.Try

/** Combine Map[String, Map[String, String]] into Map[String, Map[String, Map[String, String]]] where the
  * new key is given by `name`, which stands for the CLI name.
  */
object CombinerFullConfigParser
    extends Function2[Try[
      ParsedResult[Seq[Char], Map[String, Map[String, String]]]
    ], Try[
      ParsedResult[Seq[Char], Map[String, Map[String, String]]]
    ], Try[ParsedResult[Seq[Char], Map[String, Map[String, String]]]]] {

  def apply(
      v1: Try[ParsedResult[Seq[Char], Map[String, Map[String, String]]]],
      v2: Try[ParsedResult[Seq[Char], Map[String, Map[String, String]]]]
  ): Try[ParsedResult[Seq[Char], Map[String, Map[String, String]]]] = {

    v1.flatMap(
      a_ =>
        v2.map(
          b_ => {
            // CURRENT: Nest the maps with `lookup(name)` as their key.
            val intersection =
              (a_.result.keySet.intersect(b_.result.keySet) - "")
            // CURRENT: sapces in indented content would zero this stateful combiner.
            val otherKeys =
              a_.result.keySet.union(b_.result.keySet) - intersection
            val flattenedMap = intersection.fold
            if (intersection.isEmpty) {
              ParsedResult(
                a_.data ++ b_.data,
                a_.result ++ b_.result
              )
            } else {
              throw new RuntimeException(
                s"Intersection of dictionary keys is not empty: s'${intersection.toList.sorted}'. "
              )
            }
          }
        )
    )
  }

}
