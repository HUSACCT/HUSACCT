package husacct.validate.domain.rulefactory;

import husacct.define.DefineServiceStub;
import husacct.validate.domain.rulefactory.violationtype.java.JavaViolationTypeFactory;
import husacct.validate.domain.rulefactory.violationtypeutil.AbstractViolationType;

public class ViolationTypeFactory {
	public AbstractViolationType getViolationTypeFactory(){
		//TODO this is temp, because of the implementation of the serviceStub
		String language = new DefineServiceStub().getApplicationDetails().programmingLanguage;	
		
		if(language.equals("Java")){		
			return new JavaViolationTypeFactory();
		}
		else{
			return null;
		}
	}
}