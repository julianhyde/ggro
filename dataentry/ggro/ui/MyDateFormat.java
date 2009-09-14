package ggro.ui;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JTextField;
import java.beans.*; //Property change stuff
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParsePosition;

public class MyDateFormat extends SimpleDateFormat { 

   private int lowestYear = 1986;
   private int lowestMonth = 7; /// means 8 in real life


   public MyDateFormat (String pattern) { 
	super (pattern);
   }

   
   //// 2002-11-09
   public Date parse (String date, ParsePosition p) { 

	Date retDate = super.parse (date, p);

	if (retDate == null)
		return null;

	tmpcalendar.setTime (retDate);

	/// make sure the year isn't in the future (assumes clock is good)

	if (tmpcalendar.get(Calendar.YEAR) > nowcalendar.get (Calendar.YEAR)  || tmpcalendar.get(Calendar.YEAR) < lowestYear) { 
		System.out.println ("year is absurd");
		return null;
	}


	/// we only accept months 8-12; that's 7-11 in Java;
	//// the top is undefined, so we don't have to check for it.
	if (tmpcalendar.get (Calendar.MONTH) < lowestMonth )
		return null;

	return retDate;

   }

    GregorianCalendar nowcalendar = new GregorianCalendar ();
    GregorianCalendar tmpcalendar = new GregorianCalendar ();


	public static void main (String [] args) { 




	}
}
