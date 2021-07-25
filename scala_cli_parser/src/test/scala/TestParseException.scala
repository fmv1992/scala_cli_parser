package fmv1992.scala_cli_parser.test

import fmv1992.scala_cli_parser._
import org.scalatest.funsuite.AnyFunSuite

// This is factored out into
// `scala_cli_parser:15d3f98:scala_cli_parser/.native/src/test/scala/TestParseExceptionNative.scala:4`
// and
// `scala_cli_parser:15d3f98:scala_cli_parser/.jvm/src/test/scala/TestParseExceptionJVM.scala:3`.
class TestParseException extends AnyFunSuite {

  val input01 = "abc"
  val input02 = "" * 10 + "x" + " " * 10

  val parseException = CommentConfParser.parse(input01)

  ignore("`CommentConfParser` invalid throws ParseException.") {
    // // ???: Revert adding blinking
    // // `scala_cli_parser:b45d9a7:scala_cli_parser/src/main/scala/ParseException.scala:15`
    // // to exceptions.
    // assert(
    //   parseException.isInstanceOf[ParseException]
    // )
    // assert(
    //   parseException.getMessage === "ErrorPositionExisting(0,0,1)"
    // )
  }

}
