package fmv1992.scala_cli_parser

import java.io.File
import java.nio.file.Path

/** GNU Compliant argument parser.
  *
  * @see [[https://www.gnu.org/prep/standards/html_node/Command_002dLine-Interfaces.html]]
  */
case class GNUParser(override val format: Map[String, Map[String, String]])
    extends StandardCLIParser(format) {

  require(
    format.contains("help"),
    String.valueOf(format) + " has to contain entry 'help'."
  )
  require(
    format.contains("version"),
    String.valueOf(format) + " has to contain entry 'version'."
  )

  override def parse(args: Seq[String]): Either[Seq[Throwable], Seq[GNUArg]] = {
    mapEitherShim(
      super.parse(args),
      (l: Seq[Argument]) => l.map(x => GNUArg(x.longName, x.value))
    )
  }

}

/** Companion object for GNUParser. */
object GNUParser {

  def apply(f: File): GNUParser = {
    GNUParser(StandardCLIParser(f).format)
  }

  def apply(s: Path): GNUParser = {
    apply(s.toFile)
  }

  def apply(contents: String): GNUParser = {
    apply(StandardConfParser.parseConf(contents))
  }

}
