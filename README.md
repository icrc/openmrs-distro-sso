# openmrs-distro-keycloak
reference openmrs distro datafilter, oauth2 module using Keycloak with default users configured:
- The users list is given by `keycloak/users.csv`
- The default password is setup in a env variables, see `env.default`

# How to start
1. Copy the file `.env.default` to `.env`
2. Edit .env and provide default username, password
3. Start Using published images:
   `docker compose -f docker-compose.yml up -d --build`
4. to build images locally and start: `docker compose up -d --build`

# Login to ghrc


```bash
export CR_PAT=YOUR_TOKEN
echo $CR_PAT | docker login ghcr.io -u <yourAccount> --password-stdin
```

See https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-container-registry

# Useful command

- `docker compose down -v` to restart all from scratch
- `docker compose up -d` to start quickly

# Other Resources

- [database](./database/README.md) is used to ceate a MariaDB Docker Image with an OpenMRS Dump.



