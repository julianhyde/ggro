
package ggro.ui;

import javax.swing.*;
import java.awt.Component;


public class OurTabPane extends JTabbedPane { 


	TableDemo frame = null;

	public OurTabPane (TableDemo frame) { 
		super ();
		this.frame = frame;
	}


	public void setSelectedComponent (Component comp) { 

		super.setSelectedComponent (comp);

		System.out.println ("Call the set selected component YO");
	}

	public void setSelectedIndex (int index) { 

		
		frame.fireValidator(getSelectedIndex());
		if (!frame.somethingWrong())
			super.setSelectedIndex (index);
		else
			System.out.println ("cancelled Call the set selected index");

		/// okay, we'll want to have this guy fire back about
		/// changing the cell? and not go if things go badly?
	}





}
