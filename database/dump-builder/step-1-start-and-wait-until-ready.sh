#!/bin/bash
docker compose down -v && docker compose up -d
sleep 5
curl http://localhost:8081/openmrs
# Can be improved by monitoring http://localhost:8081/openmrs/initialsetup
echo "Wait until http://localhost:8081/openmrs will give \"OpenMRS Plateform is running\""

docker compose logs -f