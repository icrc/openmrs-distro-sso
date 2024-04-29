#!/bin/bash
VERSION=$1
if [ -z "${VERSION}" ]; then
  echo "Please give the version in first argument"
fi
docker compose exec db-core sh /dump-database.sh
mkdir -p ../dump
docker compose cp db-core:/tmp/openmrs.sql.gz "../dump/openmrs-${VERSION}.sql.gz"
