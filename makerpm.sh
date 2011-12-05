#!/bin/sh -e

if [ -z "$OPENNMS_RELEASE" ]; then
	OPENNMS_RELEASE=0.`date '+%Y%m%d'`.1
fi
mvn -Drpm=true -Drelease="$OPENNMS_RELEASE" clean install

echo -e "Your RPM is: \c"
find ncs-rpm -name \*.rpm
