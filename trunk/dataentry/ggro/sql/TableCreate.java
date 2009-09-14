
package ggro.sql;

import java.sql.*;
import java.util.*;
import ggro.util.Consts;

/**
 * drop and create the hawk watch temp and permanent tables.
 * !!!!!!!!!!!!!!!!!!!THIS WILL DESTROY THE DATA!!!!!!!!!
 * Only run this if you know exactly what you're doing and why. 
 */
public class TableCreate { 


	public static void doCreate () throws Throwable { 

		Connection con = Consts.getConnection();
		int count = 0;

		ResultSet rs = null;
		ResultSetMetaData rsmd = null;

		Statement sql = null;
		sql = con.createStatement ();

		String drop = "DROP TABLE " + Consts.getTempTable();
		String drop2 = "DROP TABLE " + Consts.getPermTable();

		String fields = null;
		try { 
			fields = Util2.getFields();
		} catch (Throwable t) { 
			t.printStackTrace();
			System.out.println ("catastrophic. Exiting");
			System.exit (1);
		}

		String create = Util2.getCreateStatementString();
		String create2 = Util2.getCreateStatementString(Consts.getPermTable());

	
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

		sql.close();
		con.close();
		System.out.println ("recs: " + count);
	}

	public static void main (String [] args) throws Throwable { 

		doCreate ();
	}

}
