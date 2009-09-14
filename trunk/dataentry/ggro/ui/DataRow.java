package ggro.ui;

/// This is a layer of abstraction for the data to be comfortable in before being bludgeoned into 
/// dbase.
/// One of our rows represents columns from the sheet; ergo, the transform.
/// change to lookup. more flexible when we change the fricking sheet.
import java.util.Date;
import java.util.Hashtable;
import java.lang.reflect.*;

public class DataRow { 


	/// of course, the trouble with this idea is that 
	/// we need a way of translating the rows from the sheets to it anyway.
	/// How about just a starting offset? 

	/// this is a little mapping of its own:
	static int weatherOffset = 2;
	static int hawkOffset = 11;
	static int morphOffset = 63;

	static Hashtable dbtable  = new Hashtable();

	static { 

		dbtable.put ( new Integer("0"), "DATE");
 		dbtable.put ( new Integer("1"), "TIME");
 		dbtable.put ( new Integer("2"), "VISIBILITY");
 		dbtable.put ( new Integer("3"), "MINVIS");
 		dbtable.put ( new Integer("4"), "TEMP");
 		dbtable.put ( new Integer("5"), "SKYCODE");
 		dbtable.put ( new Integer("6"), "WINDSPEED");
 		dbtable.put ( new Integer("7"), "WINDFROM");
 		dbtable.put ( new Integer("8"), "VISITORS");
 		dbtable.put ( new Integer("9"), "OBSERVERS");
 		dbtable.put ( new Integer("10"), "MINUTES");
 		dbtable.put ( new Integer("11"), "TUVU");
 		dbtable.put ( new Integer("12"), "OSPR");
 		dbtable.put ( new Integer("13"), "WTKIJUV");
 		dbtable.put ( new Integer("14"), "WTKIAD");
 		dbtable.put ( new Integer("15"), "WTKIUND");
 		dbtable.put ( new Integer("16"), "BAEAJUV");
 		dbtable.put ( new Integer("17"), "BAEAAD");
 		dbtable.put ( new Integer("18"), "BAEAUND");
 		dbtable.put ( new Integer("19"), "NOHAJUV");
 		dbtable.put ( new Integer("20"), "NOHAADF");
 		dbtable.put ( new Integer("21"), "NOHAADM");
 		dbtable.put ( new Integer("22"), "NOHAUND");
 		dbtable.put ( new Integer("23"), "SSHAJUV");
 		dbtable.put ( new Integer("24"), "SSHAAD");
 		dbtable.put ( new Integer("25"), "SSHAUND");
 		dbtable.put ( new Integer("26"), "COHAJUV");
 		dbtable.put ( new Integer("27"), "COHAAD");
 		dbtable.put ( new Integer("28"), "COHAUND");
 		dbtable.put ( new Integer("29"), "GOSH");
 		dbtable.put ( new Integer("30"), "RSHAJUV");
 		dbtable.put ( new Integer("31"), "RSHAAD");
 		dbtable.put ( new Integer("32"), "RSHAUND");
 		dbtable.put ( new Integer("33"), "BWHAJUV");
 		dbtable.put ( new Integer("34"), "BWHAAD");
 		dbtable.put ( new Integer("35"), "BWHAUND");
 		dbtable.put ( new Integer("36"), "SWHA");
 		dbtable.put ( new Integer("37"), "RTHAJUV");
 		dbtable.put ( new Integer("38"), "RTHAAD");
 		dbtable.put ( new Integer("39"), "RTHAUND");
 		dbtable.put ( new Integer("40"), "FEHAJUV");
 		dbtable.put ( new Integer("41"), "FEHAAD");
 		dbtable.put ( new Integer("42"), "FEHAUND");
 		dbtable.put ( new Integer("43"), "RLHA");
 		dbtable.put ( new Integer("44"), "GOEAAD");
 		dbtable.put ( new Integer("45"), "GOEAJUV");
 		dbtable.put ( new Integer("46"), "GOEAUND");
 		dbtable.put ( new Integer("47"), "AMKEF");
 		dbtable.put ( new Integer("48"), "AMKEM");
 		dbtable.put ( new Integer("49"), "AMKEUND");
 		dbtable.put ( new Integer("50"), "MERL");
 		dbtable.put ( new Integer("51"), "PEFAJUV");
 		dbtable.put ( new Integer("52"), "PEFAAD");
 		dbtable.put ( new Integer("53"), "PEFAUND");
 		dbtable.put ( new Integer("54"), "GYRF");
 		dbtable.put ( new Integer("55"), "ACCIPITER");
 		dbtable.put ( new Integer("56"), "BUTEO");
 		dbtable.put ( new Integer("57"), "FALCON");
 		dbtable.put ( new Integer("58"), "EAGLE");
 		dbtable.put ( new Integer("59"), "RAPTOR");
 		dbtable.put ( new Integer("60"), "DARKMORPH");
 		dbtable.put ( new Integer("61"), "OTHERHAWKS");
 		dbtable.put ( new Integer("62"), "TOTALHAWKS");
 		dbtable.put ( new Integer("63"), "dRTHAJUV");
 		dbtable.put ( new Integer("64"), "dRTHAAD");
 		dbtable.put ( new Integer("65"), "dRTHAUND");
 		dbtable.put ( new Integer("66"), "dBWHAJUV");
 		dbtable.put ( new Integer("67"), "dBWHAAD");
 		dbtable.put ( new Integer("68"), "dBWHAUND");
 		dbtable.put ( new Integer("69"), "dFEHAJUV");
 		dbtable.put ( new Integer("70"), "dFEHAAD");
 		dbtable.put ( new Integer("71"), "dFEHAUND");
 		dbtable.put ( new Integer("72"), "dSWHA");
 		dbtable.put ( new Integer("73"), "dRLHA");
 
	}

