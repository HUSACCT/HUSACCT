package husacct.analyse.domain.famix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import husacct.analyse.domain.IModelQueryService;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.AnalysisStatisticsDTO;
import husacct.common.dto.DependencyDTO;

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
    public String getSourceFilePathOfClass(String uniquename) {
    	String returnValue = "";
    	if (theModel.classes.containsKey(uniquename)) {
    		returnValue = theModel.classes.get(uniquename).sourceFilePath;
    	}
    	return returnValue;
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
    	TreeSet<String> allFromTypeNames = getAllPhysicalClassPathsOfSoftwareUnit(from);
    	TreeSet<String> allToTypeNames = getAllPhysicalClassPathsOfSoftwareUnit(to);
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
    
    // Returns unique names of all types (classes, interfaces, inner classes) within the SoftwareUnit with uniqueName  
    @Override
    public TreeSet<String> getAllPhysicalClassPathsOfSoftwareUnit(String uniqueName){
		TreeSet<String> uniqueNamesAllFoundTypes = new TreeSet<String>();
		if (!theModel.packages.containsKey(uniqueName)) { // Add only classes and libraries
			uniqueNamesAllFoundTypes.add(uniqueName);
		}
		TreeSet<String> children = (moduleFinder.getChildModulesNamesInModule(uniqueName));
    	if ((children != null) && (children.size() > 0)){
	    	for (String child : children){
	    		TreeSet<String> validChildName = getAllPhysicalClassPathsOfSoftwareUnit(child);
	    		uniqueNamesAllFoundTypes.addAll(validChildName);
	    	}
    	}
		return uniqueNamesAllFoundTypes;
    }

    // Returns unique names of all packages with uniqueName or within this SoftwareUnit  
    @Override
    public TreeSet<String> getAllPhysicalPackagePathsOfSoftwareUnit(String uniqueName){
		TreeSet<String> uniqueNamesAllFoundPackages = new TreeSet<String>();
		if (theModel.packages.containsKey(uniqueName)) { // Add only packages
			uniqueNamesAllFoundPackages.add(uniqueName);
		}
		TreeSet<String> children = (moduleFinder.getChildModulesNamesInModule(uniqueName));
    	if ((children != null) && (children.size() > 0)){
	    	for (String child : children){
	    		TreeSet<String> validChildName = getAllPhysicalPackagePathsOfSoftwareUnit(child);
	    		uniqueNamesAllFoundPackages.addAll(validChildName);
	    	}
    	}
		return uniqueNamesAllFoundPackages;
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
    
    @Override
	public AnalysisStatisticsDTO getAnalysisStatistics(AnalysedModuleDTO selectedModule) {
		AnalysisStatisticsDTO returnValue;
		if (selectedModule == null) {
			returnValue = new AnalysisStatisticsDTO(theModel.packages.size(), theModel.classes.size(), theModel.getTotalNumberOfLinesOfCode(), getAllDependencies().length, 0, 0, 0);
		} else {
			int packages = 0;
			int classes = 0;
			int linesOfCode = 0;
			if (selectedModule.type.equals("package")) {
				if (theModel.packages.containsKey(selectedModule.uniqueName)) {
					packages = getAllPhysicalPackagePathsOfSoftwareUnit(selectedModule.uniqueName).size();
					TreeSet<String> classesSet = getAllPhysicalClassPathsOfSoftwareUnit(selectedModule.uniqueName);
					classes = classesSet.size();
					for (String typeName : classesSet) {
						if (theModel.classes.containsKey(typeName)) {
							FamixClass selected = theModel.classes.get(typeName);
							linesOfCode = linesOfCode + selected.linesOfCode;
						}
					}
				}
			} else if (selectedModule.type.equals("class") || selectedModule.type.equals("interface")) {
				if (theModel.classes.containsKey(selectedModule.uniqueName)) {
					FamixClass selected = theModel.classes.get(selectedModule.uniqueName);
					linesOfCode = selected.linesOfCode;
					if (selected.hasInnerClasses) {
						TreeSet<String> classesSet = getAllPhysicalClassPathsOfSoftwareUnit(selectedModule.uniqueName);
						classes = classesSet.size();
					} else {
						classes = 1;
					}
				}
				packages = 0;
			} else {
				// A library is selected, so return default 0-values.
			}
			returnValue = new AnalysisStatisticsDTO(theModel.packages.size(), theModel.classes.size(), theModel.getTotalNumberOfLinesOfCode(), getAllDependencies().length, packages, classes, linesOfCode);
		}
        return returnValue;
    }

}
