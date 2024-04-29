# simple Dockerfile to include a openmrs dump in default MariaDb Setup.
ARG MARIADB_VERSION=10.11.7
FROM mariadb:${MARIADB_VERSION}
ARG DUMP_FILENAME=openmrs-2.5.12.sql.gz
COPY dump/${DUMP_FILENAME} /docker-entrypoint-initdb.d/
