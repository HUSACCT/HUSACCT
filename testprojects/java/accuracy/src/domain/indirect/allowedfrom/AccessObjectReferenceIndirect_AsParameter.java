package domain.indirect.allowedfrom;

import domain.indirect.intermediate.BackgroundService;

public class AccessObjectReferenceIndirect_AsParameter {
	
	private BackgroundService bckg = new BackgroundService();

	public AccessObjectReferenceIndirect_AsParameter(){
		Object o = (Object) bckg.getServiceTwo().getServiceOne();
		System.out.println(o.toString());
	}
}