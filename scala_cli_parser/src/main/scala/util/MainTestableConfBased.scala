package fmv1992.scala_cli_parser.util

import scala.reflect.ClassTag

import fmv1992.scala_cli_parser.cli.ArgumentCLI
import fmv1992.scala_cli_parser.conf.ParserConfigFile

/** Testable main trait with a configurable file CLI implementation.
  *
  * Combine testability with modularity.
  *
  * One of the few publicly accessible entities in this package.
  */
trait MainTestableConfBased extends TestableMain {

  // ???: The contents are pasted here since Scala Native cannot use java
  // resources at this point.
  protected[this] val CLIConfigContents: String

  lazy private val parserConf = ParserConfigFile.parse(CLIConfigContents)

  val version: String

  val programName: String

  /** Get program version name.
    *
    * @see
    *   [[https://www.gnu.org/prep/standards/html_node/_002d_002dversion.html#g_t_002d_002dversion]]
    */
  protected[this] def getVersion: Seq[String] = {
    Seq(s"$programName $version")
  }

  /** Get help string.
    *
    * @see
    *   [[https://www.gnu.org/prep/standards/html_node/_002d_002dhelp.html#g_t_002d_002dhelp]]
    */
  protected[this] def getHelp: Seq[String] = {
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
        val descriptionTailIndented = descriptionTail
          .map(x =>
            if (x.isEmpty) ""
            else ((" " * headOfFirstLine.length) + x)
          )
        ((headOfFirstLine + descriptionHead) :: descriptionTailIndented)
          .mkString("\n")
      })
      .mkString("\n")
    Seq(usage, description)
  }

  // The `ClassTag` here removes: "ambiguous reference to overloaded
  // definition".
  def testableMain[X: ClassTag](args: Seq[String]): Iterable[String] = {
    testableMain(parserConf.parse(args.toList))
  }

  def testableMain(args: Set[ArgumentCLI]): Iterable[String]

  /** Parse arguments, read stdin process and output to stdout.
    *
    * @see
    *   https://en.wikipedia.org/wiki/Standard_streams
    */
  def main(args: Array[String]): Unit = {
    val parsed: Set[ArgumentCLI] = parserConf.parse(args.toList)
    // Check if either version of help are given.
    val res = if (parsed.exists(_.name == "help")) {
      getHelp
    } else if (parsed.exists(_.name == "version")) {
      getVersion
    } else {
      testableMain(parsed)
    }
    res.foreach(println)
  }

}
