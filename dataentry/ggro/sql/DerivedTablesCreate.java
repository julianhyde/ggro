
package ggro.sql;

import java.sql.*;
import java.util.*;
import ggro.util.Consts;

/**
 * drop and create the derived tables (daily & species)
 * This DROPS the tables, but they're generated from the regular hawk data anyway, so, 
 * no biggy to recreate them
 * 
 */
public class DerivedTablesCreate { 


	public static void doCreate () throws Throwable { 

		Connection con = Consts.getConnection();
		int count = 0;

		ResultSet rs = null;
		ResultSetMetaData rsmd = null;

		Statement sql = null;
		PreparedStatement psql = null;
		sql = con.createStatement ();

		String drop = "DROP TABLE " + Consts.getDailyTable();
		String drop2 = "DROP TABLE " + Consts.getSpeciesTable();

		String fields = null;
		try { 
			fields = Util2.getFields();
		} catch (Throwable t) { 
			t.printStackTrace();
			System.out.println ("catastrophic. Exiting");
			System.exit (1);
		}

		String create = Util2.getCreateStatementString(Consts.getDailyTable());
		String create2 = Util2.getSpeciesCreateStatementString();

	
		try { 
			System.out.println (drop);
			sql.executeUpdate (drop);
			System.out.println (drop2);
			sql.executeUpdate (drop2);
		} catch (Exception e) { 
			System.out.println ("Probably don't care about this error:");
			e.printStackTrace();	
		}
		System.out.println (create);
		sql.executeUpdate (create);
		System.out.println (create2);
		sql.executeUpdate (create2);

		/*** 
			logical to populate them now

		***/

		// let's use 1910

		GregorianCalendar  gc = new GregorianCalendar();
		gc.add (Calendar.YEAR, -50);

		String tmp1 = null;
		// tmp1 = Util2.getDailyInsertStatementString();
		tmp1 = Util2.getDailyInsertStatementString(Consts.getPermTable(), (String)null, (String)null);

		System.out.println ("populating the daily database");
		System.out.println (tmp1);
		psql = con.prepareStatement (tmp1);
		// psql.setTimestamp (1, new java.sql.Timestamp (gc.getTime().getTime()));
		psql.executeUpdate ();



		System.out.println ("populating the species database");
		tmp1 = Util2.getSpeciesInsertStatementString(Consts.getDailyTable(), (String)null, (String)null);

		System.out.println (tmp1);

		psql = con.prepareStatement (tmp1);
		// psql.setTimestamp (1, new java.sql.Timestamp (gc.getTime().getTime()));
		psql.executeUpdate ();

		sql.close();
		con.close();
		System.out.println ("recs: " + count);


		
	}

	public static void main (String [] args) throws Throwable { 

		doCreate ();
	}

}
