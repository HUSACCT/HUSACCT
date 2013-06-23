package husacct.define.domain.warningmessages;

import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.util.Observable;

public class CodeLevelWarning extends WarningMessage {

	private Long moduldeId;
	private AnalyzedModuleComponent notCodeLevelModule;

	public CodeLevelWarning(Long id, AnalyzedModuleComponent notcodelevelmodule) {
		this.moduldeId = id;
		this.notCodeLevelModule = notcodelevelmodule;
		generateMessage();
	}
	public CodeLevelWarning(AnalyzedModuleComponent notcodelevelmodule) {
		
		this.notCodeLevelModule = notcodelevelmodule;
		generateMessage();
	}


	@Override
	public void generateMessage() {
		this.description = "your mapped unit does not exist at code level";
		
		this.resource =  " Unit name: "
				+ notCodeLevelModule.getUniqueName();
		this.location = "";
		this.type = "CodeLevel";

	}

	public Long getModuldeId() {
		return moduldeId;
	}

	public AnalyzedModuleComponent getNotCodeLevelModule() {
		return notCodeLevelModule;
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object[] getValue() {
		// TODO Auto-generated method stub
		return new Object[]{moduldeId,notCodeLevelModule};
	}
}