package husacct.validate.domain.factory;

import husacct.define.DefineServiceStub;
import husacct.validate.domain.factory.java.JavaViolationTypeFactory;

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