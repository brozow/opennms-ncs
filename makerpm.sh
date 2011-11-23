#!/bin/sh

mvn -Drpm=true clean install

echo -e "Your RPM is: \c"
find ncs-rpm -name \*.rpm
