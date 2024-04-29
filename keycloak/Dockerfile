FROM quay.io/keycloak/keycloak:24.0.3

COPY custom-kc.sh /opt/keycloak/bin/custom-kc.sh
COPY initialize-my-realm-and-add-users.sh /opt/keycloak/bin/initialize-my-realm-and-add-users.sh
COPY users.csv /opt/keycloak/bin/users.csv
ENTRYPOINT ["sh","/opt/keycloak/bin/custom-kc.sh" ]