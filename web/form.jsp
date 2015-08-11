<%-- Copyright (C) 2009-2009 Julian Hyde --%>
<%-- $Id: form.jsp 4 2009-09-07 21:19:10Z julianhyde $ --%>
<%@ page
    language="java"
    import="java.text.SimpleDateFormat,
            java.util.Date"
%>
<%
    String[][] fields = {
        {"Date", "date"},
        {"Author(s)", "author"},
        {"Password", "password"},
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
%>
<html>
<head>
<link rel="icon" type="image/png" href="favicon.ico">
<title>GGRO Daily Hawkwatch: Data entry</title>
</head>

<body>
<h1>GGRO Hawkwatch: Data entry</h1>

<p>This form updates the
    <a href="http://www.ggro.org/feed.xml" target="_blank">RSS feed</a>
    and <a href="http://www.parksconservancy.org/conservation/plants-animals/raptors/research/daily-hawk-count.html">Daily Hawk Count page</a>,
    posts to <a href="http://twitter.com/hawkcount" target="_blank">Twitter</a>,
    and sends an email to administrators.</p>

<p>For assistance, contact Julian Hyde (julian@hydromatic.net; (415) 336-3873).</p>

<form name='form1' method='post' action='action.jsp'>
<table border='1' cellpadding="3">
<%
    for (int i = 0; i < fields.length; i++) {
        if (fields[i].length == 1) {
            String text = fields[i][0];
%>
<tr>
<td colspan='3'><b><%= text %></b></td>
</tr>
<%
            continue;
        }
        String title = fields[i][0];
        String var = fields[i][1].toLowerCase();
        String value = request.getParameter(var);
        String err = request.getParameter(var + "_error");
        if (err == null) {
            err = (String) request.getAttribute(var + "_error");
        }
        if (value == null) {
            if (var.equals("date")) {
                value = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            } else {
                value = "";
            }
        }
        int size = 10;
        int span = 2;
        if (var.length() == 4 && !var.equals("date")) {
            span = 1;
        }
%>
<tr>
<td nowrap width='200' colspan='<%= span %>' bgcolor='<%= err == null ? "lightgray" : "pink" %>'><%= title + (err == null ? "" : " (<b>" + err + "</b>)") %></td>
<%
    if (var.length() == 4 && !var.equals("date")) {
%>
    <td bgcolor="lightgray" width="1"><%= var.toUpperCase() %></td>
<%
    }
%>
<td bgcolor="lightgray"><input type='text' name='<%= var %>' value='<%= value %>' size='<%= size %>'/></td>
</tr>
<%
    }
    String comments = request.getParameter("comments");
    if (comments == null) {
        comments = "";
    }
%>
<tr>
<td colspan='3'>Comments</td>
</tr>
<tr>
<td colspan='3'><textarea name='comments' rows='15' cols='60'><%= comments %></textarea></td>
</tr>
<tr>
<td colspan='3'><input type='submit'/></td>
</tr>
</table>
</form>

<script type='text/javascript'>
    document.form1.date.focus();
</script>

</body>
</html>
