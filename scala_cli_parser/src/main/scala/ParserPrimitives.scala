package fmv1992.scala_cli_parser

/** Parse a CLI config file. This file consists of:
  *
  * 1.  Empty lines.
  *
  * 2.  Comment lines: Starting with spaces followed by '#'.
  *
  * 3. A 'name:' followed by nested (space indented) attributes.
  */
/** As we could see in SICP: <https://mitpress.mit.edu/sites/default/files/sicp/full-text/book/book-Z-H-14.html#%25_sec_2.1.2>
  *
  * """
  * Constraining the dependence on the representation to a few interface
  * procedures helps us design programs as well as modify them, because it
  * allows us to maintain the flexibility to consider alternate implementations.
  * """
  *
  * And that's precisely what it is done in the chapter 09 (parsing) of FPIS:
  *
  * Abstraction:
  *
  * ```
  * trait Parsers[Parser[+_]] { self => * // so inner classes may call methods of trait
  * def run[A](p: Parser[A])(input: String): Either[ParseError,A]
  *
  * ...
  *
  * ```
  *
  * Implementation:
  *
  * ```
  * object ReferenceTypes {
  *
  * \/\*\* A parser is a kind of state action that can fail. \*\/
  * type Parser[+A] = ParseState => Result[A]
  *
  * ...
  *
  * ```
  */
/** New design of parsers:
  *
  * 1. KISS principle.
  *
  * 1. Proof of concept stage.
  *
  * 1. Parsers must be able to be combined to form more advanced parsers.
  *
  * 1. In this case a parser is a String => Option[Map[String, String]].
  *
  * 1. A parser can fail. Maybe Option[String] is a better approach.
  *
  * 1. In this the types are:
  *
  * a. Empty line.
  *
  * b. Comment line (starts with a #).
  *
  * c. Sequence of non spaces terminated with ": " followed by a string.
  */
object ParserPrimitives {

  // This doesn't take type checking into account.
  def emptyLine: Parser = {
    x =>
      if (x == "\n" || x.isEmpty) Some(Map.empty) else None
  }

  def commentLine: Parser = {
    x =>
      if (x.startsWith("#")) Some(Map.empty) else None
  }

  def nameContentLine: Parser = {
    x =>
      {
        val colonPos: Int = x.indexOf(':')
        val t: (String, String) = x.splitAt(colonPos)
        // `drop`: drop the (": ").
        val (id, body): (String, String) = (t._1.trim, t._2.trim.drop(2))
        require(!id.isEmpty, id)
        require(!body.isEmpty, body)
        Option(Map(id -> body))
      }
  }

  def generalContentLine: Parser = {
    x =>
      nameContentLine(x.dropWhile(_.isSpaceChar))
  }

}
