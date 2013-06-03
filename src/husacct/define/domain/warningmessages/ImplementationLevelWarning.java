package husacct.define.domain.warningmessages;

import husacct.define.domain.module.ModuleStrategy;

import java.util.Observable;



public class ImplementationLevelWarning extends WarningMessage {

	private ModuleStrategy module;

	public ModuleStrategy getModule() {
		return module;
	}

	public void setModule(ModuleStrategy module) {
		this.module = module;
	}

	public ImplementationLevelWarning(ModuleStrategy module) {
		this.module = module;
		generateMessage();
	}

	@Override
	public void generateMessage() {
		this.description = "A module must be mapped to an implementation unit";
		this.resource = "Module name: " + module.getName();
		this.type = "Implentation Level";
		this.location = "";

	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object[] getValue() {
		// TODO Auto-generated method stub
		return new Object[]{module};
	}
}