package domain.direct.allowed;

import domain.direct.Base;

public class AccessObjectReferenceWithinIfStatement extends Base{
	
	public AccessObjectReferenceWithinIfStatement(){
		if (profileDao != null) {
			String s = "wrong";
			System.out.println(s);
		}
	}
}