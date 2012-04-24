package husacct.validate.domain.rulefactory;

import husacct.define.DefineServiceStub;
import husacct.validate.domain.rulefactory.violationtype.java.JavaViolationTypeFactory;
import husacct.validate.domain.rulefactory.violationtypeutil.AbstractViolationType;

import org.apache.log4j.Logger;

public class ViolationTypeFactory {
	private Logger logger = Logger.getLogger(ViolationTypeFactory.class);
	public AbstractViolationType getViolationTypeFactory(){		
		
		//TODO this is temp, because of the implementation of the serviceStub
		String language = new DefineServiceStub().getApplicationDetails().programmingLanguage;	
		
		if(language.equals("Java")){		
			return new JavaViolationTypeFactory();
		}
		else{
			logger.warn("No programminglanguage defined in the define component");
			return null;
		}
	}
}