package husacct.define.domain.warningmessages;

import husacct.define.domain.module.Module;

public class ImplementationLevelWarning extends WarningMessage {

    private Module module;

    public ImplementationLevelWarning(Module module) {
	this.module = module;
	generateMessage();
    }

    @Override
    public void generateMessage() {
	description = "A module must be mapped to an implementation unit";
	resource = "Module name: " + module.getName();
	type = "Implentation Level";
	location = "";
    }

    public Module getModule() {
	return module;
    }

    public void setModule(Module module) {
	this.module = module;
    }

}
