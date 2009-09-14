package ggro.sql;
import java.sql.*;
import java.util.*;
import ggro.util.Consts;

public class ExecSql {


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

		GregorianCalendar cal = new GregorianCalendar ();
		cal.set (Calendar.YEAR, 2001);
		cal.set (Calendar.MONTH, 8);
		cal.set (Calendar.DAY_OF_MONTH, 14);
		cal.set (Calendar.HOUR_OF_DAY, 12);
		cal.set (Calendar.MINUTE, 0);
		cal.set (Calendar.SECOND, 0);
		cal.set (Calendar.MILLISECOND, 0);

		System.out.println (cal);

		// Timestamp ts = new Timestamp ( cal.getTime().getTime());
		java.util.Date ts =  cal.getTime();

		// Statement sql = con.createStatement ();
		// PreparedStatement pps = con.prepareStatement ("select DATE_ from hawk where DATE_ = ?");
		PreparedStatement pps = con.prepareStatement ("explain select DATE_ from hawk where format_date(DATE_, '%Y%m%d') = ?");

		// pps.setTimestamp (1, ts);
		pps.setDate (1, new java.sql.Date(ts.getTime()));

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
				System.out.println ( rs.getString(i)  );
			// }
		}

		}

		rs.close();
		con.close();
	}


}
