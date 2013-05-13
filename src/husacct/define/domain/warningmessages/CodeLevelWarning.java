package husacct.define.domain.warningmessages;


import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.module.Module;
import husacct.define.task.components.AnalyzedModuleComponent;

public class CodeLevelWarning  extends WarningMessage{

	private Long moduldeId;
	private AnalyzedModuleComponent notCodeLevelModule;
	
	
	public CodeLevelWarning(Long id, AnalyzedModuleComponent notcodelevelmodule)
	{
		this.moduldeId=id;
		this.notCodeLevelModule=notcodelevelmodule;
		generateMessage();
	}
	
	
	@Override
public	void generateMessage() {
		this.description="your mapped unit does not exist at code level";
		Module module = SoftwareArchitecture.getInstance().getModuleById(moduldeId);
		this.resource="Module name: "+module.getName()+" Unit name: "+notCodeLevelModule.getUniqueName();
		this.location="";
		this.type="CodeLevel";
		
	}


	
	
	public Long getModuldeId() {
		return moduldeId;
	}


	public AnalyzedModuleComponent getNotCodeLevelModule() {
		return notCodeLevelModule;
	}
	
	

}
