package fmv1992.scala_cli_parser

import org.scalatest.EitherValues._
import org.scalatest.funsuite.AnyFunSuite

class TestParserUtils extends AnyFunSuite {

  val comment1 = "# Comment 01."
  val space1 = " "
  val combined1 = List(comment1, space1).mkString("\n")

  test("`or` valid.") {
    val parser = ParserUtils.or(CommentConfParser, SpaceConfParser)
    assert(parser.parse(comment1).right.value === comment1)
    assert(parser.parse(space1).right.value === space1)
    assert(parser.parse(combined1).isLeft)
  }

}
