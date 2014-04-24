package com.gt.brewmasters.structures;

public class ResponseStatus {
	
	public static final int Idle    = 0;
	public static final int PREMASH = 1;
	public static final int MASH    = 2;
	public static final int SPARGE  = 3;
	public static final int BOIL    = 4;
	public static final int COOL    = 5;
	
	
	public Boolean status;
	public int     step;
	public int     timeLeft;
	public double  mashTemp;
	public double  boilTemp;
	public Boolean requireUser;
	public int     spargeNumber;
	public int     confirmationNumber;
	public String  message;
	
	public void ResponseStatus () {
		
	}
	
	@Override
	public String toString() {
		return "status: "+this.status + " step: " + step + " timeLeft: " + timeLeft +
				" mashTemp: " + mashTemp + " boilTemp: " + boilTemp + 
				" requireUser: " + requireUser + "confirmation #: " + 
				confirmationNumber + " message: " + message;
	}
	
}
