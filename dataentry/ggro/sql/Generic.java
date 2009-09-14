
package ggro.sql;

import java.sql.*;
import java.util.*;
import ggro.util.Consts;

/**
 * run various queriees. 
 */
public class Generic { 


	public static void doCreate () throws Throwable { 

		Connection con = Consts.getConnection();
		int count = 0;

		ResultSet rs = null;
		ResultSetMetaData rsmd = null;

		Statement sql = null;
		sql = con.createStatement ();

		Vector fields = null;
		try { 
			fields = Util2.getFieldsV();
		} catch (Throwable t) { 
			t.printStackTrace();
			System.out.println ("catastrophic. Exiting");
			System.exit (1);
		}


		//// For the hourly to daily: 

			first(DATE_),
			first(TIME_),
			avg(MINVIS),
			avg(VISIBILITY),
			avg(TEMP),
			avg(SKYCODE),
			avg(WINDSPEED,
			first(WINDFROM),
			sum(VISITORS),
			sum(MINUTES),
			avg(OBSERVERS),
		sum(TUVU),
		sum(OSPR),
		sum(WTKIJUV),
		sum(WTKIAD),
		sum(WTKIUND),
		sum(BAEA),
		sum(NOHAJUV),
		sum(NOHAADF),
		sum(NOHAADM),
		sum(NOHAUND),
		sum(SSHAJUV),
		sum(SSHAAD),
		sum(SSHAUND),
		sum(COHAJUV),
		sum(COHAAD),
		sum(COHAUND),
		sum(GOSH),
		sum(RSHAJUV),
		sum(RSHAAD),
		sum(RSHAUND),
		sum(BWHAJUV),
		sum(BWHAAD),
		sum(BWHAUND),
		sum(SWHA),
		sum(RTHAJUV),
		sum(RTHAAD),
		sum(RTHAUND),
		sum(FEHAJUV),
		sum(FEHAAD),
		sum(FEHAUND),
		sum(RLHA),
		sum(GOEAAD),
		sum(GOEAJUV),
		sum(GOEAUND),
		sum(AMKEF),
		sum(AMKEM),
		sum(AMKEUND),
		sum(MERL),
		sum(PEFAJUV),
		sum(PEFAAD),
		sum(PEFAUND),
		sum(PRFA),
		sum(GYRF),
		sum(ACCIPITER),
		sum(BUTEO),
		sum(FALCON),
		sum(EAGLE),
		sum(RAPTOR),
		sum(TOTALHAWKS),
		sum(d_RTHAJUV),
		sum(d_RTHAAD),
		sum(d_RTHAUND),
		sum(d_BWHAJUV),
		sum(d_BWHAAD),
		sum(d_BWHAUND),
		sum(d_FEHAJUV),
		sum(d_FEHAAD),
		sum(d_FEHAUND),
		sum(d_SWHA),
		sum(d_RLHA)


		/// for the species database: query this from the daily data:
DATE_,
TUVU,
OSPR,
sum(WTKIJUV + WTKIAD + WTKIUND),
BAEA,
sum(NOHAJUV + NOHAADF + NOHAADM + NOHAUND),
sum(SSHAJUV + SSHAAD + SSHAUND ),
sum(COHAJUV + COHAAD + COHAUND),
sum(RSHAJUV + RSHAAD + RSHAUND),
sum(BWHAJUV + BWHAAD + BWHAUND),
SWHA,
sum(RTHAJUV + RTHAAD + RTHAUND),
sum(FEHAJUV + FEHAAD + FEHAUND),
RLHA
sum(GOEAAD + GOEAJUV + GOEAUND),
sum(AMKEF + AMKEM + AMKEUND),
MERL,
sum(PEFAJUV + PEFAAD  + PEFAUND),
PRFA,
TOTALHAWKS
		}

		if (1==1)
			System.exit(0);

		String create = Util2.getCreateStatementString();
		String create2 = Util2.getCreateStatementString(Consts.getPermTable());

	
		try { 
			rs = sql.executeQuery ("SELECT first(date_), sum(TUVU) from hawk group by day(date_), month(date_), year(date_) order by year(date_), month(date_), day(date_)" );

			while (rs.next()) { 

				System.out.println (rs.getString(1) + " " + rs.getString(2));

			}
			
		} catch (Exception e) { 
			System.out.println ("Probably don't care about this error:");
			e.printStackTrace();	
		}

		sql.close();
		con.close();
		System.out.println ("recs: " + count);
	}

	public static void main (String [] args) throws Throwable { 

		doCreate ();
	}

}
