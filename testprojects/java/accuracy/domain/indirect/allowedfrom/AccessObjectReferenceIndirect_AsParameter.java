package domain.indirect.allowedfrom;

import domain.indirect.intermediate.BackgroundService;
import domain.indirect.intermediate.ServiceTwo;

public class AccessObjectReferenceIndirect_AsParameter {
	
	private BackgroundService bckg = new BackgroundService();
	private ServiceTwo st = new ServiceTwo();

	public AccessObjectReferenceIndirect_AsParameter(){
		String s = bckg.getServiceOneNameByArgument(st.getServiceOne());
		System.out.println(s);
	}
}