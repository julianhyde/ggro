package ggro.ui;

import java.awt.event.*;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.ButtonGroup;
import javax.swing.JMenuBar;
import javax.swing.JTextArea;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.ImageIcon;
import javax.swing.BoxLayout;

import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.AbstractTableModel;
import javax.swing.JScrollPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.ActionMap;
import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.filechooser.FileFilter;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import javax.swing.Icon;
import javax.swing.border.*;
import javax.print.attribute.PrintRequestAttributeSet;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.text.*;
import java.text.SimpleDateFormat;
import ggro.sql.Util2;
import ggro.sql.MoveData;
import ggro.util.Consts;


/// known issues: saveData gets enabled a little too aggressively. It
/// should really only be enabled if something has been edited. But 
/// it's not. Perhaps that should really be handled in JTableY


public class TableDemo extends JFrame implements ActionListener, ItemListener {

	JTextArea output;
	JScrollPane scrollPane;
	String newline = "\n";
	private boolean DEBUG = false;

	String defaultFilename = null;
	Date defaultDate = null;
	GregorianCalendar greg = null;	
	// String currentDate = null;
	Date currentDate = null;

	public boolean modifiedFlag = false;

	public static final String top = "c:/ggro"; ///// shbe a PROPERTY!
	public static final String thisdir = "ui";

    	public static final String title = "GGRO Hawkwatch Data Entry";
	public static final String calcHph = "Calculate Hawks Per Hour";
	public static final String setDate = "Set Date...";
	public static final String setDatabase = "Set Database...";
	public static final String commitData = "Commit Data to Main...";
	public static final String printData = "Print..";
	public static final String summaryStats = "Summary Statistics..";
	public static final String aggregates = "Send to browser";
	public static final String exit = "Exit";
	/// need?
	public static final String loadData = "Load Saved Data";
	public static final String saveData= "Save Data";

	public static final String helpSheet = "FAQ";
	public static final String about = "About";


	public static final int hawkMode = 0;
	public static final int weatherMode = 1;
	public static final int morphMode = 2;
	public static final int commentMode = 3;

	public static final String hawkName = "hawksheet";
	public static final String weatherName = "weathersheet";
	public static final String morphName = "morphsheet";
	public static final String commentName = "commentsheet";

	public Vector hawkCols = new Vector();
	public Vector morphCols = new Vector();
	public Vector weatherCols = new Vector();


	public JTableY hawkTable = null;
	public JTableY weatherTable =  null;
	public JTableY morphTable =  null;
	public JTableY commentTable =  null;
	// JTextArea comments = null;


	MyTableModel hawkModel = null;
	MyTableModel weatherModel = null;
	MyTableModel morphModel = null;
	MyTableModel commentModel = null;

	JLabel dateLabel;
	JLabel hphLabel;
	// instance it

	// JTabbedPane jtab = null;
	OurTabPane jtab = null;

	public boolean adminMode = false;

        JMenuItem saveMenuItem;


	static HashMap julian_to_ggro_map = new HashMap();

	static { 
		
		julian_to_ggro_map.put("AMKEF","amke");
		julian_to_ggro_map.put("AMKEM","amke");
		julian_to_ggro_map.put("AMKEUND","amke");
		julian_to_ggro_map.put("BAEA","baea");
		julian_to_ggro_map.put("BWHAAD","bwha");
		julian_to_ggro_map.put("BWHAJUV","bwha");
		julian_to_ggro_map.put("BWHAUND","bwha");
		julian_to_ggro_map.put("COHAAD","coha");
		julian_to_ggro_map.put("COHAJUV","coha");
		julian_to_ggro_map.put("COHAUND","coha");
		julian_to_ggro_map.put("DATE_","date");
		julian_to_ggro_map.put("FEHAAD","feha");
		julian_to_ggro_map.put("FEHAJUV","feha");
		julian_to_ggro_map.put("FEHAUND","feha");
		julian_to_ggro_map.put("GOEAAD","goea");
		julian_to_ggro_map.put("GOEAJUV","goea");
		julian_to_ggro_map.put("GOEAUND","goea");
		julian_to_ggro_map.put("GOSH","gosh");
		julian_to_ggro_map.put("MINUTES","hours_counted");
		julian_to_ggro_map.put("MERL","merl");
		julian_to_ggro_map.put("NOHAADF","noha");
		julian_to_ggro_map.put("NOHAADM","noha");
		julian_to_ggro_map.put("NOHAJUV","noha");
		julian_to_ggro_map.put("NOHAUND","noha");
		julian_to_ggro_map.put("OSPR","ospr");
		julian_to_ggro_map.put("PEFAAD","pefa");
		julian_to_ggro_map.put("PEFAJUV","pefa");
		julian_to_ggro_map.put("PEFAUND","pefa");
		julian_to_ggro_map.put("PRFA","prfa");
		julian_to_ggro_map.put("RLHA","rlha");
		julian_to_ggro_map.put("RSHAAD","rsha");
		julian_to_ggro_map.put("RSHAJUV","rsha");
		julian_to_ggro_map.put("RSHAUND","rsha");
		julian_to_ggro_map.put("RTHAAD","rtha");
		julian_to_ggro_map.put("RTHAJUV","rtha");
		julian_to_ggro_map.put("RTHAUND","rtha");
		julian_to_ggro_map.put("SSHAAD","ssha");
		julian_to_ggro_map.put("SSHAJUV","ssha");
		julian_to_ggro_map.put("SSHAUND","ssha");
		julian_to_ggro_map.put("SWHA","swha");
		julian_to_ggro_map.put("TOTALHAWKS","total_sightings");
		julian_to_ggro_map.put("TUVU","tuvu");
		julian_to_ggro_map.put("ACCIPITER", "unid_accipiter");
		julian_to_ggro_map.put("BUTEO", "unid_buteo");
		julian_to_ggro_map.put("EAGLE", "unid_eagle");
		julian_to_ggro_map.put("FALCON", "unid_falcon");
		julian_to_ggro_map.put("RAPTOR", "unid_raptor");
		julian_to_ggro_map.put("WTKIAD","wtki");
		julian_to_ggro_map.put("WTKIJUV","wtki");
		julian_to_ggro_map.put("WTKIUND","wtki");



	}


