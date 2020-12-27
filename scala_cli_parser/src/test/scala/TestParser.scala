package fmv1992.scala_cli_parser

import org.scalatest.funsuite.AnyFunSuite

class TestParser extends AnyFunSuite {

  val comment = "# This is a comment."
  val nline = "\n"
  val contentLine = "version: 2.27."
  val cliConfig: String = List(comment, nline, contentLine).mkString("\n")

  test("Test parser primitives.") {
    assert(ParserPrimitives.emptyLine.parse(nline) === Right(Map.empty))
    assert(ParserPrimitives.emptyLine.parse(comment) === Left(Seq.empty))

    assert(ParserPrimitives.commentLine.parse(comment) === Right(Map.empty))
    assert(
      ParserPrimitives.nameContentLine.parse(contentLine) === Right(
        Map("version" -> "2.27.")
      )
    )
    assert(
      ConfCLIParser.parseString(contentLine) ===
        Map("version" -> "2.27.")
    )
    assert(ConfCLIParser.parseString(cliConfig) === Map("version" -> "2.27."))
  }

  test("Test parser combinators.") {
    assert(
      ConfCLIParser.parseStringOpt(comment) === ParserPrimitives.commentLine
        .parse(comment)
    )
    assert(
      ConfCLIParser.parseStringOpt(cliConfig) === Right(
        Map(("version" -> "2.27."))
      )
    )
  }

  test("Test config parsers.") {
    assert(
      Example.cli02Parser.format.keys.toSet ===
        Set("debug", "version", "sum", "help")
    )
  }

}
