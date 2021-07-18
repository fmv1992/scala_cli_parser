package fmv1992.scala_cli_parser

import scala.util.Failure
import scala.util.Success
import scala.util.Try

/** Parse a **single** config that spans multiple lines.
  *  It **must** span more than 1 line.
  */
object MultiLineConfParser
    extends ParserPartial[
      Seq[Char],
      Try[ParsedResult[Seq[Char], Map[String, String]]]
    ]
    with ParserWithTry[
      Seq[Char],
      ParsedResult[Seq[Char], Map[String, String]]
    ] {

  override def parse(
      input: Seq[Char]
  ): Try[ParsedResult[Seq[Char], Map[String, String]]] =
    super[ParserPartial].parse(input)

  def partialParse(
      input: Seq[Char]
  ): (Seq[Char], Try[ParsedResult[Seq[Char], Map[String, String]]]) = {
    val lines = splitOnLines(input)
    // Make sure this is multi line.
    if (lines.length <= 1) {
      (
        input,
        Failure(ParseException(s"Is a single line: '${input.mkString}'."))
      )
    } else {
      // Get key.
      val keyPos = lines.head.indexOf(':')
      if (keyPos == -1) {
        (
          input,
          Failure(ParseException(s"Char ':' not found: '${input.mkString}'."))
        )
      } else {
        val key =
          if (keyPos == -1) {
            throw new ParseException(
              s"Char ':' not found: '${input.mkString}'."
            )
          } else {
            lines.head.take(keyPos)
          }

        // Get value.
        val pipePos = lines.head.indexOf('|')
        val linesWithSamePipePos = lines.takeWhile(_.indexOf('|') == pipePos)
        val value = if (linesWithSamePipePos.length >= 2) {
          val linesWithSamePipePosTrimmed = trimLeadingWhiteSpacesOnLines(
            linesWithSamePipePos
          )
          val pipePosNew = linesWithSamePipePosTrimmed.head
            .indexOf('|')
          linesWithSamePipePosTrimmed
            .map(x => x.drop(pipePosNew + 1).mkString.trim)
            .mkString("\n")
        } else {
          throw new ParseException(
            s"`MultiLineConfParser` parses multi lines only: '${input.mkString}'."
          )
        }
        val rest: Seq[Seq[Char]] = lines.drop(linesWithSamePipePos.length)
        println("-" * 79)
        println(lines.map(_.mkString).mkString("\n"))
        println("y" * 79)
        println(linesWithSamePipePos.map(_.mkString).mkString("\n"))
        println("z" * 79)
        println(rest.map(_.mkString).mkString("\n"))
        println("-" * 79)
        (
          rest.map(_.mkString).mkString("\n"),
          Success(
            ParsedResult(
              // CURRENT: The trailing "\n".
              // linesWithSamePipePos.map(_.mkString).mkString("\n") + "\n",
              // ???: Found the bug/🐛: the new line right after `name line
              // 04.`.
              linesWithSamePipePos.map(_.mkString).mkString("\n"),
              Map(key.mkString.trim -> value.trim): Map[String, String]
            )
          )
        )
      }
    }
  }

  private def splitOnLines(input: Seq[Char]): Seq[Seq[Char]] = {
    input
      .foldLeft(
        (LazyList.empty: LazyList[Seq[Char]], Seq.empty: Seq[Char])
      )((acc, char) => {
        val accll = acc._1
        val accseq = acc._2
        if (char == '\n') {
          (accll.appended(accseq), Seq.empty)
        } else {
          (accll, accseq.appended(char))
        }
      }) match {
      case (l, s) => l.appended(s)
    }
  }
  require(splitOnLines("a\nb".toSeq) == Seq(Seq('a'), Seq('b')))
  require(splitOnLines("a\nb\n".toSeq) == Seq(Seq('a'), Seq('b'), Seq()))

  private def trimLeadingWhiteSpacesOnLines(
      input: Seq[Seq[Char]]
  ): Seq[Seq[Char]] = {
    if (input.isEmpty) {
      input
    } else {
      val marginSize = input.head.takeWhile(_.isWhitespace).length
      input.map(_.drop(marginSize))
    }
  }

}
