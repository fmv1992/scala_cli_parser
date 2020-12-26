package fmv1992.scala_cli_parser

import org.scalatest.funsuite.AnyFunSuite

class TestParser extends AnyFunSuite {

  val comment = "# This is a comment."
  val nline = "\n"
  val cline = "version: 2.27."
  val cliConfig: String = List(comment, nline, cline).mkString("\n")

  test("Test parser primitives.") {
    // assert(ParserPrimitives.emptyLine.parse(nline) === Option(Map.empty))
    // assert(ParserPrimitives.commentLine.parse(comment) === Option(Map.empty))
    // assert(
    //   ParserPrimitives.nameContentLine.parse(cline) === Some(
    //     Map("version" -> "2.27.")
    //   )
    // )
    assert(!ConfCLIParser.parseString(cline).isEmpty)
    // assert(!ConfCLIParser.parseString(cliConfig).isEmpty)
  }

  // test("Test parser combinators.") {
  //   assert(
  //     ConfCLIParser.parseStringOpt(comment) === ParserPrimitives.commentLine
  //       .parse(comment)
  //   )
  //   assert(
  //     ConfCLIParser.parseStringOpt(cliConfig) === Option(
  //       Map(("version" -> "2.27."))
  //     )
  //   )
  // }

  // test("Test config parsers.") {
  //   assert(
  //     Example.cli02Parser.format.keys.toSet ===
  //       Set("debug", "version", "sum", "help")
  //   )
  // }

}
