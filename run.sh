#!/bin/sh

echo Preparing Demo App...
echo Packaging SpringBoot App
cd ./demo-app-server && mvn package
echo Running docker-compose up ...
cd ./../ && docker-compose up
