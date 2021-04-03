package fmv1992.scala_cli_parser

import org.scalatest.EitherValues._
import org.scalatest.funsuite.AnyFunSuite

class TestParseException extends AnyFunSuite {

  val input01 = "abc"
  val input02 = "" * 10 + "x" + " " * 10

  val parseException = CommentConfParser.parse(input01).left.value

  test("`CommentConfParser` invalid throws ParseException.") {
    // ???: Revert adding blinking
    // `scala_cli_parser:b45d9a7:scala_cli_parser/src/main/scala/ParseException.scala:15`
    // to exceptions.
    assert(
      parseException.isInstanceOf[ParseException]
    )
    assert(
      parseException.getMessage === "ErrorPositionExisting(0,0,1)"
    )
  }

}
