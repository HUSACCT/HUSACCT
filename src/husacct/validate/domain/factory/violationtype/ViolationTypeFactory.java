package husacct.validate.domain.factory.violationtype;

import husacct.ServiceProvider;
import husacct.define.IDefineService;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;

public class ViolationTypeFactory {

	private final IDefineService defineService = ServiceProvider.getInstance().getDefineService();

	public AbstractViolationType getViolationTypeFactory(ConfigurationServiceImpl configuration) {
		String language = defineService.getApplicationDetails().projects.get(0).programmingLanguage;
		return getViolationTypeFactory(language, configuration);
	}

	public AbstractViolationType getViolationTypeFactory(String language, ConfigurationServiceImpl configuration) {
		if (language.toLowerCase().equals("java") || language.toLowerCase().equals("c#")) {
			return new ConcreteViolationTypeFactory(configuration, language);
		} else {
			return null;
		}
	}
}