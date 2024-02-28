#!/usr/bin/env bash
DIR=$(cd -P -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd -P)
cd "$DIR"
git clone https://SR3u@bitbucket.org/visitkeeper/backend.git
cd ./backend
git fetch
git pull origin master
mvn clean package -DskipTests

