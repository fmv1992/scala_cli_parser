package fmv1992.scala_cli_parser

import org.scalatest.EitherValues._
import org.scalatest.funsuite.AnyFunSuite

class TestParserUtils extends AnyFunSuite {

  val comment1 = "# Comment 01."
  val space1 = " "
  val combined1 = List(comment1, space1).mkString("\n")

  val parserSpaceAndComment =
    ParserUtils.and(
      SpaceConfParser,
      CommentConfParser,
      (
          x: ParsedResult[Seq[Char], Map[String, String]],
          y: ParsedResult[Seq[Char], Map[String, String]]
      ) => ParsedResult(x.data ++ y.data, x.result ++ y.result)
    )

  val parserCommentAndSpace =
    ParserUtils.and(
      CommentConfParser,
      SpaceConfParser,
      (
          x: ParsedResult[Seq[Char], Map[String, String]],
          y: ParsedResult[Seq[Char], Map[String, String]]
      ) => ParsedResult(x.data ++ y.data, x.result ++ y.result)
    )

  test("`or` valid.") {
    val parser = ParserUtils.or(CommentConfParser, SpaceConfParser)
    assert(
      parser.parse(comment1).right.value === ParsedResult(
        comment1.toSeq,
        emptyMapSS
      )
    )
    assert(
      parser.parse(space1).right.value === ParsedResult(
        space1.toSeq,
        emptyMapSS
      )
    )
    assert(parser.parse(combined1).isLeft)
  }

  ignore("`or` invalid.") {}

  test("`and` valid.") {
    assert(
      parserSpaceAndComment
        .parse(space1 + comment1)
        .right
        .value === ParsedResult(
        space1.toSeq ++ comment1.toSeq,
        emptyMapSS
      )
    )
    assert(
      parserSpaceAndComment
        .parse("")
        .isLeft
    )
    assert(parserSpaceAndComment.parse(combined1).isLeft)
  }

  test("`and` invalid.") {
    assert(
      parserCommentAndSpace
        .parse(space1 + comment1)
        .isLeft
    )
    assert(parserCommentAndSpace.parse(combined1).isRight)
  }

  test("`tryAll` valid.") {
    val parser1 = ParserUtils.tryAll(CommentConfParser, SpaceConfParser)
    assert(
      parser1.parse(comment1).right.value ===
        List(ParsedResult(comment1.toList, emptyMapSS))
    )
    val parser2 = ParserUtils.tryAll(CommentConfParser, SpaceConfParser)
    assert(
      parser2.parse(combined1).right.value === List(
        ParsedResult((comment1 + '\n').toList, emptyMapSS),
        ParsedResult(space1.toList, emptyMapSS)
      )
    )
  }

  ignore("`tryAll` invalid.") {}

  test("`allSubsequencesFromStart` valid.") {
    assert(
      Seq("", "a", "ab", "abc").toSet === ParserUtils
        .allSubsequencesFromStart(
          "abc"
        )
        .map(_.toString)
        .toSet
    )
  }

  ignore("`allSubsequencesFromStart` invalid.") {}

}
