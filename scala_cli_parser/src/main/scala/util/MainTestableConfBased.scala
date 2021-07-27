package fmv1992.scala_cli_parser.util

import fmv1992.scala_cli_parser.cli.ArgumentCLI
import fmv1992.scala_cli_parser.cli.ArgumentConf
import fmv1992.scala_cli_parser.cli.ParserCLI
import fmv1992.scala_cli_parser.conf.ParserConfigFile

/** Testable main trait with a configurable file CLI implementation.
  *
  * Combine testability with modularity.
  */
trait MainTestableConfBased extends TestableMain {

  val CLIConfigContents: String

  lazy private val parserConf = ParserConfigFile.parse(CLIConfigContents)

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
  def printHelp: Seq[String] = {
    val parserConfDeterministic = parserConf.arguments.toSeq.sortBy(_.name)
    val usage: String =
      s"$programName " + parserConfDeterministic
        .map(x => "--" + x.name)
        .mkString(" ")
    val description: String = parserConfDeterministic.toList
      .map(x => {
        val headOfFirstLine = " " * 4 + "--" + x.name + ": "
        val descriptionLines = x.description.linesIterator.toList
        val (descriptionHead, descriptionTail) =
          (descriptionLines.head, descriptionLines.tail)
        val descriptionIndented = descriptionHead + descriptionTail
          .map(
            x =>
              if (x.isEmpty) ""
              else ((" " * headOfFirstLine.length) + x)
          )
          .mkString("\n")
        headOfFirstLine + descriptionIndented
      })
      .mkString("\n")
    Seq(usage, description)
  }

  /** Parse arguments, read stdin process and output to stdout.
    *
    * @see https://en.wikipedia.org/wiki/Standard_streams
    */
  def main(args: Array[String]): Unit = {
    val parsed: Set[ArgumentCLI] = parserConf.parse(args.toList)
    // Check if either version of help are given.
    val res = if (parsed.exists(_.name == "help")) {
      printHelp
    } else if (parsed.exists(_.name == "version")) {
      printVersion
    } else {
      testableMain(parsed)
    }
    res.foreach(println)
  }

}
