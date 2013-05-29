package husacct.define.domain.warningmessages;

import java.util.ArrayList;


import java.util.List;

import husacct.define.domain.services.WarningMessageService;
import husacct.define.domain.services.stateservice.StateService;
import husacct.define.task.components.AnalyzedModuleComponent;

public class WarningMessageFactory {









	public WarningMessageContainer getsortedMessages()
	{
		
		WarningMessageService.getInstance().updateWarnings();
		WarningMessageContainer root = new WarningMessageContainer(new CustomWarningMessage("WARNINGS"));
		WarningMessageContainer codelevelContainer = new WarningMessageContainer(new CustomWarningMessage("Code Level "));
		WarningMessageContainer implevelContainer = new WarningMessageContainer(new CustomWarningMessage("Implementation Level"));
		WarningMessageContainer notMapped = new WarningMessageContainer(new CustomWarningMessage("NotMapped("+StateService.instance().getAnalzedModuleRegistry().getUnitsCount()+")"));
		WarningMessageContainer customContainer = new WarningMessageContainer(new CustomWarningMessage("Custom"));
		addNotmapped(notMapped);
		addNotCodeLevel(codelevelContainer);
		for (WarningMessage message : WarningMessageService.getInstance().getWarningMessages()) {
		
			 if (message instanceof ImplementationLevelWarning) {
				
				implevelContainer.addChild(new WarningMessageContainer(message));
			}
		
		}
		
		root.addChild(implevelContainer);
		root.addChild(codelevelContainer);
		root.addChild(notMapped);
	
		
	return root;
	
	}

	private void addNotmapped(WarningMessageContainer rootOfNotmapped) {
		
		
	List<AnalyzedModuleComponent> classes = new ArrayList<AnalyzedModuleComponent>();
	List<AnalyzedModuleComponent> packages = new ArrayList<AnalyzedModuleComponent>();
	List<AnalyzedModuleComponent> enums = new ArrayList<AnalyzedModuleComponent>();
	List<AnalyzedModuleComponent> interfaces = new ArrayList<AnalyzedModuleComponent>();
	packages=	StateService.instance().getAnalzedModuleRegistry().getAnalyzedUnit("package");
	classes=	StateService.instance().getAnalzedModuleRegistry().getAnalyzedUnit("class");
	interfaces=	StateService.instance().getAnalzedModuleRegistry().getAnalyzedUnit("interface");
	enums=	StateService.instance().getAnalzedModuleRegistry().getAnalyzedUnit("enum");
	WarningMessageContainer classesroot = new WarningMessageContainer(new CustomWarningMessage("Class("+classes.size()+")"));
	WarningMessageContainer packagesroot = new WarningMessageContainer(new CustomWarningMessage("Package("+packages.size()+")"));
	WarningMessageContainer interfaceroot = new WarningMessageContainer(new CustomWarningMessage("Interface("+interfaces.size()+")"));
	WarningMessageContainer enumroot = new WarningMessageContainer(new CustomWarningMessage("Enum("+enums.size()+")"));
	packagesroot.addChildrens(packages);	
	classesroot.addChildrens(classes);
	interfaceroot.addChildrens(interfaces);
	enumroot.addChildrens(enums);
	
	rootOfNotmapped.addChild(packagesroot);
	rootOfNotmapped.addChild(classesroot);
	rootOfNotmapped.addChild(interfaceroot);
	rootOfNotmapped.addChild(enumroot);
	
		
		
	}
	
	private void addNotCodeLevel(WarningMessageContainer rootOfNotmapped)
	{
		for (CodeLevelWarning code: WarningMessageService.getInstance().getNotCodeLevelWarnings()){
			
			rootOfNotmapped.addChild(new WarningMessageContainer(code));
		}
		
	}




}
