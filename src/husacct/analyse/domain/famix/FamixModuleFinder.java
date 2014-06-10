package husacct.analyse.domain.famix;

import husacct.common.dto.AnalysedModuleDTO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.Map.Entry;

class FamixModuleFinder extends FamixFinder {

    private static enum FinderArguments {
        ROOT, CHILDS, PARENT, FETCH
    };
    private String currentArgument;
    private FinderArguments currentFunction = FinderArguments.ROOT;
    private List<AnalysedModuleDTO> currentResult;

    public FamixModuleFinder(FamixModel model) {
        super(model);
        this.currentResult = new ArrayList<AnalysedModuleDTO>();
        this.currentArgument = "";
    }

    public AnalysedModuleDTO getModuleForUniqueName(String uniquename) {
    	if (theModel.packages.containsKey(uniquename)) {
    		return createAnalysedModuleDTO("package", theModel.packages.get(uniquename));
    	} else if (theModel.classes.containsKey(uniquename)){
    		return createAnalysedModuleDTO("class", theModel.classes.get(uniquename));
    	} else if (theModel.interfaces.containsKey(uniquename)){
    		return createAnalysedModuleDTO("interface", theModel.interfaces.get(uniquename));
    	} else if (theModel.libraries.containsKey(uniquename)){
    		return createAnalysedModuleDTO("library", theModel.libraries.get(uniquename));
    	}
    	else {
            return new AnalysedModuleDTO("", "", "", "");
        } 
    }

    public List<AnalysedModuleDTO> getRootModules() {
        this.performQuery(FinderArguments.ROOT);
        return this.currentResult;
    }

    public List<AnalysedModuleDTO> getChildModulesInModule(String module) {
    	List<AnalysedModuleDTO> result = new ArrayList<AnalysedModuleDTO>();
    	TreeSet<String> children = null;
        // New finder function, based on FamixDecompositionEntity
    	if (theModel.packages.containsKey(module)){
    		FamixPackage selected = theModel.packages.get(module);
    		children = theModel.packages.get(module).children;
    	} else if (theModel.classes.containsKey(module)){
    		children = theModel.classes.get(module).children;
    	}
    	AnalysedModuleDTO current;
    	if (children != null){
	    	for (String child : children){
    			if (theModel.packages.containsKey(child)){
	    			current = createAnalysedModuleDTO("package", theModel.packages.get(child));
	    			result.add(current);
	    		}
    			if (theModel.classes.containsKey(child)){
	    			current = createAnalysedModuleDTO("class", theModel.classes.get(child));
	    			result.add(current);
	    		} else if (theModel.interfaces.containsKey(child)){
	    			current = createAnalysedModuleDTO("interface", theModel.interfaces.get(child));
	    			result.add(current);
	    		} else if (theModel.libraries.containsKey(child)){
	    			current = createAnalysedModuleDTO("library", theModel.libraries.get(child));
	    			result.add(current);
	    		}
	    	}
    	}
    	return result;
    }

    public List<AnalysedModuleDTO> getChildModulesInModule(String module, int depth) {
        this.performQuery(FinderArguments.CHILDS, module, depth);
        return this.currentResult;
    }

    public AnalysedModuleDTO getParentModuleForModule(String module) {
        this.performQuery(FinderArguments.PARENT, module);
        if (this.currentResult.isEmpty()) {
            return new AnalysedModuleDTO("", "", "", "");
        } else {
            return currentResult.get(0);
        }
    }

    private void performQuery(FinderArguments function) {
        performQuery(function, "", 0);
    }

    private void performQuery(FinderArguments function, String argument) {
        performQuery(function, argument, 0);
    }

    private void performQuery(FinderArguments function, String argument, int depth) {
    	this.prepare(function, argument);
        List<AnalysedModuleDTO> result = new ArrayList<AnalysedModuleDTO>();
        result.addAll(this.findPackages());
        result.addAll(this.findClasses());
        result.addAll(this.findInterfaces());
        result.addAll(this.findLibraries());
        if (depth > 0) {
            for (AnalysedModuleDTO moduleDTO : result) {
                moduleDTO.subModules.addAll(getChildModulesInModule(moduleDTO.uniqueName, depth - 1));
            }
        }
        this.currentResult = result;
    }

    private void prepare(FinderArguments function, String argument) {
        this.currentResult.clear();
        this.currentFunction = function;
        this.currentArgument = argument;
    }

    private List<AnalysedModuleDTO> findPackages() {
        List<AnalysedModuleDTO> result = new ArrayList<AnalysedModuleDTO>();
        Iterator<Entry<String, FamixPackage>> iterator = theModel.packages.entrySet().iterator();
        AnalysedModuleDTO current;
        while (iterator.hasNext()) {
            Entry<String, FamixPackage> currentEntry = (Entry<String, FamixPackage>) iterator.next();
            FamixPackage fPackage = currentEntry.getValue();
            boolean correctResult = this.compliesWithArguments(fPackage.uniqueName);
            if (correctResult) {
                current = createAnalysedModuleDTO("package", fPackage);
                result.add(current);
            }
        }
        return result;
    }

