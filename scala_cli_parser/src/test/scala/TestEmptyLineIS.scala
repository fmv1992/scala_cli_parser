package fmv1992.scala_cli_parser

import org.scalatest.funsuite.AnyFunSuite

class TestEmptyLineIS extends AnyFunSuite {

  val multilineComment = "# comment 01.\n# comment 02.\n \t \tNot a comment."

  test("`EmptyLineIS.isPossibleInput`.") {
    assert(
      EmptyLineIS(Seq.empty).isPossibleInput('#') ===
        false
    )
    assert(
      EmptyLineIS(Seq.empty).isPossibleInput('\n') ===
        true
    )
    assert(
      EmptyLineIS(Seq.empty).isPossibleInput(' ') ===
        true
    )
  }

  test("`EmptyLineIS.update`.") {
    assertThrows[Exception] {
      EmptyLineIS().update("abcde")
    }
  }

  test("`EmptyLineIS.getMeaningfulInput`.") {
    assert(
      EmptyLineIS(" \t ")
        .getMeaningfulInput() === (EmptyLineIS(
        " \t "
      ), Iterable.empty)
    )
  }

  test("`EmptyLineIS.consume`.") {
    val (c1, remaining1): Tuple2[
      ParsedIntermediateState[Char, Map[String, String]],
      Iterable[Char]
    ] =
      EmptyLineIS("\t").consume(" " * 100)
    val (c2, remaining2) =
      (
        EmptyLineIS("\t" + (" " * 100)),
        "".toIterable
      )
    assert(c1 === c2)
    assert(remaining1 === remaining2)
  }

}
