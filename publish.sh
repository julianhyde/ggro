#!/bin/bash
# Script to publish feed and report to GGRO web site

retry=
if [ "$1" == --retry ]; then
    retry=true
fi

# Generate report.
BASE_DIR=/home/jhyde/dev/ggro
gawk -F, -f ${BASE_DIR}/web/report.awk ${BASE_DIR}/web/data.csv > ${BASE_DIR}/web/report.html

# Fix permissions.
# (Git commit, checkout seem to mess them up.)
chmod 666 ${BASE_DIR}/web/feed.xml
chmod 777 ${BASE_DIR}/web/report.html
chmod 777 ${BASE_DIR}/web/data.csv

# Upload hawkwatch page and feed.
#put /tmp/dailyhw09.html hawkwatch/dailyhw09.html
cd ${BASE_DIR}
while true; do
    ftp -v -n ggro.org <<EOF | tee /tmp/ftp.log
user "ggroweb" "changeme"
put web/report.html report.html
put web/ggro-rappass.xml news/feed.xml
put web/feed.xml feed.xml
put web/data.csv data.csv
quit
EOF


    if grep -q "Permission denied" /tmp/ftp.log \
        && [ "$retry" ];
    then
        echo
        date
        echo "Failed to ftp. Will wait and retry."
        sleep 1m
    else
        break
    fi
done

# End publish.sh
