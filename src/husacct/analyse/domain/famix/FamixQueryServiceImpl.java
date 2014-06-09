package husacct.analyse.domain.famix;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import husacct.analyse.domain.IModelQueryService;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ExternalSystemDTO;

public class FamixQueryServiceImpl implements IModelQueryService {

    private FamixModel theModel;
    private FamixModuleFinder moduleFinder;
    private FamixDependencyFinder dependencyFinder;

    public FamixQueryServiceImpl() {
        this.theModel = FamixModel.getInstance();
        this.moduleFinder = new FamixModuleFinder(theModel);
        this.dependencyFinder = new FamixDependencyFinder(this.theModel);
    }
    
    @Override
    public void buildCache(){
    	dependencyFinder.buildCache();
    	return;
    }

    @Override
    public AnalysedModuleDTO getModuleForUniqueName(String uniquename) {
        return moduleFinder.getModuleForUniqueName(uniquename);
    }

    @Override
    public List<AnalysedModuleDTO> getRootModules() {
        return moduleFinder.getRootModules();
    }

    @Override
    public List<AnalysedModuleDTO> getChildModulesInModule(String from) {
        return moduleFinder.getChildModulesInModule(from);
    }

    @Override
    public AnalysedModuleDTO[] getChildModulesInModule(String from, int depth) {
        List<AnalysedModuleDTO> moduleList = moduleFinder.getChildModulesInModule(from, depth);
        return moduleList.toArray(new AnalysedModuleDTO[moduleList.size()]);
    }

    @Override
    public AnalysedModuleDTO getParentModuleForModule(String child) {
        return moduleFinder.getParentModuleForModule(child);
    }

    @Override
    public DependencyDTO[] getAllDependencies() {
        List<DependencyDTO> result = dependencyFinder.getAllDependencies();
        DependencyDTO[] allDependencies = result.toArray(new DependencyDTO[result.size()]);
        return allDependencies;
    }

    @Override
    public List<DependencyDTO> getDependencies(String from, String to) {
    	List<String> allFromTypeNames = getAllPhysicalClassPathsOfSoftwareUnit(from);
    	List<String> allToTypeNames = getAllPhysicalClassPathsOfSoftwareUnit(to);
        List<DependencyDTO> dependencies = new ArrayList<DependencyDTO>();
        for (String fromTypeName : allFromTypeNames) {
            for (String toTypeName : allToTypeNames) {
                for (DependencyDTO dependency : dependencyFinder.getDependenciesFromTo(fromTypeName, toTypeName)) {
                    if (!dependencies.contains(dependency)) {
                        dependencies.add(dependency);
                    }
                }
            }
        }
        return dependencies;
    }
    
    // Returns unique names of all types (classes, interfaces, inner classes) of SoftwareUnit with uniqueName  
    @Override
    public List<String> getAllPhysicalClassPathsOfSoftwareUnit(String uniqueName){
		List<String> uniqueNamesAllFoundTypes = new ArrayList<String>();
		//Determine if uniqueName is a packages or type. If it is a packages, get all sub-packages.
		if (theModel.packages.containsKey(uniqueName)){
			List<AnalysedModuleDTO> foundTypes = new ArrayList<AnalysedModuleDTO>();
			Set<String> allPackages = theModel.packages.keySet();
			for (String packageName : allPackages){
				if (packageName.startsWith(uniqueName)){
					//get all types within the package
					foundTypes.addAll(getChildModulesInModule(packageName));
				}
			}
			// Add unique names of the types to the result set
			for (AnalysedModuleDTO typeFrom : foundTypes){
				if ((typeFrom != null) && (!typeFrom.uniqueName.equals("")) && (!typeFrom.type.toLowerCase().equals("package"))){ 
					String uniqueNameTypeFrom = typeFrom.uniqueName;
					uniqueNamesAllFoundTypes.add(uniqueNameTypeFrom);
				} 
			}
		}
		else {
			// Since uniqueName refers to a type already, add it to uniqueNamesTypesFrom  
			if (theModel.libraries.containsKey(uniqueName)){
				uniqueName = theModel.libraries.get(uniqueName).physicalPath;
			}
			uniqueNamesAllFoundTypes.add(uniqueName);
		}
		// Add inner classes to the result set
		List<String> innerClassNames = new ArrayList<String>();
		for (String name : uniqueNamesAllFoundTypes){
			if (theModel.classes.containsKey(name)){
				FamixClass selectedClass = theModel.classes.get(name);
				if (selectedClass.hasInnerClasses){
					innerClassNames.addAll(selectedClass.children);
				}
			}
		}
		uniqueNamesAllFoundTypes.addAll(innerClassNames);
		return uniqueNamesAllFoundTypes;
    }

    @Override
    public DependencyDTO[] getDependencies(String from, String to, String[] dependencyFilter) {
        List<DependencyDTO> result = dependencyFinder.getDependencies(from, to, dependencyFilter);
        DependencyDTO[] allDependencies = result.toArray(new DependencyDTO[result.size()]);
        return allDependencies;
    }

    @Override
    public List<DependencyDTO> getDependenciesFrom(String from) {
        return dependencyFinder.getDependenciesFrom(from);
    }

    @Override
    public DependencyDTO[] getDependenciesFrom(String from, String[] dependencyFilter) {
        List<DependencyDTO> result = dependencyFinder.getDependenciesFrom(from, dependencyFilter);
        DependencyDTO[] allDependencies = result.toArray(new DependencyDTO[result.size()]);
        return allDependencies;
    }

    @Override
	public DependencyDTO[] getDependenciesFromTo(String classPathFrom, String classPathTo){
		return dependencyFinder.getDependenciesFromTo(classPathFrom, classPathTo);
	}
	
    @Override
    public List<DependencyDTO> getDependenciesTo(String to) {
        return dependencyFinder.getDependenciesTo(to);
    }

    @Override
    public DependencyDTO[] getDependenciesTo(String to, String[] dependencyFilter) {
        List<DependencyDTO> result = dependencyFinder.getDependenciesTo(to, dependencyFilter);
        DependencyDTO[] allDependencies = result.toArray(new DependencyDTO[result.size()]);
        return allDependencies;
    }

    @Override
    public HashMap<String, DependencyDTO> mapDependencies() {
        List<DependencyDTO> cache = this.getDependencies("", "");
        HashMap<String, DependencyDTO> dependencyMap = new HashMap<String, DependencyDTO>();
        //TODO Analyse Persistency to file - Do Sorting of the list here!
        int counter = 0;
        for (DependencyDTO dependency : cache) {
            dependencyMap.put("" + counter, dependency);
            counter++;
        }
        return dependencyMap;
    }
    
    public int getAmountOfDependencies() {
    	return getAllDependencies().length;
    }
    
    public int getAmountOfInterfaces() {
    	return theModel.interfaces.size();
    }
    
    public int getAmountOfPackages() {
    	return theModel.packages.size();
    }
    
    public int getAmountOfClasses() {
    	return theModel.classes.size();
    }
}
