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
    // println("-" * 79)
    // println(lines.toList)
    // println("-" * 79)
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
        println("x" * 79)
        println(lines.map(_.mkString).mkString("\n"))
        println("y" * 79)
        println(rest.map(_.mkString).mkString("\n"))
        println("z" * 79)
        println(linesWithSamePipePos.map(_.mkString).mkString("\n"))
        println("x" * 79)
        (
          rest.map(_.mkString).mkString("\n"),
          Success(
            ParsedResult(
              linesWithSamePipePos.map(_.mkString).mkString("\n") + "\n",
              Map(key.mkString.trim -> value): Map[String, String]
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
      case (l, Seq()) => l
      case (l, s)     => l.appended(s)
    }
  }

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

//
//  private val SpacedSolidLineStartingWithPipe
//      : ParserWithEither[Seq[Char], ParsedResult[Seq[Char], String]] =
//    ParserUtils.many(
//      ParserUtils.and(
//        SpaceConfParser,
//        SolidLineStartingWithPipe,
//        (
//            x: ParsedResult[Seq[Char], Map[String, String]],
//            y: ParsedResult[Seq[Char], String]
//        ) => ParsedResult(x.data ++ y.data, y.result)
//      ),
//      (
//          x: ParsedResult[Seq[Char], String],
//          y: ParsedResult[Seq[Char], String]
//      ) => ParsedResult(x.data ++ y.data, x.result ++ y.result)
//    )
//
//  def transform(
//      input: Seq[Char]
//  ): ParsedResult[Seq[Char], Map[String, String]] = {
//    throw new Exception(input.toString)
//  }
//
//  def isValid(input: Seq[Char]) = {
//    val splitLines = splitOnLines(input)
//    val firstLine: Seq[Char] = splitLines.head
//    val otherLines: Seq[Seq[Char]] = splitLines.tail
//    require(
//      (firstLine.toList +: otherLines.toList).flatten == input.toList,
//      (firstLine.toList, otherLines.toList, input.toList)
//    )
//    val pipePos = splitLines.map(_.indexOf('|'))
//    val pipePosAreTheSame: Boolean = pipePos.forall(_ == pipePos.head)
//    Thread.sleep(1000)
//    Console.err.println("-" * 79)
//    Console.err.println(SingleLineConfParser.isValid(firstLine))
//    Console.err.println(pipePosAreTheSame)
//    Console.err.println(
//      otherLines
//        .forall(
//          SpacedSolidLineStartingWithPipe.isValid(_)
//        )
//    )
//    Console.err.println("-" * 79)
//    SingleLineConfParser.isValid(firstLine) && pipePosAreTheSame &&
//    otherLines
//      .forall(
//        SpacedSolidLineStartingWithPipe.isValid(_)
//      )
//  }
//
//  def getValidSubSequence(input: Seq[Char]): Option[Seq[Char]] = {
//    // Console.err.println("-" * 79)
//    // Console.err.println("🐛" + input.mkString + "🐛")
//    // Console.err.println("-" * 79)
//    val splitLines = splitOnLines(input)
//    val idx = splitLines.indexWhere(!_.contains('|'))
//    if (idx == -1) {
//      if (isValid(input)) {
//        Some(input)
//      } else {
//        ???
//      }
//    } else {
//      val subLines = splitLines.slice(0, idx)
//      val subStringNew = subLines.mkString("\n")
//      if (isValid(subStringNew)) {
//        Some(subStringNew)
//      } else {
//        ???
//      }
//    }
//  }
//
//}
//
//object SolidLineStartingWithPipe
//    extends ParserWithEither[Seq[Char], ParsedResult[Seq[Char], String]] {
//
//  def transform(
//      input: Seq[Char]
//  ): ParsedResult[Seq[Char], String] = {
//    ParsedResult(input, input.tail.mkString)
//  }
//
//  def isValid(input: Seq[Char]) = {
//    lazy val notEmpty = !input.isEmpty
//    lazy val isPipeFirst = input.head == '|'
//    lazy val newLinePos = input.indexOf('\n')
//    lazy val newLinesOnlyAtEnd =
//      (newLinePos == -1) || (newLinePos == input.length - 1)
//    notEmpty && isPipeFirst && newLinesOnlyAtEnd
//  }
//
//  def getValidSubSequence(input: Seq[Char]): Option[Seq[Char]] = {
//    val newLinePos = input.indexOf('\n')
//    val line = if (newLinePos == -1) input else input.slice(0, newLinePos)
//    if (isValid(line)) {
//      Some(line)
//    } else {
//      None
//    }
//  }
}
