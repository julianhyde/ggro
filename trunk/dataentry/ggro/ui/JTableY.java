package ggro.ui;
import java.awt.event.*;
import java.awt.print.*;
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
import javax.swing.BorderFactory;
import javax.swing.Action;

import javax.swing.JTable;
import javax.swing.JComponent;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.AbstractTableModel;
import javax.swing.JScrollPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.JFrame;
import javax.swing.plaf.*;
import javax.swing.DefaultCellEditor;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.filechooser.FileFilter;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;


///// issues to consider: you can use my total, which will always be correct, 
///// or you can enter your own.
///// perhaps totaling should be toggled
///// The current program has total-checking. Perhaps we should have something like
///// that, where you make a call and you "light up" bad totals.
//// shift tab doesn't work

//// 2 bugs:  if you enter the cell by clicking, it selects the cell; 
/////         if you then hit Enter, it stays in the cell. 
////      variation on the theme: if you enter a cell by typing Enter, it doesn't select the cell.
////        The suggestion seems to be that it has to do with having selected the cell contents.

public class JTableY extends JTable implements Printable { 



	/// This is the thing to implement, I hallucinate...
	/// So, we'll want to do a getData and power through.
	public int print(Graphics g, PageFormat pf, int pi) throws PrinterException {
    		if (pi >= 1) 
        		return Printable.NO_SUCH_PAGE;
		else
			return 1;
    	}
      	// public final WholeNumberField integerField = new WholeNumberField(0, 5);
      	public final JTextField integerField = new JTextField ();
        DefaultCellEditor integerEditor = null;

	boolean DEBUG = false;


	public void setText (String s) { 

		integerField.setText (s);

	}

	public void tableChanged (TableModelEvent tme) { 

		super.tableChanged(tme);
		String view = null;
		if (tme.getType() == TableModelEvent.UPDATE) view = "UPDATE";
		if (tme.getType() == TableModelEvent.INSERT) view = "INSERT";
		if (tme.getType() == TableModelEvent.DELETE) view = "DELETE";
		// if (DEBUG) System.out.println ("Hello from tableChanged: " + view + " where: col=" + tme.getColumn() + " rows: " + tme.getFirstRow() + " " + tme.getLastRow());


	}

	int mode = 0;

	DefaultTableCellRenderer defaultCol = new DefaultTableCellRenderer();
	DefaultTableCellRenderer shadedCol = new DefaultTableCellRenderer();
	DefaultTableCellRenderer deepLineCol = new DefaultTableCellRenderer();
	DefaultTableCellRenderer col1 = new DefaultTableCellRenderer();


	public TableCellRenderer getCellRenderer (int row, int col) { 

		if (col == 0)
			return col1;

		// if ( theTableModel.isCellEditable (row, col) && (col!=getColumnCount()-1 || mode != TableDemo.hawkMode))

		if ( theTableModel.isCellEditable (row, col) )
			return deepLineCol;


		/* 
		if (col%2 ==0  && col!=0)
			return shadedCol;
		else
		*/

		return defaultCol;


	}

	TableModel theTableModel = null;


	public JTableY (TableModel tm, int mode) {
		super (tm);
		theTableModel = tm;

		// System.out.println ("FONT: " + defaultCol.getFont());
		// col1.setFont (new Font ("Dialog", Font.ITALIC, 22));
		// System.out.println ("FONT col1: " + col1.getFont());

		Color color = new Color (245, 241, 253); // perfect!
		Color color2 = new Color (245, 241, 252); // green? 
		// BorderUIResource.LineBorderUIResource border = new BorderUIResource.LineBorderUIResource(color2, 10);
		shadedCol.setBackground (color);
		/// deepLineCol.setRowMargin (5);
		// deepLineCol.setBorder (BorderFactory.createLineBorder (color.red));
		// deepLineCol.setBorder (BorderFactory.createEmptyBorder (5, 5, 5, 5));
		deepLineCol.setBackground (color2);


		// did nothing, apparently
		// col1.setFont (new Font ("Dialog", Font.BOLD, 16));

		this.mode = mode;
        	// integerField.setHorizontalAlignment(WholeNumberField.RIGHT);

        	integerEditor = new DefaultCellEditor(integerField) {
                   //Override DefaultCellEditor's getCellEditorValue method
                   //to return an Integer, not a String:
                   public Object getCellEditorValue() {
			return integerField.getText();
			/*
			if (isHawk)
                    		return new Integer(integerField.getValue());
			else
                    		return String.valueOf(integerField.getValue());
			*/
				
                   }


		   /// was commented
		   public boolean shouldSelectCell (EventObject eo) { 

			boolean b = super.shouldSelectCell (eo);
			if (b) { 
				
				if (DEBUG) System.out.println ("should select cell returned true");
				integerField.selectAll();
			} else { 
				if (DEBUG) System.out.println ("should select cell returned true");
			}

			// if (DEBUG) System.out.println ("click count to start: " + this.getClickCountToStart());
			// if (DEBUG) System.out.println ("returning " + b + " from shouldSelectCell from " + eo);
			return b;

		   }

		   
                };

		integerEditor.setClickCountToStart(1);
		//// no: integerEditor.setBackground (Color.GREEN);
        	this.setDefaultEditor(Integer.class, integerEditor);



		/// make Enter behave like tab: move things to the right. 
		/* 
		int i =  JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT;
		Object tab_action_key = getInputMap(i).getParent().get(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0));
		getInputMap(i).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), tab_action_key);
		Action action = getActionMap().get(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0));
		getActionMap().put((Object)"Enter - P", action);
		*/
//

	}

