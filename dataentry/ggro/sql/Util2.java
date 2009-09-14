package ggro.sql;


import java.io.*;
import java.util.*;
import ggro.util.Consts;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.text.FieldPosition;

/**
 * this class manages differing representations of the data.
 * There's the way it looks on the sheet....in the database....and in here.
 * it's useful to be able to map between the sheet look and the database.
 * we define this in the "allcols.csv" file
 */
public class Util2 { 


	public static boolean DEBUG = false;


	/**
	 * configuration file locations
	 * The dbColumn number location
	 */
	public static final int dbCol = 0;

	/**
	 * configuration file locations
	 * The dbColumn name location
	 */
	public static final int dbColName = 1;

	/**
	 * configuration file locations
	 * The dbColumn type
	 */
	public static final int dbColType = 2;

	/**
	 * configuration file locations
	 * The entry label (as on sheet)
	 */
	public static final int species  = 3;

	/**
	 * configuration file locations
	 * The entry desc, eg, "Juvenile" or ! for none
	 */
	public static final int description  = 4;  ///

	/**
	 * configuration file locations
	 * which sheet this appears on 
	 */
	public static final int sheet = 5;


	static final String sep = ",";


	/// this might change: (use a property)
	static String top = "c:/ggro";


	static String directory = "sql";

	static String filename = "allcols.csv";


	public static int getDbColumnCount () throws Throwable { 

		Vector v4 = getColumn (dbCol);
		return v4.size();

	}

	/// reads a file of name & field, creates an array. 
	/// currently adds the comment
	private static Vector theLines = null;
	public static Vector getFieldsV () throws Throwable { 

		/// cache the file once read:
		if (theLines==null) { 
			theLines = new Vector ();	
		} else { 
			return theLines;
		}

		String name = top + File.separator + directory + File.separator + filename;

		// System.out.println ("opening " + name);

		BufferedReader br = new BufferedReader (new FileReader (name));
		String line = null;
		while ( (line=br.readLine())!=null) { 
			theLines.add (line);
		}

		br.close();
		
		return theLines;

	}


	static int [] types = null;
	public static int[] getTypes () throws Throwable { 
		/// get the array of stuff	

		if (types != null)
			return types;

		Vector v = getColumn(dbColType);
		/// build an array of the types, 
		types = new int [v.size()];

		// System.out.println ("setting types, size is :  " + types.length);

		String tmp = null;
		for (int i=0;i<v.size();i++) { 

			tmp = (String) v.get (i);
			if (tmp.equals ("int"))
				types[i] = java.sql.Types.INTEGER;
			////LEFTOFF
			if (tmp.startsWith ("char"))
				types[i] = java.sql.Types.CHAR;
			
			if (tmp.startsWith ("date")) { 
				types[i] = java.sql.Types.DATE;
				// System.out.println ("setting types to date, i = " + i);
			}
			
			if (tmp.startsWith ("float"))
				types[i] = java.sql.Types.FLOAT;

		}

		return types;
		
	}



	public static String getFields () throws Throwable { 
		Vector lines= getFieldsV ();
		// System.out.println ("Cols: " + lines.size());
		StringBuffer sb = new StringBuffer();
		for (int i=0;i<lines.size();i++) { 

			sb.append ((String) lines.elementAt(i));
			if (i< (lines.size()-1))
				sb.append (", ");

		}
		return sb.toString();
	}


	public static void normalize (GregorianCalendar gc) { 

		gc.set (Calendar.HOUR_OF_DAY, 0);
		gc.set (Calendar.SECOND, 0);
		gc.set (Calendar.MILLISECOND, 0);


	}


	/**
	 * to populate a sheet, we need a query that returns the sheets contents 
	 * so this would be getColumns for Sheet (sheet); select <columns> from 
	 * table where date;  (we do days at a time, eh)
	 * There's a condition with doing this 
	 * Part of our problem, I think, is that we don't really want all the data in there, 
	 * we want the saved stuff; 
	public void fillData (String sheet, data[][], Date date) { 

		// String query = " where YEAR(" + dateCol + " ) = " + year + " and MONTH(" + dateCol + " ) = " + month + " and DAY(" + dateCol + " ) = " + day ;

	}
	*/