	public TableDemo(boolean adminMode) {
       	 	super(title);

		this.adminMode = adminMode;


		greg = new GregorianCalendar();
		greg.set (Calendar.HOUR_OF_DAY, 0); /// might want to reset this
		greg.set (Calendar.MINUTE, 0);
		greg.set (Calendar.SECOND, 0);
		greg.set (Calendar.MILLISECOND, 0);

		currentDate = greg.getTime();

		// genDefaultFileName();


		// jtab = new JTabbedPane();
		jtab = new OurTabPane(this);


        	hawkModel = new MyTableModel(hawkMode);
        	weatherModel = new MyTableModel(weatherMode);
        	morphModel = new MyTableModel(morphMode);
        	commentModel = new MyTableModel(commentMode);
		hawkTable = new JTableY (hawkModel, hawkMode);
		weatherTable = new JTableY (weatherModel, weatherMode);
		morphTable = new JTableY (morphModel, morphMode);
		commentTable = new JTableY (commentModel, commentMode);


		int otherHeight = 13;
		int fakeCol = 2;

		/// 59? arrived at by random I'm sure
		/*  irrelevant now with color.
		for (int i=0;i<59;i++) { 
			if (!hawkModel.isCellEditable (i, fakeCol))	
				hawkTable.setRowHeight (i, otherHeight);
		}
		*/


		// hawkModel.addTableModelListener ( new MyTableModelListener());
		// weatherModel.addTableModelListener ( new MyTableModelListener());


        	hawkTable.setPreferredScrollableViewportSize(new Dimension(700, 600));
        	weatherTable.setPreferredScrollableViewportSize(new Dimension(700, 600));
        	morphTable.setPreferredScrollableViewportSize(new Dimension(700, 600));
        	commentTable.setPreferredScrollableViewportSize(new Dimension(700, 600));

		/// need one comment per hour.
		/// I guess we could call it a table.
		/// but it would be better to have it be 
		/// row-wise for the hour
		// comments = new JTextArea();

        	//Create the scroll pane and add the table to it. 
        	JScrollPane scrollPane = new JScrollPane(hawkTable);
        	JScrollPane scrollPane2 = new JScrollPane(weatherTable);
        	JScrollPane scrollPane3 = new JScrollPane(morphTable);
        	JScrollPane scrollPane4 = new JScrollPane(commentTable);

		jtab.setToolTipText ("Toggle Hawk/Weather data/Morph");

		jtab.addTab ("Hawk Data", scrollPane);
		jtab.addTab ("Weather Data", scrollPane2);
		jtab.addTab ("Dark Morph Hawk Data", scrollPane3);
		jtab.addTab ("Comments", scrollPane4);

       		//Add the scroll pane to this window.
        	// getContentPane().add(scrollPane, BorderLayout.CENTER);  // was!
        	getContentPane().add(jtab, BorderLayout.CENTER);

		hawkTable.setRowSelectionAllowed (false);
		weatherTable.setRowSelectionAllowed (false);
		morphTable.setRowSelectionAllowed (false);
		commentTable.setRowSelectionAllowed (false);

		TableColumn tb = hawkTable.getColumnModel ().getColumn(0);
		tb.setPreferredWidth (140);
		tb = hawkTable.getColumnModel ().getColumn(7);
		tb.setPreferredWidth (100);

		TableColumn tb2 = weatherTable.getColumnModel ().getColumn(0);
		tb2.setPreferredWidth (140);

		TableColumn tb3 = morphTable.getColumnModel ().getColumn(0);
		tb3.setPreferredWidth (140);

		TableColumn tb4 = commentTable.getColumnModel ().getColumn(0);
		tb4.setPreferredWidth (140);
		TableColumn tb4a = commentTable.getColumnModel ().getColumn(1);
		tb4a.setPreferredWidth (540);

		//// HACK, hard coded, should be after the model ??? what is this for? 
		/// we could use such things to mimic the look of the page. Doubt it
		hawkTable.setRowHeight (59, 30);

        	JMenuBar menuBar;
        	JMenu menu, submenu;
        	JMenuItem menuItem;
        	JRadioButtonMenuItem rbMenuItem;
        	JCheckBoxMenuItem cbMenuItem;

	        //Create the menu bar.
       		menuBar = new JMenuBar();
        	setJMenuBar(menuBar);

        	// FILE Menu
        	menu = new JMenu("File");
        	menu.setMnemonic(KeyEvent.VK_F);
        	// menu.getAccessibleContext().setAccessibleDescription(
                // "Here is something called accessible description");
        	menuBar.add(menu);


		menuItem = new JMenuItem (calcHph, KeyEvent.VK_H);
        	menuItem.addActionListener(this);
       		menu.add(menuItem);

        	saveMenuItem = new JMenuItem(saveData, KeyEvent.VK_S);
        	saveMenuItem.addActionListener(this);
       		menu.add(saveMenuItem);

		if (adminMode) { 
        		menuItem = new JMenuItem(setDate, KeyEvent.VK_D);
        		menuItem.getAccessibleContext().setAccessibleDescription("Set Date if different from today");
        		menuItem.addActionListener(this);
        		menu.add(menuItem);

        		menuItem = new JMenuItem(setDatabase, KeyEvent.VK_A);
        		menuItem.addActionListener(this);
        		menu.add(menuItem);

        		menuItem = new JMenuItem(commitData, KeyEvent.VK_C);
        		menuItem.addActionListener(this);
        		menu.add(menuItem);

			

		}
        	//menuItem = new JMenuItem(summaryStats, KeyEvent.VK_M);
        	//menuItem.addActionListener(this);
        	//menu.add(menuItem);


        	menuItem = new JMenuItem(aggregates, KeyEvent.VK_P);
        	menuItem.addActionListener(this);
        	menu.add(menuItem);

		/* 
        	menuItem = new JMenuItem(printData, KeyEvent.VK_P);
        	menuItem.addActionListener(this);
        	menu.add(menuItem);
		*/


		/// this would be a File Chooser
		/* 
        	menuItem = new JMenuItem(loadData, KeyEvent.VK_L);
        	menuItem.addActionListener(this);
        	menu.add(menuItem);
		*/

        	menuItem = new JMenuItem(exit, KeyEvent.VK_X);
        	menuItem.addActionListener(this);

        	menu.add(menuItem);


        	// EDIT MENU: NEED? 
        	menu = new JMenu("Edit");
        	menu.setMnemonic(KeyEvent.VK_E);
        	menu.getAccessibleContext().setAccessibleDescription( "This menu does nothing");
        	menuBar.add(menu);


		//// HELP Menu
        	menu = new JMenu("Help");
        	menu.setMnemonic(KeyEvent.VK_H);
        	menuBar.add(menu);

        	menuItem = new JMenuItem(helpSheet, KeyEvent.VK_H);
        	menuItem.addActionListener(this);
        	menu.add(menuItem);

        	menuItem = new JMenuItem(about, KeyEvent.VK_A);
        	menuItem.addActionListener(this);
        	menu.add(menuItem);

        	addWindowListener(new WindowAdapter() {
            		public void windowClosing(WindowEvent e) {
			/// prompt to save if necessary
				if (DEBUG) { 
					System.out.println ("Called from Adapter");
					System.out.println (e.paramString());
				}
				
				exit(0);
				setVisible (true);
				invalidate();	
				requestFocus();
				repaint();
				
            	}});


		// declare it
		// instance it
		JPanel eatthis = new JPanel ();
		dateLabel = new JLabel("  Entry Date: " + getDateString(currentDate) +"  ");
		hphLabel = new JLabel ("  HPH: --  " ) ;
		hphLabel.setBorder (new EtchedBorder());
		// to make it look kinda neat add
		dateLabel.setBorder(new EtchedBorder());
		// add it to the contentpane
		eatthis.add (dateLabel);
		eatthis.add (hphLabel);
		// getContentPane().add(dateLabel, BorderLayout.SOUTH);

		getContentPane().add(eatthis, BorderLayout.SOUTH);

		// sort of a hack; the general thrust is that we have no data entered
		// until after init (now)
		setModifiedFlag(false);
	}


	static boolean dateNotThere = false;


	public void reFetchData () { 

		dateNotThere = false;
		hawkModel.fetchSavedData();

		if (!dateNotThere) { 
			weatherModel.fetchSavedData();
			morphModel.fetchSavedData();
			commentModel.fetchSavedData();
		} else { 
			weatherModel.clearItOut();
			morphModel.clearItOut();
			commentModel.clearItOut();
		}

		calcHawksPerHour();

		repaint();

	}


	public boolean isAdminMode () { 
		return adminMode;
	}

	public Date getDate ()  { 
		return currentDate;

	}

	private String getDateString (Date date) { 

		StringBuffer sb = new StringBuffer ();
		sb = CustomDialog.sdf1.format ( date, sb, new FieldPosition (0));

		return sb.toString();
	}


