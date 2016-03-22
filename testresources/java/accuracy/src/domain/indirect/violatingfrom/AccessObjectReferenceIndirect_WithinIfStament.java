package domain.indirect.violatingfrom;

import domain.indirect.intermediate.ServiceTwo;

public class AccessObjectReferenceIndirect_WithinIfStament {
	
	private ServiceTwo st = new ServiceTwo();

	public AccessObjectReferenceIndirect_WithinIfStament(){
		if (st.getServiceOne() != null) {
			String s = "wrong";
			System.out.println(s);}
	}
}