package husacct.validate.domain.check.util;

import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.validation.Severity;

public class CheckConformanceUtilSeverity {
	public static Severity getSeverity(ConfigurationServiceImpl configuration, Severity ruleTypeSeverity, Severity violationTypeSeverity){
		if(violationTypeSeverity == null && ruleTypeSeverity == null){
			return null;
		}

		int ruleTypeValue = -1;
		int violationTypeValue = -1;

		if(ruleTypeSeverity != null){
			ruleTypeValue = configuration.getSeverityValue(ruleTypeSeverity);
		}
		if(violationTypeSeverity != null){
			violationTypeValue = configuration.getSeverityValue(violationTypeSeverity);
		}


		if(ruleTypeValue == -1 && violationTypeValue != -1){
			return violationTypeSeverity;
		}
		else if(ruleTypeValue != -1 && violationTypeValue == -1){
			return ruleTypeSeverity;
		}
		else if(ruleTypeValue != -1 && violationTypeValue != -1){
			if(ruleTypeValue <= violationTypeValue){
				return ruleTypeSeverity;
			}
			else{
				return violationTypeSeverity;
			}
		}
		else{
			return null;
		}		
	}
}