	// we fire the validator from one tab to another 0 is hawk, 1 is weather, etc.
	public void fireValidator (int entity) { 

		if (entity == -1) 
			return;

		JTableY table = null;
		MyTableModel model = null;

		if (DEBUG) 
			System.out.println ("fire validator called with " + entity);

		if (entity == 0) { 
			table = hawkTable;
			model = hawkModel;
		} else if (entity == 1) { 
			table = weatherTable;
			model = weatherModel;
		} else if (entity == 2) { 
			table = morphTable;
			model = morphModel;
		} else
			return;
	
		/// this would be for all tabs? 	
		int row = table.getEditingRow();
		int col = table.getEditingColumn();
		somethingWasWrong = model.wasWrong();

	}

	public boolean somethingWasWrong = false;
	public boolean somethingWrong () { 
		return somethingWasWrong;
	}

	private int wrongBlankValues = 0;

	/// has missing data? 
	public void hasWrongBlankValues (MyTableModel w, MyTableModel h) { 

		/// need to cycle each hour, check totals. 
		int cols = w.getColumnCount();
		/// we start from 1, because 0th col has the label 

		int htotal=0;
		int mtotal=0;
		int ctotal=0;

		for (int i=1;i<cols;i++) { 
			htotal = h.getTotal (i);
			mtotal = w.getMinutes (i);
			ctotal = w.getCrewCount (i);

			if (htotal > 0 && (mtotal == 0 || ctotal == 0)) { 
				wrongBlankValues = i;
				return;
			}
		}
		wrongBlankValues = 0;
	}


	//// issues. should we do one row at a time? Send the totals with the h-data?
	//// how smart should submit data be? 
	public void saveData () { 

		if (DEBUG) System.out.println ("Save data");

		/// hack: change focus at save time to ensure that 
		/// date are saved

		//// THIS IS ALL ABOUT UNSAVED DATA:

		fireValidator(jtab.getSelectedIndex());
		if (somethingWrong()) { 
			return;
		}
	
		//// regular save stuff:



		hasWrongBlankValues(weatherModel, hawkModel);

		Object[][] wdata = weatherModel.getSaveableData();
		Vector wloc = weatherModel.getLocations();
		DataObj w = new DataObj(wdata, wloc);

		Object[][] hdata = hawkModel.getSaveableData();
		Vector hloc = hawkModel.getLocations();
		DataObj h = new DataObj(hdata, hloc);

		Object[][] mdata = morphModel.getSaveableData();
		Vector mloc = morphModel.getLocations();
		DataObj m = new DataObj(mdata, mloc);

		Object[][] cdata = commentModel.getSaveableData();
		Vector cloc = commentModel.getLocations();
		DataObj c = new DataObj(cdata, cloc);


		int wherew = wloc.indexOf ((Object)"MINUTES");
		
		int wheret = hloc.indexOf ((Object)"TOTALHAWKS");


		// System.out.println ("ZEROTH ITEM: " + hdata[0][0]);


		// String comment = comments.getText();


		/// we should know if we're doing an insert or an update.
		try { 
			SubmitData.doInsert (currentDate,  h, w, m, c);
			setModifiedFlag(false);	
                	JOptionPane.showMessageDialog(TableDemo.this, "   Data Saved!   ");
		} catch (Throwable t) { 

			// need more here
			/// let's log this to a file: 
			t.printStackTrace();
                	JOptionPane.showMessageDialog(TableDemo.this, "   Error Saving Data!   ");

			try { 

				FileOutputStream fos = new FileOutputStream ("c:/ggro/logs/save.err");
				
				PrintStream ps = new PrintStream (fos);	

				t.printStackTrace(ps);

				ps.flush();

				ps.close();

				fos.close();


			} catch (Throwable tt) { 

			}
		}

		// It's columns and rows. Total is the last column (and the last row)
		// I believe there's an entry for totals but not column totals
		// we need to transform this, with the other matrices, into 
		// the database entry

		/// skip column 1, that's the label for a row
		/// Walk each matrix like:  1 0, 1 1, 1 2, 1 3...; 2 0, 2 1, 2 2, 2 3..;3 0, 3 1
		
				
	}

    	public void genDefaultFileName () { 

		if (DEBUG) System.out.println ("here's the generated date: "+ defaultDate);
	
		// defaultDate = simpleDate.format (new Date());
		// currentDate = defaultDate;
		defaultFilename = defaultDate + ".csv";


	}


	public void setDate (Date date, DateFormat df) { 

		// whatever
		currentDate = date;
		StringBuffer sb = new StringBuffer ();
		sb = df.format ( date, sb, new FieldPosition (0));
		if (DEBUG) System.out.println ("Current date is: " + sb);

		dateLabel.setText("  Entry Date: " + getDateString(currentDate) + "  ");

		dateNotThere = false;
		reFetchData();
		///// NEED THE DATA ELSEWHERE?

	}
	/* 
	public void setDate (String date) { 
		currentDate = date;
		if (DEBUG) System.out.println ("Current date is: " + currentDate);
		dateLabel.setText(currentDate);
		dateLabel.updateUI();
    	}
	*/


    	JDialog dateD = null;

    	public void raiseHelpDialog () { 
    		HtmlDemo cd = new HtmlDemo (this, isAdminMode());
		// cd.setSize (580, 480);
		// cd.show();
    	}


	public static final String aboutMsg = "GGRO Hawkwatch Data Entry\nVersion 1.0\n\nSuggestions, Defects to:\nbpso@pacbell.net";

    	public void raiseAboutDialog () { 
		JOptionPane.showMessageDialog (this, (Object)aboutMsg, "About", JOptionPane.INFORMATION_MESSAGE);
    	}


    	public void raiseDateDialog () { 
    		CustomDialog cd = new CustomDialog(this, getDateString(currentDate));
		cd.setSize (280, 180);
		cd.show();
    	}

    	public void raiseCommitQuestionDialog () { 

		///
		/// simply raise an option pane question .... should we 
		/// go ahead with the /JOption
		int outcome = JOptionPane.showConfirmDialog (this, (Object)"Commit Data to Permanent Database?", "Commit to Database", JOptionPane.YES_NO_OPTION);

			if (outcome == JOptionPane.YES_OPTION) { 
				try { 
					MoveData.commitToPermanent();
                			JOptionPane.showMessageDialog(TableDemo.this, "Data Moved to Permament");
					reFetchData();
				} catch (Exception e) { 
					e.printStackTrace();
                			JOptionPane.showMessageDialog(TableDemo.this, "Error Saving Data! This is probably a bug.");

				}
			} 
	}

    	public void raiseStatsDialog () { 

		//SummaryStatsDialog db = new SummaryStatsDialog(this);
		//db.setSize (400,400);
		//db.show();


	}

	/** WIP WIP **/
    	public void raiseDatabaseDialog () { 
    		DbSelect db = new DbSelect(this, "hello");
		db.setSize (280, 180);
		db.show();
    	}


	public void setUpEditor (JTable table) { 

        	final WholeNumberField integerField = new WholeNumberField(0, 5);
        	integerField.setHorizontalAlignment(WholeNumberField.RIGHT);

        	DefaultCellEditor integerEditor =
            	new DefaultCellEditor(integerField) {
                //Override DefaultCellEditor's getCellEditorValue method
                //to return an Integer, not a String:
                   public Object getCellEditorValue() {
                    return new Integer(integerField.getValue());
                   }
                };
        	table.setDefaultEditor(Integer.class, integerEditor);
	}


	public void setModifiedFlag (boolean b) { 
		modifiedFlag = b;

		if (saveMenuItem!=null)
			saveMenuItem.setEnabled (b);

	}


