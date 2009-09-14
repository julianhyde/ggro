package ggro.ui;
import java.sql.*;
import java.util.*;
import ggro.util.Consts;
import ggro.sql.Util2;


/** 
 * do the sql insert or update to the current database.
*/

public class DbDo { 

	public static final int [] times = { 900, 1000, 1100, 1200, 1300, 1400, 1500};


	public static final int defaultInt = 0;
	public static final int defaultFloat = 0;
	public static final String defaultStr = "\"\"";

	static Calendar c = new GregorianCalendar ();

	/// is the incoming a sql date? 

	public static void doInsert (java.sql.Date theDate, DataObj hdatao, DataObj wdatao, DataObj mdatao, DataObj commentso) throws Throwable { 


		// System.out.println ("db column count 0 based: " + Util2.getDbColumnCount());


		// garb for query stuff
		int month;
		int day;
		int year;

		// Calendar c = new GregorianCalendar ();
		c.setTime (theDate);

		year  = c.get (Calendar.YEAR);
		month = c.get (Calendar.MONTH) + 1; /// java insanity: months are 0-based
		day   = c.get (Calendar.DAY_OF_MONTH);
		
		try { 
		//// 
		Class.forName(Consts.driver);
		// System.out.println ("zero item hawks: " + hdatao.data[0][0]);
		Connection con = Consts.getConnection ();

		

		// each data matrix we get sent -except comments - represents 
		// the columns represent an hour.

		// we dp tje
		/// decide for update or 
		/// we decided too soon for statement details..

		int type = -1;
		int location = 0;

		// System.out.println ("Location arrays" );
		// System.out.println ("hawk: " + hdatao.location);
		// System.out.println ("hawk size, length: " + hdatao.location.size() + " " + hdatao.data.length);
		// System.out.println ("weather: " + hdatao.location);
		// System.out.println ("weather size, length: " + wdatao.location.size() + " " + wdatao.data.length);

		String with = null;
		PreparedStatement pps = null;

		/// for each "hour"
		/// except, I think we get sent col0, which can wreak havoc
		/// on virtually every one.
		/// Probably, for getSaveable data, we should NOT send the first column.
		// System.out.println ("");
		// System.out.println ("");
		// System.out.println ("");
		// System.out.println ("");
		/// (assuming we are)
		/// 
		String lastWith = null;
		boolean isUpdate = false;
		for (int i0=0;i0<times.length;i0++) { 
			/// set the date and time:
			// if (Util2.rowExists( con, times[i0], year, month, day))  { 
			if (Util2.rowExists( con, times[i0], theDate))  { 
				// System.out.println ("CREATED NEW UPDATE STATEMENT");
				with = Util2.getUpdateStatementString (times[i0], theDate );
				isUpdate= true;
			} else {
				// System.out.println ("CREATED NEW INSERT STATEMENT");
				with = Util2.getPreparedInsertStatementString ();
			}

		
			// System.out.println ("Statement: " + with);
			if (lastWith == null) { 
				// System.out.println ("CREATED NEW PREPARED STATEMENT");
				pps = con.prepareStatement (with);
				lastWith = with;
			} else { 

			  if (lastWith!=null && !lastWith.equals (with)) { 
				if (pps!=null) { 
					// System.out.println ("CLOSED PREPARED STATEMENT");
					pps.close();
				}
				// System.out.println ("CREATED NEW PREPARED STATEMENT");
				pps = con.prepareStatement (with);
			 } else
				lastWith = with;
			}
			

			//// BUG: need to set the time in this date
			int val = new Integer(times[i0]).intValue()/100;
			
			c.set ( Calendar.HOUR_OF_DAY, val );
			Timestamp theTs = new java.sql.Timestamp( c.getTime().getTime() );
			addData(java.sql.Types.DATE, 0, theTs, pps);
			// System.out.println ("set where, what: " + 1 + " " +theDate);
			addData(java.sql.Types.INTEGER, 1, new Integer(times[i0]), pps);
			// System.out.println ("set where, what: " + 2 + " " +times[i0]);

			// set the rest: 
			for (int i=0;i<wdatao.data.length;i++) { 
				/// this returns the SQL type (translates from string)
				location = Integer.parseInt ( (String) wdatao.location.get (i) );
				type = Util2.getType (location);
				/// this returns the location in the db (ie, col number)
				// location = Util2.getLocation ("weathersheet", i);
				// System.out.println ("location returned: " + location );
				addData (type, location, wdatao.data[i][i0], pps);
			}
			for (int i=0;i<hdatao.data.length;i++) { 
				/// this returns the SQL type (translates from string)
				// type = Util2.getType ("hawksheet", i);
				location = Integer.parseInt ( (String) hdatao.location.get (i) );
				type = Util2.getType (location);
				/// this returns the location in the db (ie, col number)
				// location = Util2.getLocation ("hawksheet", i);
				// System.out.println ("location returned: " + location );
				addData (type, location, hdatao.data[i][i0], pps);
			}
			for (int i=0;i<mdatao.data.length;i++) { 
				/// this returns the SQL type (translates from string)
				// type = Util2.getType ("morphsheet", i);
				/// this returns the location in the db (ie, col number)
				// location = Util2.getLocation ("morphsheet", i);
				location = Integer.parseInt ( (String) mdatao.location.get (i) );
				type = Util2.getType (location);
				// System.out.println ("location returned: " + location );
				addData (type, location, mdatao.data[i][i0], pps);
			}

			/// comments are another matter: (ugh)
			// type = Util2.getType ("commentsheet", 0); /// not sure about the number
			/// this returns the location in the db (ie, col number)
			// location = Util2.getLocation ("commentsheet", 0);
			location = Integer.parseInt ( (String) commentso.location.get (0) ); // it's the only item
			type = Util2.getType (location);
			// System.out.println ("COMMENT: " + commentso.data[i0][0]);  /// check that data
			addData (type, location, commentso.data[i0][0], pps);  /// check that data

			///// HACK. the database has a 54 (1 based) col for GYRF. Not on sheet
			addData(java.sql.Types.INTEGER, 53, new Integer(0), pps);
			/// go!
			// System.out.println ("calling execute for " + times[i0]);
			pps.executeUpdate();
		}

		// System.out.println (locations);
		pps.close();
		// con.close();

		} catch (Throwable t) { 
			System.out.println (locations);
			System.out.println ("field count: " + locations.size());
			System.out.println (things);

			throw t;
		}
	}


