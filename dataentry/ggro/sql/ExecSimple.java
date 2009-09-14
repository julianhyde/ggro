package ggro.sql;
import java.sql.*;
import java.util.*;
import ggro.util.Consts;

public class ExecSimple {


/// brute force method: select on this year (for speed)
/// create an output table. 
/// do 1 row at a time; select if the line doesn't exist yet.
/// we could simply do it by grabbing the date & time, combine, put in a hash.


/// perhaps we could take a bit of a shortcut here. 
/// we could query if the line exists. create that table (but not of the entire row?)
/// and 


	public static void main (String [] args) throws Exception { 

		Connection con = Consts.getConnection();
		Statement sql = con.createStatement ();
		HashMap h = new HashMap();
		ResultSet rs = "select * from hawk where YEAR(date_) = 2003";


		/// create a new database "t", which has the stucture of hawk.
		/// with the resultset, insert the (entire!) row

		/// then we go back, delete the 2003 from hawk, 
		/// and insert everything we left. 


		while (rs.next()) { 
			java.sql.Date date = rs.getDate ("DATE_");
			int time = rs.getInt ("TIME_");			
		
			String result = combine (date, time);

			Object o = h.put (result);

			if (o == null) { 
				/// insert into new database.
				/// this is sort of an ugh, isn't it.
				
			}

		}


		// now, delete everything in hawk from 2003

		/// now, insert everything from that other table.
		

/// test the autonumber.
String zero  = "drop table t";
String first ="Create table t ( date_ date, time_ int, id long autonumber)";

// we need the copyinto killer.
String first2 = "Insert into t Select date_, time_ from hawk";  // can't be this easy. check the beast



	/* 
/// this should give the duplicate rows, including a row id
String Second = "Select * from t group by d,h having count(*) > 1";

// returns the date/hour all of the duplicates

// Select * from t where  (d,h) in ( Select d,h from t group by d,h having count(*) > 1 )

/// prints the duplicate rows (so you can make sure that the other columns are consistent).


// ...deletes all but the first copy of each duplicate. (Make sure you
// check the number of rows deleted is sensible before you commit.)

	*/

// String last = "Delete from t, hawk where (date_,time_) in ( Select date_,time_ from t group by date_,time_ having count(*) > 1 ) And id > (select min(id) from t where t.date_ = hawk.date_ and t.time_ = hawk.time_)";
String last = "Delete * from hawk, t where (date_,time_) in ( Select date_,time_ from t group by date_,time_ having count(*) > 1 ) And id > (select min(id) from t where t.date_ = hawk.date_ and t.time_ = hawk.time_)";
			


		// try  { sql.executeUpdate (zero); } catch (Exception e) {}
		// sql.executeUpdate (first);
		// sql.executeUpdate (first2);
		sql.executeUpdate (last);
		// sql.executeQuery (sb.toString());


		// rs.close();
		con.close();
	}


}
