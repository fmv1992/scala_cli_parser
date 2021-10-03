package fmv1992

/** Provide a config file based CLI parsing library.
  *
  * How to do that:
  *
  *   1. Define your `.conf` file. See the syntax
  *      [[fmv1992.scala_cli_parser.conf.ParserConfigFile here (ParserConfigFile)]].
  *      Example:
  *      [[https://github.com/fmv1992/scala_cli_parser/tree/dev/scala_cli_parser/src/test/resources/test_cli_example_02_gnu.txt here]].
  *   1. Get a [[fmv1992.scala_cli_parser.cli.ParserCLI CLI parser]] from the
  *      [[fmv1992.scala_cli_parser.conf.ParserConfigFile the conf parser (ParserConfigFile)]].
  *   1. Apply the CLI parser to a given command line flags.
  *
  * Public entities:
  *
  *   1. [[fmv1992.scala_cli_parser.conf.ParserConfigFile]].
  *      1. Which outputs [[fmv1992.scala_cli_parser.cli.ParserCLI]].
  *         1. Which uses [[fmv1992.scala_cli_parser.cli.ArgumentConf]].
  *            1. Which uses ("private trait Argument escapes its defining scope
  *               as part of type...")
  *               [[fmv1992.scala_cli_parser.cli.Argument]].
  *         1. Which ouputs [[fmv1992.scala_cli_parser.cli.ArgumentCLI]].
  *   1. [[fmv1992.scala_cli_parser.ParseException]] (not really used at the
  *      moment).
  */
package object scala_cli_parser
