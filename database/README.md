A Mariadb Database with an empty dump file to start.
OpenMRS can start without this dump but it simplifies to first startup.
Otherwise oauth2 module can be started at first start as the initial Setup will prevent it to start (a filter already in place)


# How to generate the dump
the current dumps are:
- `dump/openmrs-2.5.12.sql.gz` built for OpenMRS 2.5.12.
- `dump/openmrs-2.6.4.sql.gz` built for OpenMRS 2.6.4.
It's possible to create another dump by using the scripts in the folder `dump-extract`.

1. Change if needed the OpenMRS Version in `dump-extract/docker-compose.yml` file ( from `openmrs/openmrs-core:2.5.12` to `openmrs/openmrs-core:X.Y.Z`)
2. run `step-1-start-and-wait-until-ready.sh` to start a new openMRS instance and wait until the end
3. run `step-2-extract-dump.sh <YOUR_VERSION>` to extract the dump that will be moved to `dump/`
4. Modify `.github/workflow/publish-mariadb-docker-image.yml` to create a image using this dump version
5. See `.github/workflow/publish-mariadb-2.6.4-docker-image.yml` for an example


## Improvements
it's possible to automate the full process but this operation is not oftenly used so these steps are acceptable.
