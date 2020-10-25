package fmv1992.scala_cli_parser

import org.scalatest.FunSuite

class TestParser extends FunSuite {

  val comment = "# This is a comment."
  val nline = "\n"
  val cline = "version: 2.27."
  val cliConfig: String = List(comment, nline, cline).mkString("\n")

  test("Test parser primitives.") {
    assert(ParserPrimitives.emptyLine(nline).isDefined)
    assert(ParserPrimitives.commentLine(comment).isDefined)
    assert(!ParserPrimitives.nameContentLine(cline).isEmpty)
    assert(!ConfCLIParser.parseString(cline).isEmpty)
  }

  test("Test parser combinators.") {
    assert(
      ConfCLIParser.parseStringOpt(comment) == ParserPrimitives
        .commentLine(comment)
    )
    assert(
      ConfCLIParser.parseStringOpt(cliConfig) == Option(
        Map(("version" → "2.27."))
      )
    )
  }

  test("Test config parsers.") {
    assert(
      Example.cli02Parser.format.keys.toSet ==
        Set("debug", "version", "sum", "help")
    )
  }

}
