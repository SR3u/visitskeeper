#!/bin/bash
timestamp=$(date +%Y%m%d%H%M%S)
#mvn clean install -Dmaven.test.skip=true
docker image rm sr3u/visitkeeper-front:$timestamp
docker build -t sr3u/visitkeeper-front:$timestamp ./
docker push sr3u/visitkeeper-front:$timestamp
docker image rm sr3u/visitkeeper-front:$timestamp
