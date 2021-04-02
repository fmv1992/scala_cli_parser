package fmv1992.scala_cli_parser

import org.scalatest.EitherValues._
import org.scalatest.funsuite.AnyFunSuite

class TestParserUtils extends AnyFunSuite {

  val comment1 = "# Comment 01."
  val space1 = " "
  val combined1 = List(comment1, space1).mkString("\n")

  test("`or` valid.") {
    val parser = ParserUtils.or(CommentConfParser, SpaceConfParser)
    assert(
      parser.parse(comment1).right.value === ParsedResult(
        comment1.toSeq,
        comment1
      )
    )
    assert(
      parser.parse(space1).right.value === ParsedResult(space1.toSeq, space1)
    )
    assert(parser.parse(combined1).isLeft)
  }

  test("`and` valid.") {
    val parser =
      ParserUtils.and(
        SpaceConfParser,
        CommentConfParser,
        (
            x: ParsedResult[Seq[Char], String],
            y: ParsedResult[Seq[Char], String]
        ) => ParsedResult(x.data ++ y.data, x.result + y.result)
      )
    assert(
      parser.parse(space1 + comment1).right.value === ParsedResult(
        space1.toSeq ++ comment1.toSeq,
        space1 + comment1
      )
    )
    assert(
      parser.parse(space1).right.value === ParsedResult(space1.toSeq, space1)
    )
    assert(parser.parse(combined1).isLeft)
  }

  test("`tryAll` valid.") {
    val parser1 = ParserUtils.tryAll(CommentConfParser, SpaceConfParser)
    assert(
      parser1.parse(comment1).right.value ===
        List(ParsedResult(comment1.toList, comment1))
    )
    val parser2 = ParserUtils.tryAll(CommentConfParser, SpaceConfParser)
    assert(
      parser2.parse(combined1).right.value === List(
        ParsedResult((comment1 + '\n').toList, (comment1 + '\n')),
        ParsedResult(space1.toList, space1)
      )
    )
  }

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

}
