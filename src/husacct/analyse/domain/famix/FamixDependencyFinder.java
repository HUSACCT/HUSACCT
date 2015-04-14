package husacct.analyse.domain.famix;

import husacct.ServiceProvider;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ExternalSystemDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;

class FamixDependencyFinder extends FamixFinder {
	private static enum FinderFunction { FROM, TO, BOTH, ALL };
    private List<DependencyDTO> dependencyCache;
    private List<ExternalSystemDTO> externalSystemCache;
    private final Logger logger = Logger.getLogger(FamixDependencyFinder.class);
	protected int numberOfNotComplyingAssociations;
	protected int numberOfIncompleteAssociations;
	protected int numberOfDuplicateAssociations;
	protected int numberOfExtendsConcrete;
	protected int numberOfAssociationsWithoutFromClass;
	protected int numberOfAssociationsWithoutToClass;
	protected int numberOfFilteredDependencies;
	// TreeMap dependenciesOnFromTo has as first key classPathFrom, as second key classPathTo, and as value a list of dependencies.
	private HashMap<String, HashMap<String, ArrayList<DependencyDTO>>> dependenciesOnFromTo; 

    
	public FamixDependencyFinder(FamixModel model) {
		super(model);
		this.dependencyCache = null;
		this.externalSystemCache = null;
	}
	
	public void buildCache(){
		int numberOfDependencies = getAllDependencies().size();
		this.logger.info(new Date().toString() + " Dependencies added: " + numberOfDependencies + ", Not complying: " + numberOfNotComplyingAssociations + ", Incomplete: " + numberOfIncompleteAssociations + ", Removed duplicates: " + numberOfDuplicateAssociations + ", Extends concrete: " + numberOfExtendsConcrete);
		initializeDependencyHashMap();
		return;
	}
	
	public List<DependencyDTO> getAllDependencies(){
		if(dependencyCache == null)
			dependencyCache = findDependencies(FinderFunction.ALL, "", "");
		return dependencyCache;
	}
	
	public List<DependencyDTO> getDependencies(String from, String to, String[] dependencyFilter){
		return findDependencies(FinderFunction.BOTH, from, to, dependencyFilter);
	}
	
	public List<DependencyDTO> getDependenciesFrom(String from){
		return getDependenciesFrom(from, null);
	}
	
	public List<DependencyDTO> getDependenciesFrom(String from, String[] dependencyFilter){
		return findDependencies(FinderFunction.FROM, from, "", dependencyFilter);
	}
	
