
package ggro.ui;

import java.util.Vector;

public class DataObj { 

	public DataObj (Object [][] data, Vector location) { 
		this.data = data;
		this.location = location;
	}

	public Object [][] data;
	public Vector location;

	public void showMeTheMoney() { 

		System.out.println ("hello from MONEY: ");
		System.out.println ("first dimension length: " + data.length);
		System.out.println ("second dimension length: " + data[0].length);
		for (int i=0;i<7;i++){
			System.out.println ("MONEY: i " + i +" " +data[i][0]);
		}


	}

}
