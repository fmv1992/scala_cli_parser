package fmv1992.scala_cli_parser

import java.io.File

import fmv1992.fmv1992_scala_utilities.util.Reader

/** CLI parser most general trait.
  *
  * @define parseDoc Parse a sequence of strings into a sequence of
  * [[Argument Arguments]].
  *
  */
trait CLIParser {

  /** $parseDoc */
  def parse(args: Seq[String]): Seq[Argument]

}

/** Configuration file based CLI parser. */
trait ConfigFileParser extends CLIParser {

  /** Map of parsed options.
    *
    * Example:
    *
    * ```
    * name: debug
    * n: 0
    * type: int
    * help: Help text.
    * ```
    *
    * Gets transformed into this:
    *
    * Map(debug -> Map(n -> 0, type -> int, help -> Help text.),
    * verbose -> Map(n -> 0, type -> int, help -> Help text.))
    *
    */
  val format: Map[String, Map[String, String]]

}

/** Standard parser. */
class StandardParser(val format: Map[String, Map[String, String]])
    extends ConfigFileParser {

  /** $parseDoc */
  def parse(args: Seq[String]): Seq[Argument] = {

    /** Recursive parse. */
    def go(
        goArgs: Seq[String],
        acc: Seq[Argument]
    ): Seq[Argument] = {

      goArgs match {
        case Nil ⇒ acc
        case h :: t ⇒ {
          val name = h.stripPrefix("--")
          require(format.contains(name), format.keys + name)
          val n = format(name)("n").toInt
          val values = t.take(n)
          require(
            values.length == n,
            s"Number of parsed values ${values.length} are different from n (${n})."
          )
          val newArg: Argument = Arg(name, values)
          val newList: Seq[Argument] = newArg +: acc
          go(goArgs.drop(n + 1), newList)
        }
      }
    }

    val parsedArgs: Seq[Argument] = go(args, Nil).reverse
    val argsLongNames = parsedArgs.map(_.longName)

    // Add default arguments if there is any.
    val defaultKeys: Seq[String] =
      format.filter(x ⇒ x._2.contains("default")).keys.toSeq
    val notIncludedDefaultKeys: Seq[String] = (defaultKeys diff argsLongNames)
    require(
      argsLongNames.intersect(notIncludedDefaultKeys).isEmpty,
      argsLongNames + "|" + notIncludedDefaultKeys
    )
    val additionalArgs: Seq[Argument] = {
      notIncludedDefaultKeys.map(x ⇒ Arg(x, List(format(x)("default"))))
    }

    parsedArgs ++ additionalArgs
  }

}

/** Companion object for StandardParser. */
object StandardParser {

  def apply(f: File): StandardParser = {
    val parsed = ConfCLIParser.parseConf(Reader.readLines(f).mkString("\n"))
    new StandardParser(parsed)
  }

  def apply(s: String): StandardParser = {
    apply(new File(s))
  }

}

/** GNU Compliant argument parser.
  *
  * @see [[https://www.gnu.org/prep/standards/html_node/Command_002dLine-Interfaces.html]]
  */
case class GNUParser(override val format: Map[String, Map[String, String]])
    extends StandardParser(format) {

  require(format.contains("help"), format + " has to contain entry 'help'.")
  require(
    format.contains("version"),
    format + " has to contain entry 'version'."
  )

  override def parse(args: Seq[String]): Seq[GNUArg] = {
    super.parse(args).map(x ⇒ GNUArg(x.longName, x.value))
  }

}

/** Companion object for GNUParser. */
object GNUParser {

  def apply(f: File): GNUParser = {
    GNUParser(StandardParser(f).format)
  }

  def apply(s: String): GNUParser = {
    apply(new File(s))
  }

}

/** General trait for an argument. */
trait Argument {

  def longName: String

  def value: Seq[String]

}

/** General (non GNU as of now) argument.
  *
  * Requirements are assured by the parser.
  */
case class Arg(longName: String, value: Seq[String]) extends Argument {}

/** Companion object for Arg. */
object Arg {}

/** GNU style argument. Requirements are assured by the parser. */
case class GNUArg(longName: String, value: Seq[String]) extends Argument {

  // ???: Enforce gnu argument names and coherence inside package.

}

/** Companion object for GNUArg. */
object GNUArg {}