	private static String getColsForSheetSelect (String thesheet) throws Throwable { 

		Vector v3 = getColumn (sheet);
		Vector v4 = getColumn (dbColName);
		StringBuffer sb = new StringBuffer ();
		String tmp = null;
		for (int i=0;i<v3.size();i++) { 
			tmp = (String) v3.get (i);
			if (tmp.equals (thesheet)) { 
				sb.append (v4.get(i));
				sb.append (", ");
			}
		}

		sb.deleteCharAt (sb.length()-2);
		return sb.toString();

	}

	static SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd");

	public static boolean hasDay (java.util.Date date) throws Throwable { 

		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime (date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);

		String start = "select TIME_ from " + Consts.getCurrentTable() + " where ";

		String qq = " YEAR(" + dateCol + " ) = ? and MONTH(" + dateCol + " ) = ? and DAY(" + dateCol + " ) = ?";
		

		System.out.println (start + qq);

		Connection con = Consts.getConnection();
		PreparedStatement stmt = con.prepareStatement (start + qq);

		stmt.setInt (1, year);
		stmt.setInt (2, month);
		stmt.setInt (3, day);

		ResultSet rs = stmt.executeQuery ();

		ResultSetMetaData rsm = rs.getMetaData();
		int size = rsm.getColumnCount();

		if (size == 0)
			return false;

		while (rs.next()) { 
			rs.close();
			return true;

		}

		
		return false;
		
	}


	/// collapse time , do the comparison straightaway

	public static Vector fetch (String sheet, java.util.Date date, String time) throws Throwable { 

		StringBuffer query = new StringBuffer();	
		query.append ("select ");
		query.append (getColsForSheetSelect (sheet));
		query.append (" from ");
		query.append (Consts.getCurrentTable());
		query.append (" where DATE_ = ?");
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime (date); /// hmmm
		cal.set (Calendar.HOUR_OF_DAY, (Integer.parseInt(time)/100));
		
		java.sql.Timestamp ts = new java.sql.Timestamp (cal.getTime().getTime());

		/* 
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		*/


		/// it's tempting for comments to just blow off the time, because this will be
		/// I think, the one and only call

		/// if (sheet.equals ("commentSheet"))
		//	System.out.println ("Util2.fetch call for time: " + time);

		// String qq = " where " + timeCol + " = " + time + " and YEAR(" + dateCol + " ) = " + year + " and MONTH(" + dateCol + " ) = " + month + " and DAY(" + dateCol + " ) = " + day ;
		// query.append (qq);

		Connection con = Consts.getConnection();
		// Statement stmt = con.createStatement();

		PreparedStatement stmt = con.prepareStatement(query.toString());
		stmt.setTimestamp (1, ts);


		if (DEBUG) System.out.println ("QUERY: " + query.toString());
		// ResultSet rs = stmt.executeQuery (query.toString());
		ResultSet rs = stmt.executeQuery ();

		ResultSetMetaData rsm = rs.getMetaData();
		int size = rsm.getColumnCount();
		// System.out.println ("get column count: " + size);	
		Vector vret = new Vector ();

		rs.next();
		for (int i=1;i<=size;i++) { 
			vret.add ( rs.getString (i) );
		}

		return vret;	



	}
	/** 
	 * sends in the vret and dbNames for a sheet
	 * the method populates vret with the column labels; 
	 * and populates dbcols with the db column for a given entry row
	 * BETTER TO DO IT HERE..... with data[][] (except we don't want it all or know where it goes)
	 */
	public static Vector fillSheet (Vector vret, String name, Vector dbcols) throws Throwable { 

		Vector v1 = getColumn (species);
		Vector v2 = getColumn (description);
		Vector v3 = getColumn (sheet);
		Vector v4 = getColumn (dbCol);

		Vector noeds = new Vector ();

	
		// System.out.println ("size compare: " + v1.size() + " " + v2.size() + " " + v3.size());
		
		String tmp = null;
		String spec = null;
		String desc = null;

		String lastHeading = null;

		

		for (int i=0;i<v3.size();i++) { 

			tmp = (String) v3.get (i);

			if (tmp.equals (name)) {

				// determine what goes in the return	
				desc = (String)  v2.get (i);			
				spec  = (String) v1.get (i);			
			

				// there's a decription. so use that
				if (!desc.equals ("!")) { 
					if (!spec.equals (lastHeading)) { 
						vret.add (spec); /// this is a noed
						noeds.add (new Integer (vret.size()-1));
						lastHeading = spec;
					}
					vret.add ("  " + desc);
					dbcols.add ( v4.get (i) );
							
				}

				/// no desc, use species:
				if (desc.equals ("!")) { 
					if (!spec.equals ("!")) { 
						vret.add (spec);
						dbcols.add ( v4.get (i) );
					} else { 
						// this must be the total row, managed elsewhere
						// but present on hawksheet
						dbcols.add ( v4.get (i) );
					}
				}

			}

		}

		return noeds;

	}