	public void exit (int whoCares) { 
		if (DEBUG) System.out.println ("hello, exit called modified? " + modifiedFlag);


		if (modifiedFlag) { 
			int outcome = JOptionPane.showConfirmDialog (this, (Object)"Save the data you entered?", 
			"Save Data?", JOptionPane.YES_NO_CANCEL_OPTION);

			if (outcome == JOptionPane.YES_OPTION) { 
				saveData();
			} else if (outcome == JOptionPane.CANCEL_OPTION) { 
				if (DEBUG) System.out.println ("outcome was cancel");
				return;
			}  
		}


		if (wrongBlankValues > 0) { 
			String msg = "Data Error: The " + hawkModel.columnNames[wrongBlankValues] + " column has more than zero total hawks, but the Weather Data shows\nzero minutes or crew for that hour.\n-Make sure that you've entered the weather data;\n-Make sure you didn't enter a hawk in a column you didn't intend.\nSee help for more details. \n\nClick 'Cancel' to continue data entry.\nClick 'Yes' to exit anyway.";
			int outcome = JOptionPane.showConfirmDialog ((JFrame)this, (Object)msg, "Data Consistency Warning", JOptionPane.YES_NO_CANCEL_OPTION);
			if (outcome != JOptionPane.YES_OPTION) { 
				return;
			}

		}

		try { 
			
			Consts.getConnection().close();

		} catch (Exception e) { }

		System.exit (whoCares);

	}

	public void actionPerformed(ActionEvent e) {

		if (DEBUG) System.out.println ("actionPerformed noticed");
       		JMenuItem source = (JMenuItem)(e.getSource());
        	String s = "Actionperformed: ction event detected." 
                   + newline
                   + "    Event source: " + source.getText()
                   + " (an instance of " + getClassName(source) + ")";

		if (DEBUG) System.out.println ("action: text: " + source.getText());

		if (source.getText().equals (exit)) { 
			// check for unsaved data?	
			exit(0);
		} else if (source.getText().equals (helpSheet)) { 
			if (DEBUG) System.out.println ("help called");
			raiseHelpDialog ();
		} else if (source.getText().equals (commitData)) { 
			if (DEBUG) System.out.println ("commitData called");
			raiseCommitQuestionDialog ();
		} else if (source.getText().equals (setDatabase)) { 
			if (DEBUG) System.out.println ("setDatabase called");
			raiseDatabaseDialog ();
		} else if (source.getText().equals (summaryStats)) { 
			if (DEBUG) System.out.println ("summaryStats called");
			raiseStatsDialog ();
		} else if (source.getText().equals (calcHph)) { 
			if (DEBUG) System.out.println ("setDate called");
			calcHawksPerHour ();
		} else if (source.getText().equals (setDate)) { 
			if (DEBUG) System.out.println ("setDate called");
			raiseDateDialog ();
		} else if (source.getText().equals (about )) { 
			if (DEBUG) System.out.println ("about called");
			raiseAboutDialog ();
		} else if (source.getText().equals (loadData)) { 
			if (DEBUG) System.out.println ("load data called");
			fireFileChooser ("load");
		} else if (source.getText().equals (saveData)) { 
			if (DEBUG) System.out.println ("save data called");
			saveData(); // I guess
			// fireFileChooser ("save");
		} else if (source.getText().equals (printData)) { 
			if (DEBUG) System.out.println ("print data called");
		} else if (source.getText().equals (aggregates)) { 
			if (DEBUG) System.out.println ("aggregates called");
			doTotalsToPublish(); // I guess
		} else if (source.getText().equals ("toggle")) { 
			if (DEBUG) System.out.println ("toggle called");
		}

	}

	public static String lastDir = null;


	/// not sure for what anymore?
	public void fireFileChooser (String which) { 

		if (lastDir == null)
			lastDir = System.getProperty ("user.dir");
	
		JFileChooser jf = new JFileChooser (lastDir);	
	
		jf.addChoosableFileFilter ( new MyFileFilter  (".csv"));

		int ret = -1;
		if (which.equals ("load")) { 
			ret = jf.showOpenDialog(this);

			if (ret == JFileChooser.APPROVE_OPTION) { 
				if (DEBUG) System.out.println ("open file: " + jf.getSelectedFile());
				openFile (jf.getSelectedFile());
			}

		}
	
		if (which.equals ("save")) {
			ret = jf.showSaveDialog(this);
			if (ret == JFileChooser.APPROVE_OPTION) { 
				if (DEBUG) System.out.println ("save file: " + jf.getSelectedFile());
				saveFile (jf.getSelectedFile());
			}
		}
	

		if (DEBUG) System.out.println ("return was: " + ret);	
		//// SAVE THE FILE  on okay
		//// add a listener

	}


	DecimalFormat hphformat = new DecimalFormat ("###.##");
	public void calcHawksPerHour () { 



		/// reset the hphLabel
		
		int total = hawkModel.getTheTotal();	
		int minutes = weatherModel.getTotalMinutes();

		if (minutes==0) { 
			hphLabel.setText ("  HPH:--  " );
			return;
		}

		System.out.println ("total, minutes: " + total + " " + minutes);

		double val = (double)total/ ((double)minutes/60d);

		System.out.println ("the hph will be: " + val);

		if (hphLabel == null)
			System.out.println ("the hph label is null");

		StringBuffer sb = new StringBuffer ();
		sb = hphformat.format (val, sb, new FieldPosition(0));

		hphLabel.setText ("  HPH: " + sb.toString() +"  ");


		repaint();
		



	}


	public String getModeName (int mode) { 
		switch (mode) { 
			case hawkMode:
				return hawkName;
			case weatherMode: 
				return weatherName;
			case morphMode:
				return morphName;
			case commentMode:
				return commentName;
		}

		return null;




	}


	/* 
	class DoPrint extends Thread { 

		public DoPrint() {
			this.setDaemon(false);
		}

	
		public void run() { 	

			StringBuffer sb = new StringBuffer();
			sb = new SimpleDateFormat ("MM-dd-yyyy").format (new Date(), sb, new FieldPosition(0));
	
			MessageFormat header = new MessageFormat (sb.toString());
			sb.append (" dark morphs");
			MessageFormat dm_header = new MessageFormat (sb.toString());
			MessageFormat footer = new MessageFormat ("");
	
			try { 
			hawkTable.print(JTable.PrintMode.FIT_WIDTH, header, footer);
			} catch (Exception e) { 
				e.printStackTrace();
			}
			try { 
			weatherTable.print(JTable.PrintMode.FIT_WIDTH, header, footer, false, (PrintRequestAttributeSet)null, true);
			} catch (Exception e) { 
				e.printStackTrace();
			}
			try { 
			morphTable.print(JTable.PrintMode.FIT_WIDTH, dm_header, footer, false, (PrintRequestAttributeSet)null, true);
			} catch (Exception e) { 
				e.printStackTrace();
			}
			try { 
			commentTable.print(JTable.PrintMode.FIT_WIDTH, header, footer, false, (PrintRequestAttributeSet)null, true);
			} catch (Exception e) { 
				e.printStackTrace();
			}
		}
	}

	public void printData () {

		DoPrint dp = new DoPrint();	
	
		dp.start();	
			

	}
	*/


	// remember: you have to open from both pages
	public void openFile (File filename) { 


	}

	// remember: you have to save from both pages
	public void saveFile (File filename) { 

	}

	class MyFileFilter extends FileFilter { 
		String end = null;
		public MyFileFilter (String ed) { 
			end = ed;	
		}

		public boolean accept (File f) { 
			if (f.getName().endsWith (end))
				return true;
			else
				return false;
	
		}
	
		public String getDescription () {
			return "Comma-separated Files";
		}

	}


