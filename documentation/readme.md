# `scala_cli_parser`

*   `dev`:

    *   ![Build status](https://travis-ci.org/fmv1992/scala_cli_parser.svg?branch=dev)

    *   [![codecov](https://codecov.io/gh/fmv1992/scala_cli_parser/branch/dev/graph/badge.svg)](https://codecov.io/gh/fmv1992/scala_cli_parser)

*   Available for Scala `2.11` (both JVM and [Scala Native](http://www.scala-native.org/en/latest/user/sbt.html)), `2.12` and `2.13`.

* * *

A library for parsing command line arguments.

It's main feature is that CLI parsing is defined on a config file. For example consider a very simple sum program:

~~~~ {#mycode .scala .numberLines pipe="bash" startFrom="1"}
cat scala_cli_parser/src/test/scala/util/TestSum.scala
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

It can be configured with the following config file:

~~~~ {#mycode .default .numberLines pipe="bash" startFrom="1"}
cat ./scala_cli_parser/src/test/resources/test_cli_example_02_gnu.txt
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

And its usages are as follows:

1.  ~~~~ {#mycode .default .numberLines startFrom="1"}
    TestSum --version
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    ~~~~ {#mycode .default .numberLines pipe="bash" startFrom="1"}
    make --quiet -B SCALA_CLI_ARGUMENTS='TestSum.main(Array("--version"))' tmp/test_sum.scala
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

1.  ~~~~ {#mycode .default .numberLines startFrom="1"}
    TestSum --help
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    ~~~~ {#mycode .default .numberLines pipe="bash" startFrom="1"}
    make --quiet -B SCALA_CLI_ARGUMENTS='TestSum.main(Array("--help"))' tmp/test_sum.scala
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

1.  ~~~~ {#mycode .default .numberLines startFrom="1"}
    TestSum --sum 1992 1
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    ~~~~ {#mycode .default .numberLines pipe="bash" startFrom="1"}
    make --quiet -B SCALA_CLI_ARGUMENTS='TestSum.main(Array("--sum", "1992", "1"))' tmp/test_sum.scala
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

## Config specification

The source of truth is the full `.conf` [fullConfigParser](https://github.com/fmv1992/scala_cli_parser/blob/4d0e4ab10951b81cec7f2fe8d8c0ce5e08a1308a/scala_cli_parser/src/main/scala/conf/ConfigFileParser.scala#L22).

Also the current options are considered around [here](https://github.com/fmv1992/scala_cli_parser/blob/4d0e4ab10951b81cec7f2fe8d8c0ce5e08a1308a/scala_cli_parser/src/main/scala/cli/ParserCLI.scala#L153).

## Links

*   [How to force case class constructors to have a pre defined signature in Scala?](https://stackoverflow.com/questions/65544763/how-to-force-case-class-constructors-to-have-a-pre-defined-signature-in-scala).

    >    ⋯ the answer is not possible, constructors are weird, they are not inherited, can not be overridden nor specified in an interface, they also have some weird limitations normal methods do not have.

## TODO

*   When Scala 2.11 support is dropped: between `0022b3e0a0198d4c970531db3a74c25e0b055f98` and `37424215d82a77ca618333521bce4827394bee66` some shims had to be introduced to make 2.11 compatible with future versions. Revert this when dropping support for Scala 2.11.

*   On project [`one`](https://github.com/SemanticSugar/one/blob/947e498e0b46ce7a27a5fb2d6e7ba67685c85b7e/one/src/main/scala/One.scala#L15): the design of `CLIConfigTestableMain` is conflicting with `zio.App`.

### Branches

*   `dev`:

*   `dev_0.x_scala_native`:

    *   Scala Native support was added by `dev_unstable`. Delete it.

*   `dev_restart`:

    *   Used to devise new strategy for `dev_unstable`. Delete it soon.

*   `dev_unstable`:

    *   (Ongoing): Add a `default` subsection to be parsed.

    *   (Backlog): Add config specification.

    *   (Backlog): On the part of parsing config files everything but `fullConfigParser` should be private.

    *   (Backlog): Create an interface for this package (newly created `fmv1992.scala_cli_parser.conf`) so that other packages might use it **through a well defined interface**.

    *   (Backlog): Improve parsing process. Parsers can actually fail and provide a useful error message.

    *   (Backlog): add lihaoyi's scala compiler `acyclic` plugin. See `commaefa4ec`.

    *   (Backlog): The `type` subsection seems hard to implement. Using `str` or `int` would make `ArgumentCLI` → `ArgumentCLI[String]` for instance. Uniform usage of it in a `Set[ArgumentCLI]` would be cumbersome. Maybe ok by using pattern matching? In any case this is not a priority right now.

*   `master`:

## Discussion

Interesting to notice that a parser behavior is influenced by the parsers and the combiners (e.g.: [`mapper`](https://github.com/fmv1992/scala_cli_parser/blob/e62ad7327eb7e46406bb94bf40ad82e418f4550b/scala_cli_parser/src/main/scala/conf/ParserUtils.scala#L125)).

### Backlog

*   Add [scalacheck](https://www.scalacheck.org/) to testing.

<!-- vim: set foldexpr=0 filetype=pandoc fileformat=unix nowrap spell spelllang=en: -->
