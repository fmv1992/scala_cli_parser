package fmv1992.scala_cli_parser

import org.scalatest.funsuite.AnyFunSuite

class TestSumAndMainExample01Parser extends AnyFunSuite {

  val defaultArgs: List[String] = "--debug --verbose".split(" ").toList
  val helpArgs: List[String] = "--help".split(" ").toList

  test("Most basic test: test the idea.") {
    val parsed = Example.cli01Parser.parse(defaultArgs)
    assert(parsed === List(Arg("debug", Nil), Arg("verbose", Nil)))
  }

  test("Test functionality with TestMainExample01.") {

    assert(
      TestMainExample01.testableMain(
        getOrElseEitherShim(
          Example.cli01Parser
            .parse(defaultArgs.toList),
          throw new Exception()
        )
      )
        ===
          """
        |Got debug flag.
        |Got verbose flag.""".stripMargin.trim.split("\n").toList
    )

    assert(
      TestSum.testableMain(
        getOrElseEitherShim(
          Example.cli05TestSumParser
            .parse("--sum 2 7".split(" ").toList),
          throw new Exception()
        )
      ) === List("9")
    )

  }

  // ???: Re enable this test.
  // test("Test '--help' with TestMainExample01.") {

  //   assert(
  //     TestMainExample01.testableMain(
  //       Example.cli01Parser.parse(defaultArgs.toList)
  //     ) ==
  //       """""".stripMargin.trim.split("\n").toList
  //   )

  // }

}
