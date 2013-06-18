package domain.indirect.violatingfrom;

import domain.indirect.intermediate.BackgroundService;
import domain.indirect.intermediate.ServiceTwo;

public class AccessObjectReferenceIndirect_AsParameter_POI {
	
	private BackgroundService bckg = new BackgroundService();
	private ServiceTwo st = new ServiceTwo();

	public AccessObjectReferenceIndirect_AsParameter_POI(){
		String s = bckg.getPOINameByArgument(st.getServiceOne().poi);
		System.out.println(s);
	}
}