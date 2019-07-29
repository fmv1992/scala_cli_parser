#! /bin/bash

# Halt on error.
set -e
set -x

stdin=$(</dev/stdin)

if echo "$stdin"    | grep -E '\<master\>'  1>/dev/null 2>/dev/null
then
    full_checks=5
elif echo "$stdin"  | grep -E '\<dev\>'     1>/dev/null 2>/dev/null
then
    full_checks=2
else
    exit 0
fi

for i in $(seq 1 "$full_checks")
do
    make --jobs 1 clean
    make --jobs 1 test
done

make coverage

# vim: set filetype=sh fileformat=unix wrap:
