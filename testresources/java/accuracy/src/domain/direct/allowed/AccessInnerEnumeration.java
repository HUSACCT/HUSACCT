package domain.direct.allowed;

import java.util.EnumSet;

import technology.direct.dao.CallInstanceOuterClassDAO;
import technology.direct.dao.CallInstanceOuterClassDAO.InnerEnumeration;

public class AccessInnerEnumeration {
	public AccessInnerEnumeration(){
		for(InnerEnumeration skippedType : EnumSet.allOf(InnerEnumeration.class)){
			if(!skippedType.equals("")){
				InnerEnumeration x = skippedType;
			}
		}
	}
}