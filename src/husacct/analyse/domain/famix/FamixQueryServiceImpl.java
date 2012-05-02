package husacct.analyse.domain.famix;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import husacct.analyse.domain.ModelQueryService;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;

public class FamixQueryServiceImpl implements ModelQueryService{

	private FamixModel theModel;
	
	public FamixQueryServiceImpl(){
		theModel = FamixModel.getInstance();
	}
	
	@Override
	public List<AnalysedModuleDTO> getRootModules() {
		List<AnalysedModuleDTO> rootModules = new ArrayList<AnalysedModuleDTO>();
		Iterator<Entry<String, FamixPackage>> packageIterator = theModel.packages.entrySet().iterator();
		AnalysedModuleDTO rootModuleDTO;
		while(packageIterator.hasNext()){
			Entry<String, FamixPackage> currentEntry = (Entry<String, FamixPackage>)packageIterator.next();
			FamixPackage fPackage = currentEntry.getValue();
			if(isRootModule(fPackage.uniqueName)){
				String uniqueName = fPackage.uniqueName;
				String name = fPackage.name;
				String type = "package";
				//FIXME AnalysedModuleDTO Constructor not correct!
				rootModuleDTO = new AnalysedModuleDTO(uniqueName,name,type, "");
				rootModules.add(rootModuleDTO);
			}
		}
		Iterator<Entry<String, FamixClass>> classIterator = theModel.classes.entrySet().iterator();
		while(classIterator.hasNext()){
			Entry<String, FamixClass> currentEntry = (Entry<String, FamixClass>) classIterator.next();
			FamixClass fClass = currentEntry.getValue();
			if(isRootModule(fClass.uniqueName)){
				String uniqueName = fClass.uniqueName;
				String name = fClass.name;
				String type = "class";
				rootModuleDTO = new AnalysedModuleDTO(uniqueName, name, type, "");
				rootModules.add(rootModuleDTO);
			}
		}
		return rootModules;
	}
	
	private boolean isRootModule(String uniqueName){
		int seperatorCount = uniqueName.split("\\.").length;
		return seperatorCount <= 1;
	}

	@Override
	public List<AnalysedModuleDTO> getChildModulesInModule(String from) {
		List<AnalysedModuleDTO> foundModules = new ArrayList<AnalysedModuleDTO>();
		AnalysedModuleDTO module;
		Iterator<Entry<String, FamixPackage>> packageIterator = theModel.packages.entrySet().iterator();
		Iterator<Entry<String, FamixClass>> classIterator = theModel.classes.entrySet().iterator();
		while(packageIterator.hasNext()){
			Entry<String, FamixPackage> currentEntry = (Entry<String, FamixPackage>)packageIterator.next();
			FamixPackage fPackage = currentEntry.getValue();
			if(fPackage.belongsToPackage.equals(from)){
				String uniqueName = fPackage.uniqueName;
				String name = fPackage.name;
				String type = "package";
				module = new AnalysedModuleDTO(uniqueName,name,type, "");
				if(!analysedModuleAlreadyListed(foundModules, module)) foundModules.add(module);
			}
		}
		while(classIterator.hasNext()){
			Entry<String, FamixClass> currentEntry = (Entry<String, FamixClass>) classIterator.next();
			FamixClass fClass = currentEntry.getValue();
			if((fClass.isInnerClass && fClass.belongsToClass.equals(from)) || fClass.belongsToPackage.equals(from)){
				String uniqueName = fClass.uniqueName;
				String name = fClass.name;
				String type = "class";
				module = new AnalysedModuleDTO(uniqueName, name, type, "");
				if(!analysedModuleAlreadyListed(foundModules, module)) foundModules.add(module);
			}
		}
		return foundModules;
	}

	@Override
	public List<DependencyDTO> getDependencies(String from, String to) {
		ArrayList<DependencyDTO> allDependencies = new ArrayList<DependencyDTO>(); 
		ArrayList<FamixAssociation> AssociationIterator = theModel.associations ; 
		for(FamixAssociation currentAssociation: AssociationIterator){ 
			if(currentAssociation.from.startsWith(from)){ 
				if(currentAssociation.to.startsWith(to)){
					DependencyDTO found = new DependencyDTO(currentAssociation.from,currentAssociation.to, currentAssociation.type,currentAssociation.lineNumber);
					if(!dependencyAlreadyListed(allDependencies, found)) allDependencies.add(found);
				}
 			} 
		} 
		return allDependencies;
	}
	
	private boolean dependencyAlreadyListed(List<DependencyDTO> dependencies, DependencyDTO dependency){
		boolean result = false;
		for(DependencyDTO item: dependencies){
			if(item.equals(dependency)) result = true;
		}
		return result;
	}
	
	private boolean analysedModuleAlreadyListed(List<AnalysedModuleDTO> modules, AnalysedModuleDTO module){
		boolean result = false;
		for(AnalysedModuleDTO item: modules){
			if(item.equals(module)) result = true;
		}
		return result;
	}
}
