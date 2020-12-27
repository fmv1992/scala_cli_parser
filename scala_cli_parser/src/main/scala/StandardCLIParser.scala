package fmv1992.scala_cli_parser

import java.io.File
import java.nio.file.Path

import fmv1992.fmv1992_scala_utilities.util.Reader

/** Standard parser. */
class StandardCLIParser(val format: ParsedConfigStructure) extends CLIParser {

  def parse(args: Seq[String]): Either[Seq[Throwable], Seq[Argument]] = {

    /** Recursive parse. */
    def go(
        goArgs: Seq[String],
        acc: Seq[Argument]
    ): Seq[Argument] = {

      goArgs match {
        case Nil => acc
        case h :: t => {
          val name = h.stripPrefix("--")
          require(format.contains(name), String.valueOf(format.keys) + name)
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
      format.filter(x => x._2.contains("default")).keys.toSeq
    val notIncludedDefaultKeys: Seq[String] =
      (defaultKeys diff argsLongNames)
    require(
      argsLongNames.intersect(notIncludedDefaultKeys).isEmpty,
      String.valueOf(argsLongNames) + "|" + notIncludedDefaultKeys
    )
    val additionalArgs: Seq[Argument] = {
      notIncludedDefaultKeys.map(x => Arg(x, List(format(x)("default"))))
    }

    Right(parsedArgs ++ additionalArgs)
  }

}

/** Companion object for StandardCLIParser. */
object StandardCLIParser {

  def apply(f: File): StandardCLIParser = {
    apply(Reader.readLines(f).mkString("\n"))
  }

  def apply(p: Path): StandardCLIParser = {
    apply(p.toFile)
  }

  def apply(contents: String): StandardCLIParser = {
    new StandardCLIParser(StandardConfParser.parseConf(contents))
  }

}
