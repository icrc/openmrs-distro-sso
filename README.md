<!--
SPDX-FileCopyrightText: 2025 ICRC

SPDX-License-Identifier: BSD-3-Clause
-->

[![REUSE status](https://api.reuse.software/badge/github.com/fsfe/reuse-action)](https://api.reuse.software/info/github.com/fsfe/reuse-action)

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
4. Go to http://localhost:8080
5. You should be redirected to Keycloak.
6. Sign in using a user selected from the provided list [keycloak/users.csv](./keycloak/users.csv) and the password defined in `.env` 

To login you can use the username (before @). For instance to log as `doctor.many@localhost.local`, you can use `doctor.many` in sso login page.


## Maven configuration and GitHub token

https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry#authenticating-to-github-packages

To access to packages deployed on GitHub (https://github.com/icrc/openmrs-module-fhir2extension), you must have a GitHub account and provide a PAT.
Any GitHub account is working,  You should configure your `settings.xml`file by adding:

```xml
<server>
    <id>openmrs-module-fhir2extension</id>
    <username>#{GITHUB_USERNAME}#</username>
    <password>#{GITHUB_TOKEN}#</password>
</server>
```

GITHUB_TOKEN is a `personal access token (classic) with at least read:packages scope to install packages associated with other private repositories (which GITHUB_TOKEN can't access).`

To access to GitHub repositories a token is required. The file `settings-template.xml` can be used as a template to edit
your file `settings.xml`
Developers have to edit their file `settings.xml` to add valid token taken from a GitHub account.

# How to use images:
A default docker-compose file is available here:
[https://github.com/icrc/openmrs-android-fhir/docker-compose.yml](https://github.com/icrc/openmrs-android-fhir/blob/main/docker-compose.yml)

A `.env` is always required to define passwords and sso admin username.





