package domain.indirect.intermediate;

import java.util.Calendar;

import domain.indirect.indirectto.ServiceOne;

public class ServiceTwo{
	public static String sName = "ServiceTwo";
	public String name;
	public Calendar day;
	public ServiceOne serviceOne;
	
	public ServiceTwo() {
		name = "ServiceTwo";
		day = Calendar.getInstance();
	}
	public String getName(){
		return name;
	}
	
	public Calendar getDay(){
		return day;
	}

	public static String getsName(){
		return sName;
	}
	
	public ServiceOne getServiceOne(){
		return serviceOne;
	}
	

}