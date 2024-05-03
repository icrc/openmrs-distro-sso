# Why
The goal of this distribution is to enable the effortless initiation of an OpenMRS Distro with Single Sign-On (SSO) and DataFilter activated through a single command line (utilizing Docker).

# ICRC Customization

This distribution is a customized version of https://github.com/openmrs/openmrs-distro-referenceapplication to:
- integrate Oauth2 Module: [openmrs-module-oauth2login](https://github.com/openmrs/openmrs-module-oauth2login)
- integrate DataFilter Module: [openmrs-module-datafilter](https://github.com/openmrs/openmrs-module-datafilter)
- Generate default users and populate the database with patient data.

# Components

- [database](./database/README.md) is used to create a MariaDB Docker Image with an "initial" OpenMRS Dump.
- [frontend](./frontend/README.md) activate oauth2 login in the frontend
- [backend](./backend/README.md) create an OpenMRS distribution incorporating OAuth2, DataFilter modules, and a custom module for generating default users and patients. 
- [keycloak](./keycloak/README.md) start and configure automatically Keycloak

# How to build and start

1. Copy the file `.env.default` to `.env`
2. Edit .env and provide default username, password
3. Start dockers with: `docker compose up -d --build`
4. Go to http://localhost
5. You should be redirected to Keycloak.
6. Sign in using a user selected from the provided list [keycloak/users.csv](./keycloak/users.csv) and the password defined in `.env` 

To login you can use the username (before @). For instance to log as `doctor.many@localhost.local`, you can use `doctor.many` in sso login page.

# How to use images:
A default docker-compose file is available here:
https://github.com/icrc/openmrs-android-fhir/docker-compose.yml

A `.env` is always required to define passwords and sso admin username.





