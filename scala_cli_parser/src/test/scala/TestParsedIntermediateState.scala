package fmv1992.scala_cli_parser

import org.scalatest.funsuite.AnyFunSuite

class TestParsedIntermediateState extends AnyFunSuite {

  val multilineComment = "# comment 01.\n# comment 02.\n \t \tNot a comment."

  test("`CommentLine.isPossibleInput`.") {
    assert(
      CommentLine(Seq.empty).isPossibleInput('#') ===
        true
    )
    assert(
      CommentLine(Seq.empty).isPossibleInput('x') ===
        false
    )
  }

  test("`CommentLine.update`.") {
    assertThrows[Exception] {
      CommentLine().update("abcde")
    }
  }

  test("`CommentLine.hasMeaningfulInputAccumulated`.") {
    assert(CommentLine("x ").hasMeaningfulInputAccumulated() === false)
    assert(CommentLine("# ").hasMeaningfulInputAccumulated() === true)
    assert(
      CommentLine()
        .update("# comment.")
        .hasMeaningfulInputAccumulated() === true
    )
    assertThrows[Exception] {
      assert(
        CommentLine()
          .update("| comment.")
          .hasMeaningfulInputAccumulated() === true
      )
    }
  }

  test("`CommentLine.getFirstSignificantCharInLine`.") {

    assert(
      CommentLine("#").getFirstSignificantCharInLine
        === Some('#')
    )
    assert(
      CommentLine(" #").getFirstSignificantCharInLine
        === Some('#')
    )
    assert(
      CommentLine(" x").getFirstSignificantCharInLine
        === Some('x')
    )
    assert(
      CommentLine(" cc\n x \n l ").getFirstSignificantCharInLine
        === Some('l')
    )
  }

  test("`CommentLine.getMeaningfulInput`.") {
    assert(
      CommentLine("# My comment.\n ")
        .getMeaningfulInput() === (CommentLine(
        "# My comment.\n"
      ), " ".toIterable)
    )
    assert(
      CommentLine(multilineComment)
        .getMeaningfulInput() === (CommentLine(
        "# comment 01.\n# comment 02.\n"
      ), " \t \tNot a comment.".toIterable)
    )
  }

  // test("`CommentLine.consume`.") {
  //   val (c1, remaining1) = CommentLine().consume(multilineComment)
  //   val (c2, remaining2) =
  //     (
  //       CommentLine("# comment 01.\n# comment 02.\n".toList),
  //       " \t \tNot a comment."
  //     )
  //   assert(c1 === c2)
  //   assert(remaining1.toString === remaining2)
  // }

}
