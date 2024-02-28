#!/usr/local/bin/bash
DIR=$(cd -P -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd -P)
cd "$DIR"
/usr/bin/killall java
./update.sh
./start.sh
