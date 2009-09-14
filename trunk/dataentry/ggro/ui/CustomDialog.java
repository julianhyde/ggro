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

class CustomDialog extends JDialog {
    private String typedText = null;

    private String magicWord;
    private JOptionPane optionPane;

    public String getValidatedText() {
        return typedText;
    }


   boolean DEBUG = false;


   final static String beg = "Date entered ";
   final static String begend = " did not follow this format";


/// let's extend SimpleDateFormat to do our own clever parse; let's make sure about the year, 
/// etc
   
   public final static MyDateFormat sdf1 = new MyDateFormat ("MM-dd-yyyy");
   final static MyDateFormat sdf2 = new MyDateFormat ("MM/dd/yyyy");
   final static MyDateFormat sdf3 = new MyDateFormat ("MM.dd.yyyy");

	static { 

		sdf1.setLenient (false);
		sdf2.setLenient (false);
		sdf3.setLenient (false);


	}
   
   
   //// 20021109
   public Date isValidDate (String date) { 

	Date retDate = null;

	retDate = sdf1.parse (date, new ParsePosition(0));
	if (retDate == null)  { 
		// System.out.println ("first type returned null");
		retDate = sdf2.parse (date, new ParsePosition(0));
	}
	if (retDate == null) { 
		// System.out.println ("second type returned null");
		retDate = sdf3.parse (date, new ParsePosition(0));
	}

	return retDate;
		

   }

    GregorianCalendar calendar = new GregorianCalendar ();

    // public CustomDialog(Frame aFrame, String aWord, DialogDemo parent, TableDemo callMe) {
    public CustomDialog(final TableDemo aFrame, String current) { 
        super(aFrame, true);
        // final DialogDemo dd = parent;

        ////  magicWord = aWord.toUpperCase();
        setTitle("Set Date");

        final String msgString1 = "Enter desired date: (mm-dd-yyyy)";
        final JTextField textField = new JTextField(10);
	textField.setText (current);
        Object[] array = {msgString1, textField};

        final String btnString1 = "Enter";
        final String btnString2 = "Cancel";
        Object[] options = {btnString1, btnString2};

        optionPane = new JOptionPane(array, 
                                    JOptionPane.QUESTION_MESSAGE,
                                    JOptionPane.YES_NO_OPTION,
                                    null,
                                    options,
                                    options[0]);
        setContentPane(optionPane);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent we) {
                /*
                 * Instead of directly closing the window,
                 * we're going to change the JOptionPane's
                 * value property.
                 */
                    optionPane.setValue(new Integer(
                                        JOptionPane.CLOSED_OPTION));
            }
        });

        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                optionPane.setValue(btnString1);
            }
        });

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
                    optionPane.setValue(
                            JOptionPane.UNINITIALIZED_VALUE);

		///// here, we would check to make sure the date is a date
                    if (value.equals(btnString1)) {
                            typedText = textField.getText();
                            String ucText = typedText.toUpperCase();
			    // String msg = null;
			    Date date = null;
			    if ( (date = isValidDate (typedText)) != null) { 
				// aFrame.setDate (typedText);	
				aFrame.setDate (date, sdf1);	
                            	setVisible(false);
                            } else { 
                            // text was invalid
                            textField.selectAll();
                            JOptionPane.showMessageDialog(
                                            CustomDialog.this,
                                            "Sorry, \"" + typedText + "\" " + "does not appear to be a valid date.\n" + "Please use this format: \"mm-dd-yyyy\".\n\nValid months are from 08 to 12;\nValid years are 1986 to the present.", "huh?", JOptionPane.ERROR_MESSAGE);
                            typedText = null;
                        }
                    } else { // user closed dialog or clicked cancel
                        setVisible(false);
                    }
                }
            }
        });
    }
}
