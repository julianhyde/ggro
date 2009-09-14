package ggro.ui;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import java.beans.*; //Property change stuff
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParsePosition;

public class HelpDialog extends JDialog {

	private String typedText = null;
    private String magicWord;
    private JOptionPane optionPane;

    public String getValidatedText() {
        return typedText;
    }


   boolean DEBUG = false;


    final static String helpMsg = "Notes on using the data entry program.\n" + 
    "Enter hourly data for each hour and species as it appears on the HawkWatch Field Form." + 
    "\n" + 
    "If there are no entries for a species in a given hour, leave that field blank\n" + 
    "\n" + 
    "Once you are done with the front page of the Field Form," + 
    "\n" + 
    "turn it over; select the next relevant tab (Weather Data or Morph Data) " + 
    "and enter the data as it appears on the sheet\n" + 
    "\n" + 
    "You can navigate between rows and columns any way you like." + 
    "\n" + 
    "The enter key and arrow keys are programmed to move to the next allowable entries" + 
    "\n" + 
    "\n" + 
    "The program will try to prevent you from entering values in the wrong place." + 
    "\n" + 
    "If you enter a patently incorrect value, for example, '-2' for number of hawks," + 
    "\n" + 
    "An Alert will be raised." +
    "\n";


    final String btnString2 = "Close";

    JPanel mypanel = new JPanel();

    // public HelpDialog(Frame aFrame, String aWord, DialogDemo parent, TableDemo callMe) {
    public HelpDialog(final TableDemo aFrame) { 
        super(aFrame, false);
        // final DialogDemo dd = parent;

	

        setTitle("Help for GGRO Hawk Data Entry");


        // final JTextField te
        final JTextArea textArea = new JTextArea (helpMsg);
        Object[] array = {"Date Entry from the Hawkwatch Field Form", textArea };

        Object[] options = {btnString2};

        optionPane = new JOptionPane(array, 
                                    JOptionPane.QUESTION_MESSAGE,
                                    JOptionPane.OK_OPTION,
                                    null,
                                    options,
                                    options[0]);
        setContentPane(optionPane);

	getContentPane().add (mypanel);

	/* 
	final HtmlDemo content = new HtmlDemo (this);

	setContentPane (content);
	*/
	

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); /// strange
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent we) {
                /*
                 * Instead of directly closing the window,
                 * we're going to change the JOptionPane's
                 * value property.
                 *
		 */
                    // this.setValue(new Integer( JOptionPane.CLOSED_OPTION));
		    // this.setVisible (false);
		// super.hide();
		setVisible (false);
            }
        });


        // content.addPropertyChangeListener(new PropertyChangeListener() {
        optionPane.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                String prop = e.getPropertyName();

                if (isVisible() 
                 && (e.getSource() == optionPane)
                 && (prop.equals(JOptionPane.VALUE_PROPERTY) ||
                     prop.equals(JOptionPane.INPUT_VALUE_PROPERTY))) {
                    Object value = optionPane.getValue();

                    if (value == JOptionPane.UNINITIALIZED_VALUE) {
                        //ignore reset
                        return;
                    }

                    // Reset the JOptionPane's value.
                    // If you don't do this, then if the user
                    // presses the same button next time, no
                    // property change event will be fired.
                    optionPane.setValue( JOptionPane.UNINITIALIZED_VALUE);

		///// here, we would check to make sure the date is a date
                    if (value.equals(btnString2)) {

			   setVisible (false);
				
                    }
                }
            }
        });
    }
}