	// return type for nth row in a sheet
	public static int getType (String sheet, int where) throws Throwable { 
		int row = getLocation (sheet, where);
		// System.out.println ("looking for type for row: " + where);
		return getType (where);
	}


	public static int getType (int where) throws Throwable  { 
		if (types == null)
			getTypes();	
		// System.out.println  ("types=null? " + (types==null));

		return types[where];	
	}

	/// it will be costly to do this for every row in every sheet
	/// what if we hashed the data.
	/// adjust these dynamically, I suppose
	private static int hawkOffset = 13;
	private static int weatherOffset = 2;
	private static int morphOffset = 62;
	private static int commentOffset = 73;


	/* 
	public static int getLocation (String sheet, int where) { 

	
		if (sheet.equals ("hawksheet"))	
			return where + hawkOffset;

		if (sheet.equals ("morphsheet"))	
			return where + morphOffset;

		if (sheet.equals ("weathersheet"))	
			return where + weatherOffset;

		if (sheet.equals ("commentsheet"))	
			return where + commentOffset;

		return -1;

	}
	*/

	/// this is not efficient, but hopefully correct
	public static int getLocation (String asheet, int where) throws Throwable { 

		Vector cols = getColumn (dbCol);	
		Vector sheets = getColumn (sheet);	

		// System.out.println ("getLocation request for " + asheet + " " + where);
	
		int count = 0;
		String tmp = null;
		for (int i=0;i<sheets.size();i++) { 
			tmp = (String) sheets.get(i);
			
			if (tmp.equals (asheet))
				count++;

			if (count==where) { 
				// System.out.println ("getLocation returns for " + where + " " + cols.get(i));
				return Integer.parseInt ((String)cols.get(i));
			}
		}

		return -1;
	}

	public static Vector getColumn (int which) throws Throwable { 
		Vector lines= getFieldsV();
		StringTokenizer  st = null;
		Vector cols = new Vector ();
		String tmp = null;
		String t0 = null;
		int j=0;
		for (int i=0;i<lines.size();i++) { 
			j=0;

			t0 = (String)lines.elementAt(i);
			st = new StringTokenizer(t0, sep);
			// System.out.println (which + " token count: " + st.countTokens() + " " + t0 );
			while (st.hasMoreTokens()) { 
	
					
				tmp = st.nextToken();
				// System.out.println ("this is token: " + j + " " + tmp);

			
				if (j==which) { 
					cols.add(tmp.trim());
					break;
				}

				j++;
			}
		}
		return cols;

	}

	/// okay, it sounds like we need two columns to deal with a sheet.
	/// fine.
	public static Vector getColumnNames () throws Throwable { 
		return getColumn (dbColName);
	}


	/*
	public static Vector getColumnNames () throws Throwable { 
		Vector lines= getFieldsV();
		StringTokenizer st = null;
		Vector cols = new Vector ();
		for (int i=0;i<lines.size();i++) { 
			st = new StringTokenizer((String)lines.elementAt(i));
			cols.add(st.nextToken());
		}
		return cols;
	}
	*/


	/// ISSUE:
	/// date/time column names to be generalized with vars (see below) 

