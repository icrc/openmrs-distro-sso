# SPDX-FileCopyrightText: 2025 ICRC
#
# SPDX-License-Identifier: BSD-3-Clause

mysqldump --no-create-db -u ${MYSQL_USER} -p${MYSQL_PASSWORD} ${MYSQL_DATABASE} > "/tmp/openmrs.sql"
gzip -f "/tmp/openmrs.sql" 2>&1
