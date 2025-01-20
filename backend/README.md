<!--
SPDX-FileCopyrightText: 2025 ICRC

SPDX-License-Identifier: BSD-3-Clause
-->

ICRC Customization of https://github.com/openmrs/openmrs-distro-referenceapplication to:
- integrate Oauth2 Module: [openmrs-module-oauth2login](https://github.com/openmrs/openmrs-module-oauth2login)
- integrate DataFilter Module: [openmrs-module-datafilter](https://github.com/openmrs/openmrs-module-datafilter)
- activate Location Based identifier ( see related configuration in `openmrs_custom_config` folder)
- 4 Locations are activated: `Location 1`,`Location 2`,`Location 3`,`Location 4`. See [openmrs_custom_config/locations/locations-core_demo_with_code.csv](./openmrs_custom_config/locations/locations-core_demo_with_code.csv)
- Create Default user via the intern module `module-create-users` (to be improved): see [module-create-users/README.md](./module-create-users/README.md)


O