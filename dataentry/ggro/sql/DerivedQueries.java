
package ggro.sql;

import java.sql.*;
import java.util.*;
import ggro.util.Consts;

/**
 *  Various queries of interest
 * 
 */
public class DerivedQueries { 


	public static void getMaxes (String table) throws Throwable { 

		// create some good default dates

		GregorianCalendar gc = new GregorianCalendar();
		gc.set (Calendar.YEAR, 1989);

		long begin = gc.getTime().getTime();

		gc.set (Calendar.YEAR, 2002);

		long end = gc.getTime().getTime();


		getMaxes (table, begin, end);
	}


	public static void getMaxes (String table, long startDate, long endDate) throws Throwable { 

		Connection con = Consts.getConnection();

		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		PreparedStatement psql = null;


		Hashtable queries = null;

		if (table.equals (Consts.getSpeciesTable()))
			queries = Util2.getMaxQueriesSpecies(table);


		String strstmt = null;
		String species = null;

		StringBuffer sb = new StringBuffer();

		Iterator it = queries.entrySet().iterator();

		Map.Entry me = null;


		while (it.hasNext()) { 

			me = (Map.Entry)it.next();
	
			strstmt = (String)me.getValue ();	
			species = (String)me.getKey ();	
			System.out.println ("stmt: " + strstmt);
			psql = con.prepareStatement (strstmt);

			// psql.setDate (1, new java.sql.Date (startDate));
			// psql.setDate (2, new java.sql.Date (endDate));


			try { 
	
				rs = psql.executeQuery();

				addResult (rs, sb, species);
			} catch (Exception e) { 
				e.printStackTrace();	
			}
		}

		/// not sure how we want to return this. It will probably end up in a grid.
		/// so, even less sure.

		System.out.println (sb.toString());

	}


	public static void addResult (ResultSet rs, StringBuffer sb, String species) throws Exception{ 


		sb.append (species);
		sb.append ("\t");

		while (rs.next()) { 

			sb.append (rs.getString (1));
			sb.append ("\t");

			sb.append (rs.getString (2));
			sb.append ("\t");

			break;

		}

		sb.append ("\n");



	}

	public static void main (String [] args) throws Throwable { 


		getMaxes(Consts.getSpeciesTable());

	}

}
