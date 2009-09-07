<%@ page language="java" %>
<%@ page import="java.io.*" %>
<%@ page import="javax.xml.transform.*" %>
<%@ page import="javax.xml.transform.dom.DOMSource" %>
<%@ page import="javax.xml.transform.stream.StreamResult" %>
<%@ page import="org.xml.sax.SAXException" %>
<%@ page import="javax.xml.parsers.*" %>
<%@ page import="org.w3c.dom.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.ParsePosition" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="javax.mail.internet.MimeMessage" %>
<%@ page import="javax.mail.*" %>
<%@ page import="java.net.*" %>
<%!
    boolean debug = false;

    static String htmlEncode(String s) {
        return s.replaceAll("\r\n", "<br/>")
            .replaceAll("\n", "<br/>")
            .replaceAll("\r", "<br/>");
    }

    static boolean isCount(String paramName) {
        return (paramName.length() == 4
                && !paramName.equals("date"))
               || paramName.startsWith("unid_");
    }

    static String toLong(String paramName) {
        String[][] fields = {
            {"Date", "date"},
            {"Author(s)", "author"},
            {"Total Sightings", "total_sightings"},
            {"Hours Counted", "hours_counted"},
            {"HPH", "hph"},
            {"Total Species", "total_species"},
            {"Species Counts"}, // break
            {"Turkey Vulture", "TUVU"},
            {"Osprey", "OSPR"},
            {"White-tailed Kite", "WTKI"},
            {"Bald Eagle", "BAEA"},
            {"Northern Harrier", "NOHA"},
            {"Sharp-shinned Hawk", "SSHA"},
            {"Cooper's Hawk", "COHA"},
            {"Northern Goshawk", "GOSH"},
            {"Red-shouldered Hawk", "RSHA"},
            {"Broad-winged Hawk", "BWHA"},
            {"Swainson's Hawk", "SWHA"},
            {"Red-tailed Hawk", "RTHA"},
            {"Ferruginous Hawk", "FEHA"},
            {"Rough-legged Hawk", "RLHA"},
            {"Golden Eagle", "GOEA"},
            {"American Kestrel", "AMKE"},
            {"Merlin", "MERL"},
            {"Peregrine Falcon", "PEFA"},
            {"Prairie Falcon", "PRFA"},
            {"Unidentified..."}, // break
            {"Accipiter", "unid_accipiter"},
            {"Buteo", "unid_buteo"},
            {"Eagle", "unid_eagle"},
            {"Falcon", "unid_falcon"},
            {"Raptor", "unid_raptor"},
        };
        for (String[] field : fields) {
            if (field.length == 2 && field[1].equalsIgnoreCase(paramName)) {
                return field[0];
            }
        }
        return null;
    }

    static Iterable<Node> iterate(final NodeList nodeList) {
        return new Iterable<Node>() {
            public Iterator<Node> iterator() {
                return new Iterator<Node>() {
                    int i = -1;
                    public boolean hasNext() {
                        return i < nodeList.getLength();
                    }

                    public Node next() {
                        ++i;
                        return nodeList.item(i);
                    }

                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    static class Base64 {

        public static String base64code =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "abcdefghijklmnopqrstuvwxyz"
            + "0123456789"
            + "+/";

        public static byte[] zeroPad(int length, byte[] bytes) {
            byte[] padded = new byte[length]; // initialized to zero by JVM
            System.arraycopy(bytes, 0, padded, 0, bytes.length);
            return padded;
        }

        public static String encode(String string) {
            StringBuilder encoded = new StringBuilder();
            byte[] stringArray;
            try {
                stringArray = string.getBytes("UTF-8");  // use appropriate encoding string!
            } catch (Exception ignored) {
                stringArray = string.getBytes();  // use locale default rather than croak
            }
            // determine how many padding bytes to add to the output
            int paddingCount = (3 - (stringArray.length % 3)) % 3;
            // add any necessary padding to the input
            stringArray = zeroPad(stringArray.length + paddingCount, stringArray);
            // process 3 bytes at a time, churning out 4 output bytes
            // worry about CRLF insertions later
            for (int i = 0; i < stringArray.length; i += 3) {
                int j =
                    (stringArray[i] << 16)
                    + (stringArray[i + 1] << 8)
                    + stringArray[i + 2];
                encoded.append(base64code.charAt((j >> 18) & 0x3f));
                encoded.append(base64code.charAt((j >> 12) & 0x3f));
                encoded.append(base64code.charAt((j >> 6) & 0x3f));
                encoded.append(base64code.charAt(j & 0x3f));
            }
            // replace encoded padding nulls with "="
            encoded.setLength(encoded.length() - paddingCount);
            encoded.append("==".substring(0, paddingCount));
            return encoded.toString();
        }
    }

    static class Context {
        final Map<String, String> fieldErrors = new LinkedHashMap<String, String>();
        final Map<String, Object> paramValues = new HashMap<String, Object>();

        void error(
            String paramName,
            String text)
        {
            fieldErrors.put(paramName + "_error", text);
        }

        String validateString(
            String paramName,
            boolean required)
        {
            String value = (String) paramValues.get(paramName);
            if (required && (value == null || value.equals(""))) {
                error(paramName, "Value is required");
                return null;
            } else {
                return value;
            }
        }

        Date validateDate(
            String paramName,
            boolean required)
        {
            String value = validateString(paramName, required);
            if (value == null) {
                return null;
            }
            Date date =
                new SimpleDateFormat("yyyy/MM/dd").parse(value, new ParsePosition(0));
            if (date == null) {
                error(paramName, "Date '" + value + "' must be of format yyyy/mm/dd");
            }
            return date;
        }

        BigDecimal validateDecimal(
            String paramName,
            boolean required)
        {
            String value = validateString(paramName, required);
            if ("".equals(value)) {
                paramValues.remove(paramName);
                value = null;
            }
            if (value == null && required) {
                return null;
            }
            if (value == null) {
                value = "0";
            }
            try {
                BigDecimal decimal = new BigDecimal(value);
                paramValues.put(paramName, decimal);
                return decimal;
            } catch (NumberFormatException e) {
                error(paramName, "Not a valid decimal value: " + value);
                return null;
            }
        }

        Integer validateNonNegInteger(
            String paramName,
            boolean required)
        {
            String value = validateString(paramName, required);
            if ("".equals(value)) {
                paramValues.remove(paramName);
                value = null;
            }
            if (value == null && required) {
                return null;
            }
            if (value == null) {
                value = "0";
            }
            try {
                Integer integer = new Integer(value);
                paramValues.put(paramName, integer);
                if (integer.compareTo(0) < 0) {
                    error(paramName, "Must not be less than zero");
                }
                return integer;
            } catch (NumberFormatException e) {
                error(paramName, "Not a valid integer value: " + value);
                return null;
            }
        }

        void forward(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException
        {
            for (Map.Entry<String, String> entry : fieldErrors.entrySet()) {
                request.setAttribute(entry.getKey(), entry.getValue());
            }
            request.getRequestDispatcher("form.jsp")
                .forward(request, response);
        }
    }
%>
<html>
<head>
<link rel="icon" type="image/png" href="favicon.ico">
<title>GGRO Hawkwatch Post Result</title>
</head>
<body>
<%
    String[] parameters = {
        "date",
        "author",
        "total_sightings",
        "hours_counted",
        "hph",
        "total_species",
        "tuvu",
        "ospr", 
        "wtki",      
        "baea",
        "noha",
        "ssha",
        "coha",
        "gosh",
        "rhsa",
        "bwha",
        "swha",
        "rtha",
        "feha",
        "rlha",
        "goea",
        "amke",
        "merl",
        "pefa",
        "prfa",
        "unid_accipiter",
        "unid_buteo",
        "unid_eagle",
        "unid_falcon",
        "unid_raptor",
        "comments",
    };
    Context context = new Context();
    Date date = null;
    for (int i = 0; i < parameters.length; ++i) {
        String name = parameters[i];
        context.paramValues.put(name, request.getParameter(name));
        if (name.equals("comments")
            || name.equals("author"))
        {
            context.validateString(name, true);
        } else if (name.equals("date")) {
            date = context.validateDate(name, true);
        } else if (name.equals("hours_counted")) {
            BigDecimal decimal = context.validateDecimal(name, true);
            if (decimal != null
                && (decimal.compareTo(new BigDecimal("6")) > 0
                    || decimal.compareTo(new BigDecimal("0")) < 0)) {
                context.error(name, "Must be between 0 and 6");
            }
        } else if (name.equals("hph")) {
            context.validateDecimal(name, true);
        } else if (name.equals("total_sightings")
            || name.equals("total_species")) {
            context.validateNonNegInteger(name, true);
        } else {
            context.validateNonNegInteger(name, false);
        }
    }
    if (!context.fieldErrors.isEmpty()) {
        context.forward(request, response);
        return;
    }
    int totalSightings = (Integer) context.paramValues.get("total_sightings");
    int totalSpecies = (Integer) context.paramValues.get("total_species");
    BigDecimal hoursCounted = (BigDecimal) context.paramValues.get("hours_counted");
    BigDecimal hph = (BigDecimal) context.paramValues.get("hph");
    int actualTotalSightings = 0;
    int actualSpeciesCount = 0;
    for (String parameter : parameters) {
        if (isCount(parameter)) {
            int count = (Integer) context.paramValues.get(parameter);
            if (count > 0 && parameter.length() == 4) {
                ++actualSpeciesCount;
            }
            actualTotalSightings += count;
        }
    }
    if (actualSpeciesCount != totalSpecies) {
        context.error(
            "total_species",
            "Does not match computed species count " + actualSpeciesCount);
    }
    if (actualTotalSightings != totalSightings) {
        context.error(
            "total_sightings",
            "Does not match computed total sightings "
            + actualTotalSightings);
    }
    if (!context.fieldErrors.isEmpty()) {
        context.forward(request, response);
        return;
    }
%>
<p>Validation successful!</p>
<%
    // Compose email
    String mailSubject =
        "GGRO Hawkwatch: "
        + new SimpleDateFormat("yyyy/MM/dd").format(date);
    String mailFrom = "julian@hydromatic.net";
    String mailTo = "julianhyde@gmail.com";
    Date mailSentDate = new Date();
    StringBuilder buf = new StringBuilder();
    String newline = System.getProperty("line.separator");
    for (String name : parameters) {
        buf.append(name)
            .append(": ")
            .append(context.paramValues.get(name))
            .append(newline);
    }
    String mailText = buf.toString();

    // Send email
    Properties props = new Properties();
    props.put("mail.smtp.host", "localhost");
    props.put("mail.from", mailFrom);
    javax.mail.Session mailSession = javax.mail.Session.getInstance(props, null);
    try {
        MimeMessage msg = new MimeMessage(mailSession);
        msg.setFrom();
        msg.setRecipients(
            Message.RecipientType.TO,
            mailTo);
        msg.setSubject(mailSubject);
        msg.setSentDate(mailSentDate);
        msg.setText(mailText);
        if (!debug) {
            Transport.send(msg);
        }
    } catch (MessagingException mex) {
%>
<p>Failed to send email: <%= mex.getMessage() %></p>
<%
        mex.printStackTrace();
        return;
    }
%>
<table>
<tr>
<td colspan='2'>Email sent successfully.</td>
<tr><td>To:</td><td><%= mailTo %></tr>
<tr><td>From:</td><td><%= mailFrom %></tr>
<tr><td>To:</td><td><%= mailSubject %></tr>
<tr><td>Sent:</td><td><%= mailSentDate %></td></tr>
<tr><td valign='top'>Body:</td><td><%= htmlEncode(mailText) %></td></tr>
</table>

<%
    if (false) {
        for (String parameter : parameters) {
%>
<%= parameter + "=" + context.paramValues.get(parameter) + " (" + context.paramValues.get(parameter).getClass() + ")\n" %>
<%
        }
        return;
    }

    // Compose tweet. For example:
    //      108 3.17h 34.11/h 5sp TUVU=42 OSPR=1 COHA=9 RTHA=45 AMKE=3
    //      http://u.nu/8mb63#0831 I emerged from the NPS dorm this morning
    //      amazed and dismayed...

    buf.setLength(0);
    buf.append(totalSightings)
        .append(' ')
        .append(hoursCounted)
        .append("h ")
        .append(hph)
        .append("/h ")
        .append(totalSpecies)
        .append("sp ");
    for (String parameter : parameters) {
        if (isCount(parameter)) {
            Integer count = (Integer) context.paramValues.get(parameter);
            if (count != null && count != 0) {
                buf.append(parameter.toUpperCase())
                    .append('=')
                    .append(count)
                    .append(' ');
            }
        }
    }
    String tweetUrl = "http://u.nu/8mb63"; // abbrev for http://www.hydromatic.net/ggro/daily.jsp
    //String tweetUrl = "http://u.nu/5knx"; // abbrev for http://ggro.org/hawkwatch/dailyhw09.html
    final String shortAnchor = new SimpleDateFormat("MMdd").format(date);
    buf.append(tweetUrl).append("#").append(shortAnchor).append(" ");
    String comments = (String) context.paramValues.get("comments");
    buf.append(comments.replaceAll(newline, " "));
    if (buf.length() > 140) {
        int i = 138;
        while (i > 0 && buf.charAt(i) != ' ') {
            --i;
        }
        while (i > 0 && buf.charAt(i) == ' ') {
            --i;
        }
        buf.setLength(i);
        buf.append("...");
    }
    String tweet = buf.toString();

    // Send tweet.
    if (!debug) {
        try {
            URL url = new URL("http://twitter.com:80/statuses/update.xml");
            String name = "hawkcount";
            String password = "predater";
            HttpURLConnection connection =
                (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty(
                "Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty(
                "Authorization", "Basic " + Base64.encode(name + ":" + password));
            String data =
                URLEncoder.encode("status") + "=" + URLEncoder.encode(tweet);
            connection.setRequestProperty("User-Agent", "myTwitterApp");
            connection.setRequestProperty(
                "Content-Length", "" + data.getBytes().length);
            OutputStream oStream = connection.getOutputStream();
            oStream.write(data.getBytes("UTF-8"));
            oStream.flush();
            oStream.close();

            int responseCode = connection.getResponseCode();
            System.out.println(
                "Sent tweet [" + tweet + "] at " + mailSentDate
                + ", response=" + responseCode);
            %>
<table>
<tr>
<td colspan='2'>Tweet sent successfully (code=<%= responseCode %>).</td>
<tr><td>Message:</td><td><%= tweet %></tr>
</table>
<%
        } catch (IOException e) {
            %>
<p>Failed to send tweet: <%= e.getMessage() %></p>
<%
            System.out.println(
                "Failed to send tweet [" + tweet + "] at " + mailSentDate);
            e.printStackTrace();
            return;
        }
    }

    // Generate feed.
    boolean success = false;
    Throwable throwable = null;
    String timestamp =
        new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ").format(mailSentDate);
    try {
        File file = new File("/home/jhyde/ggro/feed.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        final Element docElement = doc.getDocumentElement();
        Element feedElement;
        if (docElement.getNodeType() == Node.ELEMENT_NODE
            && docElement.getNodeName().equals("feed"))
        {
            feedElement = docElement;
        } else {
            throw new RuntimeException("no <feed> element");
        }
        Element firstEntryElement = null;
        Element updatedElement = null;
        for (Node node : iterate(feedElement.getChildNodes())) {
            System.out.println("node=" + node);
            if (node == null) {
                continue;
            }
            if (node.getNodeType() == Node.ELEMENT_NODE
                && node.getNodeName().equals("updated"))
            {
                updatedElement = (Element) node;
            }
            if (node.getNodeType() == Node.ELEMENT_NODE
                && node.getNodeName().equals("entry")
                && firstEntryElement == null)
            {
                firstEntryElement = (Element) node;
            }
        }
        if (updatedElement == null) {
            throw new RuntimeException("no <updated> element");
        } else {
            updatedElement.setTextContent(timestamp);
        }
        System.out.println("update=" + firstEntryElement);
        Element entryElement = doc.createElement("entry");
        feedElement.insertBefore(entryElement, firstEntryElement);
        feedElement.insertBefore(doc.createTextNode("\n\n"), firstEntryElement);

        // feed/entry/id
        Element idElement = doc.createElement("id");
        entryElement.appendChild(idElement);
        final String anchor = new SimpleDateFormat("yyyyMMdd").format(date);
        final String entryId = "hydromatic-ggro-" + anchor;
        idElement.setTextContent(entryId);

        // feed/entry/published
        Element publishedElement = doc.createElement("published");
        entryElement.appendChild(publishedElement);
        publishedElement.setTextContent(timestamp);

        // feed/entry/updated
        Element entryUpdatedElement = doc.createElement("updated");
        entryElement.appendChild(entryUpdatedElement);
        entryUpdatedElement.setTextContent(timestamp);

        // feed/entry/title
        Element titleElement = doc.createElement("title");
        entryElement.appendChild(titleElement);
        String title =
            new SimpleDateFormat("EEEE, MMMM dd, yyyy").format(date);
        titleElement.setTextContent(title);

        // feed/entry/content
        Element contentElement = doc.createElement("content");
        entryElement.appendChild(contentElement);
        contentElement.setAttribute("type", "html");
        buf.setLength(0);
        buf.append(
            comments.replaceAll("\r\n", "<br/>\n")
                .replaceAll("\n", "<br/>\n")
                .replaceAll("\r", "<br/>\n"));
        buf.append("<br/>\n")
            .append("Total Sightings: ")
            .append(totalSightings)
            .append("<br/>\n")
            .append("Hours Counted: ")
            .append(hoursCounted)
            .append("<br/>\n")
            .append("HPH: ")
            .append(hph)
            .append("<br/>\n")
            .append("Total Species: ")
            .append(totalSpecies)
            .append("<br/>\n");
        int unidCount = 0;
        int speciesCount = 0;
        for (String name : parameters) {
            if (!isCount(name)) {
                continue;
            }
            final Integer count =
                (Integer) context.paramValues.get(name);
            if (count == 0) {
                continue;
            }
            if (name.startsWith("unid_")
                && unidCount++ == 0)
            {
                buf.append("<br/>\n")
                    .append("Unidentified...")
                    .append("<br/>\n");
            } else if (speciesCount++ == 0) {
                buf.append("<br/>\n")
                    .append("Species Counts:")
                    .append("<br/>\n");
            }
            buf.append(toLong(name))
                .append(": ")
                .append(count)
                .append("<br/>\n");
        }

        contentElement.setTextContent(buf.toString());

        // feed/entry/link
        Element linkElement = doc.createElement("link");
        entryElement.appendChild(linkElement);
        linkElement.setAttribute("rel", "alternative");
        linkElement.setAttribute("type", "text/html");
        linkElement.setAttribute("type", "text/html");
        linkElement.setAttribute("href", "http://ggro.org/hawkwatch/dailyhw09.html");
        linkElement.setAttribute("title", title);

        // feed/entry/link#2
        Element link2Element = doc.createElement("link");
        entryElement.appendChild(link2Element);
        link2Element.setAttribute("rel", "self");
        link2Element.setAttribute("type", "application/atom+xml");
        link2Element.setAttribute("href", "http://www.hydromatic.net/ggro.xml#" + anchor);

        // feed/entry/author
        Element authorElement = doc.createElement("author");
        entryElement.appendChild(authorElement);

        // feed/entry/author/name
        String author = (String) context.paramValues.get("author");
        Element nameElement = doc.createElement("name");
        authorElement.appendChild(nameElement);
        nameElement.setTextContent(author);

        // Modify the document update time.
        Transformer transformer =
            TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        Source source = new DOMSource(doc);
        Result result = new StreamResult(file);
        transformer.transform(source, result);
        success = true;
    } catch (ParserConfigurationException e) {
        throwable = e;
    } catch (SAXException e) {
        throwable = e;
    } catch (TransformerConfigurationException e) {
        throwable = e;
    } catch (TransformerException e) {
        throwable = e;
    } catch (RuntimeException e) {
        throwable = e;
    }
    if (success) {
%>
<table>
<tr>
<td colspan='2'><p>Feed updated successfully.</p>

<p>Results should be visible at <a href="daily.jsp">Daily Hawkwatch Count Page</a>.</p></td>
</table>
<%
    } else {
%>
<p>Failed to update RSS feed: <%= throwable.getClass() %>: <%= throwable.getMessage() %></p>
<%
        throwable.printStackTrace();
        return;
    }
%>


<p>Successful!</p>
</body>
</html>