	public void itemStateChanged(ItemEvent e) {
		if (DEBUG) System.out.println ("itemStateChanged");
	        JMenuItem source = (JMenuItem)(e.getSource());
	        String s = "Item event detected."
	                   + newline
	                   + "    Event source: " + source.getText()
	                   + " (an instance of " + getClassName(source) + ")"
	                   + newline
	                   + "    New state: " 
	                   + ((e.getStateChange() == ItemEvent.SELECTED) ?
	                     "selected":"unselected");
		if (DEBUG) System.out.println (s);
	}

	// Returns just the class name -- no package info.
	protected String getClassName(Object o) {
       		String classString = o.getClass().getName();
        	int dotIndex = classString.lastIndexOf(".");
        	return classString.substring(dotIndex+1);
    	}


	class MyTableModelListener implements TableModelListener { 

		public void tableChanged (TableModelEvent te) { 
			if (DEBUG) {
			System.out.println ("LISTENER: " + te.getFirstRow() + " " + 
				te.getLastRow() + " " + 	
				te.getColumn() + " " + 	
				te.getType());
			}

		}
	}


		HashMap julian_names_count = new HashMap();

		String [] julian_names = { "date", "author", "total_sightings", "hours_counted", "hph", "total_species", "tuvu", "ospr", "wtki", "baea", "noha", "ssha", "coha", "gosh", "rsha", "bwha", "swha", "rtha", "feha", "rlha", "goea", "amke", "merl", "pefa", "prfa", "unid_accipiter", "unid_buteo", "unid_eagle", "unid_falcon", "unid_raptor" };

		/////UPDATE 09/2009 to send to web
		/// we need  mapper between Julian's names and our names
		public void doTotalsToPublish()  {

			// where we'll put it
			HashMap totals_spec = new HashMap();

			/// the table that we'll send: 
			for (int i=0;i<julian_names.length;i++) { 
				julian_names_count.put (julian_names[i], null);
			}

			Object [][] h_data = hawkModel.getSaveableData();
			Object [][] w_data = weatherModel.getSaveableData();


			// System.out.println ("h_data00: " + h_data[0][0]);

			
			// what to use: we'll have to aggregate later.
			Vector col_names = null;
			try { 
				col_names = (Vector)(Util2.getColumn(Util2.dbColName)).clone();
			} catch (Throwable t) { 
				t.printStackTrace();
			}


		
			/// hack conformance
			col_names.remove ("GYRF");

			/// placeholders:
			String col = null;
			Integer tmp = null;

			int end = (hawkModel.getColumnCount()-1);
			int rowTotal = 0;
			Integer x = null;



			String j_tmp = null;


			/// everything is guided from allcols.csv. 
			/// the hawksheet stuff begins at hOffset
			int hOffset = 11;
			int hSheeti = 0;
			int wSheeti = 0;
	
			/// more magic numbers. Hawk stuff ends here:
			int hawkEnd = 48;


			for (int i=0;i<hawkEnd;i++) { 

				col = (String)col_names.get((i+hOffset));	
				// System.out.println ("col: " + col);
				rowTotal = 0;
				// System.out.println ("I is now: " + i);

				for (int j=0;j<(end-1);j++) { 
					try { 
					// System.out.println ("at i, j: " + i + " " + j);
					x = (Integer)h_data[i][j];
					// System.out.println ("at i, j: val:  "  + x);
					 } catch (ClassCastException cce) { 
						// can't total this row
						break;
					}
					if (x==null)
						continue;
					rowTotal+= x.intValue();
				}
				// System.out.println ("total: " + rowTotal);

				/// more interesting, put in aggregate species table
				j_tmp = (String) julian_to_ggro_map.get(col);		
			
				tmp = (Integer)julian_names_count.get (j_tmp);

				if (tmp == null) {
					tmp = new Integer (rowTotal);
				} else { 
					tmp = new Integer ( tmp.intValue() + rowTotal );
				}
				// System.out.println ("putting: " + j_tmp + " " + tmp);
				julian_names_count.put (j_tmp, tmp);
					
			}	

			rowTotal = 0;

			/// weather data, we only care about the minutes:
			for (int j=0;j<(end-1);j++) { 

				try { 
				// System.out.println ("at i, j: " + i + " " + j);
				/// magic number: it's in row 7
				x = (Integer)w_data[7][j];
				// System.out.println ("at i, j: val:  "  + x);
				 } catch (ClassCastException cce) { 
					// can't total this row
					break;
				}
				if (x==null)
					continue;
				rowTotal+= x.intValue();
	
			}

			if (rowTotal == 0) { 
				System.err.println ("No minutes entered, can't send");
                		JOptionPane.showMessageDialog(TableDemo.this, " No Minutes entered! Can't Send to Browser   ");
				return;
			}
			julian_names_count.put ("hours_counted", new Integer((rowTotal/60)));

			x = (Integer)julian_names_count.get ("total_sightings");


			/*
			double d = (double)(x.intValue()/(rowTotal/60));
			DecimalFormat df = new DecimalFormat("##.#");
			StringBuffer d_formatted = new StringBuffer();
			d_formatted = df.format (d, d_formatted, new FieldPosition(0));
			*/
			//if (rowTotal > 0)
			// julian_names_count.put ("hph", new Integer((x.intValue()/(rowTotal/60))));
			
			/// post process: total and merge into the julian names

			// System.out.println ("julian totals: " + julian_names_count);



			/// call: http://www.hydromatic.net/ggro/form.jsp?

			// which browser? (Handle errors?)
			String browser = getBrowser();

			//// get the url?
			String url = getBrowserURL(julian_names_count);
			
			// System.out.println ("Exec time: " + browser + " " + url);
			String [] cmdArr = { browser, url };

			// exec a browser with that url
			try { 
				Runtime.getRuntime().exec (cmdArr);

			} catch (Exception e) { 
				e.printStackTrace();
			}
			


		}

		static String urlStart = "http://www.hydromatic.net/ggro/form.jsp?";

		static final String amper = "&";
		static final String equal = "=";

		/// build the url to launch 
		public String getBrowserURL(HashMap hm) {

			Iterator it = hm.entrySet().iterator();

			Map.Entry me = null;

			StringBuffer sb = new StringBuffer();

			Integer val = null;
			String key = null;

			String tmp0 = null;

			int i = 0;

			int species_i = 0;

			while (it.hasNext()) {
				me = (Map.Entry)it.next();
			
				if (me.getValue()==null)
					continue;

				/// assemble arg
				key = (String) me.getKey();
				val = (Integer) me.getValue();


				if (val.intValue()==0)
					continue;


				if (!key.startsWith ("unid_") && !key.equals ("total_sightings") && !key.equals ("hours_counted") && !key.equals ("hph"))
					species_i++;
			
	
	
				if (i>0)	
					sb.append(amper);	

				sb.append(key);	
				sb.append(equal);	
				sb.append(val.toString());	

				i++;

			}

			/// excess stuff: HPH, species
			if (species_i > 0) { 
				sb.append(amper);	
				sb.append ("total_species");
				sb.append(equal);	
				sb.append(""+species_i);	
			}


			return urlStart + sb.toString();

		}

		public String getBrowser() {


			try { 

			// not supported in 1.4.1, Hard coding it for now
			// String where = System.getenv("PROGRAMFILES");
			String where = "c:/Program Files";
			String ie = where + File.separator + "Internet Explorer/iexplore.exe";
			String ff =  where + File.separator + "Mozilla Firefox/firefox.exe";

			if (new File(ff).exists()) { 
				return ff;
			} else if (new File(ie).exists()) {
				return ie;
			} 

	
			} catch (Exception e) { 
				e.printStackTrace();
			
			}

			return (String)null;
		}

