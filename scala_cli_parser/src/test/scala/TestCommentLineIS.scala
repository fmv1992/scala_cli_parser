package fmv1992.scala_cli_parser

import org.scalatest.funsuite.AnyFunSuite

class TestCommentLineIS extends AnyFunSuite {

  val multilineComment = "# comment 01.\n# comment 02.\n \t \tNot a comment."

  test("`CommentLineIS.isPossibleInput`.") {
    assert(
      CommentLineIS(Seq.empty).isPossibleInput('#') ===
        true
    )
    assert(
      CommentLineIS(Seq.empty).isPossibleInput('x') ===
        false
    )
  }

  // test("`CommentLineIS.update`.") {
  //   assertThrows[Exception] {
  //     CommentLineIS("abcde")
  //   }
  // }

  test("`CommentLineIS.getFirstSignificantCharInLastLine`.") {

    assert(
      CommentLineIS("#").getFirstSignificantCharInLastLine
        === Some('#')
    )
    assert(
      CommentLineIS(" #").getFirstSignificantCharInLastLine
        === Some('#')
    )
    assert(
      CommentLineIS(" x").getFirstSignificantCharInLastLine
        === Some('x')
    )
    assert(
      CommentLineIS(" cc\n x \n l ").getFirstSignificantCharInLastLine
        === Some('l')
    )
  }

  test("`CommentLineIS.getMeaningfulInput`.") {
    assert(
      CommentLineIS("# My comment.\n ")
        .getMeaningfulInput() === (CommentLineIS(
        "# My comment.\n"
      ), " ".toIterable)
    )
    assert(
      CommentLineIS(multilineComment)
        .getMeaningfulInput() === (CommentLineIS(
        "# comment 01.\n# comment 02.\n"
      ), " \t \tNot a comment.".toIterable)
    )
  }

  test("`CommentLineIS.consume`.") {
    val (c1, remaining1) = CommentLineIS("").consume(multilineComment)
    val (c2, remaining2) =
      (
        CommentLineIS("# comment 01.\n# comment 02.\n".toList),
        " \t \tNot a comment.".toIterable
      )
    assert(c1 === c2)
    assert(remaining1 === remaining2)
  }

}