	public static String getPreparedInsertStatementString () throws Throwable { 
	
		Vector names = getColumn (dbColName);	
		// System.out.println ("column count is: " + names.size());
		StringBuffer sb = new StringBuffer ();
		sb.append ("(");
		StringBuffer sb2 = new StringBuffer ();
		sb2.append ("(");


		String front = "INSERT INTO " + Consts.getCurrentTable();

		for (int i=0;i<names.size();i++) { 
			sb.append ((String)names.get (i));
			sb2.append ( "?" );
			if (i<names.size()-1) { 
				sb.append (",");
				sb2.append (",");
			}
		}

		sb.append (")");
		sb2.append (")");

		String theWholeThing = front + sb.toString() + " VALUES " + sb2.toString();
		// System.out.println (theWholeThing);
		return theWholeThing;	

	}

	private static final int AVG = 100;
	private static final int FIRST= 101;
	private static final int SUM = 102;

	private static final String avg_s = "avg(";
	private static final String first_s = "first(";
	private static final String sum_s = "sum(";


	public static final int getProcToUse (String colName) { 


		/// usually the answer is sum. Some exceptions exist.

		if (colName.equals ("DATE_") || colName.equals ("TIME_") || colName.equals ("WINDFROM") || colName.equals ("DAYLEADER") || colName.equals ("SKYCODE") || colName.equals ("COMMENTS")) { 
			return 101;
		} else if ( colName.equals ("MINVIS") || colName.equals ("VISIBILITY") || colName.equals ("TEMP") || colName.equals ("WINDSPEED") ) { 
			return 100;
		} else { 
			return 102;
		}
	}


	/**

		this currently creates an INSERT into daily (col,col) select (avg(col), sum(col) from currentTable;
		possibly microevils will want it to be a select into, as in": 



		select cols from current into daily;



		this would be fine, we just need to change the  our stuff like this: 


		select avg(col) as col, sum(col) as col from current into daily;

			



	**/

	public static String [] speciesCols = { "DATE_", "TUVU", "OSPR", "WTKI", "BAEA", "NOHA", "SSHA", "COHA", "RSHA", "BWHA", "SWHA", "RTHA", "FEHA", "RLHA", "GOEA", "AMKE", "MERL", "PEFA", "PRFA", "TOTALHAWKS" };

	public static String[] getSpeciesColumns() { 
		return speciesCols;
	}


	/**
	 * this is actually a series of queries
	 * select date_, max(field) from trable
	 */
	public static Hashtable getMaxQueriesSpecies (String table) { 

		String select = "select ";

		Hashtable ht = new Hashtable();

		/// start at 1 to skip date
		for (int i=1;i< speciesCols.length; i++) { 
			// select = "select date_, max(" +  speciesCols[i] + ")  from " + table + " where date_ >= ? and date <= ? group by date_ order by max(" + speciesCols[i] + ") desc";
			select = "select date_, max(" +  speciesCols[i] + ")  from " + table + " group by date_ order by max(" + speciesCols[i] + ") desc";

			// System.out.println ("here's the select stmt: " + select);

			ht.put (speciesCols[i], select);
		}

		return ht;
	}


	/// Presumes that you call it with the daily table name
	public static String getSpeciesInsertStatementString (String table, String today, String tomorrow) throws Throwable { 


		/// okay, this does an insert by summing (usually) averaging, or firsting a field
	
		// System.out.println ("column count is: " + names.size());
		StringBuffer sb = new StringBuffer ();
		sb.append ("(");
		StringBuffer sb2 = new StringBuffer ();
		sb2.append ("(");

	
		String	end = null;
		if (tomorrow == null && today == null) { 
	
			end = " group by date_ order by date_";

		}  else { 
		
			end = " group by date_ where date_>=? and date_ <?";
			
	
		}

		String front = "INSERT INTO " + Consts.getSpeciesTable();
		String middle = " (DATE_, TUVU, OSPR, WTKI, BAEA, NOHA, SSHA, COHA, RSHA, BWHA, SWHA, RTHA, FEHA, RLHA, GOEA, AMKE, MERL, PEFA, PRFA, TOTALHAWKS) select  DATE_, sum(TUVU), sum(OSPR), sum(WTKIJUV + WTKIAD + WTKIUND), sum(BAEA), sum(NOHAJUV + NOHAADF + NOHAADM + NOHAUND), sum(SSHAJUV + SSHAAD + SSHAUND ), sum(COHAJUV + COHAAD + COHAUND), sum(RSHAJUV + RSHAAD + RSHAUND), sum(BWHAJUV + BWHAAD + BWHAUND), sum(SWHA), sum(RTHAJUV + RTHAAD + RTHAUND), sum(FEHAJUV + FEHAAD + FEHAUND), sum(RLHA), sum(GOEAAD + GOEAJUV + GOEAUND), sum(AMKEF + AMKEM + AMKEUND), sum(MERL), sum(PEFAJUV + PEFAAD  + PEFAUND), sum(PRFA), sum(TOTALHAWKS) from " + table;

		System.out.println ("result species str: "+ front + middle + end);

		return front + middle + end;

	}

