package fmv1992.scala_cli_parser

/** Parse a **single** config that spans multiple lines.
  */
object MultiLineConfParser
    extends ParserWithEither[
      Seq[Char],
      ParsedResult[Seq[Char], Map[String, String]]
    ] {

  private def splitOnLines(input: Seq[Char]): Seq[Seq[Char]] = {
    input
      .foldLeft(
        (LazyList.empty: LazyList[Seq[Char]], Seq.empty: Seq[Char])
      )((acc, char) => {
        val accll = acc._1
        val accseq = acc._2
        if (char == '\n') {
          (accll.appended(accseq.appended('\n')), Seq.empty)
        } else {
          (accll, accseq.appended(char))
        }
      }) match {
      case (l, Seq()) => l
      case (l, s)     => l.appended(s)
    }
  }

  private val SpacedSolidLineStartingWithPipe
      : ParserWithEither[Seq[Char], ParsedResult[Seq[Char], String]] =
    ParserUtils.many(
      ParserUtils.and(
        SpaceConfParser,
        SolidLineStartingWithPipe,
        (
            x: ParsedResult[Seq[Char], Map[String, String]],
            y: ParsedResult[Seq[Char], String]
        ) => ParsedResult(x.data ++ y.data, y.result)
      ),
      (
          x: ParsedResult[Seq[Char], String],
          y: ParsedResult[Seq[Char], String]
      ) => ParsedResult(x.data ++ y.data, x.result ++ y.result)
    )

  ParserUtils.and(
    ParserUtils.and(
      SpaceConfParser,
      SingleLineConfParser,
      (
          x: ParsedResult[Seq[Char], Map[String, String]],
          y: ParsedResult[Seq[Char], Map[String, String]]
      ) => ParsedResult(x.data ++ y.data, x.result ++ y.result)
    ),
    ParserUtils.many(
      SolidLineStartingWithPipe,
      (
          x: ParsedResult[Seq[Char], String],
          y: ParsedResult[Seq[Char], String]
      ) => ParsedResult(x.data ++ y.data, x.result + '\n' + y.result)
    ),
    (
        x: ParsedResult[Seq[Char], Map[String, String]],
        // y: ParsedResult[Seq[Char], Map[String, String]]
        y: ParsedResult[Seq[Char], String]
    ) => ParsedResult(x.data ++ y.data, 1)
  )

  def transform(
      input: Seq[Char]
  ): ParsedResult[Seq[Char], Map[String, String]] = {
    throw new Exception(input.toString)
  }

  def isValid(input: Seq[Char]) = {
    val splitLines = splitOnLines(input)
    val firstLine: Seq[Char] = splitLines.head
    val otherLines: Seq[Seq[Char]] = splitLines.tail
    require(
      (firstLine.toList +: otherLines.toList).flatten == input.toList,
      (firstLine.toList, otherLines.toList, input.toList)
    )
    val pipePos = splitLines.map(_.indexOf('|'))
    val pipePosAreTheSame: Boolean = pipePos.forall(_ == pipePos.head)
    SingleLineConfParser.isValid(firstLine) && pipePosAreTheSame && otherLines
      .forall(
        SpacedSolidLineStartingWithPipe.isValid(_)
      )
  }

  def getValidSubSequence(input: Seq[Char]): Option[Seq[Char]] = {
    val splitLines = splitOnLines(input)
    val idx = splitLines.indexWhere(!_.contains('|'))
    if (idx == -1) {
      if (isValid(input)) {
        Some(input)
      } else {
        ???
      }
    } else {
      val subLines = splitLines.slice(0, idx)
      val subStringNew = subLines.mkString("\n")
      if (isValid(subStringNew)) {
        Some(subStringNew)
      } else {
        ???
      }
    }
  }

}

object SolidLineStartingWithPipe
    extends ParserWithEither[Seq[Char], ParsedResult[Seq[Char], String]] {

  def transform(
      input: Seq[Char]
  ): ParsedResult[Seq[Char], String] = {
    ParsedResult(input, input.tail.mkString)
  }

  def isValid(input: Seq[Char]) = {
    lazy val notEmpty = !input.isEmpty
    lazy val isPipeFirst = input.head == '|'
    lazy val newLinePos = input.indexOf('\n')
    lazy val newLinesOnlyAtEnd =
      (newLinePos == -1) || (newLinePos == input.length - 1)
    notEmpty && isPipeFirst && newLinesOnlyAtEnd
  }

  def getValidSubSequence(input: Seq[Char]): Option[Seq[Char]] = {
    val newLinePos = input.indexOf('\n')
    val line = if (newLinePos == -1) input else input.slice(0, newLinePos)
    if (isValid(line)) {
      Some(line)
    } else {
      None
    }
  }
}
