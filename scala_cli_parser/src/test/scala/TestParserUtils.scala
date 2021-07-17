// project scala_cli_parserCrossProjectJVM;~testOnly fmv1992.scala_cli_parser.TestParserUtils

package fmv1992.scala_cli_parser

import org.scalatest.funsuite.AnyFunSuite

class TestParserUtils extends AnyFunSuite {

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

  val parserSpaceAndComment =
    ParserUtils.and(
      SpaceConfParser,
      CommentConfParser
    )
//
//   val parserCommentAndSpace =
//     ParserUtils.and(
//       CommentConfParser,
//       SpaceConfParser,
//       standardCombiner
//     )
//

  test("`or` valid.") {
    val parser = ParserUtils.or(CommentConfParser, SpaceConfParser)
    assert(
      ParsedResult(
        comment1.toSeq,
        emptyMapSS
      ) === parser.parse(comment1).get
    )
    assert(
      ParsedResult(
        space1.toSeq,
        emptyMapSS
      ) === parser.parse(space1).get
    )
    assert(parser.parse(spaceAndComment).isFailure)
  }

//
//   ignore("`or` invalid.") {}
//
  test("`and` valid.") {
    assert(
      parserSpaceAndComment
        .parse(spaceAndComment)
        .get === ParsedResult(
        space1.toSeq ++ comment1.toSeq,
        emptyMapSS
      )
    )
    // assert(
    // parserSpaceAndComment
    // .parse("")
    // .isLeft
    // )
    // assert(parserSpaceAndComment.parse(spaceAndComment).isLeft)
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
//   ignore("`many` valid.") {
//     // This is wrong. The newline causes problem when joining two valid lines.
//     val parserMany = ParserUtils.many(SingleLineConfParser, standardCombiner)
//     assert(
//       parserMany.parse(List("n: 10", "required: true").mkString("\n")) ===
//         Right(
//           ParsedResult(
//             "n: 10\nrequired: true".toSeq,
//             Map("n" -> "10", "required" -> "true")
//           )
//         )
//     )
//   }
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
