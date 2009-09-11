<%-- Copyright (C) 2009-2009 Julian Hyde --%>
<%-- $Id: daily.jsp 4 2009-09-07 21:19:10Z julianhyde $ --%>
<%@ page language="java" %>
<%@ page import="java.io.*" %>
<%@ page import="org.xml.sax.SAXException" %>
<%@ page import="javax.xml.parsers.*" %>
<%@ page import="org.w3c.dom.*" %>
<%@ page import="java.util.*" %>
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
<html><!-- #BeginTemplate "/Templates/ggro.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Golden Gate Raptor Observatory</title>
<link href="../styles/ggro.css" rel="stylesheet" type="text/css">
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
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
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
</style><!-- #EndEditable --> 
<link REL="SHORTCUT ICON" HREF="../Templates/favicon.ico">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">


<script src="http://www.google-analytics.com/urchin.js" type="text/javascript">
</script>
<script type="text/javascript">
_uacct = "UA-74599-3";
urchinTracker();
</script>

</head>

<body bgcolor="#6388D6"  background="../images/cloud-bg.jpg" text="#00000" link="#000066" vlink="#9A3300" onLoad="MM_preloadImages('../images/s-broadwing.jpg')">
<table width="700" border="0" align="left">
  <tr>
    <td width="20%" height="385" valign="top"> 
      <p><a href="../index.html" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('GGRO Home','','../images/ggrohomebtn_f2.jpg',1)" target="_parent"><b><font size="2"><img src="../images/blankspace-no-bg.gif" width="120" height="1" border="0"><br>
        <font size="3"> Home <br>
        </font></font></b></a><font size="3"><b><a href="../quiz/raptorquiz3.html" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('quiz','','../images/quizbtn_f2.jpg',1)">Raptor 
        Quiz <br>
        </a><a href="../raptrends.html" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Timing','','../images/timingbtn_f2.jpg',1)">Timing<br>
        </a><a href="../camap.html" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('map','','../images/mapbtn_f2.jpg',1)">Hawk 
        Hill Map</a><br>
        <a href="../weather.html" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('weather','','../images/weatherbtn_f2.jpg',1)">Hawk 
        Hill Weather </a><br>
        <a href="../hhlandmarks.html" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image17','','../images/hawkhillvwbtn_f2.jpg',1)">Hawk 
        Hill View</a><br>
        <a href="../idhelp.html" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('IDHELP','','../images/idhelpbtn_f2.jpg',1)">I.D. 
        Help </a><br>
        <a href="../research.html" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('research','','../images/researchbtn_f2.jpg',1)">Research</a><br>
        <a href="../pubed.html" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('education','','../images/educationbtn_f2.jpg',1)">Education</a><br>
        <a href="../vols.html" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('volunteer','','../images/volunteerbtn_f2.jpg',1)">Volunteer</a><br>
        <a href="../ggrofaq.html" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('faq','','../images/faqbtn_f2.jpg',1)">FAQ</a><br>
        <a href="../mission.html" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('mission','','../images/missionbtn_f2.jpg',1)">Mission</a><br>
        <a href="../photoalbum06.html" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image15','','../images/photobtn-f2.jpg',1)">Photo 
        Album </a><br>
        <a href="../rare-raptors.html" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image16','','../images/rare-raptors-f2.jpg',1)">Rare 
        Raptors </a><br>
        <a href="../hawkwatch/dailyhw09.html" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Hawkwatch Today','','../images/hawkwtchtdybtn_f2.jpg',1)">HawkWatch 
        Today <br>
        </a><a href="../current-e.html" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image20','','../images/currrent-ef2.jpg',1)">Current 
        Events </a><br>
        <a href="../links.html" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('links','','../images/linksbtn_f2.jpg',1)">Links</a><br/>
          </b></font>
          <br/>
        <a href="../feed.xml"><img src="../images/rss16x16.png"/></a>
          <a href="../feed.xml">Subscribe to RSS feed</a>
          (What is <a href="http://en.wikipedia.org/wiki/RSS" target=_blank>RSS</a>?)
         </p>    </td>
    <td width="80%" valign="top" height="385"> <!-- #BeginEditable "content" -->

 
      <table width="661" border="0">
        <tr>
          <td align="center" valign="top" height="212">&nbsp;</td>
          <td valign="top" align="center"><p><br>
              <a href="http://www.ggro.org" target="_blank"><img src="../images/GGRO-Logo-new-2-color_shell.gif" width="150" height="152" border="0" class="logoBorder"></a></p>          </td>
        </tr>
        <tr> 
          <td align="center" valign="top" height="293" width="5%">            <p>&nbsp;</p></td>
          <td align="center" valign="top" bordercolor="#903F00" bgcolor="#F4EBC2"> 
            <center>
              <span class="orangeBold"><br/>DAILY HAWK COUNT 2009 </span>
              <p><span class="header">If the hawk count is not immediately posted, try the RSS/blog tab on our Facebook page: <a href="http://www.facebook.com/ggraptors" target="_blank">http://www.facebook.com/ggraptors</a><br>
              or call the Hawkwatch Hotline, 415-561-3030 ext 2500.</span>
                <br/><br/>
                <span class="textblue">NOTE: We expect this page to change with our website transitions.<br>
                <strong>Thank you for your patience!</strong></span>

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
              <p>
            </center>          </td>
        </tr>
      </table>
      <center>
        <hr>
        <div align="center" class="style1"></div>
        <p>&nbsp;</p>
      </center>
      <center>
        <table width="45%" border="0">
          <tr> 
            <td valign="bottom" width="49%" height="23"> 
              <h4><strong>Web page questions: </strong></h4>            </td>
            <td width="51%" valign="top" height="23"><strong><a href="mailto:ggro@parksconservancy.org">ggro@parksconservancy.org</a></strong></td>
          </tr>
        </table>
        <h4><b><a href="../broadwing.html" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('buteo','','../images/s-broadwing.jpg',1)"> 
          </a><a href="../ferruginous.html"><img name="ferrug" border="0" src="../images/s-laferrug2.jpg" width="99" height="40" alt="Ferruginous Hawk" align="absmiddle"></a></b></h4>
        <hr>
        <div align="left"><i><b>Website originally designed and created by <a href="http://www.buteo.com">Greg 
          Gothard</a> with lots of help from<a href="http://www.bantleydesign.com" target="_blank"> 
          Lynn Bantley</a> and <a href="http://www.barbarascamera.com" target="_blank">Barbara 
          Samuelson</a>. RSS support added by <a href="http://julianhyde.blogspot.com" target="_blank">Julian Hyde</a>
          and Brian O'Laughlin.</b></i></div>
      </center>
      <hr>
    <!-- #EndEditable --> 
      <p><b>Contact the GGRO: </b></p>
      <p><b>Mail:</b> <br>
        <b>Golden Gate Raptor Observatory<br>
        Building 201, Fort Mason<br>
        San Francisco, CA 94123</b></p>
      <p><b>Phone: <br>
        (415) 331-0730</b></p>
      <p><b>E-mail address:<br>
        <a href="mailto:ggro@parksconservancy.org"> ggro@parksconservancy.org</a></b>      <font size="2">
      <hr>
      <center>
        <a href="../index.html"><img src="../images/flyhomebtn.gif" border="0" width="74"
height="37" alt="Fly Home"></a><br>
        <a href="../index.html"><i><font face="Times New Roman, Times, serif">Fly 
        Home</font></i></a> 
      </center>
      </font></td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>
