package ggro.sql;
import java.sql.*;
import java.util.*;
import ggro.util.Consts;

public class ExecSql2 {


	public static void main (String [] args) throws Exception { 

		Connection con = Consts.getConnection();

		ResultSet rs = null;
		ResultSetMetaData rsmd = null;


		/* 
		for (int i=0;i<args.length;i++) { 
			sb.append (args[i]);
			sb.append (" " );
		}
		*/

		String q = "select day(DATE_), month(DATE_), year(DATE_) from hawk where year(DATE_) > 1990 GROUP by day(DATE_), month(DATE_), year(DATE_) order by year(date_), month(date_), day(date_)";

		PreparedStatement pps = con.prepareStatement (q);

		// pps.setTimestamp (1, ts);
		// pps.setDate (1, new java.sql.Date(ts.getTime()));

		StringBuffer sb = new StringBuffer();


		// sb.append ("delete from hawk where YEAR(DATE_) = 2002");
		// sb.append ("select count(*) as reps, DATE_ from hawk GROUP BY DATE_");
		// sb.append ("select TIME_ from hawk where DATE_ = '" + ts + "'");
			

		// sql.execute (sb.toString());
		// System.out.println ( "Q:" + sb.toString());
		// rs = sql.executeQuery (sb.toString());
		rs = pps.executeQuery();
		rsmd = rs.getMetaData();

		String strtName = null;

		int reps = 0;

		while (rs.next())  {

		for (int i=1;i<=rsmd.getColumnCount();i++) { 
			/* 
			if (i==1) { 
				reps = rs.getInt (i);
				if (reps == 1) 
					break;
			} else { 
			*/
			System.out.print ( rs.getString(i) + " "  );
			// }
		}
		System.out.println ( );

		}

		rs.close();
		con.close();
	}


}
