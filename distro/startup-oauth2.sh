#!/bin/bash
#See the code https://github.com/openmrs/openmrs-core/blob/2.5.12/startup-init.sh
OMRS_HOME=${OMRS_HOME:-'/openmrs'}
cat > "$OMRS_HOME"/data/oauth2.properties << EOF
clientId=openmrs
clientSecret=${OMRS_OAUTH_CLIENT_SECRET}
userAuthorizationUri=${OMRS_OAUTH_USER_AUTHORIZATION_URI}
accessTokenUri=http://keycloak:8080/realms/main/protocol/openid-connect/token
keysUrl=http://keycloak:8080/realms/main/protocol/openid-connect/certs
userInfoUri=http://keycloak:8080/realms/main/protocol/openid-connect/userinfo
logoutUri=${OMRS_OAUTH_USER_LOGOUT_URI}
scope=openid
openmrs.mapping.user.username=preferred_username
openmrs.mapping.user.username.serviceAccount=email
openmrs.mapping.person.givenName=given_name
openmrs.mapping.person.familyName=family_name
openmrs.mapping.user.systemId=sub
EOF

cat > "$OMRS_HOME"/data/openmrs-runtime.properties << EOF
hibernate.cache.use_query_cache=true
EOF

/openmrs/startup.sh

