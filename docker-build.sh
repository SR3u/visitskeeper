#!/bin/bash
timestamp=$(date +%Y%m%d%H%M%S)
mvn clean install -Dmaven.test.skip=true
#TAG=$(date +%Y%m%d-%H%M%S)
#docker build -t "sr3u/private:visitkeeper-$TAG" ./
#docker push "sr3u/private:visitkeeper-$TAG"
#docker build -t sr3u/private:visitkeeper-latest ./
#docker push sr3u/private:visitkeeper-latest
docker image rm sr3u/visitkeeper:$timestamp
docker build -t sr3u/visitkeeper:$timestamp ./
docker push sr3u/visitkeeper:$timestamp
docker image rm sr3u/visitkeeper:$timestamp
