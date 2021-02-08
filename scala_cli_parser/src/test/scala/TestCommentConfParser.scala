package fmv1992.scala_cli_parser

import org.scalatest.funsuite.AnyFunSuite

class TestCommentConfParser extends AnyFunSuite {

  val multilineComment = "# comment 01.\n# comment 02.\n \t \tNot a comment."

  test("`CommentConfParser` invalid.") {
    assert(
      !CommentConfParser("abcde").isValid
    )
    assert(
      !CommentConfParser("# Comment.\n# Other comment.\n ").isValid
    )
  }

  test("`CommentConfParser` valid.") {
    assert(
      CommentConfParser("# Comment.").isValid
    )
    assert(
      CommentConfParser("# Comment.\n# Other comment.").isValid
    )
  }

  test("`CommentConfParser.parse`.") {
    val ccp = CommentConfParser("# Comment.\n# Other comment.")
    assert(
      ccp.parse.getOrElse(
        throw new Exception()
      ) === ParsedResult(
        "# Comment.\n# Other comment.".toSeq,
        "# Comment.\n# Other comment."
      )
    )
  }

  // test("`CommentConfParser.getFirstSignificantCharInLastLine`.") {

  //   assert(
  //     CommentConfParser("#").getFirstSignificantCharInLastLine
  //       === Some('#')
  //   )
  //   assert(
  //     CommentConfParser(" #").getFirstSignificantCharInLastLine
  //       === Some('#')
  //   )
  //   assert(
  //     CommentConfParser(" x").getFirstSignificantCharInLastLine
  //       === Some('x')
  //   )
  //   assert(
  //     CommentConfParser(" cc\n x \n l ").getFirstSignificantCharInLastLine
  //       === Some('l')
  //   )
  // }

  // test("`CommentConfParser.getMeaningfulInput`.") {
  //   assert(
  //     CommentConfParser("# My comment.\n ")
  //       .getMeaningfulInput() === (CommentConfParser(
  //       "# My comment.\n"
  //     ), " ".toIterable)
  //   )
  //   assert(
  //     CommentConfParser(multilineComment)
  //       .getMeaningfulInput() === (CommentConfParser(
  //       "# comment 01.\n# comment 02.\n"
  //     ), " \t \tNot a comment.".toIterable)
  //   )
  // }

  // test("`CommentConfParser.consume`.") {
  //   val (c1, remaining1) = CommentConfParser("").consume(multilineComment)
  //   val (c2, remaining2) =
  //     (
  //       CommentConfParser("# comment 01.\n# comment 02.\n".toList),
  //       " \t \tNot a comment.".toIterable
  //     )
  //   assert(c1 === c2)
  //   assert(remaining1 === remaining2)
  // }

}
