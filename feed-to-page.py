#!/usr/bin/python3
#
# Reads the XML feed and generates an HTML web page
#
# Usage:
#   python3 ./feed-to-page.py > web/archive/2018/page.html
#
# (replacing 2018 with the current year)
#
import xml.etree.ElementTree as ET
import re
import sys

sys.stdout.reconfigure(encoding='utf-8')

tree = ET.parse('web/archive/2022/feed.xml')
root = tree.getroot()
ns = "{http://www.w3.org/2005/Atom}"
print("<html>")
print("<head><title>Daily Hawk Count (2022)</title></head>")
print("<body>")
for entry in root.iter(ns + "entry"):
  # "Sunday, December 02, 2018"
  title = entry.find(ns + "title").text
  # hydromatic-ggro-20181202
  id = entry.find(ns + "id").text
  tag = re.sub(r".*-", "", id)
  print("<h2><a name='" + tag + "'/>" + title + "</h2>")
  content = entry.find(ns + "content").text
  print("<p>" + content + "</p>")
  print
print("</body>")
print("</html>")

# End feed-to-page.py
