

# Users configured accordingly to Datafilter filter (location based filter)

Will run liquibase scripts at startup to create users attached to one or several locations.
The liquibase scripts are in [api/src/main/resources](./api/src/main/resources)

- The users `l1` will be assigned to the location `l1`, etc
- The users `many` will be assigned to the locations `l1`, `l2` and `l3`

These users will be also created in keycloak. See [../../keycloak/README.md](../../keycloak/README.md). So the users
created in liquibase should be also present in [../../keycloak/users.csv](../../keycloak/users.csv).

# TODO

improve/configure oauth2 module to automatically create users based on their role/location defined in keycloak Oauth2
module can auto-create users in OpenMRS if not present.


# Patients / Visits Created.

this module is based on [openmrs-module-referencedemodata](https://github.com/openmrs/openmrs-module-referencedemodata) to create patients, visits...

Modifications are done to:
- create patients on all locations and not only one location
- use the patient location to create visits,...
- try to avoid hardcoded configuration ( providers,...)
- use thread to create visits in parallel ( otherwise startup is too long)

## TODO
to be improved and pushed back in `referencedemodata` module.
