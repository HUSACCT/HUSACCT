package husacct.validate.domain.factory.severity;

import husacct.common.dto.ApplicationDTO;
import husacct.define.DefineServiceStub;
import husacct.validate.domain.ConfigurationServiceImpl;
import husacct.validate.domain.exception.SeverityNotFoundException;
import husacct.validate.domain.validation.Severity;

import org.apache.log4j.Logger;

public class SeverityFactory {
	private Logger logger = Logger.getLogger(SeverityFactory.class);

	private final ConfigurationServiceImpl configuration;

	public SeverityFactory(ConfigurationServiceImpl configuration){
		this.configuration = configuration;
	}

	public Severity getSeverity(String key){
		//if there is no language defined in the define component severity will be null
		//TODO replace with serviceProvider
		ApplicationDTO applicationInformation = new DefineServiceStub().getApplicationDetails();
		if(applicationInformation.programmingLanguage == null || applicationInformation.programmingLanguage.isEmpty()){
			//FIXME: throw exception
			//Or implement defaultSeverities?
			return null;
		}
		else{
			return null;
		}
	}

	private Severity getSeverityFromRepository(String severityName){
		try{
			return configuration.getSeverityByName(severityName);
		}catch(SeverityNotFoundException e){
			logger.debug(String.format("No custom severity defined by user with key: %s", severityName), e);
		}
		
		return null;
	}
	
//	private Severity getDefaultSeverity(String key){
//		
//	}
}