	static Vector locations = new Vector ();
	static Vector things = new Vector ();


	public static void addData (int type, int location, Object data, PreparedStatement ps) throws Throwable { 

		locations.add (new Integer (location));

		location = location + 1;

		// System.out.println ("set where, what: " + location + " " + data);

		switch (type) { 
		
			case java.sql.Types.INTEGER:
				int i = defaultInt;
				if (data!=null) { 
					i = Integer.parseInt (String.valueOf(data));
				}
				// System.out.println ("setInt: " + location + " " + i);
				ps.setInt (location, i);
				things.add (new Integer (i));
				break;

			/// double seems better
			case java.sql.Types.FLOAT:
				float f = defaultFloat;
				if (data!=null) 
					f = Float.parseFloat (String.valueOf(data));
				// System.out.println ("setFloat: " + location + " " + f);
				ps.setFloat (location, f);
				things.add (new Double (f)); 
				break;

			case java.sql.Types.CHAR:
				String s = defaultStr;
				if (data!=null) 
					s = String.valueOf(data);
				// System.out.println ("setString: " + location + " " + s);
				ps.setString (location, s);
				things.add (s);
				break;

			case java.sql.Types.DATE:
				// System.out.println ("setDate: " + location + " " + data);
				/// was setDate/Dateps.setTimestamp (location, (java.sql.Timestamp)data, c);
				ps.setTimestamp (location, (java.sql.Timestamp)data);
				things.add (data);
				break;

			default:
				System.out.println ("fell through to DEFAULT!!!! BUG!!!!");
				System.out.println ("type, location, data: " + type +" " + location + " " + data);

		}

	}




}