	/**

		there's an initial one, with different criteria.

		the daily one should 	

	**/
	public static String getDailyInsertStatementString (String table, String today, String tomorrow ) throws Throwable { 

		Vector names = getColumn (dbColName);	

		/// okay, this does an insert by summing (usually) averaging, or firsting a field
	
		// System.out.println ("column count is: " + names.size());
		StringBuffer sb = new StringBuffer ();
		sb.append ("(");
		StringBuffer sb2 = new StringBuffer ();
		// I don't think this is needed: sb2.append ("(");

		String front = "INSERT INTO " + Consts.getDailyTable();

		String which = null;
		String field = null;
		int proc = 102;

		StringBuffer showit = new StringBuffer();

		for (int i=0;i<names.size();i++) { 
			field = (String) names.get (i);
			sb.append (field);

			showit.append (field);
			showit.append (" ");
			// showit.append (types.get(i));
			// showit.append (" ");
		
				
			proc = getProcToUse (field);

			if (proc == SUM) { 
				sb2.append (sum_s);
				showit.append (sum_s);
			} else if ( proc == FIRST ) { 
				sb2.append (first_s);
				showit.append (first_s);
			} else if ( proc == AVG ) { 
				showit.append (avg_s);
				sb2.append (avg_s);
			} else { 
				System.out.println ("fell through: " + field);
			}

			showit.append ("\n");
			sb2.append (field);
			sb2.append (")");
			// sb2.append (" as " + field);

			if (i<names.size()-1) { 
				sb.append (",");
				sb2.append (",");
			}
		}

		sb.append (")");
		/// sb2.append (")");
		// sb2.append (" FROM " + table);
		// sb2.append (" WHERE date >= ? " );
		// sb2.append (" INTO " + Consts.getDailyTable());

		// String theWholeThing = front + sb2.toString();

		/// okay, the first time, it looks like this on the end: 
		String end=null;
		if (today == null && tomorrow == null)
			end = "group by day(date_), month(date_), year(date_) order by year(date_),  month(date_),  day(date_)";
		else
			end = "group by day(date_), month(date_), year(date_) where date_>=? and date_ <?";
	
		String theWholeThing = front + sb.toString() + " select " + sb2.toString() + " from "  + table + " " + end;
		System.out.println (showit);
		return theWholeThing;	

	}

	public static String getSpeciesCreateStatementString () throws Throwable { 

	
		String ymp  = "CREATE TABLE " + Consts.getSpeciesTable() + " (DATE_ datetime, TUVU smallint, OSPR smallint, WTKI smallint, BAEA smallint, NOHA smallint, SSHA smallint, COHA smallint,RSHA smallint, BWHA smallint, SWHA smallint, RTHA smallint, FEHA smallint, RLHA smallint, GOEA smallint, AMKE smallint, MERL smallint, PEFA smallint, PRFA smallint, TOTALHAWKS smallint);";
	

		return ymp;


	}

	/// not something that should get a lot of use
	/// default to the temp table for create.
	public static String getCreateStatementString () throws Throwable { 
		return getCreateStatementString(Consts.getTempName());
	}