	// Returns all dependencies for the exact match from classPathFrom and classPathTo
	// Either classPathTFrom or classPathTo should have a value other than "", otherwise an empty array is returned.
	// If classPathTFrom = "", then all dependencies to classPathTo are returned, which refer to existing classPathFrom's.
	// If classPathTo = "", then all dependencies from classPathFrom are returned, which refer to existing classPathTo's.
	public DependencyDTO[] getDependenciesFromTo(String classPathFrom, String classPathTo){
		ArrayList<DependencyDTO> foundDependencies = new ArrayList<DependencyDTO>();
		if((classPathFrom == null || classPathFrom == "") && (classPathTo == null || classPathTo == "")){
			DependencyDTO[] emptyDependencyArray = new DependencyDTO[foundDependencies.size()];
			this.logger.warn(" Incomplete calls: ClassPathFrom = "  + classPathFrom + ", ClassPathTo = " + classPathTo);
			return emptyDependencyArray;
		}
		try{
			if ((dependenciesOnFromTo != null) && (dependenciesOnFromTo.size() > 0)){
				if(classPathFrom != ""){
					// Select all dependencies within TreeMap dependenciesOnFromTo whose pathFrom equals classPathFrom
					HashMap<String, ArrayList<DependencyDTO>> fromMap = dependenciesOnFromTo.get(classPathFrom);
					// Select all dependencies within fromMap whose pathTo starts with classPathTo
					if(fromMap != null ){
						if(classPathTo != ""){
							ArrayList<DependencyDTO> dependencyList = fromMap.get(classPathTo);
							if(dependencyList != null){
								foundDependencies.addAll(dependencyList);
							}
						} else{
							// If classPathTo = "", then find all dependencies from classPathFrom. Add each, when it refers to an existing classPathTo 
							Set<String> keyset = fromMap.keySet();
							for(String keyTo : keyset){
								ArrayList<DependencyDTO> dependencyList = fromMap.get(keyTo);
								if(dependencyList != null){
									for(DependencyDTO dependency : dependencyList){
										if((theModel.classes.containsKey(dependency.toClassPath)) || (theModel.libraries.containsKey(dependency.toClassPath))){
											foundDependencies.add(dependency);
										}
									}
								}
							}
						}
					}
				}
				else{ //classPathFrom = ""
					if(classPathTo != ""){
						Set<String> fromKeyList = dependenciesOnFromTo.keySet();
						for(String fromKey : fromKeyList){
							HashMap<String, ArrayList<DependencyDTO>> fromMap = dependenciesOnFromTo.get(fromKey);
							// Select all dependencies within fromMap whose pathTo starts with fromKey
							if(fromMap != null ){
								if(classPathTo != ""){
									ArrayList<DependencyDTO> dependencyList = fromMap.get(classPathTo);
									if(dependencyList != null){
										foundDependencies.addAll(dependencyList);
									}
								}
							}
						}
					}
				}
			}	
		} catch (Exception e) {
	        this.logger.error(" Exception: "  + e);
	        //e.printStackTrace();
	    }

		
		DependencyDTO[] matchDependency = new DependencyDTO[foundDependencies.size()];
       	int iterator = 0;
       	for (DependencyDTO d : foundDependencies) {
           	matchDependency[iterator] = d;
           	iterator++;
       	}
       	return matchDependency;	
    }
	
	public List<DependencyDTO> getDependenciesTo(String to){
		return getDependenciesTo(to, null);
	}
	
	public List<DependencyDTO> getDependenciesTo(String to, String[] dependencyFilter){
		return findDependencies(FinderFunction.TO, "", to, dependencyFilter);
	}

	public List<ExternalSystemDTO> getExternalSystems(){
		if(externalSystemCache != null){
			return externalSystemCache;
		}
		List<ExternalSystemDTO> externalSystems = new ArrayList<ExternalSystemDTO>();
		HashSet<String> completeImportStrings = new HashSet<String>();

		// Select all imported types. Note: key of imports is combined from.to.
		for(String importKey : theModel.imports.keySet()){
			String importString = theModel.imports.get(importKey).completeImportString;
			completeImportStrings.add(importString);
		}

		// Check for each completeImportString if it is an internal class or interface. If not, create an ExternalSystemDTO.
		for(String completeImportString : completeImportStrings){
			if((!completeImportString.startsWith("java.")) && (!completeImportString.startsWith("javax."))){
				if(!theModel.classes.containsKey(completeImportString)){
					ExternalSystemDTO dto = new ExternalSystemDTO();
					dto.systemName = (completeImportString.contains(".") && completeImportString.lastIndexOf('.') != completeImportString.length() - 1) ? completeImportString.substring(completeImportString.lastIndexOf('.')+1) : completeImportString;
					dto.systemPackage = completeImportString;
					DependencyDTO[] dependencies = getDependenciesFromTo("", completeImportString);
					ArrayList<DependencyDTO> fromDependencies = new ArrayList<DependencyDTO>();
					int lenght = dependencies.length;
					if (lenght > 0){
						for(int i = 0; i < lenght; i++){
							fromDependencies.add(dependencies[i]);
						}
					}
					dto.fromDependencies = fromDependencies;
					externalSystems.add(dto);
				}
			}
		}
		externalSystemCache = externalSystems;
		return externalSystemCache;
	}
	
	private List<DependencyDTO> findDependencies(FinderFunction findFunction, String from, String to){
		return findDependencies(findFunction, from, to, null);
	}
	
