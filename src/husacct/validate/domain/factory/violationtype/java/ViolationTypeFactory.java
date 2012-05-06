package husacct.validate.domain.factory.violationtype.java;

import husacct.ServiceProvider;
import husacct.define.IDefineService;
import husacct.validate.domain.ConfigurationServiceImpl;

import org.apache.log4j.Logger;

public class ViolationTypeFactory {
	private Logger logger = Logger.getLogger(ViolationTypeFactory.class);
	private final IDefineService defineService = ServiceProvider.getInstance().getDefineService();
	
	public AbstractViolationType getViolationTypeFactory(ConfigurationServiceImpl configuration){
		String language = defineService.getApplicationDetails().programmingLanguage;	
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