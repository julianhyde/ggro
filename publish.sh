#!/bin/bash
# Script to publish feed and report to GGRO web site

retry=
if [ "$1" == --retry ]; then
    retry=true
fi

# Generate report.
gawk -F, -f /home/jhyde/web2/ggro/web/report.awk /home/jhyde/web2/ggro/web/data.csv > /home/jhyde/web2/ggro/web/report.html

# Upload hawkwatch page and feed.
#put /tmp/dailyhw09.html hawkwatch/dailyhw09.html
while true; do
    ftp -v -n ggro.org <<EOF | tee /tmp/ftp.log
user "ggroweb" "changeme"
put /home/jhyde/web2/ggro/web/report.html report.html
put /home/jhyde/web2/ggro/web/ggro-rappass.xml news/feed.xml
put /home/jhyde/web2/ggro/web/feed.xml feed.xml
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
