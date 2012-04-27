package husacct.validate.domain.factory.severity;

import husacct.common.dto.ApplicationDTO;
import husacct.define.DefineServiceStub;
import husacct.validate.domain.validation.Severity;

public class SeverityFactory {

	
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
}
