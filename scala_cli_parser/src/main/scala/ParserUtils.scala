package fmv1992.scala_cli_parser

object ParserUtils {

  def or[A, B](
      p1: ParserWithTry[A, B],
      p2: ParserWithTry[A, B]
  ): ParserWithTry[A, B] = {
    ParserWithTryImpl((x: A) => p1.parse(x).orElse(p2.parse(x)).get)
  }

  def and[A <: Seq[_], B](
      p1: ParserPartial[A, B],
      p2: ParserPartial[A, B]
  )(implicit combiner: (B, B) => B): ParserPartial[A, B] = {
    ParserPartialImpl((a: A) => {
      val (rest1: A, parsed1: B) = p1.partialParse(a)
      if (rest1 == a) {
        throw new ParseException(a.toString)
      } else {
        val (rest2: A, parsed2: B) = p2.partialParse(rest1)
        Console.err.println("→" + rest1.toString + "←")
        Console.err.println("→" + rest2.toString + "←")
        (rest2, combiner(parsed1, parsed2))
      }
    })
  }

//   def and[A, B, C, D](
//       p1: ParserWithTry[Seq[A], B],
//       p2: ParserWithTry[Seq[A], C],
//       combiner: (B, C) => D
//   ): ParserWithTry[Seq[A], D] = {
//     // Tries to find the longest valid sequence for `p1`.
//     ParserImpl(
//       (x: Seq[A]) => {
//         val parsedOpt1 = p1.getValidSubSequence(x)
//         parsedOpt1 match {
//           case Some(x1) => {
//             val inputRemaining = x.drop(x1.length)
//             val parsedOpt2 = p2.getValidSubSequence(inputRemaining)
//             Console.err.println(parsedOpt1)
//             Console.err.println(parsedOpt2)
//             parsedOpt2 match {
//               case Some(x2) if inputRemaining.drop(x2.length).length == 0 =>
//                 combiner(p1.transform(x1), p2.transform(x2))
//               case Some(_) =>
//                 throw ParseException.fromInput(inputRemaining, p2)
//               case None =>
//                 throw ParseException.fromInput(inputRemaining, p2)
//             }
//           }
//           case None => throw ParseException.fromInput(x, p1)
//         }
//       }
//     )
//   }
//
//   def tryAll[A, B](
//       input: ParserWithTry[Seq[A], B]*
//   ): ParserWithTry[Seq[A], Seq[B]] = {
//     val parserSet = input.toSet
//     def go(
//         currentSegment: Seq[A],
//         remainingSegment: Seq[A],
//         validParsers: Set[ParserWithTry[Seq[A], B]],
//         acc: Seq[B] = Seq.empty
//     ): Seq[B] = {
//       if (remainingSegment.isEmpty) {
//         val correctedAcc: Seq[B] = if (currentSegment.isEmpty) {
//           acc
//         } else {
//           acc.appended(
//             (validParsers.head
//               .parse(currentSegment)
//               .getOrElse(throw new Exception()): B)
//           )
//         }
//         if (correctedAcc.isEmpty) {
//           Seq.empty
//         } else {
//           correctedAcc
//         }
//       } else {
//         val newCurrentSegment = currentSegment.appended(remainingSegment.head)
//         val newValidParsers =
//           validParsers.filter(_.parse(newCurrentSegment).isRight)
//         if (newValidParsers.size > 0) {
//           go(newCurrentSegment, remainingSegment.tail, newValidParsers, acc)
//         } else {
//           go(
//             Seq.empty,
//             remainingSegment,
//             parserSet,
//             validParsers.head.parse(currentSegment) match {
//               case Left(_)  => acc
//               case Right(r) => acc.appended(r)
//             }
//           )
//         }
//       }
//     }
//     ParserImpl((x: Seq[A]) => {
//       go(Seq.empty, x, parserSet)
//     })
//   }
//
//   // ???: I believe there is a lot of redundancy in this method.
//   def many[A, B](
//       p: ParserWithTry[Seq[A], B],
//       combiner: (B, B) => B
//   ): ParserWithTry[Seq[A], B] = {
//     def go(
//         input: Seq[A],
//         acc: LazyList[Either[Throwable, B]] = LazyList.empty
//     ): LazyList[Either[Throwable, B]] = {
//       // Console.err.println("-" * 79)
//       // Console.err.println("¦" + input.mkString + "¦")
//       // Console.err.println(acc.toList)
//       // Console.err.println("-" * 79)
//       if (input.isEmpty) {
//         acc
//       } else {
//         val parsedOpt = p.getValidSubSequence(input)
//         parsedOpt match {
//           case Some(x) => go(input.drop(x.length), acc.appended(p.parse(x)))
//           case None    => acc
//         }
//       }
//     }
//     ParserImpl((x: Seq[A]) => {
//       val parsedAcc = go(x)
//       val left = parsedAcc.filter(_.isLeft).headOption
//       if (left.isDefined) {
//         // ???: This does not feel right.
//         p.transform(x)
//       } else {
//         val rights =
//           parsedAcc.map(_.getOrElse(throw new Exception())).reduce(combiner)
//         rights
//       }
//     })
//   }
//
//   def allSubsequencesFromStart[A](s: Seq[A]): Seq[Seq[A]] = {
//     (0 to s.length).map(l1 => s.slice(0, l1))
//   }
//
}
