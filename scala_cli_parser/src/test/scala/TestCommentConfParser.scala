// project scala_cli_parserCrossProjectJVM;testOnly ~fmv1992.scala_cli_parser.TestCommentConfParser

package fmv1992.scala_cli_parser

import scala.util.Success

import org.scalatest.funsuite.AnyFunSuite

class TestCommentConfParser extends AnyFunSuite {

  val multilineComment = "# comment 01.\n# comment 02.\n \t \tNot a comment."
  val comment1 = "# Comment.\n# Other comment."

  test("`CommentConfParser.parse`.") {
    assert(
      CommentConfParser
        .parse(comment1)
        .get
        === ParsedResult(
          "# Comment.\n# Other comment.".toSeq,
          emptyMapSS
        )
    )
    // assert(
    // CommentConfParser
    // .parse(multilineComment.toSeq)
    // .get === ParsedResult(multilineComment.toSeq, emptyMapSS)
    // )
  }

  test("`CommentConfParser.partialParse`.") {
    assert(
      CommentConfParser.partialParse(
        "# Line 1.\nname: a_name."
      ) === ("name: a_name.".toSeq,
      Success(ParsedResult("# Line 1.".toSeq, emptyMapSS)))
    )
  }

}
