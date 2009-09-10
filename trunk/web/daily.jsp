<%-- Copyright (C) 2009-2009 Julian Hyde --%>
<%-- $Id$ --%>
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
<%@ page import="com.sun.xml.internal.messaging.saaj.soap.SOAPPartImpl" %>
<%!
    private Element findChild(Node node, String name) {
        for (Node child : iterate(node.getChildNodes())) {
            if (child.getNodeType() == Node.ELEMENT_NODE
                && child.getNodeName().equals(name))
            {
                return (Element) child;
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
%>
<html>
<head>
<!-- #BeginEditable "doctitle" -->
<title>Golden Gate Raptor Observatory</title>
<link href="http://ggro.org/styles/ggro.css" rel="stylesheet" type="text/css">
<style type="text/css">
<!--
.style1 {
	font-size: medium;
	font-weight: bold;
}
-->
</style>
<style type="text/css">
<!--
a {
	font-family: Arial, Helvetica, sans-serif;
}
-->
</style>
<script type="text/JavaScript">
<!--
function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;&amp;&amp;i&lt;a.length&&(x=a[i])&amp;&amp;x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i&lt;a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&amp;&amp;parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&amp;&amp;d.all) x=d.all[n]; for (i=0;!x&amp;&amp;i&lt;d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&amp;&amp;d.layers&amp;&amp;i&lt;d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x &amp;&amp; d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i&lt;(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
//-->
</script>
<style type="text/css">
<!--
.style2 {
	font-family: Arial;
	font-weight: bold;
}
-->
</style><link REL="SHORTCUT ICON" HREF="http://ggro.org/Templates/favicon.ico">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<body bgcolor="#6388D6"  background="http://ggro.org/images/cloud-bg.jpg" text="#00000" link="#000066" vlink="#9A3300" onLoad="MM_preloadImages('../images/s-broadwing.jpg')">
<table width="700" border="0" align="left">
    <table width="661" border="0">
        <tr>
        <td align="center" valign="top" height="212">&nbsp;</td>
        <td valign="top" align="center"><p><br>
            <a href="http://www.ggro.org" target="_blank"><img src="http://ggro.org/images/GGRO-Logo-new-2-color_shell.gif" width="150" height="152" border="0" class="logoBorder"></a></p>
        </td>
        </tr>
        <tr>
          <td align="center" valign="top" height="293" width="5%">            <p>&nbsp;</p></td>
          <td align="center" valign="top" bordercolor="#903F00" bgcolor="#F4EBC2">
              <p><br>
              <span class="orangeBold">DAILY HAWK COUNT 2009 </span>
              <p><span class="header">If the hawk count is not immediately posted, try the RSS/blog tab on our Facebook page: <a href="http://www.facebook.com/ggraptors" target="_blank">http://www.facebook.com/ggraptors</a><br>
              or call the Hawkwatch Hotline, 415-561-3030 ext 2500 </span><span class="orangeBold"><br>
                </span><span class="textblue">NOTE: We expect this page to change with our website transitions.<br>
                <strong>Thank you for your patience!                </strong></span>
              <table width="600" border="1">

<%
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
        for (Node node : iterate(feedElement.getChildNodes())) {
            if (node != null
                && node.getNodeType() == Node.ELEMENT_NODE
                && node.getNodeName().equals("entry"))
            {
                String id = findChild(node, "id").getTextContent();
                assert id.startsWith("ggro-hydromatic-");
                String anchor = id.substring("ggro-hydromatic-".length());
                String shortAnchor = anchor.substring(anchor.length() - 4);
                String title = findChild(node, "title").getTextContent();
                String author = findChild(findChild(node, "author"), "name").getTextContent();
                String content = findChild(node, "content").getTextContent();
                content = content.replaceAll("Species Counts:", "<strong>$0</strong>");
                content = content.replaceAll("Total Sightings:", "<strong>$0</strong>");
                content = content.replaceAll("Hours Counted:", "<strong>$0</strong>");
                content = content.replaceAll("HPH:", "<strong>$0</strong>");
                content = content.replaceAll("Total Species:", "<strong>$0</strong>");
                content = content.replaceAll("Unidentified\\.\\.\\.", "<strong>$0</strong>");

%>
              <!-- Begin entry -->
                <tr>
                  <td><p class="header"><a name="<%= anchor %>"><%= title %></a><a name="<%= shortAnchor %>">&nbsp;</a></p>
                      <p><small>by <%= author %></small></p>
                      <p style="margin-bottom: 0.19in"><strong><span style="font-family:Arial; ">Daily HawkBlog: </span></strong><span style="font-family:Arial; font-weight:normal; "><%= content %></span></p>
                      <p class="smalltext">Data have not been entirely checked &mdash; contact Buzz Hull at bhull@parksconservancy.org for final results and for permission to use.</p>
                  </td>
                </tr>
              <!-- End entry -->
<%
            }
        }
    } catch (ParserConfigurationException e) {
        e.printStackTrace();
    } catch (SAXException e) {
        e.printStackTrace();
    } catch (RuntimeException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
%>
              </table>
          </td>
        </tr>
    </table>
</table>
</body>
</html>