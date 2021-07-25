// project scala_cli_parserCrossProjectJVM;~testOnly fmv1992.scala_cli_parser.TestParserUtils
package fmv1992.scala_cli_parser.conf.test

import scala.util.Success

import fmv1992.scala_cli_parser.conf._
import org.scalatest.concurrent.TimeLimits
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.time.Millis
import org.scalatest.time.Span

class TestParserUtils extends AnyFunSuite with TimeLimits {

//   def parserFactory(
//       x: String
//   ): ParserWithEither[Seq[Char], ParsedResult[Seq[Char], Map[String, Int]]] =
//     ParserImpl((s: Seq[Char]) => {
//       if (s == x.toSeq) {
//         (ParsedResult(s, Map(x -> 1)): ParsedResult[
//           Seq[Char],
//           Map[String, Int]
//         ])
//       } else {
//         throw new Exception()
//       }
//     })
//
//   val bParser = parserFactory("b")
//   val aParser = parserFactory("a")
//
//   val combinerString = (
//       x: ParsedResult[Seq[Char], Map[String, Int]],
//       y: ParsedResult[Seq[Char], Map[String, Int]]
//   ) =>
//     ParsedResult(
//       x.data ++ y.data,
//       (x.result.keySet ++ y.result.keySet).map {
//         i => (i, x.result.getOrElse(i, 0) + y.result.getOrElse(i, 0))
//       }.toMap
//     )
//
  val comment1 = "# Comment 01."
  val space1 = " "

  val spaceAndComment = space1 ++ comment1
  val commentAndSpace = comment1 ++ "\n" ++ space1

  val parserSpaceAndComment =
    ParserUtils.and(
      SpaceConfParser,
      CommentConfParser
    )

  val parserCommentAndSpace =
    ParserUtils.and(
      CommentConfParser,
      SpaceConfParser
    )

  val parserCommentOrSpaceWithTry: PWT =
    ParserUtils.or(CommentConfParser, SpaceConfParser)

  val parserCommentOrSpacePartial: PP =
    ParserUtils.or(CommentConfParser, SpaceConfParser)

  val parserManyCommentsOrSpaces = ParserUtils.many(parserCommentOrSpacePartial)

  val parserManyCommentsOrSpacesOrMultiLinesConf = ParserUtils.many(
    ParserUtils.or(
      ParserUtils.newLines,
      ParserUtils.or(
        MultiLineConfParser,
        ParserUtils.or(CommentConfParser, SpaceConfParser)
      )
    )
  )

  test("`or` valid.") {
    assert(
      ParsedResult(
        comment1.toSeq,
        emptyMapSS
      ) === parserCommentOrSpaceWithTry.parse(comment1).get
    )
    assert(
      ParsedResult(
        space1.toSeq,
        emptyMapSS
      ) === parserCommentOrSpaceWithTry.parse(space1).get
    )
    assert(parserCommentOrSpaceWithTry.parse(spaceAndComment).isFailure)
    assert(parserCommentOrSpaceWithTry.parse(commentAndSpace).isFailure)
  }

  ignore("`or` invalid.") {}