	public TableCellEditor getCellEditor (int row, int col) { 

		return integerEditor;
	}


	public boolean editCellAt(int row, int column,
                                        EventObject evt) {

		if (DEBUG) System.out.println ("CALL TO editCellAt");
		return super.editCellAt (row, column, evt);

	}

	/// maybe we need editingStopped to throw the next event?
	/// or just leave it.


	boolean digest = false;

	public void setDigest (boolean b) { 
		if (DEBUG) System.out.println ("set digest to: " + b);
		digest = b;
	}

	public void processMouseEvent (MouseEvent e) { 
		if (digest) 
			digest=false;
		else {
			/*  
			if (DEBUG) { 
				System.out.println ("Calling the super process mouse event");
				System.out.println ("event was: " + e);
			}
			*/
			super.processMouseEvent (e);
		}

	}

	// public void processKeyEvent (KeyEvent e) { 
	protected void processKeyEvent (KeyEvent e) { 
		 if (digest) { 
			if (DEBUG) System.out.println ("digesting the KeyEvent");
			digest=false;
		 } else {
			
			if (DEBUG) System.out.println ("Calling the super process key event for " +e);
			
			super.processKeyEvent (e);
		}


		//// possibly we want to notice any key event and push things along
		/// arrows, tabs are suspects
		/// so, we would want to generalize the code here

		int code = e.getKeyCode();

		if (DEBUG) System.out.println ("-->here is the key code: " + code);

		// if (  e.getKeyCode() == KeyEvent.VK_ENTER)

		int column, row;


		/// avoid getting thrown in illegal spots
		// if ( code == KeyEvent.VK_ENTER || code == KeyEvent.VK_TAB && e.getID() == KeyEvent.KEY_PRESSED ) { 
		if ( code == KeyEvent.VK_ENTER || code == KeyEvent.VK_TAB && e.getID() == KeyEvent.KEY_RELEASED ) { 
			column = this.getSelectedColumn();
			row = this.getSelectedRow();

			if (DEBUG) System.out.println ("in the preferred handle area. key event, row col: " + row + " " + column);


			if (mode==TableDemo.commentMode) { 
				super.processKeyEvent(e);
				return;
			}	



			// if we're in column 0, bump. Col 0 is a label
			if (column == 0)
				column = 1;


			/// in the hawksheet, last col is total
			if (mode==TableDemo.hawkMode && column == theTableModel.getColumnCount()-1) { 
				column = 1;
				row++;
			}

			/// last row bounce to top in hawkSheet
			if (mode==TableDemo.hawkMode && row == theTableModel.getRowCount()-1) { 
				column++;
				row=0;
			}
			
			if (row > theTableModel.getRowCount()) { 
				row=0;
				column++;
			}

			/// final order of business: the cell can be edited.
			if (! theTableModel.isCellEditable (row, column)) { 
				row++;
			}

			// resetting selection: 
			if (DEBUG) System.out.println ("re-setting selecton to :" + row + " " + column);
			this.clearSelection();
			this.setRowSelectionInterval(row, row);
			this.setColumnSelectionInterval(column, column);
		} else { 

			if (DEBUG) System.out.println ("key event calling super (again) ");
			super.processKeyEvent (e);

		}


		// super.processKeyEvent(e);
	}

	public void editingCanceled (ChangeEvent ce) { 
		super.editingCanceled (ce);
	}


	String use="hawks";

	public void setUse (String s) { 
		use = s;
	}


	public static void main(String[] args) {
    	}
}
