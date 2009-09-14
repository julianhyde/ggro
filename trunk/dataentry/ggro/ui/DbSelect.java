package ggro.ui;
import javax.swing.*;
import java.beans.*; //Property change stuff
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParsePosition;
import ggro.util.Consts;

/// This should have the choice of some set of
public class DbSelect extends JDialog {



    static final String tmpDb = "Entry Database";
    static final String permDb = "Permanent Database";

    JRadioButton tempDb_rb = null;
    JRadioButton permDb_rb = null;

    boolean tmpActive = false;
    boolean permActive = false;


    private JOptionPane optionPane;

    GregorianCalendar calendar = new GregorianCalendar ();

    public DbSelect (final TableDemo aFrame, String current) { 
        super(aFrame, current, true);

        ////  magicWord = aWord.toUpperCase();
        setTitle("Administration: Select Database");

        final String msgString1 = "Please Choose a Database: ";


	if (Consts.getCurrentTable().equals (Consts.getTempTable()))
		tmpActive = true;
	if (Consts.getCurrentTable().equals (Consts.getPermTable()))
		permActive = true;

	tempDb_rb = new JRadioButton (tmpDb, tmpActive);	
	tempDb_rb.setMnemonic (KeyEvent.VK_T);
	permDb_rb = new JRadioButton (permDb, permActive);	
	permDb_rb.setMnemonic (KeyEvent.VK_P);

	ButtonGroup bg = new ButtonGroup();
	bg.add (tempDb_rb);
	bg.add (permDb_rb);

	RadioListener mylistener = new RadioListener ();
	permDb_rb.addActionListener (mylistener);
	tempDb_rb.addActionListener (mylistener);

	Object [] array = { tempDb_rb, permDb_rb }; 
	

        final String btnString1 = "Select";
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
                    optionPane.setValue(new Integer( JOptionPane.CLOSED_OPTION));
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
                    optionPane.setValue( JOptionPane.UNINITIALIZED_VALUE);

	            boolean reFetch = false;

                    if (value.equals(btnString1)) {

			// System.out.println ("selected " + btnString1);


			// figure which radio is selected, make that current
			if (tempDb_rb.isSelected()) { 
				// System.out.println ("temp is selected");
				Consts.setCurrentTable (Consts.getTempTable());
				if (permActive)
					reFetch = true;
			}
			if (permDb_rb.isSelected()) { 
				// System.out.println ("permanent is selected");
				Consts.setCurrentTable (Consts.getPermTable());
				if (tmpActive)
					reFetch = true;
			}

			aFrame.reFetchData();			
                        setVisible(false);
			

                    } else { // user closed dialog or clicked cancel
                        setVisible(false);
                    }
                }
            }
        });



    }

	class RadioListener implements ActionListener { 
		public void actionPerformed (ActionEvent e) { 
			/// guess it depends on the action
			// setSelected (true);		
			// System.out.println ("Call to action performed " + e);
			// System.out.println ("Call to action performed " + e.getActionCommand());
			
		}
	}
}
