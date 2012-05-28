package husacct.analyse.domain.famix;

import husacct.common.dto.AnalysedModuleDTO;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

class FamixModuleFinder extends FamixFinder{
	
	private static enum FinderArguments{ROOT, CHILDS, PARENT, FETCH};
	private String currentArgument;
	private FinderArguments currentFunction = FinderArguments.ROOT;
	private List<AnalysedModuleDTO> currentResult;

	public FamixModuleFinder(FamixModel model) {
		super(model);
		this.currentResult = new ArrayList<AnalysedModuleDTO>();
		this.currentArgument = "";
	}
	
	public AnalysedModuleDTO getModuleForUniqueName(String uniquename){
		this.performQuery(FinderArguments.FETCH, uniquename);
		if(this.currentResult.isEmpty()) return new AnalysedModuleDTO("", "", "", "");
		else return currentResult.get(0);
	}
	
	public List<AnalysedModuleDTO> getRootModules() {
		this.performQuery(FinderArguments.ROOT);
		return this.currentResult;
	}
	
	public List<AnalysedModuleDTO> getChildModulesInModule(String module){
		this.performQuery(FinderArguments.CHILDS, module);
		return this.currentResult;
	}
	
	public List<AnalysedModuleDTO> getChildModulesInModule(String module, int depth){
		this.performQuery(FinderArguments.CHILDS, module, depth);
		return this.currentResult;
	}
	
	public AnalysedModuleDTO getParentModuleForModule(String module){
		this.performQuery(FinderArguments.PARENT, module);
		if(this.currentResult.isEmpty()) return new AnalysedModuleDTO("", "", "", "");
		else return currentResult.get(0);
	}
	
	private void performQuery(FinderArguments function){
		performQuery(function, "", 0);
	}
	
	private void performQuery(FinderArguments function, String argument){
		performQuery(function, argument, 0);
	}
	
	private void performQuery(FinderArguments function, String argument, int depth){
		this.prepare(function, argument);
		List<AnalysedModuleDTO> result = new ArrayList<AnalysedModuleDTO>();
		result.addAll(this.findPackages());
		result.addAll(this.findClasses());
		result.addAll(this.findInterfaces());
		if(depth > 0){
			for(AnalysedModuleDTO moduleDTO : result){
				moduleDTO.subModules.addAll(getChildModulesInModule(moduleDTO.uniqueName, depth -1));
			}
		}
		this.currentResult = result;
	}
	
	private void prepare(FinderArguments function, String argument){
		this.currentResult.clear();
		this.currentFunction = function;
		this.currentArgument = argument;
	}
	
	private List<AnalysedModuleDTO> findPackages(){
		List<AnalysedModuleDTO> result = new ArrayList<AnalysedModuleDTO>();
		Iterator<Entry<String, FamixPackage>> iterator = theModel.packages.entrySet().iterator();
		AnalysedModuleDTO current;
		while(iterator.hasNext()){
			Entry<String, FamixPackage> currentEntry = (Entry<String, FamixPackage>)iterator.next();
			FamixPackage fPackage = currentEntry.getValue();
			boolean correctResult = this.compliesWithArguments(fPackage.uniqueName);
			if(correctResult){
				current = createAnalysedModuleDTO("package", fPackage);
				result.add(current);
			}
		}
		return result;
	}
	
	private List<AnalysedModuleDTO> findClasses(){
		List<AnalysedModuleDTO> result = new ArrayList<AnalysedModuleDTO>();
		Iterator<Entry<String, FamixClass>> iterator = theModel.classes.entrySet().iterator();
		AnalysedModuleDTO current;
		while(iterator.hasNext()){
			Entry<String, FamixClass> currentEntry = (Entry<String, FamixClass>)iterator.next();
			FamixClass fClass = currentEntry.getValue();
			boolean correctResult = this.compliesWithArguments(fClass.uniqueName);
			if(correctResult){
				current = createAnalysedModuleDTO("class", fClass);
				result.add(current);
			}
		}
		return result;
	}
	
	private List<AnalysedModuleDTO> findInterfaces(){
		List<AnalysedModuleDTO> result = new ArrayList<AnalysedModuleDTO>();
		Iterator<Entry<String, FamixInterface>> iterator = theModel.interfaces.entrySet().iterator();
		AnalysedModuleDTO current;
		while(iterator.hasNext()){
			Entry<String, FamixInterface> currentEntry = (Entry<String, FamixInterface>)iterator.next();
			FamixInterface fInterface = currentEntry.getValue();
			boolean correctResult = this.compliesWithArguments(fInterface.uniqueName);
			if(correctResult){
				current = createAnalysedModuleDTO("interface", fInterface);
				result.add(current);
			}
		}
		return result;
	}
		
	private boolean compliesWithArguments(String uniqueName){
		switch (this.currentFunction){
		case ROOT: return isRootModule(uniqueName);
		case CHILDS: return isChild(uniqueName, this.currentArgument);
		case PARENT: return isParent(uniqueName, this.currentArgument);
		case FETCH: return uniqueName.equals(this.currentArgument);
		}
		return false;
	}
	
	private boolean isRootModule(String uniqueName){
		int seperatorCount = uniqueName.split("\\.").length;
		return seperatorCount <= 1;
	}
	
	private boolean isChild(String child, String parent){
		if(isRootModule(child)) return false;
		String currentParent = child.substring(0, child.lastIndexOf("."));
		return parent.equals(currentParent);
	}
	
	private boolean isParent(String parent, String child){
		if(!moduleExists(child)) return false;
		String name =  child.substring(child.lastIndexOf(".") + 1, child.length());
		String currentChild = parent + "." + name;
		return currentChild.equals(child);
	}
	
	private AnalysedModuleDTO createAnalysedModuleDTO(String type, FamixEntity theEntity){
		List<AnalysedModuleDTO> emptyList = new ArrayList<AnalysedModuleDTO>();
		return createAnalysedModuleDTO(type, theEntity, emptyList);
	}
	
	private boolean moduleExists(String uniqueName){
		if(theModel.classes.get(uniqueName) != null) return true;
		if(theModel.packages.get(uniqueName) != null) return true;
		if(theModel.interfaces.get(uniqueName) != null) return true;
		return false;
	}
	
	private AnalysedModuleDTO createAnalysedModuleDTO(String type, FamixEntity theEntity, List<AnalysedModuleDTO> childs){
		String name = theEntity.name;
		String uniqueName = theEntity.uniqueName;
		if(theEntity instanceof FamixPackage) return new AnalysedModuleDTO(uniqueName, name, type, "public");
		else return new AnalysedModuleDTO(uniqueName, name, type, theEntity.visibility);
	}
}
