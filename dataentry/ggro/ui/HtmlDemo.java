package ggro.ui;
/*
 * Copyright (c) 2002 Sun Microsystems, Inc. All  Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 * -Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 * 
 * -Redistribution in binary form must reproduct the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT
 * BE LIABLE FOR ANY DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT
 * OF OR RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THE SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE SOFTWARE, EVEN
 * IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that Software is not designed, licensed or intended for
 * use in the design, construction, operation or maintenance of any nuclear
 * facility.
 */

/*
 * @(#)HtmlDemo.java	1.7 02/06/13
 */


import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import javax.swing.border.*;
import javax.swing.colorchooser.*;
import javax.swing.filechooser.*;
import javax.accessibility.*;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import java.io.*;
import java.applet.*;
import java.net.*;

/**
 * Html Demo
 * @lifted from Sun example
 */
// public class HtmlDemo extends DemoModule {
// public class HtmlDemo  extends Container { 
public class HtmlDemo  extends JDialog { 

    JEditorPane html;
    JOptionPane optionPane = null;
    
    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
	// JDialog jd = new JDialog ((Frame)null, "Help");
	HtmlDemo demo = new HtmlDemo((Frame)null, false);
	// demo.mainImpl();
    }

    public static final String title = "Help Questions";
    
    /**
     * HtmlDemo Constructor
     */
    // public HtmlDemo(SwingSet2 swingset) {
    // public HtmlDemo(JDialog jd) { 
    public HtmlDemo(Frame parent, boolean adminMode) {  // JDialog jd) { 
	super (parent, title, false);
        // Set the title for this demo, and an icon used to represent this
        // demo inside the SwingSet2 app.
        // super(swingset, "HtmlDemo", "toolbar/JEditorPane.gif");

	System.out.println ("Hello from HtmlDemo constructor");
	
        try {
	    URL url = null;
	    // System.getProperty("user.dir") +
	    // System.getProperty("file.separator");
	    String path = null;
	    try {
	        // ### HACK ALERT: should use property here:
		if (adminMode) 
			path = "ggro/ui/HelpAdmin.html";
		else
			path = "ggro/ui/Help1.html";

		// path = "c:/ggro/ui/Help1.html";
		url = new URL ("file://c:/" + path);

		// url = getClass().getResource(path);
            } catch (Exception e) {
		e.printStackTrace();
		System.out.println("Failed to open " + path);
		url = null;
            }
	    
            if(url != null) {
                html = new JEditorPane(url);
                html.setEditable(false);
                html.addHyperlinkListener(createHyperLinkListener());

		System.out.println ("html is null: " + (html==null));
		
		JScrollPane scroller = new JScrollPane();
		JViewport vp = scroller.getViewport();
		vp.add(html);
		/// I think we could do this off a content pane
                this.getContentPane().add(scroller, BorderLayout.CENTER);
		this.setSize (580, 480);
		this.show();
            } else
		System.out.println ("Url was null");
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL: " + e);
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        } catch (Exception ee) {
		ee.printStackTrace();
	}
        optionPane = new JOptionPane();
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent we) {
                /*
                 * Instead of directly closing the window,
                 * we're going to change the JOptionPane's
                 * value property.
                 */

        	optionPane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
		setVisible (false);
            }
        });

	setVisible (true);
	}
    public HyperlinkListener createHyperLinkListener() {
	return new HyperlinkListener() {
	    public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
		    if (e instanceof HTMLFrameHyperlinkEvent) {
			((HTMLDocument)html.getDocument()).processHTMLFrameHyperlinkEvent(
			    (HTMLFrameHyperlinkEvent)e);
		    } else {
			try {
			    html.setPage(e.getURL());
			} catch (IOException ioe) {
			    System.out.println("IOE: " + ioe);
			}
		    }
		}
	    }
	};
    }
    
}
