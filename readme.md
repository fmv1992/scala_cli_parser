[comment]: # ( ???: XXX: Do not edit this file directly! Edit `./documentation/readme.md` and `make` this file.)

# `scala_cli_parser`

[Documentation](http://fmv1992.github.io/scala_cli_parser/latest/api).

  - `dev`:
    
      - ![Build
        status](https://travis-ci.com/fmv1992/scala_cli_parser.svg?branch=dev)
    
      - [![codecov](https://codecov.io/gh/fmv1992/scala_cli_parser/branch/dev/graph/badge.svg)](https://codecov.io/gh/fmv1992/scala_cli_parser)

  - Available for Scala `2.11` (both JVM and [Scala
    Native](http://www.scala-native.org/en/latest/user/sbt.html)),
    `2.12` and `2.13`.

-----

A library for parsing command line arguments.

It’s main feature is that CLI parsing is defined on a config file. For
example consider a very simple sum program:

``` scala numberLines
package fmv1992.scala_cli_parser.util.test

import fmv1992.fmv1992_scala_utilities.util.S
import fmv1992.scala_cli_parser.cli.ArgumentCLI
import fmv1992.scala_cli_parser.util.MainTestableConfBased

object TestSum extends MainTestableConfBased {

  val version = "0.0.0"

  val programName = "TestSum"

  val CLIConfigContents =
    S.putfile("./src/test/resources/test_cli_example_02_gnu.txt")

  def testableMain(args: Set[ArgumentCLI]): Seq[String] = {
    args
      .find(_.name == "help")
      .orElse(args.find(_.name == "version"))
      .orElse(args.find(_.name == "sum"))
      .getOrElse(throw new Exception()) match {
      case a: ArgumentCLI if (a.name == "help")    => getHelp
      case a: ArgumentCLI if (a.name == "version") => getVersion
      case a: ArgumentCLI if (a.name == "sum") =>
        Seq(a.values.map(_.toInt).sum.toString)
    }
  }
}
```

It can be configured with the following config file:

``` default numberLines
# Well formated cli config file.
# Used in tests:
#   1.  Used to test `default`.

name: debug
    n: 0
    description: Turn on debugging.

name: version
    n: 0
    description: Show the program version.
# This is a comment.

name: sum
    n: 2
    description: | Sum arguments.
                 |
                 | Use multiline.
    default: 0,0

name: help
    n: 0
    description: Help text.

# vim: set filetype=config fileformat=unix wrap spell spelllang=en:
```

And its usages are as follows:

1.  ``` default numberLines
    TestSum --version
    ```
    
    ``` default numberLines
    TestSum 0.0.0
    ```

2.  ``` default numberLines
    TestSum --help
    ```
    
    ``` default numberLines
    TestSum --debug --help --sum --version
        --debug: Turn on debugging.
        --help: Help text.
        --sum: Sum arguments.
    
               Use multiline.
        --version: Show the program version.
    ```

3.  ``` default numberLines
    TestSum --sum 1992 1
    ```
    
    ``` default numberLines
    1993
    ```

## Config specification

The source of truth is the full `.conf`
[fullConfigParser](https://github.com/fmv1992/scala_cli_parser/blob/4d0e4ab10951b81cec7f2fe8d8c0ce5e08a1308a/scala_cli_parser/src/main/scala/conf/ConfigFileParser.scala#L22).

Also the current options are considered around
[here](https://github.com/fmv1992/scala_cli_parser/blob/4d0e4ab10951b81cec7f2fe8d8c0ce5e08a1308a/scala_cli_parser/src/main/scala/cli/ParserCLI.scala#L153).

## Links

  - [How to force case class constructors to have a pre defined
    signature in
    Scala?](https://stackoverflow.com/questions/65544763/how-to-force-case-class-constructors-to-have-a-pre-defined-signature-in-scala).
    
    > ⋯ the answer is not possible, constructors are weird, they are not
    > inherited, can not be overridden nor specified in an interface,
    > they also have some weird limitations normal methods do not have.

## TODO

### Short term

  - Create an interface for this package (newly created
    `fmv1992.scala_cli_parser.conf`) so that other packages might use it
    **through a well defined interface**.
    
    CURRENT: needs verification.
    
      - Review permissions for util entities like `ParserWithTry`.

  - Add config specification (this is somewhat in the docs).

\*̶ A̶d̶d̶ a̶ `̶M̶a̶i̶n̶`̶ c̶l̶a̶s̶s̶ t̶o̶ t̶h̶i̶s̶ p̶r̶o̶j̶e̶c̶t̶.̶
I̶t̶ s̶h̶o̶u̶l̶d̶ c̶h̶e̶c̶k̶ t̶h̶e̶ v̶a̶l̶i̶d̶i̶t̶y̶ o̶f̶
`̶.̶c̶o̶n̶f̶`̶ f̶i̶l̶e̶s̶ (̶b̶y̶ r̶e̶a̶d̶i̶n̶g̶ t̶h̶e̶
s̶t̶d̶i̶n̶)̶.̶

  - Stop using the:
    
        object Obj {
        
        private case class TraitImpl() ⋯
        
        def apply ⋯
        
        }
    
    Pattern. It disallows pattern matching.

### Long term

  - Add richer interactions between CLI arguments. Eg:
    
        rule_01:
            type: mutually_exclusive
            args: version,help

  - Add [scalacheck](https://www.scalacheck.org/) to testing.

  - On the part of parsing config files everything but
    `fullConfigParser` should be private.

  - Improve the documentation of this project. Good `scaladoc`
    references:
    
    1.  <https://gist.github.com/JohnStarich/6d8ba934828180cdcc502f571cefecd8>.
    
    2.  <https://github.com/scala/docs.scala-lang/blob/main/_style/scaladoc.md>.

  - When Scala 2.11 support is dropped: between
    `0022b3e0a0198d4c970531db3a74c25e0b055f98` and
    `37424215d82a77ca618333521bce4827394bee66` some shims had to be
    introduced to make 2.11 compatible with future versions. Revert this
    when dropping support for Scala 2.11.

  - On project
    [`one`](https://github.com/SemanticSugar/one/blob/947e498e0b46ce7a27a5fb2d6e7ba67685c85b7e/one/src/main/scala/One.scala#L15):
    the design of `CLIConfigTestableMain` is conflicting with `zio.App`.
    
    <!-- Fix wrong vim highlighting [](www) -->

  - Improve parsing process. Parsers can actually fail and provide a
    useful error message.

  - Add lihaoyi’s scala compiler `acyclic` plugin. See `commaefa4ec`.

  - The `type` subsection seems hard to implement. Using `str` or `int`
    would make `ArgumentCLI` → `ArgumentCLI[String]` for instance.
    Uniform usage of it in a `Set[ArgumentCLI]` would be cumbersome.
    Maybe ok by using pattern matching? In any case this is not a
    priority right now.

## Discussion

Interesting to notice that a parser behavior is influenced by the
parsers and the combiners (e.g.: [`???`]()).

### Branches

  - `dev`:

  - `dev_0.x_scala_native`:
    
      - Scala Native support was added by `dev_unstable`. Delete it.

  - `dev_restart`:
    
      - Used to devise new strategy for `dev_unstable`. Delete it soon.

  - `dev_add_default_to_argumentconf`:
    
      - Add a `default` subsection to be parsed. Done. Delete it soon.

  - `dev_unstable`:
    
    \*̶ (̶B̶a̶c̶k̶l̶o̶g̶)̶:̶ O̶n̶ t̶h̶e̶ p̶a̶r̶t̶ o̶f̶ p̶a̶r̶s̶i̶n̶g̶
    c̶o̶n̶f̶i̶g̶ f̶i̶l̶e̶s̶ e̶v̶e̶r̶y̶t̶h̶i̶n̶g̶ b̶u̶t̶
    `̶f̶u̶l̶l̶C̶o̶n̶f̶i̶g̶P̶a̶r̶s̶e̶r̶`̶ s̶h̶o̶u̶l̶d̶ b̶e̶
    p̶r̶i̶v̶a̶t̶e̶.̶
    
    \*̶ (̶B̶a̶c̶k̶l̶o̶g̶)̶:̶ I̶m̶p̶r̶o̶v̶e̶ p̶a̶r̶s̶i̶n̶g̶
    p̶r̶o̶c̶e̶s̶s̶.̶ P̶a̶r̶s̶e̶r̶s̶ c̶a̶n̶ a̶c̶t̶u̶a̶l̶l̶y̶
    f̶a̶i̶l̶ a̶n̶d̶ p̶r̶o̶v̶i̶d̶e̶ a̶ u̶s̶e̶f̶u̶l̶ e̶r̶r̶o̶r̶
    m̶e̶s̶s̶a̶g̶e̶.̶

  - `master`:

## Discussion

Interesting to notice that a parser behavior is influenced by the
parsers and the combiners (e.g.:
[`mapper`](https://github.com/fmv1992/scala_cli_parser/blob/e62ad7327eb7e46406bb94bf40ad82e418f4550b/scala_cli_parser/src/main/scala/conf/ParserUtils.scala#L125)).

<!-- vim: set foldexpr=0 filetype=pandoc fileformat=unix nowrap spell spelllang=en: -->