	private List<DependencyDTO> findDependencies(FinderFunction findFunction, String from, String to, String[] applyFilter){
		if(dependencyCache == null)
			return findDependenciesRaw(findFunction, from, to, applyFilter);
		else
			return queryCache(findFunction, from, to, applyFilter);
	}
	
	// Returns a list of filtered  and sorted DependencyDTOs.
	private List<DependencyDTO> findDependenciesRaw(FinderFunction findFunction, String from, String to, String[] applyFilter){
	    TreeMap<String, DependencyDTO> result = new TreeMap<String, DependencyDTO>();
		List<DependencyDTO> resultToReturn = new ArrayList<DependencyDTO>();
		List<FamixAssociation> allAssociations = theModel.associations;
		String fromClassPath;
		String toClassPath;
		numberOfNotComplyingAssociations = 0;
		numberOfIncompleteAssociations = 0;
		numberOfDuplicateAssociations = 0;
		numberOfFilteredDependencies = 0;
		try {
			for(FamixAssociation association : allAssociations){
				fromClassPath = "";
				toClassPath = "";
				if(compliesWithFunction(association, findFunction, from, to) && compliesWithFilter(association, applyFilter)){
					if (association.from == null || association.from.equals("") || association.to == null || association.to.equals("") ||association.lineNumber == 0 || association.type == null){
						numberOfIncompleteAssociations ++;
					}
					else{
						// Filter out dependencies if from and to do not refer to types
						String libraryRoot = "xLibraries.";
						if(!(theModel.classes.containsKey(association.from) && (theModel.classes.containsKey(association.to) || theModel.libraries.containsKey((libraryRoot + association.to))))){
							numberOfFilteredDependencies ++;
						}
						else {
							if (theModel.libraries.containsKey((libraryRoot + association.to))) {
								association.to = libraryRoot + association.to; // Prefix it with the libraryRoot to present  external systems everywhere the same to the tool users.
							}
							// Filter-out duplicate associations of same type at same line.
							String uniqueName = (association.from + association.to + association.lineNumber + association.type + association.subType + Boolean.toString(association.isIndirect));
							fromClassPath = association.from;
							toClassPath = association.to;
							if (!result.containsKey(uniqueName)){
								// Create Dependency and add to result
								DependencyDTO foundDependency = new DependencyDTO(association.from, fromClassPath, association.to, toClassPath, association.type, association.subType ,association.lineNumber, association.isIndirect);
								if (association.isInheritanceRelated) {
									foundDependency.isInheritanceRelated = true;
								}
								if (theModel.classes.get(association.from).isInnerClass || (!theModel.libraries.containsKey(association.to) && theModel.classes.get(association.to).isInnerClass)) {
									foundDependency.isInnerClassRelated = true;
								}
								result.put(uniqueName, foundDependency);
							}
							else {
								numberOfDuplicateAssociations ++; }
						}	
					}
				}
				else{
					numberOfNotComplyingAssociations ++;
				}
			}
		} catch (Exception e) {
	        this.logger.error(" Exception: "  + e);
	        //e.printStackTrace();
	    }
		// Convert the result ThreeMap into a sorted ArrayList<DependencyDTO>
		for(String key: result.keySet()) {
			DependencyDTO dependency = result.get(key);
			resultToReturn.add(dependency);
		}
		return resultToReturn;
	}

	private List<DependencyDTO> queryCache(FinderFunction findFunction, String from, String to, String[] applyFilter){
		List<DependencyDTO> foundDependencies = new ArrayList<DependencyDTO>();
		for(DependencyDTO dependency : dependencyCache){
			switch(findFunction){
			case FROM:
				if(isFrom(dependency, from)){
					foundDependencies.add(dependency);
				}
				break;
			case TO:
				if(isTo(dependency, to)){
					foundDependencies.add(dependency);
				}
				break;
			case BOTH:
				if(isFrom(dependency, from) && isTo(dependency, to)){
					foundDependencies.add(dependency);
				}
				break;
			case ALL:
				return dependencyCache;
			default:
				break;
			}
		}
		return foundDependencies;
	}
	