	public final static String [] winds = { "N", "E", "W", "S", "NE", "NW", "SE", "SW", "VAR", "ENE", "ESE", "NNE", "NNW", "SSE", "SSW", "WNW", "WSW", "N/A" };

	public static final int skyCodeHigh = 5;
	public static final int skyCodeLow = 0;
	public static final int hourMin = 0;
	public static final int hourMax = 60;

	class MyTableModel extends AbstractTableModel {

		int mode = 0;
		final String aString = "hi";
		final Integer anInt = new Integer (0);
	
		public MyTableModel (int mode) { 
			this.mode = mode;
			if (DEBUG) System.out.println ("hello from TableModel constructor " + mode);
			getCol1 ();
			fetchSavedData();
		}
	
	
		String backupName = null;
	
		public void setBackupName (String name) { 
			backupName = name;
		}
	
	
		public String getBackupName () { 
			return backupName;
		}


		final String[] h_query = { 
			"dummy",
			"900",
			"1000",
			"1100",
			"1200",
			"1300",
			"1400",
			"1500"
		};

	        public final String[] columnNames = {
					"Description",
					"9-10", 
					"10-11", 
					"11-12", 
					"12-1", 
					"1-2",
					"2-3",
					"3-4",
					"Total"
					};
	        final String[] w_columnNames = {
					"Description",
					"9-10", 
					"10-11", 
					"11-12", 
					"12-1", 
					"1-2",
					"2-3",
					"3-4"
					};

		final String[] c_columnNames = { 
			"Hour", "Comments"
		};

		final String [] c_rowNames = { 
					"9-10", 
					"10-11", 
					"11-12", 
					"12-1", 
					"1-2",
					"2-3",
					"3-4"
		};
	
		/// populate column 1 from a file. The rest is blank
	
		int [] noedit = null;


		public int[] getNoEdits() { 
			return noedit;
		}
	
		public boolean isNonRow (int row) { 

			if (row<0)
				return true;
			if (row>getRowCount())
				return true;

			if (mode == weatherMode || mode == commentMode)
				return false;
	
			for (int i=0;i<noedit.length;i++) { 
				if (noedit[i] == row)
					return true;
	
			}
	
			return false;
	
		}


		private Vector locations = new Vector();

		Vector noeds = null;

		/// could be much more
		public void getCol1() { 
			if (DEBUG) System.out.println ("hello from getCol1");

			Vector v = new Vector ();
			// Vector noeds = new Vector ();
			String name = hawkName;
			if (mode == weatherMode)
				name = weatherName;
			if ( mode == morphMode )
				name = morphName;
			if ( mode == commentMode)
				name = commentName;

			// name = top + File.separator + thisdir + File.separator + name;

			try { 
				/// this is fillsheet in the sense of initial look.
				noeds = Util2.fillSheet (v, name, locations);
			} catch (Throwable e) { e.printStackTrace(); System.exit(1); }
	
			if (mode==hawkMode)
				v.add ("Total");

			/// This whole comment thing is a bit of a hack
			if (mode==commentMode) { 
				/// rather the hack:	
				if ( ((String)v.elementAt(0)).equals ("Comment"))
					v.remove (0);
		
				// insert the comment areas:
				for (int i=0;i<c_rowNames.length;i++) { 
					v.add (c_rowNames[i]);
				}

			}
	
			String [] names = new String [v.size()];
	
			v.copyInto ((String[]) names);
	
			/// tell how long to make it:
			if (mode==hawkMode) { 
				data = new Object [v.size()] [columnNames.length];
			} else if (mode == morphMode || mode == weatherMode) { 
				data = new Object [v.size()] [w_columnNames.length];
			}  else if (mode == commentMode) {
				/// a little more hard coded than the other guys
				data = new Object [v.size()] [2];
			}

	
			if (mode == commentMode) {
				//// TWEAK HERE
			 	for (int i=0;i<names.length;i++) { 
					setValueAt (names[i], i, 0);
					/// THIS IS WHERE WE WILL ENTER CURRENT DATA
				}
			} else { 
			 for (int i=0;i<names.length;i++) { 
				setValueAt (names[i], i, 0);
				/// THIS IS WHERE WE WILL ENTER CURRENT DATA
	
			 }
			}

			if (DEBUG) System.out.println ("Noeds v size: " + 	noeds.size());

			noedit = new int [noeds.size()];
			for (int i=0;i<noeds.size();i++) { 
				noedit[i] = ((Integer)noeds.get (i)).intValue();	
			}	
	
		}
	
		String [] birds = null;
	
		Object [][] data = null;


		public Object [][] getData () { 
			return data;
		}

		public Vector getLocations () { 
			return locations;
		}

		public Object [][] getSaveableData () { 

			/// skip non-editable rows....a vector?

			Object [][] datain = getData();
			/// fudging with length to pass totals along
			int rowcount = getRowCount() - (noedit.length);
			
			// System.out.println ("getSaveable: mode, rowcount, noedit length: " + mode +" " + getRowCount() + " " + noedit.length);
			// System.out.println ("getSaveable: mode rowcount, colcount: " + mode + " " + rowcount + " " + (getColumnCount()-1));
			Object [][] dataout = new Object[rowcount][getColumnCount()];
			
			// System.out.println (i0 + " " +i +" "+ wdata[i][i0]);
			int offsetRow = 0;
			int offsetCol = 1;
			int current = 0;
			for (int i0=1;i0<getColumnCount();i0++) { 
				// reset offset for new
				offsetRow = 0;
				for (int i=0;i<datain.length;i++) { 
					current = i - offsetRow;

					if (i!= (datain.length-1) && isNonRow (i)) { 
						offsetRow++;
						continue;
					} else { 
						 // System.out.println ("getSaveable row(adj), col, value : " + i + "(" + current + ") " + i0 +" " + datain[i][0] + " " +datain[i][i0]);
						dataout[current][i0-offsetCol] = datain[i][i0];
					}
					
				}
			}

			return dataout;
		}


		public void clearItOut () { 

			for (int i0=1;i0<getColumnCount();i0++) { 
				// reset offset for new
				for (int i=0;i<getRowCount();i++) { 
					data[i][i0]=null;
				}
			}
			repaint();



		}
	
