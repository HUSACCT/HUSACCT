package domain.direct.violating;

import java.util.EnumSet;
import technology.direct.dao.CallInstanceOuterClassDAO;

public class AccessInnerEnumeration {
	public AccessInnerEnumeration(){
		for(String skippedType : EnumSet.allOf(InnerEnumeration.class)){
			if(!skippedType.equals("")){
				String x = skippedType;
			}
		}
	}
}