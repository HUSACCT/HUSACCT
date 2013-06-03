package domain.indirect.indirectto;

import java.util.Calendar;

public class ServiceOne{
	public static String sName = "ServiceOne";
	public String name;
	public Calendar day;
	public POI poi;
	
	public ServiceOne() {
		name = "ServiceOne";
		day = Calendar.getInstance();
	}
	public String getName(){
		return name;
	}
	
	public static String getsName(){
		return sName;
	}
	
		public Calendar getDay(){
		return day;
	}
	
		public POI getPOI(){
			return poi; 
		}
		
}