		public void fetchSavedData () { 

			/// skip non-editable rows....a vector?
			Object [][] datathere = getData();
			/// fudging with length to pass totals along
			int rowcount = getRowCount() - (noedit.length);
			
			// System.out.println ("getSaveable: mode, rowcount, noedit length: " + mode +" " + getRowCount() + " " + noedit.length);
			// System.out.println ("getSaveable: mode rowcount, colcount: " + mode + " " + rowcount + " " + (getColumnCount()-1));

			if (DEBUG) System.out.println ("These are the noeds: " + noeds);
			// System.out.println (i0 + " " +i +" "+ wdata[i][i0]);
			int offsetRow = 0;
			int offsetCol = 1;
			int current = 0;

			boolean foundanyyet = false;
			
			// for each column, do the relevant query, 
			/// select <sheet columns> from data where data, time
			/// then populate the rows in our data here, 
			/// using the noeds call to figure out if we need to skip.
			//  adjust for modes...


			boolean foundAnyData = false;
			Vector ret = null;

			try { 

			/// notice 1 based here:
			
			int max = getColumnCount ();

			/* 
			if (!Util2.hasDay (getDate())) { 
				dateNotThere = true;
				clearItOut();
				return;
			}
			*/


			// oh, hacking for comment mode. Its data is transposed (compared to others)
			if (mode == commentMode)
				max = getRowCount() + 1;
			if (mode == hawkMode)
				max = max-1;


			if (DEBUG) { 
				System.out.println ("Fetch: max is " + max);
				System.out.println ("Fetch: mode is comment? " + (commentMode == mode));
			}


			for (int i0=1;i0<max;i0++) { 
				// reset offset for new
				

				//// DO Util2 fetch here
				/// okay, it fetches with sheet and column(?) don't know how col gets in
				/// we'll 
		
				try	 { 
					ret = Util2.fetch (getModeName(mode), getDate(), h_query[i0]);
					foundanyyet = true;

				} catch (java.sql.SQLException sqe) { 
					/// pop a dialog
					/// clear the data out, repaint.
					if (!foundanyyet)
						clearItOut ();	
					continue;

				}

				offsetRow = 0;
				/// okay, everything we get, we put, it's mostly just a 
				/// question of WHERE
				for (int i=0;i<noedit.length;i++) {    ///// fading in
					ret.insertElementAt ("", noedit[i]);
				}
				for (int i=0;i<ret.size();i++) {    ///// fading in

					foundAnyData = true;

					/// SHEET DEPENDENT:
					try { 
						if (mode!=commentMode)
							datathere[i][i0] = new Integer((String)ret.get (i));
						else
							datathere[i0-1][1] = ((String)ret.get (i));
					} catch (NumberFormatException nfe) { 

						if (mode!=commentMode)
							datathere[i][i0] = ((String)ret.get (i));
						else
							datathere[i0-1][1] = ((String)ret.get (i));
						
					} 
					
				}
				if (!foundAnyData) { 
					clearItOut();
					return;
				}
			}

			} catch (Throwable e) { 
				e.printStackTrace();
			}

			/// as good as place as any to do this.
			if (foundanyyet)
				doTotals();
		}

	
	        public int getColumnCount() {
		    if (mode==hawkMode)  {
	           	return columnNames.length;
		    } else if  ( mode == weatherMode || mode == morphMode) { 
	            	return w_columnNames.length;
		    } else { 
	            	return c_columnNames.length;
		    }
	        }
	
	
	        public int getRowCount() {
			
	            return data.length;
	        }
	
	        public String getColumnName(int col) {
			if (mode!=commentMode)
	            		return columnNames[col];
			else
				return c_columnNames[col];
	        }
	
	        public Object getValueAt(int row, int col) {
			// if (DEBUG) System.out.println ("request data at row, col: " + row +" " + col);
	            return data[row][col];
	        }
	
	        /*
	         * JTable uses this method to determine the default renderer/
	         * editor for each cell.  If we didn't implement this method,
	         * then the last column would contain text ("true"/"false"),
	         * rather than a check box.
	         */
		
	        public final Class getColumnClass(int c) {

			// 
			/// 

		    if (c==0 || mode == commentMode)
			return aString.getClass();
		    else 
			return anInt.getClass();
	
	        }
	
	        /*
	         * Question: do we want the totals to be editable?
	         */
	        public boolean isCellEditable(int row, int col) {
	            // Note that the data/cell address is constant,
	            // no matter where the cell appears onscreen.
		    // 
		    // first col is always display
	            // if (col < 1 || (mode!=weatherMode && col == getColumnCount())) { 
	            if (col < 1 || (mode==hawkMode && col == getColumnCount())) { 
	                return false;
	            } else {
			if (isNonRow (row))
				return false; 
			else
	                	return true;
	            }
	        }




		/// decidedly the raw method of handling this problem
		/// seems like we should have handlers, or listeners, or at least a lookup
		public void validate (Object value, int row, int col) throws NumberFormatException, ClassCastException { 

			terriblyWrong = false;


			if (col==0) { 
				data[row][col] = value;
	                    	fireTableCellUpdated(row, col);
				// somehow, I would have guessed this would be false:
				setModifiedFlag(true);
				return;
			}


			if (mode==hawkMode || mode == morphMode) { 

				// this could throw if it's not a number

				//// added to deal with people deleting values they entered.
				//// seem
				if ( value == null || ((String)value).equals("")) { 
					data[row][col]=null;
					if (DEBUG) System.out.println ("OK, looks like the value is null");
					return;
				}
				
			    	Integer x = new Integer ((String) value);
			    	if (x == null)
					x = new Integer (0);
				

				// check value:
				if (x.intValue() < 0)
					throw new NumberFormatException ("Positive number required");

				
	
				data[row][col] = x;
				setModifiedFlag(true);
				return;

			/// most of the action/variety is on the weather sheet
			} else if (mode==weatherMode) { 

			    	Integer x = null;

				if ( value == null || ((String)value).equals("")) { 
					data[row][col]=null;
					if (DEBUG) System.out.println ("OK, looks like the value is null");
					return;
				}


				/// presumptive decimal rows: 
				if (row==0 || row == 1 || row == 2) { 

					try { 
			    			x = new Integer((String) value);
			    			if (x == null)
							x = new Integer (0);


						ensurePositive(x);
						//// other checks here (WIP)
						
						data[row][col] = x;
						setModifiedFlag(true);
						return;
	
					} catch (NumberFormatException e) { 
						Double d = new Double ((String)value);
						ensurePositive(d);
						if (d.doubleValue() < 0)
							throw new NumberFormatException ("Positive Number required");
						data[row][col] = d;
						setModifiedFlag(true);
						return;
					}
				

				// SKYCODE:
				} else if (row==3) { 

					try { 
			    			x = new Integer((String) value);
	
						if (x.intValue() > skyCodeHigh || x.intValue() < skyCodeLow) 
							throw new NumberFormatException ("CODE: This row accepts high value of " + skyCodeHigh + "  and low value of " + skyCodeLow );
						else { 
							data[row][col] = x;

							setModifiedFlag(true);
						}
					} catch (NumberFormatException nfe) { 
						
						String tmp0 = new String ((String)value);
						if (!tmp0.toUpperCase().equals ("A"))
							throw new NumberFormatException ("CODE: This row accepts high value of " + skyCodeHigh + "  and low value of " + skyCodeLow + " or an 'A'");
						else { 
							data[row][col] = tmp0;
							setModifiedFlag(true);
						}
					}

					return;

				} else if (row == 4) { 
			    			x = new Integer((String) value);
			    			if (x == null)
							x = new Integer (0);

						//// other checks here (WIP)
						data[row][col] = x;
					setModifiedFlag(true);
					return;
						
				} else if (row==5) { 

					String y = ((String)value).trim().toUpperCase();
					//// other checks here  (WIP) WIP WIP: add VAR
					//// plus logic extension
					boolean matched = false;
					for (int i=0;i<winds.length;i++) { 

						if (winds[i].equals (y)) { 
							matched=true;
							break;
						}
					}
	
					if (!matched) { 	
						throw new NumberFormatException ("CODE: This row accepts reasonable combinations of the characters 'N', 'E', 'W', 'S', or 'VAR'.");
					}
					data[row][col] = y.toUpperCase();
					
					setModifiedFlag(true);
					return;

				} else if (row==7) { 
			    			x = new Integer((String) value);
			    			if (x == null)
							x = new Integer (0);

						//// other checks here (WIP)
						if (x.intValue() > hourMax || x.intValue() < hourMin)
							throw new NumberFormatException ("CODE: This row accepts high value of " + hourMax + "  and low value of " + hourMin );
						if ((col == 1 || col == 7) && x.intValue() > 30)
							throw new NumberFormatException ("CODE: This row/column accepts high value of 30 and low value of " + hourMin );
						
						data[row][col] = x;
						setModifiedFlag(true);
					return;
						
				} else if (row==9) { 

					String y = ((String)value).trim().toUpperCase();

					if (y.length()>3)
						throw new NumberFormatException ("CODE: enter dayleader initials; this row accepts up to 3 characters" );
						
					data[row][col] = y;
					setModifiedFlag(true);
					return;

				} else { 

			    		x = new Integer ((String) value);
			    		if (x == null)
						x = new Integer (0);
				

					// check value:
					if (x.intValue() < 0)
						throw new NumberFormatException ("Positive integer required");
					data[row][col] = x;
					setModifiedFlag(true);
					return;


				}
					
			} else if (mode == commentMode) { 

				/// no validation at all

				if (value!=null) { 
					data[row][col] = (value);
					setModifiedFlag(true);
				} 
				return;

			}

		}