	public static String getCreateStatementString (String table) throws Throwable { 

		Vector names = getColumn (dbColName);	
		Vector types = getColumn (dbColType);	

		
		String front = "CREATE TABLE " + table;
		StringBuffer sb = new StringBuffer ();
		sb.append (front);
		sb.append ("("); 
		String tmptype = null;
		for (int i=0;i<names.size();i++) { 

							
			sb.append ((String)names.get (i));
			sb.append (" ");
			tmptype = (String)types.get (i);
			if (tmptype.equals ("int")) { 
				tmptype = "smallint";
				sb.append (tmptype);
			} else if (tmptype.indexOf ("char") != -1) { 
				tmptype = "text";
				sb.append (tmptype);
			} else { 
				sb.append (tmptype);
			}
			
			System.out.println ("setting: " + names.get(i) + " " + tmptype);	
			if (i<names.size()-1)
				sb.append (", ");

		}
		sb.append (")");
		
		// System.out.println ("here's the insert statement: " );
		// System.out.println (sb.toString());
		// System.out.println ("");
		return sb.toString();

	}


	/// 3 issues:
	/// probably update won't work for the same reason select won't (date issue)
	/// date/time column names to be generalized with vars (see below) 
	/// check col alignment (mistmatches on execute at the moment)

	public static String getUpdateStatementString (int time, java.sql.Date date) throws Throwable { 
		Vector names = getColumn (dbColName);	
		// System.out.println ("column count (noting here in update): " + names.size());
		StringBuffer sb = new StringBuffer ();
		StringBuffer sb2 = new StringBuffer ();


		String front = "UPDATE  " + Consts.getCurrentTable() + " set ";

		sb.append (front);

		for (int i=0;i<names.size();i++) { 
			sb.append ((String)names.get (i));
			sb.append ("=");
			sb.append ( "?" );
			if (i<names.size()-1) { 
				sb.append (",");
			}
		}

		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime (date); /// hmmm
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);

		// let's guess the problem is the date comparison (

		// get the year, month, day pieces	
		
	
		/// BUG: the bridge doesn't seem to deal with date comparisons. 	
		/// Possibly still an issue with Access.
		// sb.append (" where " + dateCol + " = " + date + " and " + timeCol + " = " + time);
		String query = " where " + timeCol + " = " + time + " and YEAR(" + dateCol + " ) = " + year + " and MONTH(" + dateCol + " ) = " + month + " and DAY(" + dateCol + " ) = " + day ;
		sb.append (query);

