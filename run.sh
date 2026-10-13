#!/bin/sh
set -eu
project_dir=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
mkdir -p "$project_dir/out"
javac -encoding UTF-8 --release 17 -d "$project_dir/out" "$project_dir/src/HangzhouMetroTicketSystem.java"
java -cp "$project_dir/out" HangzhouMetroTicketSystem
