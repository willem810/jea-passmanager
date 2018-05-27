#!/bin/sh
mvn clean package && docker build -t jea.passmanager.bff/passmanager-bff .
docker rm -f passmanager-bff || true && docker run -d -p 8080:8080 -p 4848:4848 --name passmanager-bff jea.passmanager.bff/passmanager-bff 
