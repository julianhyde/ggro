package ggro.sql;
import java.sql.*;

public class Util1 {


	public static void main (String [] args) throws Exception { 


		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver"); 

		Connection con = DriverManager.getConnection ("jdbc:odbc:ggro");

		DatabaseMetaData dbm = con.getMetaData ();	

		System.out.println ("meta: " + dbm);

		ResultSet rs = null;
		ResultSetMetaData rsmd = null;


		Statement sql = con.createStatement ();
		/// NOTE
		rs = sql.executeQuery ("select * from hawks00");
		rsmd = rs.getMetaData();


		String strtName = null;

		System.out.println ("old columns: " );
		for (int i=1;i<=rsmd.getColumnCount();i++) { 
			strtName = rsmd.getColumnName (i);
			System.out.print ( strtName + ", " );
		}

		System.out.println ("");
		System.out.println ("new columns: " );

		for (int i=1;i<=rsmd.getColumnCount();i++) { 
			strtName = rsmd.getColumnName (i);
			if (strtName.endsWith ("IMM"))
				strtName = strtName.substring (0, strtName.length()-3).concat ("JUV");
			System.out.print ( strtName + ", " );
		}



		rs.close();
		con.close();
	}


}