		public boolean wasWrong () { 
			return terriblyWrong;
		}

		/// need: code rows
		public int defaultHigh = 1000;
		public int defaultLow = 0;
		/// not sure about 2
		public int  [] decimalRow = { 0, 1, 2};
	
	        /*
	         * Don't need to implement this method unless your table's
	         * data can change.
	         */
	        public void setValueAt(Object value, int row, int col) {
	            if (DEBUG && value!=null) {
	                System.out.println("Setting value at " + row + "," + col
	                                   + " to " + value
	                                   + " (an instance of " 
	                                   + value.getClass() + ")");
	            }

	
		    try { 

	
			    validate (value, row, col);

	                    fireTableCellUpdated(row, col);
	
		  	    // totalling doesn't apply to weather data
			    if (mode==hawkMode && col>0) { 
			    	totalRow (row);
			    	totalCol ( col);
				totalRow (getRowCount()-1);
			    } 
	
	
		    } catch (ClassCastException cce) { 

			if (DEBUG) System.out.println ("cce for " + row + " " + col + " " + value);

			    /*	
			    if (col==0) { 
				data[row][col] = value;
	                    	fireTableCellUpdated(row, col);
				return;
			    }
			    */

			   if (((String)value).equals("")) { 
					data[row][col]=value;
					return;
			   }
	
	                    JOptionPane.showMessageDialog(TableDemo.this,
	                        "The \"" + getColumnName(col)
	                        + "\" column accepts only positive number values, please.");
	
				cce.printStackTrace();
	
		    } catch (NumberFormatException cce) { 
			    


			   String msg = "The \"" + getColumnName(col) + "\" column accepts only integer values.";


			   

			   String tmp = null;
			   if ( (tmp = cce.getMessage()).startsWith ("CODE:")) { 
		
				/// we'll get ROW|COL and HIGH and LOW	
				msg = tmp.substring (5).trim();	
				
			    }

			  
	                    JOptionPane.showMessageDialog(TableDemo.this, msg);

			     fireEditingCanceled();

				terriblyWrong = true;
		



				/// I don't remember what this does:
				hawkTable.setText ("0");
				DefaultCellEditor de = (DefaultCellEditor) hawkTable.getCellEditor (row, col);
				de.getComponent ().requestFocus();
				de.getComponent ().repaint();
				

			    
				
		    } catch (Exception cce) { 

			cce.printStackTrace();

		    }
	
	
	        }

		boolean terriblyWrong = false;


	
		protected void fireEditingCanceled () { 
			// super.fireEditingCanceled();	
			
			if (DEBUG) System.out.println ("hello from cell edit canceled");
			// setValueAt (new Integer(0), row, col);
			
		}
		
	
		public void totalCol (int col) { 
			int end = (getRowCount()-1);
			/// add the column: 
			int colTotal = 0;
			Integer x = null;
			for (int i=0;i<end;i++) { 
				try { 
				x = (Integer) data[i][col];
				} catch (Exception e) { 
					// meaning, not an int
					continue;
				}
				if (x==null)
					continue;
				colTotal+= x.intValue();
			}
			data[end][col] = new Integer(colTotal);
			if (DEBUG) System.out.println ("new total is: " + colTotal);
	                fireTableCellUpdated(end, col);
		}


		/// concept of a total is a total per hour
		/// This is a 1-based thing.
		/// hour 1 is 9-10; hour 2 is 10-11, etc (conveniently, that works with the array)
		public int getTotal (int hour) { 


			/// possibly this should be -1 (for "wrong!")
			if (mode != hawkMode) { 
				// System.out.println ("getTotal for non hawkmode! mode is " + mode);
				return 0;
			}

			//// get the value at the bottom corner
			try { 
				Integer i = (Integer) getValueAt (getRowCount()-1, hour);
				return i.intValue();	
			} catch (NullPointerException npw) { 
				/// they entered no data, apparently? 
				return 0;
			}
		}


		private final int minutesRow = 7;
		public int getMinutes (int hour) { 

			if (mode != weatherMode)
				return 0;

			//// get the value at the bottom corner
			try { 
				Integer i = (Integer) getValueAt (minutesRow, hour);
				return i.intValue();	
			} catch (ClassCastException cce) { 
				return 0;
			} catch (NullPointerException npw) { 
				/// they entered no data, apparently? 
				return 0;
			}
		}


		public int getTotalMinutes () { 

			if (mode != weatherMode)
				return 0;

			int total = 0;

			for (int i=0;i<getColumnCount();i++) { 
				total+=getMinutes (i);	
			}
			return total;
		}

		private final int crewRow = 7;
		public int getCrewCount (int hour) { 

			if (mode != weatherMode)
				return 0;

			//// get the value at the bottom corner
			try { 
				Integer i = (Integer) getValueAt (crewRow, hour);
				return i.intValue();	
			} catch (NullPointerException npw) { 
				/// they entered no data, apparently? 
				return 0;
			}
		}


		public void doTotals () { 

			if (mode!=hawkMode)
				return;

			for (int i=0;i<getRowCount();i++) { 
				totalRow (i);
			}	

			// repaint();


		}
	
		public void totalRow (int row) { 
			int end = (getColumnCount()-1);
			int rowTotal = 0;
			Integer x = null;
			// one-based, because the first row is hawks
			for (int i=1;i<end;i++) { 
				
				// System.out.println ("casting for totalRow for " + row + " " + i + " this thing: " + data[row][i]);
				try { 
				x = (Integer) data[row][i];
				} catch (ClassCastException cce) { 
					// can't total this row
					return;
				}
				if (x==null)
					continue;
				rowTotal+= x.intValue();
			}
			data[row][end] = new Integer(rowTotal);
			if (DEBUG) System.out.println ("new total is: " + rowTotal);
	                fireTableCellUpdated(row, end);
		}


		public int getTheTotal () { 
			try { 
				return ((Integer)data[getRowCount()-1][getColumnCount()-1]).intValue();
			} catch (Exception e) { 
				return 0;
			}
		}



		/// no one calls him. Debug data shbe in SubmitData Class	
	        private void printDebugData() {
	
			// saveData();
			if (1==1)
				return;
	
	            int numRows = getRowCount();
	            int numCols = getColumnCount();
	
	            for (int i=0; i < numRows; i++) {
	                System.out.print("    row " + i + ":");
	                for (int j=0; j < numCols; j++) {
	                    System.out.print("," + data[i][j]);
	                }
	                System.out.println();
	            }
	        }
    	}


	public void ensurePositive (Number n) throws NumberFormatException { 

		if (n.intValue() < 0)
			throw new NumberFormatException ("CODE: This row accepts positive values");


	}

    /// TODO: add a switch for access to load data from other day 
    public static void main(String[] args) {

	boolean adminMode = false;

	if (args.length > 0) { 

		for (int i=0;i<args.length;i++) { 
			if (args[i].equals ("-admin")) 
				adminMode = true;
		}
	}

        TableDemo frame = new TableDemo(adminMode);
        frame.pack();
        frame.setVisible(true);
    }
}
