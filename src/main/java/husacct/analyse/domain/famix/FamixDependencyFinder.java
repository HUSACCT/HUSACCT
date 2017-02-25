package husacct.analyse.domain.famix;

import husacct.common.dto.DependencyDTO;

import java.util.*;

import org.apache.log4j.Logger;

class FamixDependencyFinder extends FamixFinder {
	private static enum FinderFunction { FROM, TO, BOTH, ALL };
    private List<DependencyDTO> dependencyCache;
    private final Logger logger = Logger.getLogger(FamixDependencyFinder.class);
	protected int numberOfDuplicateAssociations;
	// HashMap dependenciesMapFromTo has as first key classPathFrom, as second key classPathTo, and as value a list of dependencies.
	private HashMap<String, HashMap<String, ArrayList<DependencyDTO>>> dependenciesMapFromTo; 
	// HashMap dependenciesMapTo has as key classPathTo and as value a list of all dependencies to this class (from all classes to the selected class).
	private HashMap<String, ArrayList<DependencyDTO>> dependenciesMapTo; 

    
	public FamixDependencyFinder(FamixModel model) {
		super(model);
		this.dependencyCache = null;
	}
	
	public void buildCache(){
		int numberOfDependencies = getAllDependencies().size();
		this.logger.info(new Date().toString() + " Dependencies added: " + numberOfDependencies + ", Removed duplicates: " + numberOfDuplicateAssociations);
		initializeDependencyHashMap();
		return;
	}
	
    public void importDependencies(List<DependencyDTO> dependencies){
    	dependencyCache = dependencies;
    }

    public List<DependencyDTO> getAllDependencies(){
		if(dependencyCache == null)
			dependencyCache = findDependencies(FinderFunction.ALL, "", "");
		return dependencyCache;
	}
	
	// Deprecated
    public List<DependencyDTO> getDependencies(String from, String to, String[] dependencyFilter){
		return findDependencies(FinderFunction.BOTH, from, to, dependencyFilter);
	}
	
	// Deprecated
	public List<DependencyDTO> getDependenciesFrom(String from, String[] dependencyFilter){
		return findDependencies(FinderFunction.FROM, from, "", dependencyFilter);
	}
	
