package fmv1992.scala_cli_parser.util.test

import org.scalatest.funsuite.AnyFunSuite
import fmv1992.scala_cli_parser.cli.ParserCLI
import fmv1992.scala_cli_parser.conf.ParserConfigFile
import java.nio.file.Paths
import fmv1992.scala_cli_parser.cli.ArgumentCLI

class TestMainTestableConfBased extends AnyFunSuite {

  val defaultArgs: List[String] = "--version --help".split(" ").toList
  val helpArgs: List[String] = "--help".split(" ").toList

  val parserCLI: ParserCLI = ParserConfigFile.parse(
    Paths.get(
      "src/test/resources/test_cli_example_05_sum.txt"
    )
  )
  // val parsed: Set[ArgumentCLI] = parserCLI.parse(defaultArgs)

  test("Most basic test: test the idea.") {
    // assert(3 === parserCLI.arguments.size)
    val parsed = ParserConfigFile
      .parse(
        Paths.get(
          "src/test/resources/test_cli_example_05_sum.txt"
        )
      )
      .parse(defaultArgs)
    assert(
      Set(
        ArgumentCLI("version", "int", List()),
        ArgumentCLI("help", "int", List())
      ) === parsed
    )
  }

//  test("Test functionality with TestMainExample01.") {
//
//    assert(
//      TestMainExample01.testableMain(
//        getOrElseEitherShim(
//          Example.cli01Parser
//            .parse(defaultArgs.toList),
//          throw new Exception()
//        )
//      )
//        ===
//          """
//        |Got debug flag.
//        |Got verbose flag.""".stripMargin.trim.split("\n").toList
//    )
//
//    assert(
//      TestSum.testableMain(
//        getOrElseEitherShim(
//          Example.cli05TestSumParser
//            .parse("--sum 2 7".split(" ").toList),
//          throw new Exception()
//        )
//      ) === List("9")
//    )
//
//  }

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
