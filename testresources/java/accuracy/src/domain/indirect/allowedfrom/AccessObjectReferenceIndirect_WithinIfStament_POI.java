package domain.indirect.allowedfrom;

import domain.indirect.intermediate.ServiceTwo;

public class AccessObjectReferenceIndirect_WithinIfStament_POI {
	
	private ServiceTwo st = new ServiceTwo();

	public AccessObjectReferenceIndirect_WithinIfStament_POI(){
		if (st.getServiceOne().poi != null) {
			String s = "wrong";
			System.out.println(s);}
	}
}