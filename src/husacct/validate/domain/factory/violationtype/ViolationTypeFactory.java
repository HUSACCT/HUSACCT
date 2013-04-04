package husacct.validate.domain.factory.violationtype;

import husacct.ServiceProvider;
import husacct.define.IDefineService;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;

public class ViolationTypeFactory {
	private final IDefineService defineService = ServiceProvider.getInstance().getDefineService();

	public AbstractViolationType getViolationTypeFactory(ConfigurationServiceImpl configuration) {
		String language = defineService.getApplicationDetails().programmingLanguage;
		return getViolationTypeFactory(language, configuration);
	}

	public AbstractViolationType getViolationTypeFactory(String language, ConfigurationServiceImpl configuration) {
		if (language.toLowerCase().equals("java")) {
			return new JavaViolationTypeFactory(configuration);
		}
		else if (language.toLowerCase().equals("c#")) {
			return new CSharpViolationTypeFactory(configuration);
		}
		else {
			return null;
		}
	}
}