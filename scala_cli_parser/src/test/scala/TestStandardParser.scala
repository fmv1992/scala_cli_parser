package fmv1992.scala_cli_parser

import org.scalatest.funsuite.AnyFunSuite

class TestSumAndMainExample01Parser extends AnyFunSuite {

  val defaultArgs = "--debug --verbose".split(" ").toList

  test("Most basic test: test the idea.") {
    val parsed = Example.cli01Parser.parse(defaultArgs)
    assert(parsed == List(Arg("debug", Nil), Arg("verbose", Nil)))
  }

  test("Test functionality with TestMainExample01.") {

    assert(
      TestMainExample01.testableMain(
        Example.cli01Parser.parse(defaultArgs.toList)
      ) ==
        """
        |Got debug flag.
        |Got verbose flag.""".stripMargin.trim.split("\n").toList
    )

    assert(
      TestSum.testableMain(
        Example.cli05TestSumParser.parse("--sum 2 7".split(" ").toList)
      ) == List("9")
    )

  }

}

class TestStandardParser extends AnyFunSuite {

  test("Test API.") {

    // Test valid instantiation.
    StandardParser(Example.cli02File)

  }

  test("Test default keys.") {

    val parser = StandardParser(Example.cli06File)
    assert(!parser.parse(List()).isEmpty)

    val parsed = parser.parse(List("--execute", "ten"))
    assert(parsed(0).value(0) == "ten")

  }

}
