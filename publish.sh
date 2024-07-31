#!/bin/bash
# Script to publish feed and report to GGRO web site

LFTP_USER="changeme"
LFTP_PW="changeme"
LFTP_HOST="changeme"

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
cd ${BASE_DIR}
LOG=/tmp/lftp_${$}.log
while true; do
    lftp -u "${LFTP_USER},${LFTP_PW}" "${LFTP_HOST}" <<EOF | tee ${LOG}
set ssl:verify-certificate/D3:B1:2F:1F:20:31:53:2F:2F:EA:18:A0:FF:F6:6C:D9:D1:54:83:51 no
put web/report.html
put web/feed.xml
put web/data.csv
ls -trl
quit
EOF

    if grep -q "Permission denied" ${LOG} \
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
