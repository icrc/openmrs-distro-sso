# syntax=docker/dockerfile:1

### Dev Stage
FROM maven:3.8-amazoncorretto-11 AS dev
WORKDIR /openmrs_distro
RUN mkdir -p /openmrs/distribution/openmrs_core/

# Copy build files
COPY pom.xml ./
COPY distro.properties ./
COPY module-create-users ./module-create-users
# Build the distro, but only deploy from the amd64 build
RUN --mount=type=secret,id=m2settings,target=/usr/share/maven/ref/settings-docker.xml \
    cat /usr/share/maven/ref/settings-docker.xml
# Build the distro, but only deploy from the amd64 build
RUN --mount=type=cache,target=/usr/share/maven/ref/repository --mount=type=secret,id=m2settings,target=/usr/share/maven/ref/settings-docker.xml \
    mvn -s /usr/share/maven/ref/settings-docker.xml \
    -Dmaven.repo.local=/usr/share/maven/ref/repository \
    clean install

RUN cp /openmrs_distro/target/sdk-distro/web/openmrs.war /openmrs/distribution/openmrs_core/

RUN cp ./target/sdk-distro/web/openmrs-distro.properties /openmrs/distribution/
RUN cp -R ./target/sdk-distro/web/modules /openmrs/distribution/openmrs_modules
RUN cp -R ./target/sdk-distro/web/owa /openmrs/distribution/openmrs_owas
RUN cp -R ./target/openmrs_config /openmrs/distribution/openmrs_config


### Run Stage
# Replace 'nightly' with the exact version of openmrs-core built for production (if available)
FROM openmrs/openmrs-core:2.5.12

# Do not copy the war if using the correct openmrs-core image version
COPY --from=dev /openmrs/distribution/openmrs_core/openmrs.war /openmrs/distribution/openmrs_core/

COPY --from=dev /openmrs/distribution/openmrs-distro.properties /openmrs/distribution/
COPY --from=dev /openmrs/distribution/openmrs_modules /openmrs/distribution/openmrs_modules
COPY --from=dev /openmrs/distribution/openmrs_owas /openmrs/distribution/openmrs_owas
COPY --from=dev /openmrs/distribution/openmrs_config /openmrs/distribution/openmrs_config
COPY openmrs_custom_config /openmrs/distribution/openmrs_config
COPY logging.sysout.properties /usr/local/tomcat/conf/logging.properties
# To display all logs in terminal
COPY server.xml /usr/local/tomcat/conf/server.xml
COPY log4j2.xml /openmrs/distribution/

COPY --chmod=0755 ./startup-oauth2.sh /openmrs/
COPY --chmod=0755 ./startup.sh /openmrs/
CMD ["/openmrs/startup-oauth2.sh"]