	private boolean compliesWithFilter(FamixAssociation association, String[] filter){
		if(filter == null || filter.length == 0)
			return true;
		for(String loopingFilter : filter)
			if(association.type.equals(loopingFilter))
				return true;
		return false;
	}
	
	private boolean compliesWithFunction(FamixAssociation association, FinderFunction findFunction, String from, String to){
		switch(findFunction){
			case BOTH:
				return isFrom(association, from) && isTo(association, to);
			case FROM:
				return isFrom(association, from);
			case TO:
				return isTo(association, to);
			case ALL:
				return true;
		}
		return false;
	}
	
	private boolean isFrom(FamixAssociation association, String from){
		boolean result = from.equals("") || association.from.equals(from);
		result = result || association.from.startsWith(from + ".");
		result = result && !association.from.equals(association.to);
		return result;
	}
	
	private boolean isFrom(DependencyDTO dependency, String from){
		boolean result = from.equals("") || dependency.from.equals(from);
		result = result || dependency.from.startsWith(from + ".");
		result = result && !dependency.from.equals(dependency.to);
		return result;
	}
	
	private boolean isTo(FamixAssociation association, String to){
		boolean result = to.equals("") || association.to.equals(to);
		result = result || association.to.startsWith(to + ".");
		result = result && !association.to.equals(association.from);
		return result;
	}
	
	private boolean isTo(DependencyDTO dependency, String to){
		boolean result = to.equals("") || dependency.to.equals(to);
		result = result || dependency.to.startsWith(to + ".");
		result = result && !dependency.to.equals(dependency.from);
		return result;
	}
	
    // Fill HashMap dependenciesOnFromTo 
	public void initializeDependencyHashMap(){
		this.dependenciesOnFromTo = new HashMap<String, HashMap<String, ArrayList<DependencyDTO>>>();
		DependencyDTO[] dependencies = ServiceProvider.getInstance().getAnalyseService().getAllDependencies();
		HashMap<String, ArrayList<DependencyDTO>> toMap;
		try{
	        for(DependencyDTO dependency : dependencies) {
            	String uniqueNameFrom = dependency.fromClassPath;
            	String uniqueNameTo = dependency.toClassPath;
            	if(dependenciesOnFromTo.containsKey(uniqueNameFrom)){
            		toMap = dependenciesOnFromTo.get(uniqueNameFrom);
            		if(toMap.containsKey(uniqueNameTo)){
            			// Check if there is a dependency with the same Type and LineNr in the ArrayList
            			ArrayList<DependencyDTO> matchingDependencies = toMap.get(uniqueNameTo);
            			boolean found = false;
            			for(DependencyDTO matchingDependency : matchingDependencies){
	            			if((matchingDependency.from == dependency.from) && (matchingDependency.to == dependency.to)){
	            				if((matchingDependency.type == dependency.type) && (matchingDependency.lineNumber == dependency.lineNumber) && (matchingDependency.isIndirect == dependency.isIndirect)){
	            					// Do nothing, dependency already exists
	            					found = true;
	            					break;
	            				}
            				}
            			}
	            		if(!found){
	            			// The dependency does not exist yet under the from-key and to-key, so add it.
	            			matchingDependencies.add(dependency);
            			}
        			}
            		else{
            			// No toMap exists for the to-key, so create it.
            			ArrayList<DependencyDTO> newList = new ArrayList<DependencyDTO>();
            			newList.add(dependency);
            			toMap.put(uniqueNameTo, newList);
            		}
            	}
            	else{
            		// No map exists for the from-key, so add it.
        			ArrayList<DependencyDTO> newList = new ArrayList<DependencyDTO>();
        			newList.add(dependency);
            		toMap = new HashMap<String, ArrayList<DependencyDTO>>();
            		toMap.put(uniqueNameTo, newList);            		
	            	dependenciesOnFromTo.put(uniqueNameFrom, toMap);
            	}
	        }
	        
	        
		} catch(Exception e) {
	        this.logger.warn("Exception may result in incomplete dependency list. Exception:  " + e);
	        //e.printStackTrace();
		}
	}
	
}
