package domain.indirect.violatingfrom;

import domain.indirect.intermediate.BackgroundService;
import domain.indirect.intermediate.ServiceTwo;

public class AccessObjectReferenceIndirect_AsParameter {
	
	private BackgroundService bckg = new BackgroundService();
	private ServiceTwo st = new ServiceTwo();

	public AccessObjectReferenceIndirect_AsParameter(){
		Object o = (Object) bckg.getServiceTwo().getServiceOne();
		System.out.println(o.toString());
	}
}