	public DataRow () { 


	}

	Date DATE = null; 
	int TIME = 0;
	int VISIBILITY = 0;
	int MINVIS = 0;
	int TEMP = 0;
	/*   NEED ?  ASK ALLEN. KEEP AT ALL? 
	int BAROMETER = 0;
	int HUMIDITY = 0;
	*/
	int SKYCODE = 0;
	int WINDSPEED = 0;
	String WINDFROM = "";
	int VISITORS = 0;
	int OBSERVERS = 0;
	int MINUTES = 0;


	int TUVU = 0;
	int OSPR = 0;
	int WTKIJUV = 0;
	int WTKIAD = 0;
	int WTKIUND = 0;
	int BAEAJUV = 0;
	int BAEAAD = 0;
	int BAEAUND = 0;
	int NOHAJUV = 0;
	int NOHAADF = 0;
	int NOHAADM = 0;
	int NOHAUND = 0;
	int SSHAJUV = 0;
	int SSHAAD = 0;
	int SSHAUND = 0;
	int COHAJUV = 0;
	int COHAAD = 0;
	int COHAUND = 0;
	int GOSH = 0;
	int RSHAJUV = 0;
	int RSHAAD = 0;
	int RSHAUND = 0;
	int BWHAJUV = 0;
	int BWHAAD = 0;
	int BWHAUND = 0;
	int SWHA = 0;
	int RTHAJUV = 0;
	int RTHAAD = 0;
	int RTHAUND = 0;
	int FEHAJUV = 0;
	int FEHAAD = 0;
	int FEHAUND = 0;
	int RLHA = 0;
	int GOEAAD = 0;
	int GOEAJUV = 0;
	int GOEAUND = 0;
	int AMKEF = 0;
	int AMKEM = 0;
	int AMKEUND = 0;
	int MERL = 0;
	int PEFAJUV = 0;
	int PEFAAD = 0;
	int PEFAUND = 0;
	int GYRF = 0;
	int ACCIPITER = 0;
	int BUTEO = 0;
	int FALCON = 0;
	int EAGLE = 0;
	int RAPTOR = 0;
	int DARKMORPH = 0;
	String OTHERHAWKS = "";
	int TOTALHAWKS = 0;


	int dRTHAJUV = 0;
	int dRTHAAD = 0;
	int dRTHAUND = 0;
	int dBWHAJUV = 0;
	int dBWHAAD = 0;
	int dBWHAUND = 0;
	int dFEHAJUV = 0;
	int dFEHAAD = 0;
	int dFEHAUND = 0;
	int dSWHA = 0;
	int dRLHA = 0;



	Integer forNull = new Integer (0);	
	String forNullStr = "";


	/// the reflective effort

	public void addHawks (Object [][] o, int col) { 

		int correct = 0;
		String fieldName = null;
		for (int i=0;i<o.length;i++) { /// descend each row

			correct = i + hawkOffset;
			
			fieldName = (String) dbtable.get (new Integer (correct));	

			if (fieldName == null)
				System.out.println ("STILL NULL");
			else
				System.out.println ("Fieldname: " + fieldName);
						

		}


	}
	

	public void addWeather (Object [][] o, int col) { 
		for (int i=0;i<o.length;i++) { /// descend each row
			// System.out.println (i + " = " + o[i][col]);
			Object strt = o[i][col];
			if (strt == null) { 
				try { 
					strt = forNull;
				} catch (ClassCastException cce) { 
					strt = forNullStr;	
				}
			}
			// exception
			switch (i) { 
				/// CHECK! the sheet transposes the database order. 
				/// does the data in the database make sense? 
				/// ie, VIS >= MINVIS   CHECK!
				case 0: 
					MINVIS = ((Integer)strt).intValue(); 
					continue;
				case 1: 
					VISIBILITY =  ((Integer)strt).intValue(); 
					continue;
				case 2: TEMP = ((Integer)strt).intValue(); 
					continue;
				case 3: SKYCODE = ((Integer)strt).intValue(); 
					continue;
				case 4: WINDSPEED = ((Integer)strt).intValue(); 
					continue;
				case 5: WINDFROM = (String)strt;
					continue;
				case 6: VISITORS = ((Integer)strt).intValue(); 
					continue;
				case 7: OBSERVERS =((Integer)strt).intValue(); 
					continue;
				case 8: MINUTES =((Integer)strt).intValue(); 
					continue;
			}
		}
	}