  test("`and` valid.") {
    assert(
      parserSpaceAndComment
        .parse(spaceAndComment)
        .get === ParsedResult(
        space1.toSeq ++ comment1.toSeq,
        emptyMapSS
      )
    )
    assertThrows[ParseException](
      parserSpaceAndComment
        .parse("")
    )
    assertThrows[ParseException](
      parserSpaceAndComment.partialParse(commentAndSpace)
    )
//      ((
//        "",
//        Failure(ParseException(commentAndSpace))
//      ): Tuple2[String, Try[
//        ParsedResult[Seq[Char], Map[String, String]]
//      ]]) === partialParse01
//    )
  }

//
//   test("`and` invalid.") {
//     assert(
//       parserCommentAndSpace
//         .parse(space1 + comment1)
//         .isLeft
//     )
//     assert(parserCommentAndSpace.parse(spaceAndComment).isRight)
//   }
//
//   test("`tryAll` valid.") {
//     val parser1 = ParserUtils.tryAll(CommentConfParser, SpaceConfParser)
//     assert(
//       parser1.parse(comment1).right.value ===
//         List(ParsedResult(comment1.toList, emptyMapSS))
//     )
//     val parser2 = ParserUtils.tryAll(CommentConfParser, SpaceConfParser)
//     assert(
//       parser2.parse(spaceAndComment).right.value === List(
//         ParsedResult((comment1 + '\n').toList, emptyMapSS),
//         ParsedResult(space1.toList, emptyMapSS)
//       )
//     )
//   }
//
//   ignore("`tryAll` invalid.") {}
//
//   test("`allSubsequencesFromStart` valid.") {
//     assert(
//       Seq("", "a", "ab", "abc").toSet === ParserUtils
//         .allSubsequencesFromStart(
//           "abc"
//         )
//         .map(_.toString)
//         .toSet
//     )
//   }
//
//   ignore("`allSubsequencesFromStart` invalid.") {}
//

  test("`many` valid (simple).") {
    // This is wrong. The newline causes problem when joining two valid lines.
    val input =
      " # Comment 01.\n\t\t # Comment 02.\n# Comment 03.  \n\t".toSeq
    assert(
      Success(ParsedResult(input, emptyMapSS)) ===
        parserManyCommentsOrSpaces.parse(input)
    )
    val commentOnly = "# Comment 01.\n# Comment 02.\n# Comment 03."
    assert(
      Success(ParsedResult(commentOnly.toSeq, emptyMapSS)) ===
        parserManyCommentsOrSpaces.parse(commentOnly)
    )
    val spaceOnly = "\t \n\n\n \n \t \t\t \n\n"
    assert(
      Success(ParsedResult(spaceOnly.toSeq, emptyMapSS)) ===
        parserManyCommentsOrSpaces.parse(spaceOnly)
    )
  }

  test("`many` valid (complex).")(failAfter(Span(200_000, Millis))({
    val input = """
# This is a comment.
name:       | name line 01.
            | name line 02.
            |
            | name line 04.

                version: | ver a.b.c
                         | ver x.y.z
# Final comment.
      """.trim
    val parsed =
      parserManyCommentsOrSpacesOrMultiLinesConf.partialParse(input.toSeq)

    assert(
      (
        "".toSeq,
        Success(
          ParsedResult(
            input.toList,
            Map(
              "name" -> "name line 01.\nname line 02.\n\nname line 04.",
              "version" -> "ver a.b.c\nver x.y.z"
            )
          )
        )
      )
        ===
          parsed
    )
    assert(input.toSeq === parsed._2.get.data)
  }))

  test("`many` invalid.") {
    val input = "a"
    assertThrows[ParseException](
      parserManyCommentsOrSpaces.parse(input)
    )
  }

//
//   // Many should not have to guess and decrease the input w any strategy.
//   ignore("`many` \"b\"s.") {
//     val manyBsParser = ParserUtils.many(bParser, combinerString)
//     assert(manyBsParser.parse("b").right.value.result === Map("b" -> 1))
//     assert(manyBsParser.parse("bbbb").right.value.result === Map("b" -> 4))
//
//     val manyAsParser = ParserUtils.many(aParser, combinerString)
//     val manyAsOrBsParser = ParserUtils.many(
//       ParserUtils.or(manyAsParser, manyBsParser),
//       combinerString
//     )
//     assert(
//       manyAsOrBsParser.parse("ab").right.value.result === Map(
//         "a" -> 1,
//         "b" -> 1
//       )
//     )
//     assert(
//       manyAsOrBsParser.parse("abababbb").right.value.result === Map(
//         "a" -> 3,
//         "b" -> 5
//       )
//     )
//     assert(
//       manyAsOrBsParser.parse("ab ab").isLeft
//     )
//   }
//
//   ignore("`many` invalid.") {}
//
}
