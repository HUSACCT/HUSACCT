package husacct.validate.domain.factory.violationtype.java;

import husacct.define.DefineServiceStub;

import org.apache.log4j.Logger;

public class ViolationTypeFactory {
	private Logger logger = Logger.getLogger(ViolationTypeFactory.class);

	public AbstractViolationType getViolationTypeFactory(){
		//TODO this is temp, because of the implementation of the serviceStub
		String language = new DefineServiceStub().getApplicationDetails().programmingLanguage;	
		return getViolationTypeFactory(language);
	}

	public AbstractViolationType getViolationTypeFactory(String language){	

		if(language.equals("Java")){		
			return new JavaViolationTypeFactory();
		}
		else if(language.equals("C#")){
			return new CSharpViolationTypeFactory();
		}
		else{
			logger.warn("No programminglanguage defined in the define component");
			return null;
		}
	}
}