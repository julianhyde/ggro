package ggro.ui;
import java.sql.*;
import java.util.*;
import ggro.util.Consts;
import ggro.sql.Util2;


/** 
 * do the sql insert or update to the daily database.
*/

public class DbDailyDo { 


	public static final int defaultInt = 0;
	public static final int defaultFloat = 0;
	public static final String defaultStr = "\"\"";

	static GregorianCalendar c = new GregorianCalendar ();

	/// is the incoming a sql date? 

	public static void doInsert (java.sql.Date theDate) throws Throwable { 


		/// normalize the date to lacking hour/minute/second
		c.setTime (theDate);
		Util2.normalize(c);

		java.util.Date ndate = c.getTime();


		try { 
		//// 
			Class.forName(Consts.driver);
			Connection con = Consts.getConnection ();
	
			int type = -1;
			int location = 0;
	
			String with = null;
			PreparedStatement pps = null;
			boolean isUpdate = false;
	
			if (Util2.rowExistsDaily( con, ndate)) {
				System.out.println ("Daily doing a delete");
				deleteDailyRow ( theDate );
			} 

			System.out.println ("Daily doing an insert");
			/// I think we need to just force the normalized data
			with = Util2.getDailyInsertStatementString ( ); 

			/// seems anti-climactic	
			pps = con.prepareStatement (with);
			pps.setTimestamp (1, new java.sql.Timestamp (ndate.getTime()));
			pps.executeUpdate();  /// ALWAYS? 
			pps.close();

		} catch (Throwable t) { 
			System.out.println ("Failure was on the insert/update to daily");
			throw t;
		}
	}


	public static void deleteDailyRow (java.sql.Date date) throws Throwable { 

		c.setTime (date);
		Util2.normalize(c);

		String del = "delete from " + Consts.getDailyTable() + " where date_ = ?";

		java.sql.Timestamp stmp = new java.sql.Timestamp (c.getTime().getTime());

		try { 

			Connection con = Consts.getConnection ();
			PreparedStatement delete = con.prepareStatement (del);

			delete.setTimestamp (1, stmp);

			delete.executeUpdate ();

			delete.close();


		} catch (Throwable t) { 
			System.out.println ("Failure was on the delete row for daily");
			throw t;
		}



	}


}

