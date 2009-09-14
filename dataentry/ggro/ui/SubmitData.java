package ggro.ui;

import java.sql.*;
import java.util.*;
import ggro.util.*;


/**
 * drop and create the hawkwatch table
 * NOTA BENE: DROP will destroy the table if present. This is only meant to be run 
 * as part of the migration 
 * to look at the sql in isolation, see *.sql in this directory
 */
public class SubmitData  { 

	/// perhaps we should think about the data more fluidly.
	/// when the program begins, or enters a date, 
	/// we go off the to database to see if it's there.
	/// if it is, we populate the fields. 
	/// if it isn't, we don't.
	/// when they do a save, we save everything. 
	/// if we've saved once, we update thereafter. (maybe).
	/// the database holds data. Let it do that.
	/// we should rename this file.



	public static final int [] times = { 900, 1000, 1100, 1200, 1300, 1400, 1500};
	public static final String quote = "'";
	public static final String comma = ",";
	public static final int defaultInt = 0;
	public static final String defaultStr = "";
	static boolean DEBUG = true;
	static boolean NO_DB = false;


	/// columns, in the order they happen
	/// this list will likely change
	/// not sure about the 

	/// let's read the columns from a file.

	/// these are for testing:
	public static String [] columns = null;

	static { 
		try { 
		Vector v = ggro.sql.Util2.getColumnNames();
		columns = new String[v.size()];
		System.out.println ("Here's the size: " + v.size());
		
		v.copyInto((String[])columns);
		System.out.println ("n of columns, read: " + v.size());
		} catch (Throwable e) { 
			e.printStackTrace();
			System.out.println ("This is catastrophic.program should save something & EXIT");
		}
	}


	
	/**
	 * insert rows into the "database"
	 * passing the (cleaned up) object data from the tables
	 */
	public static void doInsert (java.util.Date theDate, DataObj hdata, DataObj wdata, DataObj mdata, DataObj comments) throws Throwable { 


		/// not sure I get why we bother with the date shit here:
		java.sql.Date newDate = new java.sql.Date (theDate.getTime());

		DbDo.doInsert (newDate, hdata, wdata, mdata, comments);


		//// ADD: DbDailyDo.doInsert (newDate);  /// 
		//// ADD: DbSpeciesDo.doInsert (newDate);  /// 


	}


