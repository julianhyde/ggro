
package ggro.sql;

import java.sql.*;
import ggro.util.*;
import java.io.PrintStream;
import java.io.FileOutputStream;

public class MoveData { 


	public static void commitToPermanent () throws Exception { 

		// get all the data from the temp database. 
		// ordered. Append to the permanent database

		/// grab from temp, throw in perm

		/// get a connection to the database.		
		/// 

		Connection con = Consts.getConnection ();
		Statement stmt = con.createStatement();



		/** transactions not apparently supported...
		Savepoint sp = con.setSavepoint("dog");
		*/

		String theCopy = "INSERT into " + Consts.getPermName() + " select * from " + Consts.getTempName();


		String theDelete = "delete from " + Consts.getTempName() + " where TIME_ > 0";

		try { 
			System.out.println (theCopy);
			stmt.executeUpdate (theCopy);
			System.out.println (theDelete);
			stmt.executeUpdate (theDelete);
			stmt.close();
			con.close();
		} catch (Exception e) { 

			/// no transactions: con.rollback (sp);
			try { 
				stmt.close();
				con.close();
			} catch (Exception ee)  {}

			try { 

				FileOutputStream fos = new FileOutputStream ("c:/ggro/logs/commit.err");
				
				PrintStream ps = new PrintStream (fos);	

				e.printStackTrace(ps);

				ps.flush();

				ps.close();

				fos.close();


			} catch (Throwable tt) { 

			}
			

			throw e;
		}

	}


	/// For testing? ease? do the move
	public static void main (String [] args) throws Exception { 

		commitToPermanent();

	}


}