	// Returns all dependencies for the exact match from classPathFrom and classPathTo
	// Either classPathTFrom or classPathTo should have a value other than "", otherwise an empty array is returned.
	// If classPathTFrom = "", then all dependencies to classPathTo are returned, which refer to existing classPathFrom's.
	// If classPathTo = "", then all dependencies from classPathFrom are returned, which refer to existing classPathTo's.
	public ArrayList<DependencyDTO> getDependenciesFromTo(String classPathFrom, String classPathTo){
		ArrayList<DependencyDTO> foundDependencies = new ArrayList<>();
		if((classPathFrom == null || Objects.equals(classPathFrom, "")) && (classPathTo == null || Objects.equals(classPathTo, ""))){
			this.logger.warn(" Incomplete calls: ClassPathFrom = "  + classPathFrom + ", ClassPathTo = " + classPathTo);
			return foundDependencies;
		}
		try{
			if ((dependenciesMapFromTo != null) && (dependenciesMapTo != null)){
				if(!Objects.equals(classPathFrom, "")){
					// Select all dependencies within dependenciesMapFromTo whose pathFrom equals classPathFrom
					HashMap<String, ArrayList<DependencyDTO>> fromMap = dependenciesMapFromTo.get(classPathFrom);
					// Select all dependencies within fromMap whose pathTo starts with classPathTo
					if(fromMap != null ){
						if(!Objects.equals(classPathTo, "")){
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
										if((theModel.classes.containsKey(dependency.to)) || (theModel.libraries.containsKey(dependency.to))){
											foundDependencies.add(dependency);
										}
									}
								}
							}
						}
					}
				}
				else{ //classPathFrom = ""
					// Select the list of dependencies within dependenciesMapTo,where the key equals classPathTo.
					ArrayList<DependencyDTO> toDependenciesList = dependenciesMapTo.get(classPathTo);
					if(toDependenciesList != null ){
						foundDependencies = toDependenciesList;
					}
				}
			}	
		} catch (Exception e) {
	        this.logger.error(" Exception: "  + e);
	        //e.printStackTrace();
	    }

       	return foundDependencies;	
    }
	
	// Deprecated
	public List<DependencyDTO> getDependenciesTo(String to, String[] dependencyFilter){
		return findDependencies(FinderFunction.TO, "", to, dependencyFilter);
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
		List<DependencyDTO> foundDependenciesReturnList = new ArrayList<>();
	    TreeMap<String, DependencyDTO> foundDependenciesTreeMap = new TreeMap<>();
		numberOfDuplicateAssociations = 0;
		try {
			for(FamixAssociation association : theModel.associations){
				if(compliesWithFunction(association, findFunction, from, to) && compliesWithFilter(association, applyFilter)){
					if (association.from == null || association.from.equals("") || association.to == null || association.to.equals("") ||association.lineNumber == 0 || association.type == null){
						// Do not add the association as a dependency, since it is incomplete.
					}
					else{
						// Filter out dependencies if from and to do not refer to types
						String libraryRoot = "xLibraries.";
						if(!(theModel.classes.containsKey(association.from) && (theModel.classes.containsKey(association.to) || theModel.libraries.containsKey((libraryRoot + association.to))))){
							// Do not add the association as a dependency, since it is inconsistent with theModel.
						}
						else {
							if (theModel.libraries.containsKey((libraryRoot + association.to))) {
								association.to = libraryRoot + association.to; // Prefix it with the libraryRoot to present  external systems everywhere the same to the tool users.
							}
							// Filter-out duplicate associations.
							String uniqueName = (association.from + association.to + association.lineNumber + association.type + association.subType + Boolean.toString(association.isIndirect));
							if (!foundDependenciesTreeMap.containsKey(uniqueName)){
								// Create Dependency and add to result
								DependencyDTO foundDependency = new DependencyDTO(association.from, association.to, association.type, association.subType ,association.lineNumber, association.isIndirect, association.isInheritanceRelated);
								if (association instanceof FamixInvocation) {
									FamixInvocation invocation = (FamixInvocation) association;
									foundDependency.usedEntity = invocation.usedEntity;
									foundDependency.belongsToMethod = invocation.belongsToMethod;
									foundDependency.statement = invocation.statement;
								}
								if (theModel.classes.get(association.from).isInnerClass || (!theModel.libraries.containsKey(association.to) && theModel.classes.get(association.to).isInnerClass)) {
									foundDependency.isInnerClassRelated = true;
								}
								foundDependenciesTreeMap.put(uniqueName, foundDependency);
							}
							else {
								numberOfDuplicateAssociations ++; }
						}	
					}
				}
				else{
					// Do not add the association as a dependency, since it does not comply.
				}
			}
		} catch (Exception e) {
	        this.logger.error(" Exception: "  + e);
	        //e.printStackTrace();
	    }
        foundDependenciesReturnList.addAll(foundDependenciesTreeMap.values());
		return foundDependenciesReturnList;
	}

	private List<DependencyDTO> queryCache(FinderFunction findFunction, String from, String to, String[] applyFilter){
		List<DependencyDTO> foundDependencies = new ArrayList<>();
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
	
    // Fill the HashMaps dependenciesMapFromTo and dependenciesMapTo
	public void initializeDependencyHashMap(){
		this.dependenciesMapFromTo = new HashMap<>();
		this.dependenciesMapTo = new HashMap<>();
		HashMap<String, ArrayList<DependencyDTO>> toMap;
		try{
	        for(DependencyDTO dependency : getAllDependencies()) {
            	String uniqueNameFrom = dependency.from;
            	String uniqueNameTo = dependency.to;
            	// Fill dependenciesMapFromTo
            	if(dependenciesMapFromTo.containsKey(uniqueNameFrom)){
            		toMap = dependenciesMapFromTo.get(uniqueNameFrom);
            		if(toMap.containsKey(uniqueNameTo)){
            			ArrayList<DependencyDTO> matchingDependencies = toMap.get(uniqueNameTo);
            			matchingDependencies.add(dependency);
        			}
            		else{
            			// No toMap exists for the to-key, so create it.
            			ArrayList<DependencyDTO> newList = new ArrayList<>();
            			newList.add(dependency);
            			toMap.put(uniqueNameTo, newList);
            		}
            	}
            	else{
            		// No map exists for the from-key, so add it.
        			ArrayList<DependencyDTO> newList = new ArrayList<>();
        			newList.add(dependency);
            		toMap = new HashMap<>();
            		toMap.put(uniqueNameTo, newList);            		
	            	dependenciesMapFromTo.put(uniqueNameFrom, toMap);
            	}
            	// Fill dependenciesMapTo
            	if(dependenciesMapTo.containsKey(uniqueNameTo)){
        			ArrayList<DependencyDTO> matchingDependencies = dependenciesMapTo.get(uniqueNameTo);
           			matchingDependencies.add(dependency);
            	}
            	else{
            		// No list exists for uniqueNameTo, so add it.
        			ArrayList<DependencyDTO> newList = new ArrayList<>();
        			newList.add(dependency);
            		dependenciesMapTo.put(uniqueNameTo, newList);
            	}
	        }
		} catch(Exception e) {
	        this.logger.warn("Exception may result in incomplete dependency list. Exception:  " + e);
	        //e.printStackTrace();
		}
	}
	
}
