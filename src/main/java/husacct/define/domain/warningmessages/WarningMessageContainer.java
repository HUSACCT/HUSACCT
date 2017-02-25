package husacct.define.domain.warningmessages;


import husacct.define.task.components.AnalyzedModuleComponent;

import java.util.ArrayList;

import java.util.List;


public class WarningMessageContainer {
	private ArrayList<WarningMessageContainer> warnings= new ArrayList<WarningMessageContainer>();
	private WarningMessage warning;
	
	
	
	
	public WarningMessageContainer(WarningMessage msg) {
		this.warning=msg;
	}
	
	
	public WarningMessageContainer() {
		
	}


	public void addChild(WarningMessageContainer child)
	{
		this.warnings.add(child);
	}
	
	public ArrayList<WarningMessageContainer> getchildren(){
		
		return warnings;
	}
	
	
	
	
	public WarningMessage getvalue(){
		
		return warning;
	}


	public void addChildrens(List<AnalyzedModuleComponent> classes) {
		for (AnalyzedModuleComponent classs: classes){
			this.warnings.add(new WarningMessageContainer(new NotmappedWarningMessage(classs)));
		}
		
	}
	
	
	public int getWarningsCount()
	{
		
		return warnings.size();
	}


	public int getAllWarningsCount() {
		int total =0;
		for (WarningMessageContainer cont : this.getchildren()) {
			total+=cont.getchildren().size();
		}
		
		return total;
		
	}
	
	
}
