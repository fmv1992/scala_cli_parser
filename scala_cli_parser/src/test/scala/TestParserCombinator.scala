package fmv1992.scala_cli_parser

import org.scalatest.funsuite.AnyFunSuite

class TestParserCombinator extends AnyFunSuite {

  val combinedCommentAndSpaceParser =
    ParserCombinator.or(CommentConfParser, SpaceConfParser)

  // test("`ParserCombinator` invalid.") {
  // assert(
  // false
  // )
  // }

  test("`ParserCombinator` valid.") {
    val parsed1 = combinedCommentAndSpaceParser.parse("\t\n")
    assert(
      parsed1 === Right("\t\n")
    )
    val parsed2 = combinedCommentAndSpaceParser.parse("# This and that.")
    assert(
      parsed2 === Right("# This and that.")
    )
    val parsed3 = combinedCommentAndSpaceParser.parse("# This and that.\n ")
    assert(
      parsed3.isLeft
    )
  }

  // test("`ParserCombinator.parse`.") {
  // assert(false)
  // }

}