	/* 
	public void addHawks (Object [][] o, int col) { 
		for (int i=0;i<o.length;i++) { /// descend each row
			// System.out.println (i + " = " + o[i][col]);
			Object strt = o[i][col];
			if (strt == null && i == 7)
				strt = forNull;
			// exception
			switch (i) { 
			case 0: TUVU = ((Integer)strt[i][col]).intValue();continue;
			case 1: OSPR = ((Integer)strt[i][col]).intValue();continue;
			case 2: WTKIJUV = ((Integer)strt[i][col]).intValue();continue;
			case 3: WTKIAD = ((Integer)strt[i][col]).intValue();continue;
			case 4: WTKIUND = ((Integer)strt[i][col]).intValue();continue;
			case 5: BAEAJUV = ((Integer)strt[i][col]).intValue();continue;
			case 6: BAEAAD = ((Integer)strt[i][col]).intValue();continue;
			case 7: BAEAUND = ((Integer)strt[i][col]).intValue();continue;
			case 8: NOHAJUV = ((Integer)strt[i][col]).intValue();continue;
			case 9: NOHAADF = ((Integer)strt[i][col]).intValue();continue;
			case 10: NOHAADM = ((Integer)strt[i][col]).intValue();continue;
			case 11: NOHAUND = ((Integer)strt[i][col]).intValue();continue;
			case 12: SSHAJUV = ((Integer)strt[i][col]).intValue();continue;
			case 13: SSHAAD = ((Integer)strt[i][col]).intValue();continue;
			case 14: SSHAUND = ((Integer)strt[i][col]).intValue();continue;
			case 15: COHAJUV = ((Integer)strt[i][col]).intValue();continue;
			case 16: COHAAD = ((Integer)strt[i][col]).intValue();continue;
			case 17: COHAUND = ((Integer)strt[i][col]).intValue();continue;
			case 18: GOSH = ((Integer)strt[i][col]).intValue();continue;
			case 19: RSHAJUV = ((Integer)strt[i][col]).intValue();continue;
			case 20: RSHAAD = ((Integer)strt[i][col]).intValue();continue;
			case 21: RSHAUND = ((Integer)strt[i][col]).intValue();continue;
			case 22: BWHAJUV = ((Integer)strt[i][col]).intValue();continue;
			case 23: BWHAAD = ((Integer)strt[i][col]).intValue();continue;
			case 24: BWHAUND = ((Integer)strt[i][col]).intValue();continue;
			case 25: SWHA = ((Integer)strt[i][col]).intValue();continue;
			case 26: RTHAJUV = ((Integer)strt[i][col]).intValue();continue;
			case 27: RTHAAD = ((Integer)strt[i][col]).intValue();continue;
			case 28: RTHAUND = ((Integer)strt[i][col]).intValue();continue;
			case 29: FEHAJUV = ((Integer)strt[i][col]).intValue();continue;
			case 30: FEHAAD = ((Integer)strt[i][col]).intValue();continue;
			case 31: FEHAUND = ((Integer)strt[i][col]).intValue();continue;
			case 32: RLHA = ((Integer)strt[i][col]).intValue();continue;
			case 33: GOEAAD = ((Integer)strt[i][col]).intValue();continue;
			case 34: GOEAJUV = ((Integer)strt[i][col]).intValue();continue;
			case 35: GOEAUND = ((Integer)strt[i][col]).intValue();continue;
			case 36: AMKEF = ((Integer)strt[i][col]).intValue();continue;
			case 37: AMKEM = ((Integer)strt[i][col]).intValue();continue;
			case 38: AMKEUND = ((Integer)strt[i][col]).intValue();continue;
			case 39: MERL = ((Integer)strt[i][col]).intValue();continue;
			case 40: PEFAJUV = ((Integer)strt[i][col]).intValue();continue;
			case 41: PEFAAD = ((Integer)strt[i][col]).intValue();continue;
			case 42: PEFAUND = ((Integer)strt[i][col]).intValue();continue;
			case 43: GYRF = ((Integer)strt[i][col]).intValue();continue;
			case 44: ACCIPITER = ((Integer)strt[i][col]).intValue();continue;
			case 45: BUTEO = ((Integer)strt[i][col]).intValue();continue;
			case 46: FALCON = ((Integer)strt[i][col]).intValue();continue;
			case 47: EAGLE = ((Integer)strt[i][col]).intValue();continue;
			case 48: RAPTOR = ((Integer)strt[i][col]).intValue();continue;
			case 49: DARKMORPH = ((Integer)strt[i][col]).intValue();continue;

			/// do we keep this: 
			case 50: String = ((Integer)strt[i][col]).intValue();continue;

			//// here's your problem: 
			case 51: TOTALHAWKS = ((Integer)strt[i][col]).intValue();continue;

			}
		}
	}
	*/
	public void printWeather () { 
		System.out.println (VISIBILITY+ " " + MINVIS+ " " + TEMP+ " " +
			SKYCODE+ " " + WINDSPEED+ " " + WINDFROM+ " " + 
			VISITORS+ " " + OBSERVERS + " " + MINUTES);
	}





}
