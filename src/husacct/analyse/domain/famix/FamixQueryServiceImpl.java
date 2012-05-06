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
		Iterator<Entry<String, FamixInterface>> interfaceIterator = theModel.interfaces.entrySet().iterator();
		while(interfaceIterator.hasNext()){
			Entry<String, FamixInterface> currentEntry = (Entry<String, FamixInterface>) interfaceIterator.next();
			FamixInterface fInterface = currentEntry.getValue();
			if(isRootModule(fInterface.uniqueName)){
				String uniqueName = fInterface.uniqueName;
				String name = fInterface.name;
				String type = "interface";
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
		Iterator<Entry<String, FamixInterface>> interfaceIterator = theModel.interfaces.entrySet().iterator();
		while(interfaceIterator.hasNext()){
			Entry<String, FamixInterface> currentEntry = (Entry<String, FamixInterface>) interfaceIterator.next();
			FamixInterface fInterface = currentEntry.getValue();
			if(fInterface.belongsToPackage.equals(from)){
				String uniqueName = fInterface.uniqueName;
				String name = fInterface.name;
				String type = "interface";
				module = new AnalysedModuleDTO(uniqueName, name, type, "");
				if(!analysedModuleAlreadyListed(foundModules, module)) foundModules.add(module);
			}
		}
		return foundModules;
	}

	@Override
	public List<DependencyDTO> getDependencies(String from, String to) {
		List<DependencyDTO> result = new ArrayList<DependencyDTO>();
		List<FamixAssociation> allAssocations = theModel.associations;
		DependencyDTO foundDepency;
		for(FamixAssociation assocation: allAssocations){
			if(assocation.from.startsWith(from)){
				if(assocation.to.startsWith(to)){
					foundDepency = new DependencyDTO(assocation.from, assocation.to, assocation.type, assocation.lineNumber);
					if(!dependencyAlreadyListed(result, foundDepency)){
						result.add(foundDepency);
					}
				}
			}
		}
		return result;
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
	
	public List<AnalysedModuleDTO> searchClassesInPackage(String thePackage, String name){
		List<AnalysedModuleDTO> foundClasses = new ArrayList<AnalysedModuleDTO>();
		AnalysedModuleDTO module;
		Iterator<Entry<String, FamixClass>> classIterator = theModel.classes.entrySet().iterator();
		while(classIterator.hasNext()){
			Entry<String, FamixClass> currentEntry = (Entry<String, FamixClass>) classIterator.next();
			FamixClass fClass = currentEntry.getValue();
			if(fClass.uniqueName.contains(thePackage) && fClass.uniqueName.contains(name)){
				String uniqueName = fClass.uniqueName;
				String nm = fClass.name;
				String type = "class";
				module = new AnalysedModuleDTO(uniqueName, nm, type, "");
				foundClasses.add(module);
			}
		}
		return foundClasses;
	}
	
	public List<FamixImport> searchImportsForClass(String uniqueClassName){
		List<FamixImport> imports = theModel.getImports();
		for(FamixImport anImport: imports){
			if(anImport.from.equals(uniqueClassName)){
				imports.add(anImport);
			}
		}
		return imports;
	}
}