	/// This method is obsolete.
	public static void oldInsert (java.util.Date theDate, Object [][]hdata, Object[][]wdata, Object[][]mdata, Object [][] comments) throws Throwable { 

		Class.forName(Consts.driver);
		System.out.println ("zero item hawks: " + hdata[0][0]);

		
		Connection con = DriverManager.getConnection (Consts.connectString);

		int count = 0;

		ResultSet rs = null;
	
		ResultSetMetaData rsmd = null;


		/// skip column 1, that's the label for a row
		/// Walk each matrix like:  1 0, 1 1, 1 2, 1 3...; 2 0, 2 1, 2 2, 2 3..;3 0, 3 1
		// we want to line up the rows correctly. (Most) Everyone gets transposed; 
		/// everyone walks one at a time (col to row)

		StringBuffer look = new StringBuffer ();

		String insertStart = "INSERT into " + Consts.tableName + " values ( "; 
		// String insertStart = "INSERT into HAWKSTST values ( "; 
		Statement sql = con.createStatement ();
		int start = 0; /// zeroth column is labels 
		int maxcol = 7;
		// is it 

		/// it's row-column

		// current: 0 0 1 0 2 0	
		/// need 0 0 0 1, 0 2
		/// this won't work;
		/// at any given row, the column count is the same
		int placeholder=0;
		int overall = 0;


		java.sql.Date replace = new java.sql.Date ( theDate.getTime());

		Vector test = new Vector ();
		for (int i0=start;i0<maxcol;i0++) {  /// for each column....(ie, hour)


			/// each line gets a date:
			look.append (insertStart);
			look.append (quote);
			// look.append (theDate);
			look.append (replace);
			// test.add (theDate);
			test.add (replace);
			overall++;
			look.append (quote);
			look.append (comma);
			test.add (String.valueOf(times[placeholder]));
			look.append (times[placeholder]);
			look.append (comma);

			placeholder++;

			/// logic in here is bad: or there's too many rows
			for (int i=0;i<wdata.length;i++) { /// descend each row
				// System.out.println (i0 + " " +i +" "+ wdata[i][i0]);
				// look.append (i + " " + i0+" -" +wdata[i][i0] + ",");
				/// weather data anomalies: the wind direction
				overall++;
				// System.out.println ("Here we go: " + i0 + " " +i +" "+ " overall: " + overall );
				if (i==5) { 
					test.add (wdata[i][i0]);
					look.append (quote);
					look.append (wdata[i][i0]);
					look.append (quote);
					look.append (comma);
				} else { 
					if (wdata[i][i0] == null) { 
						test.add (String.valueOf (defaultInt));
						look.append (defaultInt);
					} else { 
						test.add (wdata[i][i0]);
						look.append (wdata[i][i0]);
					}

					look.append (comma);
				}
			}
			System.out.println (test);
			for (int i=0;i<hdata.length;i++) {
				// look.append (i + " " + i0+" -" + hdata[i][i0] + ",");

				overall++;

				/// System.out.println (i0 + " " +i +" "+ " overall: " + overall + " " + hdata[i][i0]);
				// System.out.println ("hawks at: " + i + " " + i0 + " " + hdata[i][i0]);
				/// one of these is a comment, watch out for it.!!!!!!
				if (hdata[i][i0] == null) { 
					test.add (String.valueOf (defaultInt));
					look.append (defaultInt);
				} else { 
					test.add (hdata[i][i0]);
					look.append (hdata[i][i0]);
				}
				look.append (comma);
				
			}


			/* 
			look.append (quote);
			look.append (columns[overall] + "=OTHERHAWKS");  /// no place on sheet anymore
			look.append (quote);
			look.append (comma);
			*/
			// overall++; // maybe
		
			/* 	 shbe in the data already
			look.append (columns[overall] +"=" + total);
			look.append (comma);
			*/
			for (int i=0;i<mdata.length;i++) {
				// System.out.println (i + " " + i0 + " " + wdata[i0][i]);
				// look.append (i + " " + i0+" -" + mdata[i][i0] + ",");
				overall++;
				// if (overall==74)
				//	overall=73;
				if (mdata[i][i0] == null) { 
					test.add (String.valueOf (defaultInt));
					look.append (defaultInt);
				} else { 
					test.add (mdata[i][i0]);
					look.append (mdata[i][i0]);
				}

				look.append (comma);
			}


			/// and then, comments, where column and row sense is reversed on the sheet
			/// the row is the time; column 1 (meaning "0") is the comment
			/// so row 0 is the first hour, 1 is the next hour, etc
			if (comments[i0][1] == null)
				comments[i0][1] = defaultStr;
			
			look.append (quote);
			test.add (quote + comments[i0][1] + quote);
			look.append (comments[i0][0]);
			look.append (quote);
			look.append (")");

			/// okay, here we would do an insert
			if (DEBUG) 
				System.out.println (look.toString()); { 
				System.out.println ("param count, from our inputs: "+ test.size());
				System.out.println ("param count, as the database sees it: "  + columns.length);
			}
			/* 
			if (DEBUG) { 
			for ( int i=0;i<test.size();i++) { 
				System.out.println ("compare the cols: " + i + " "+ columns[i] + " = " + test.elementAt(i) + " ");
			}
			}
			*/
			System.out.println ("");
			if (!NO_DB) 
				sql.executeUpdate (look.toString());
			look.delete (0, look.length());
			test.clear();
			/// then clear the buffer.
			overall = 0;
		}

		if (1==1)
		return;


		/// this is misplaced anyway:

		/// columns are time in hdata and mdata. and in wdata?

		/// probably a prepare is better
		try { 
			sql.executeUpdate (look.toString());
		} catch (Exception e) { 
			e.printStackTrace();	
		}


		sql.close();
		con.close();
		if (DEBUG ) System.out.println ("recs: " + count);
	}


}