		return sb.toString();


	}

	public static final String dateCol = "DATE_";
	public static final String timeCol = "TIME_";


	/// InSANE! so dBase won't compare the whole date at once. It wants the 
	/// InSANE! 
	/// InSANE! so we'll need to do YEAR(date) MONTH(date) DAY
	//// it's probably the bridge. 
	public static void deleteRow (java.sql.Connection con, int time, int year, int month, int day) throws Throwable { 

		Statement stmt = con.createStatement();
		String delete = "delete from  " + Consts.getCurrentTable() + " where " + timeCol + " = " + time + " and YEAR(" + dateCol + " ) = " + year + " and MONTH(" + dateCol + " ) = " + month + " and DAY(" + dateCol + " ) = " + day ;

		// System.out.println ("D: " + delete);
		stmt.executeUpdate (delete);
		stmt.close();
	}

	public static void deleteRowDaily (java.sql.Connection con, Date theDate) throws Throwable { 

		String delete = "delete from  " + Consts.getDailyTable() + " where date_ = ?";
		PreparedStatement stmt = con.prepareStatement (delete);


		stmt.setTimestamp (1, new java.sql.Timestamp (theDate.getTime()));

		stmt.executeUpdate (delete);
		stmt.close();
	}


	/// better pass in a normalized date or we're hosed
	public static boolean rowExistsSpecies (java.sql.Connection con, java.util.Date theDate) throws Throwable { 
		if (con.isClosed())
			con = Consts.getConnection();
	

			
		
		String find = "select from  " + Consts.getSpeciesTable() + " where date_ = ?";
		PreparedStatement stmt = con.prepareStatement (find);


		stmt.setTimestamp (1, new java.sql.Timestamp (theDate.getTime()));

		ResultSet rs = stmt.executeQuery ();
		boolean found = false;
		while (rs.next()) { 
			found = true;
			break;

		}
		stmt.close();
		return found;

	}

	public static boolean rowExistsDaily (java.sql.Connection con, java.util.Date theDate) throws Throwable { 
		if (con.isClosed())
			con = Consts.getConnection();
		
		///PreparedStatement stmt = con.prepareStatement(query);
		

		///// NYI NYI!!!
		String find = "select from  " + Consts.getDailyTable() + " where date_ = ?";
		PreparedStatement stmt = con.prepareStatement (find);


		stmt.setTimestamp (1, new java.sql.Timestamp (theDate.getTime()));

		ResultSet rs = stmt.executeQuery ();
		boolean found = false;
		while (rs.next()) { 
			found = true;
			break;

		}
		stmt.close();
		return found;

	}


	public static boolean rowExists (java.sql.Connection con, int time, java.util.Date date) throws Throwable { 


		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime (date); /// hmmm
		cal.set (Calendar.HOUR_OF_DAY, (time/100) );
		java.sql.Timestamp ts = new java.sql.Timestamp (cal.getTime().getTime());

		String query = "select " + dateCol + "  from " + Consts.getCurrentTable() + " where " + dateCol + "  = ?";

		System.out.println ("here is the rowExists q:" + query);
		System.out.println ("here is the rowExists ts:" + ts);

		/// this ought to have been much easier

		System.out.println ("Connection shit: " );

		if (con.isClosed())
			con = Consts.getConnection();
		
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setTimestamp (1, ts);


		// Statement stmt = con.createStatement();
		// String query = "select " + dateCol + ", " + timeCol + "  from " + Consts.getCurrentTable() + " where " + timeCol + " = " + time + " and YEAR(" + dateCol + " ) = " + year + " and MONTH(" + dateCol + " ) = " + month + " and DAY(" + dateCol + " ) = " + day ;



		if (DEBUG) System.out.println ("rowExists Q: " + query);
		ResultSet rs = stmt.executeQuery ();
		/// determine if  this row exists
		java.sql.Date d2;
		boolean ret = false;
		while ( rs.next()) { 
			d2 = rs.getDate(1);
			// System.out.println ("yo: " + d2 + " " + rs.getString(2));
			// System.out.println ("eq? " + (d2.equals (d)));
			ret = true;
			break;
		}

		// boolean hasNext = rs.next();
		// if (rs.getDouble (1) == (double)time && rs.getDate (2).equals (d))
		//	return true;

		rs.close();
		stmt.close();
		return ret;

	}


	public static void main (String [] args) throws Throwable { 


		// Vector v = getColumnNames();
		// System.out.println (v);
		/* 
		Vector v = null;
		Vector v2 = null;
		for (int i=0;i<6;i++) { 
			v = getColumn (i);
			System.out.println (v);
		}

		// System.out.println ("");
		// System.out.println ("");
		System.out.println ("");
		System.out.println ("");

		v = new Vector ();
		v2 = new Vector ();
		fillSheet (v, "hawksheet", v2);

		System.out.println (Consts.getCurrentTable());
		*/

		/// so, perhaps we should send in the day/month/year rather than this shit
		/* 
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		Connection con = DriverManager.getConnection ("jdbc:odbc:ggro");
		java.sql.Date d = java.sql.Date.valueOf ("2003-05-31");
		System.out.println (rowExists( con, 900, 2003, 5, 31));
		// System.out.println (v2);
		*/

		System.out.println (getCreateStatementString());


		/* 

		// getPreparedInsertStatementString();			
		java.sql.Date d = java.sql.Date.valueOf ("2001-09-12");
		java.sql.Date d2 = java.sql.Date.valueOf ("2001-09-14");

		GregorianCalendar cal = new GregorianCalendar ();
		cal.set (Calendar.YEAR, 2001);
		cal.set (Calendar.MONTH, 8);
		cal.set (Calendar.DAY_OF_MONTH, 14);
		cal.set (Calendar.HOUR_OF_DAY, 12);
		cal.set (Calendar.MINUTE, 0);
		cal.set (Calendar.SECOND, 0);
		cal.set (Calendar.MILLISECOND, 0);
		
		java.sql.Date d3 = new java.sql.Date (cal.getTime().getTime());

		// Vector v = fetch ("hawksheet", d, "0900");

		Consts.setCurrentTable (Consts.getPermTable());
		System.out.println (hasDay (d));
		System.out.println (hasDay (d2));
		System.out.println (hasDay (d3));
		*/

	}


}
