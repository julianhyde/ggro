package ggro.util;
import java.sql.*;

public class Consts { 

	/**
	 * default entry table.
	 */
	public static String tableName = "test";
	// public static String tableName = "db1";

	/**
	 * data table names
	 */
	public static String permTableName = "hawk";

	public static String tempTableName= "temp";

	public static String tempDatabase = "temp";

	public static String permanente = "hawk";

	public static String currentTable = tempDatabase;

	public static String dailyTable = "hawkdaily";

	public static String speciesTable = "speciesdaily";



	public static String getDailyTable () { 
		return dailyTable;
	}

	public static String getSpeciesTable () { 
		return speciesTable;
	}

	public static String getPermTable () { 
		return permTableName;
	}

	public static String getTempTable () { 
		return tempTableName;
	}

	public static String getPermName () { 
		return permTableName;
	}

	public static String getTempName () { 
		return tempTableName;
	}


	// it's really just a table, but whatever
	public static void setCurrentTable (String val) { 
		currentTable = val;
	}

	// it's really just a table, but whatever
	public static String getCurrentTable () { 
		return currentTable;
	}


	/**
	 * the default driver. This is the Sun bridge driver. (not great)
	 */
	public static final String driver = "sun.jdbc.odbc.JdbcOdbcDriver"; 

	/**
	 * the default connect string. It's the name of the odbc entry (ggro)
	 */
	public static final String connectString = "jdbc:odbc:ggro_access";


	private static Connection theCon = null;

	public static Connection getConnection () throws Exception { 
		Class.forName(driver);
		if (theCon==null || theCon.isClosed()) 
              		theCon = DriverManager.getConnection (connectString);

		return theCon;
	}

	public static void main (String [] args) { 

		System.out.println ("tableName: " + tableName);

	}

}
