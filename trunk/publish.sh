#!/bin/bash

# Get the latest hawkwatch page (generated from feed.xml).
cd /tmp
rm -f dailyhw09.jsp dailyhw09.html
wget http://www.hydromatic.net/ggro/dailyhw09.jsp
mv dailyhw09.jsp dailyhw09.html

# Upload hawkwatch page and feed.
ftp -v -n ggro.org <<EOF
user "ggroweb" "changeme"
lcd /home/jhyde/ggro
put feed.xml
cd hawkwatch
put dailyhw09.html
lcd /home/jhyde/ggro
cd ..
put feed.xml
quit
EOF

rm -f dailyhw09.jsp dailyhw09.html

# Copy feed.xml so it appears as http://www.hydromatic.net/ggro.xml.
cp /home/jhyde/ggro/feed.xml /home/jhyde/web/ggro.xml

# End publish.sh
