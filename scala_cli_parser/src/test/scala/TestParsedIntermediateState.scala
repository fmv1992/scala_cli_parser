package fmv1992.scala_cli_parser

import org.scalatest.funsuite.AnyFunSuite

class TestParsedIntermediateState extends AnyFunSuite {

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

  test("`CommentLine.cons`.") {
    assertThrows[Exception] {
      CommentLine().cons("abcde")
    }
  }

}
