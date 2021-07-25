// project scala_cli_parserCrossProjectJVM;testOnly ~fmv1992.scala_cli_parser.TestCommentConfParser
package fmv1992.scala_cli_parser.conf.test

import scala.util.Failure
import scala.util.Success

import fmv1992.scala_cli_parser.conf._
import org.scalatest.funsuite.AnyFunSuite

class TestCommentConfParser extends AnyFunSuite {

  val multilineCommentWithError =
    "# comment 01.\n# comment 02.\n \t \tNot a comment."
  val multilineComment01 = "# Comment 01.\n# Comment 02."
  val comment1 = "# Comment.\n# Other comment."

  test("`CommentConfParser.parse`.") {
    assert(
      ParsedResult(
        "# Comment.\n# Other comment.".toSeq,
        emptyMapSS
      ) ===
        CommentConfParser
          .parse(comment1)
          .get
    )
    assertThrows[ParseException](
      CommentConfParser
        .parse(multilineCommentWithError.toSeq)
        .get
    )
  }

  test("`CommentConfParser.partialParse`.") {
    assert(
      (
        "name: a_name.".toSeq,
        Success(ParsedResult("# Line 1.\n".toSeq, emptyMapSS))
      ) ===
        CommentConfParser.partialParse(
          "# Line 1.\nname: a_name."
        )
    )
    assert(
      (" ".toSeq, Failure(ParseException(" "))) ===
        CommentConfParser.partialParse(
          " "
        )
    )
    assert(
      (
        " a".toSeq,
        Success(ParsedResult("# Comment 02.\n".toSeq, emptyMapSS))
      ) ===
        CommentConfParser.partialParse(
          "# Comment 02.\n a"
        )
    )
  }

  test(
    "Test `CommentConfParser` catches multi line comments in a single invokation."
  ) {
    val commentAndComment =
      ParserUtils.and(CommentConfParser, CommentConfParser)
    assert(
      (Seq.empty, Failure(ParseException(""))) ===
        commentAndComment.partialParse(multilineComment01)
    )
    assert(
      (
        Seq.empty,
        Success(ParsedResult(multilineComment01.toSeq, emptyMapSS))
      ) ===
        CommentConfParser.partialParse(multilineComment01)
    )
  }

  test("`CommentConfParser` includes the trailing new line.") {
    val input = "# # Comment 01.\n# Comment 02.\n"
    assert(
      input.toSeq ===
        CommentConfParser.partialParse(input)._2.get.data.toSeq
    )
  }

}
