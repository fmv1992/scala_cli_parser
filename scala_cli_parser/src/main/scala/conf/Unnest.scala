package fmv1992.scala_cli_parser.conf

import scala.util.Success
import scala.util.Try

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
    // Process the sequence of `ParsedResult`s merging them when a block
    // **starting** with `name:` appears.
    val folded = v1
      .foldLeft(
        (
          // Seq of parsed results accumulator.
          Seq(Success(ParsedResult(Seq.empty, Map.empty))),
          // `ParsedResult.data` accumulator.
          Seq.empty,
          // Nested map accumulator.
          Map("" -> Map())
        ): Tuple3[Seq[Try[
          ParsedResult[Seq[Char], Map[String, Map[String, String]]]
        ]], Seq[Char], Map[String, Map[String, String]]]
      )(
        (
            x: Tuple3[
              Seq[scala.util.Try[ParsedResult[Seq[
                Char
              ], Map[String, Map[String, String]]]]],
              Seq[Char],
              Map[String, Map[String, String]]
            ],
            elem: scala.util.Try[ParsedResult[Seq[
              Char
            ], Map[
              String,
              String
            ]]]
        ) => {
          val spracc = x._1
          val schar = x._2
          val macc = x._3
          val pr = elem.get

          // Console.err.println("-" * 79)
          // Console.err.println(x.toString)
          // Console.err.println("-" * 79)
          // Console.err.println(elem.toString)
          // Console.err.println("-" * 79)

          // If we get a "name" we save `macc` and start a new one.
          if (pr.result.get("name").isDefined) {
            Map(pr.result("name") -> Map.empty): Map[
              String,
              Map[String, String]
            ]
            (
              spracc.appended(
                Success(
                  ParsedResult(
                    schar,
                    macc
                  )
                )
              ),
              pr.data,
              getMaccUpdated(Map("" -> Map()), pr.result)
            )
            // Otherwise we just accumulate `macc`.
          } else {
            (spracc, schar ++ pr.data, getMaccUpdated(macc, pr.result))
          }

        }
      )
    // Manage last not folded block.
    val (retIncomplete, dataLeft, mapLeft) = folded
    // ???.
    retIncomplete.appended(Success(ParsedResult(dataLeft, mapLeft)))
  }
}
