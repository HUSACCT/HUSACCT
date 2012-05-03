package husacct.validate.domain.factory.violationtype.java;

import husacct.define.DefineServiceStub;
import husacct.validate.domain.ConfigurationServiceImpl;

import org.apache.log4j.Logger;

public class ViolationTypeFactory {
	private Logger logger = Logger.getLogger(ViolationTypeFactory.class);

	public AbstractViolationType getViolationTypeFactory(ConfigurationServiceImpl configuration){
		//TODO this is temp, because of the implementation of the serviceStub
		String language = new DefineServiceStub().getApplicationDetails().programmingLanguage;	
		return getViolationTypeFactory(language, configuration);
	}

	public AbstractViolationType getViolationTypeFactory(String language, ConfigurationServiceImpl configuration){	

		if(language.equals("Java")){		
			return new JavaViolationTypeFactory(configuration);
		}
		else if(language.equals("C#")){
			return new CSharpViolationTypeFactory(configuration);
		}
		else{
			logger.warn("No programminglanguage defined in the define component");
			return null;
		}
	}
}