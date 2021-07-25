package fmv1992.scala_cli_parser

import scala.util.Try
import scala.util.Success

/** Combine Map[String, String] into Map[String, Map[String, String]] where the
  * new key is given by `name`, which stands for the CLI name.
  */
object MapperFullConfigParser
    extends Function1[
      Seq[Try[ParsedResult[Seq[Char], Map[String, String]]]],
      Seq[Try[ParsedResult[Seq[Char], Map[String, Map[String, String]]]]]
    ] {

  private def getMaccUpdated(
      macc: Map[String, Map[String, String]],
      msss: Map[String, String]
  ): Map[String, Map[String, String]] = {
    msss.get("name") match {
      case Some(x) => {
        Map(x -> macc(""))
      }
      case None => {
        Map(macc.keys.head -> (macc(macc.keys.head) ++ msss))
      }
    }
  }

  def apply(
      v1: Seq[Try[ParsedResult[Seq[Char], Map[String, String]]]]
  ): Seq[Try[ParsedResult[Seq[Char], Map[String, Map[String, String]]]]] = {
    v1.foldLeft(
      (
        // Seq accumulator.
        Seq(Success(ParsedResult(Seq.empty, Map.empty))),
        // Nested map accumulator.
        Map("" -> Map())
      ): Tuple2[Seq[Try[
        ParsedResult[Seq[Char], Map[String, Map[String, String]]]
      ]], Map[String, Map[String, String]]]
    )(
      (
          x: Tuple2[
            Seq[scala.util.Try[fmv1992.scala_cli_parser.ParsedResult[Seq[
              Char
            ], Map[String, Map[String, String]]]]],
            Map[String, Map[String, String]]
          ],
          elem: scala.util.Try[fmv1992.scala_cli_parser.ParsedResult[Seq[
            Char
          ], Map[
            String,
            String
          ]]]
      ) => {
        val sacc = x._1
        val macc = x._2
        val pr = elem.get

        // Console.err.println("-" * 79)
        // Console.err.println(x.toString)
        // Console.err.println("-" * 79)
        // Console.err.println(elem.toString)
        // Console.err.println("-" * 79)

        // Trigger addition of macc.
        if (pr.result.isEmpty) {
          if (macc.isEmpty) {
            (sacc, Map("" -> Map.empty))
          } else {
            (
              sacc.appended(
                Success(ParsedResult(pr.data, getMaccUpdated(macc, pr.result)))
              ),
              Map("" -> Map.empty)
            )
          }
          // Update macc accumulator.
        } else {
          (sacc, getMaccUpdated(macc, pr.result))
        }
      }
    )._1
    // ()._1
  }
}