    private List<AnalysedModuleDTO> findClasses() {
        List<AnalysedModuleDTO> result = new ArrayList<AnalysedModuleDTO>();
        Iterator<Entry<String, FamixClass>> iterator = theModel.classes.entrySet().iterator();
        AnalysedModuleDTO current;
        while (iterator.hasNext()) {
            Entry<String, FamixClass> currentEntry = (Entry<String, FamixClass>) iterator.next();
            FamixClass fClass = currentEntry.getValue();
            boolean correctResult = this.compliesWithArguments(fClass.uniqueName);
            if (correctResult) {
                current = createAnalysedModuleDTO("class", fClass);
                result.add(current);
            }
        }
        return result;
    }

    private List<AnalysedModuleDTO> findInterfaces() {
        List<AnalysedModuleDTO> result = new ArrayList<AnalysedModuleDTO>();
        Iterator<Entry<String, FamixInterface>> iterator = theModel.interfaces.entrySet().iterator();
        AnalysedModuleDTO current;
        while (iterator.hasNext()) {
            Entry<String, FamixInterface> currentEntry = (Entry<String, FamixInterface>) iterator.next();
            FamixInterface fInterface = currentEntry.getValue();
            boolean correctResult = this.compliesWithArguments(fInterface.uniqueName);
            if (correctResult) {
                current = createAnalysedModuleDTO("interface", fInterface);
                result.add(current);
            }
        }
        return result;
    }

    private List<AnalysedModuleDTO> findLibraries() {
        List<AnalysedModuleDTO> result = new ArrayList<AnalysedModuleDTO>();
        Iterator<Entry<String, FamixLibrary>> iterator = theModel.libraries.entrySet().iterator();
        AnalysedModuleDTO current;
        while (iterator.hasNext()) {
            Entry<String, FamixLibrary> currentEntry = (Entry<String, FamixLibrary>) iterator.next();
            FamixLibrary fLibrary = currentEntry.getValue();
            boolean correctResult = this.compliesWithArguments(fLibrary.uniqueName);
            if (correctResult) {
                current = createAnalysedModuleDTO("library", fLibrary);
                result.add(current);
            }
        }
        return result;
    }

    private boolean compliesWithArguments(String uniqueName) {
        switch (this.currentFunction) {
            case ROOT:
                return isRootModule(uniqueName);
            case CHILDS:
                return isChild(uniqueName, this.currentArgument);
            case PARENT:
                return isParent(uniqueName, this.currentArgument);
            case FETCH:
                return uniqueName.equals(this.currentArgument);
        }
        return false;
    }

    private boolean isRootModule(String uniqueName) {
        int seperatorCount = uniqueName.split("\\.").length;
        return seperatorCount <= 1;
    }

    private boolean isChild(String child, String parent) {
        if (isRootModule(child)) {
            return false;
        }
        String currentParent = child.substring(0, child.lastIndexOf("."));
        return parent.equals(currentParent);
    }

    private boolean isParent(String parent, String child) {
        if (!moduleExists(child)) {
            return false;
        }
        String name = child.substring(child.lastIndexOf(".") + 1, child.length());
        String currentChild = parent + "." + name;
        return currentChild.equals(child);
    }

    private AnalysedModuleDTO createAnalysedModuleDTO(String type, FamixEntity theEntity) {
        List<AnalysedModuleDTO> emptyList = new ArrayList<AnalysedModuleDTO>();
        return createAnalysedModuleDTO(type, theEntity, emptyList);
    }
    private AnalysedModuleDTO createAnalysedModuleDTO(String type, FamixEntity theEntity, List<AnalysedModuleDTO> childs) {
        String name = theEntity.name;
        String uniqueName = theEntity.uniqueName;
        String visibility = theEntity.visibility;
        if (theEntity instanceof FamixLibrary){
        	// To the uniqueName of a FamixLibrary the prefix "xLibrary." is added.
        	uniqueName = ((FamixLibrary) theEntity).physicalPath;
        }
        return new AnalysedModuleDTO(uniqueName, name, type, visibility);
    }

    private boolean moduleExists(String uniqueName) {
        if (theModel.classes.get(uniqueName) != null) {
            return true;
        }
        if (theModel.packages.get(uniqueName) != null) {
            return true;
        }
        if (theModel.interfaces.get(uniqueName) != null) {
            return true;
        }
        if (theModel.libraries.get(uniqueName) != null) {
            return true;
        }
        return false;
    }

}
