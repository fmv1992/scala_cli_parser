package fmv1992.scala_cli_parser.util

import fmv1992.scala_cli_parser.conf.ParserConfigFile
import fmv1992.scala_cli_parser.cli.ArgumentCLI

/** Testable main trait with a configurable file CLI implementation.
  *
  * Combine testability with modularity.
  */
trait MainTestableConfBased extends TestableMain {

  val CLIConfigContents: String

  val version: String

  val programName: String

  /** Get program version name.
    *
    * @see [[https://www.gnu.org/prep/standards/html_node/_002d_002dversion.html#g_t_002d_002dversion]]
    */
  def printVersion: Seq[String] = {
    List(s"$programName $version")
  }

  /** Get help string.
    *
    * @see [[https://www.gnu.org/prep/standards/html_node/_002d_002dhelp.html#g_t_002d_002dhelp]]
    */
  def printHelp(format: Set[ArgumentCLI]): Seq[String] = {
    // val usage: String = s"$programName " + format.keys.toList.sorted
    //   .map(x => "--" + x.format("name"))
    //   .mkString(" ")
    // val description: String = format.keys.toList.sorted
    //   .map(x => " " * 4 + "--" + x + ": " + format(x)("help"))
    //   .mkString("\n")
    // Seq(usage, description)
    ???
  }

  /** Parse arguments, read stdin process and output to stdout.
    *
    * @see https://en.wikipedia.org/wiki/Standard_streams
    */
  def main(args: Array[String]): Unit = {
    val parser = ParserConfigFile.parse(CLIConfigContents)
    val parsed: Set[ArgumentCLI] = parser.parse(args.toList)
    // Check if either version of help are given.
    val res = if (parsed.exists(_.name == "help")) {
      printHelp(parsed)
    } else if (parsed.exists(_.name == "version")) {
      printVersion
    } else {
      testableMain(parsed)
    }
    res.foreach(println)
  }

}
