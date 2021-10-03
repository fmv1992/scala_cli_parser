#! /usr/bin/env bash

# Halt on error.
set -euo pipefail

# Go to execution directory.
{ cd "$(dirname $(readlink -f "${0}"))" && git rev-parse --is-inside-work-tree > /dev/null 2>&1 && cd "$(git rev-parse --show-toplevel)"; } || cd "$(dirname "$(readlink -f ${0})")"
# Close identation: }
test -d ./.git

jar_file=$(find ./scala_cli_parser -iname '*.jar' | one --n 1)

java -jar ${jar_file} --help | grep -E '[-]-help'
java -jar ${jar_file} --help | grep -E '[-]-version'

java -jar ${jar_file} --version | one --n 1 | grep -E '^scala_cli_parser [0-9]\.'

find ./scala_cli_parser/src -iname 'scala_cli_parser_config.conf'

# vim: set filetype=sh fileformat=unix nowrap:
