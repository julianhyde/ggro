<%-- Copyright (C) 2009-2009 Julian Hyde --%>
<%-- $Id: action.jsp 4 2009-09-07 21:19:10Z julianhyde $ --%>
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
<%@ page import="javax.mail.internet.*" %>
<%@ page import="javax.mail.*" %>
<%@ page import="javax.mail.PasswordAuthentication" %>
<%@ page import="java.net.*" %>
<%!
    // If true, doesn't tweet, and prints more diagnostics.
    static final boolean debug = false;

    // Same password for web form and for twitter.
    static final String password = "changeme";

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

    static int tweet(String password, String tweet, Date mailSentDate) throws java.io.IOException {
        if (false) {
            return tweetViaApi(password, tweet, mailSentDate);
        }
        return tweetViaEmail(password, tweet, mailSentDate);
    }

    static int tweetViaApi(String password, String tweet, Date mailSentDate) throws java.io.IOException {
        int responseCode = -1;
        for (int i = 0; i < 10; i++) {
            URL url = new URL("http://twitter.com:80/statuses/update.xml?source=twitterandroid&lat=37.82853&long=-122.498771");
            String name = "hawkcount";
            String twitterPassword = password;
            HttpURLConnection connection =
                (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty(
                "Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty(
                "Authorization",
                "Basic " + Base64.encode(name + ":" + password));
            String data =
                URLEncoder.encode("status") + "="
                + URLEncoder.encode(tweet);
            connection.setRequestProperty("User-Agent", "myTwitterApp");
            connection.setRequestProperty(
                "Content-Length", "" + data.getBytes().length);
            OutputStream oStream = connection.getOutputStream();
            oStream.write(data.getBytes("UTF-8"));
            oStream.flush();
            oStream.close();

            // HTTP 200 is success.
            // But about 60% of the time, we get an HTTP 408. Wait 3
            // seconds, and retry up to 10 times.
            responseCode = connection.getResponseCode();
            System.out.println(
                "Sent tweet [" + tweet + "] at " + mailSentDate
                + ", response=" + responseCode);
            if (responseCode == 200) {
                break;
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                // ignore
            }
        }
        return responseCode;
    }

    static int tweetViaEmail(String password, String tweet, Date mailSentDate) throws java.io.IOException {
        try {
            email(
                "tweet@tweetymail.com",
                tweet,
                mailSentDate,
                "");
            return 0;
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
    }

    static void email(
        String mailTo,
        String mailSubject,
        Date mailSentDate,
        String mailText)
        throws MessagingException
    {
        final String username = "julianhyde@gmail.com";
        final String password = "changeme";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
 
        Session mailSession = Session.getInstance(
            props,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

        MimeMessage message = new MimeMessage(mailSession);
        message.setFrom(new InternetAddress("julianhyde@gmail.com"));
        message.setRecipients(
            Message.RecipientType.TO,
            mailTo);
        message.setSubject(mailSubject);
        message.setSentDate(mailSentDate);
        message.setText(mailText);
        if (!debug) {
            Transport.send(message);
        }
        System.out.println("action.jsp: email sent"); 
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
                // use appropriate encoding string!
                stringArray = string.getBytes("UTF-8");
            } catch (Exception ignored) {
                // use locale default rather than croak
                stringArray = string.getBytes();
            }
            // determine how many padding bytes to add to the output
            int paddingCount = (3 - (stringArray.length % 3)) % 3;
            // add any necessary padding to the input
            stringArray =
                zeroPad(stringArray.length + paddingCount, stringArray);
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

    static class StreamGobbler extends Thread {
        final InputStream is;
        final String type;

        StreamGobbler(InputStream is, String type) {
            this.is = is;
            this.type = type;
        }

        public void run() {
            try {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(type + ">" + line);
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    static class Context {
        final Map<String, String> fieldErrors =
            new LinkedHashMap<String, String>();
        final Map<String, Object> paramValues = new HashMap<String, Object>();

        final String[] parameters = {
            "date",
            "author",
            "password",
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
            "rsha",
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
            }
            if (paramName.equals("password")) {
                if (!value.equals(password)) {
                    error(
                        paramName,
                        "Invalid password. Ask Jill Harley for the correct password.");
                } else {
                    // Mask out password so it doesn't appear in emails etc.
                    value = "xxx";
                    paramValues.put(paramName, value);
                }
            }
            return value.trim();
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
                new SimpleDateFormat("yyyy/MM/dd")
                    .parse(value, new ParsePosition(0));
            if (date == null) {
                error(
                    paramName,
                    "Date '" + value + "' must be of format yyyy/mm/dd");
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

        /**
         * Append to data file.
         * Fields: date,author,total_sightings,hours_counted,hph,total_species,tuvu, ... ,unid_raptor
         */ 
        void appendToFile(
            Date date,
            BigDecimal hph,
            int totalSpecies)
            throws IOException
        {
            FileWriter fw = new FileWriter(new File("/home/jhyde/ggro/data.csv"), true);
            PrintWriter pw = new PrintWriter(fw);
            StringBuilder buf = new StringBuilder();
            buf.append(new SimpleDateFormat("yyyyMMdd").format(date))
                .append(',')
                .append(paramValues.get("author"))
                .append(',')
                .append(paramValues.get("total_sightings"))
                .append(',')
                .append(paramValues.get("hours_counted"))
                .append(',')
                .append(hph)
                .append(',')
                .append(totalSpecies);
            for (String parameter : parameters) {
                if (isCount(parameter)) {
                    Integer count = (Integer) paramValues.get(parameter);
                    if (count == null) {
                        count = 0;
                    }
                    buf.append(',')
                        .append(count);
                }
            }
            pw.println(buf.toString());
            pw.close();
            fw.close();
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
System.out.println("action.jsp at " + new Date());
    StringBuilder buf = new StringBuilder();
    Context context = new Context();
    Date date = null;
    for (int i = 0; i < context.parameters.length; ++i) {
        String name = context.parameters[i];
        context.paramValues.put(name, request.getParameter(name));
        if (name.equals("comments")
            || name.equals("author")
            || name.equals("password"))
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
System.out.println("action.jsp " + context.fieldErrors);
        context.forward(request, response);
        return;
    }
System.out.println("action.jsp y");
    int totalSightings =
       (Integer) context.paramValues.get("total_sightings");
    int totalSpecies =
       (Integer) context.paramValues.get("total_species");
    BigDecimal hoursCounted =
       (BigDecimal) context.paramValues.get("hours_counted");
    BigDecimal hph = (BigDecimal) context.paramValues.get("hph");
    int actualTotalSightings = 0;
    int actualSpeciesCount = 0;
    for (String parameter : context.parameters) {
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
    if (hoursCounted.compareTo(new BigDecimal("0.1")) > 0) {
        BigDecimal totalSightingsBd = new BigDecimal(totalSightings);
        BigDecimal computedTotalSightings =
            hph
                .multiply(hoursCounted);
        if (!computedTotalSightings.equals(totalSightingsBd)) {
            BigDecimal ceilSightings =
                hph
                    .multiply(
                        hoursCounted
                            .add(new BigDecimal("0.1")));
            BigDecimal floorSightings =
                hph
                    .multiply(
                        hoursCounted
                            .subtract(new BigDecimal("0.1")));
            if (totalSightingsBd.compareTo(floorSightings) <= 0
                || totalSightingsBd.compareTo(ceilSightings) >= 0)
            {
                context.error(
                    "hours_counted",
                    "Make sure hours_counted is accurate to within 0.1 hours. "
                    + "It is currently inconsistent with hph and total_sightings. "
                    + "Assuming " + hph + " hph is accurate and hours_counted is "
                    + hoursCounted
                    + " +/- 0.1, would allow range of " + floorSightings + " to "
                    + ceilSightings + " sightings.");
            }
        }
    }
    if (!context.fieldErrors.isEmpty()) {
        context.forward(request, response);
        return;
    }
%>
<p>Validation successful!</p>
<%
    if (!debug) {
        context.appendToFile(date, hph, totalSpecies);
    }

    // Compose email
    String mailSubject =
        "GGRO Hawkwatch: "
        + new SimpleDateFormat("yyyy/MM/dd").format(date);
    String mailFrom = "julian@hydromatic.net";
    String mailTo =
        "julianhyde@gmail.com"
        + ",jharley@parksconservancy.org";
    Date mailSentDate = new Date();
    buf.setLength(0);
    String newline = System.getProperty("line.separator");
    for (String name : context.parameters) {
        buf.append(name)
            .append(": ")
            .append(context.paramValues.get(name))
            .append(newline);
    }
    String mailText = buf.toString();

    if (false) 
    try {
        email(mailTo, mailSubject, mailSentDate, mailText);
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
        for (String parameter : context.parameters) {
%>
<%= parameter + "=" + context.paramValues.get(parameter)
    + " (" + context.paramValues.get(parameter).getClass() + ")\n" %>
<%
        }
        return;
    }

    // Compose tweet. For example:
    //      108 3.17h 34.11/h 5sp TUVU=42 OSPR=1 COHA=9 RTHA=45 AMKE=3
    //      http://u.nu/8mb63#0831 SLAK: I emerged from the NPS dorm this
    //      morning amazed and dismayed...

    buf.setLength(0);
    buf.append(totalSightings)
        .append(' ')
        .append(hoursCounted)
        .append("h ")
        .append(hph)
        .append("/h ")
        .append(totalSpecies)
        .append("sp ");
    for (String parameter : context.parameters) {
        if (isCount(parameter)) {
            Integer count = (Integer) context.paramValues.get(parameter);
            if (count != null && count != 0 && parameter.length() == 4) {
                buf.append(parameter.toUpperCase())
                    .append('=')
                    .append(count)
                    .append(' ');
            }
        }
    }
    String tweetUrl;
    // abbrev for http://www.hydromatic.net/ggro/daily.jsp
    if (false) tweetUrl = "http://u.nu/8mb63";
    // abbrev for http://www.ggro.org/hawkwatch/dailyhw09.html
    if (false) tweetUrl = "http://u.nu/5knx";
    // abbrev for http://www.ggro.org/events/hawkwatchToday.aspx
    if (false) tweetUrl = "http://u.nu/67aj3";
    if (false) tweetUrl = "http://3.ly/ggro";
    if (true) tweetUrl = "is.gd/PbDDqy";
    if (false) tweetUrl = "t.co/mzFFpTr";
    if (false) tweetUrl = "http://www.ggro.org/events/hawkwatchToday.aspx";
    tweetUrl += "#" + new SimpleDateFormat("MMdd").format(date);
    String placeholder = "xxxxxxxxxxxxxxxxxxxxx"; // 22 chars
    if (buf.length() + placeholder.length() <= 140) {
        buf.append(placeholder);
    }
    String comments = (String) context.paramValues.get("comments");
    String body = comments.replaceAll(newline, " ");
    if (buf.length() <= 137) {
        buf.append(" ");
        buf.append(body);
    }
    if (buf.length() > 140) {
        int i = 137;
        while (i > 0 && buf.charAt(i) != ' ') {
            --i;
        }
        while (i > 0 && buf.charAt(i) == ' ') {
            --i;
        }
        buf.setLength(i + 1);
        buf.append("...");
    }
    String tweet = buf.toString().replace(placeholder, tweetUrl);

    // Send tweet.
    if (true) {
        try {
            int responseCode = tweet(password, tweet, mailSentDate);
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
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(mailSentDate);
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
            comments.replaceAll("\r\n", "<br/>")
                .replaceAll("\n", "<br/>")
                .replaceAll("\r", "<br/>"));
        buf.append("<br/>\n")
            .append("<br/>\n")
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
        for (String name : context.parameters) {
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
        linkElement.setAttribute("rel", "alternate");
        linkElement.setAttribute("type", "text/html");
        linkElement.setAttribute("type", "text/html");
        linkElement.setAttribute(
            "href", "http://www.ggro.org/events/hawkwatchToday.aspx#" + anchor);
        linkElement.setAttribute("title", title);

        // feed/entry/link#2
        Element link2Element = doc.createElement("link");
        entryElement.appendChild(link2Element);
        link2Element.setAttribute("rel", "self");
        link2Element.setAttribute("type", "application/atom+xml");
        link2Element.setAttribute(
            "href", "http://www.ggro.org/feed.xml#" + anchor);

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
        System.out.println("Local feed updated successfully.");
%>
<table>
<tr>
<td colspan='2'><p>Local feed updated successfully. (See <a href="feed.xml" target=_blank>feed</a>.)</p></td>
</table>
<%
    } else {
%>
<p>Failed to update RSS feed:
<%= throwable.getClass() %>: <%= throwable.getMessage() %></p>
<%
        System.out.println("Failed to update local feed.");
        throwable.printStackTrace();
        return;
    }
%>

<%
    // Run shell script to publish.
    try {
        final Runtime runtime = Runtime.getRuntime();
        final Process process = runtime.exec("/home/jhyde/ggro/publish.sh");
        new StreamGobbler(process.getInputStream(), "out");
        new StreamGobbler(process.getErrorStream(), "err");
        int rc = process.waitFor();
        if (rc == 0) {
            System.out.println("Publish succeeded.");
%>
<table>
<tr>
<td colspan='2'><p>Published successfully.</p>
<p>Results should be visible at
    <a href="http://www.ggro.org/events/hawkwatchToday.aspx" target=_blank>Daily Hawkwatch Count Page</a> and
    <a href="http://www.ggro.org/feed.xml" target="_blank">feed</a>.</p></td>
</table>
<%
        } else {
            System.out.println("Publish returned status " + rc);
            throw new RuntimeException("Command returned status " + rc);
        }
    } catch (Throwable t) {
%>
<p>Failed to update RSS feed:
<%= throwable.getClass() %>: <%= throwable.getMessage() %></p>
<%
        System.out.println("Failed to publish.");
        t.printStackTrace();
    }
%>

<p>Successful!</p>
</body>
</html>

