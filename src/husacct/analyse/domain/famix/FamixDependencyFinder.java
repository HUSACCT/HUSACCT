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
	private static enum DependencyType { DIRECT, INDIRECT, EXTERNAL };
    private List<DependencyDTO> dependencyCache;
    private List<ExternalSystemDTO> externalSystemCache;
    private final Logger logger = Logger.getLogger(FamixDependencyFinder.class);
	protected int numberOfNotComplyingAssociations;
	protected int numberOfIncompleteAssociations;
	protected int numberOfDuplicateAssociations;
	protected int numberOfExtendsConcrete;
	protected int numberOfAssociationsWithoutFromClass;
	protected int numberOfAssociationsWithoutToClass;
	protected int numberOfFilteredDependenciesToLanguageConstruct;
	
	// TreeMap dependenciesOnFromTo has as first key classPathFrom, as second key classPathTo, and as value a list of dependencies.
	// Note: To find inner classes to, make use of tailMap() instead of get(). tailMap() may return a Map of values!
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

		getExternalSystems().size();
		return;
	}
	
	public List<DependencyDTO> getAllDependencies(){
		if(dependencyCache == null)
			dependencyCache = findDependencies(FinderFunction.ALL, "", "");
		return dependencyCache;
	}
	
	public List<DependencyDTO> getDependencies(String from, String to){
		return getDependencies(from, to, null);
	}
	
	public List<DependencyDTO> getDependencies(String from, String to, boolean preventRecursion){
		return findDependencies(FinderFunction.BOTH, from, to, null, preventRecursion);
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
								if((theModel.classes.containsKey(dependency.toClassPath)) || 
									(theModel.interfaces.containsKey(dependency.toClassPath) || 
									(theModel.libraries.containsKey(dependency.toClassPath)))){
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
				if(!theModel.classes.containsKey(completeImportString) && !theModel.interfaces.containsKey(completeImportString)){
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
		return findDependencies(findFunction, from, to, applyFilter, true);
	}
	
	private List<DependencyDTO> findDependencies(FinderFunction findFunction, String from, String to, String[] applyFilter, boolean preventRecursion){
		if(dependencyCache == null)
			return findDependenciesRaw(findFunction, from, to, applyFilter, preventRecursion);
		else
			return queryCache(findFunction, from, to, applyFilter, preventRecursion);
	}
	
	private List<DependencyDTO> findDependenciesRaw(FinderFunction findFunction, String from, String to, String[] applyFilter, boolean preventRecursion){
	    HashMap<String, DependencyDTO> result = new HashMap<String, DependencyDTO>();
		List<DependencyDTO> resultToReturn = new ArrayList<DependencyDTO>();
		List<FamixAssociation> allAssociations = theModel.associations;
		String fromClassPath;
		String toClassPath;
		numberOfNotComplyingAssociations = 0;
		numberOfIncompleteAssociations = 0;
		numberOfDuplicateAssociations = 0;
		numberOfExtendsConcrete = 0;
		numberOfAssociationsWithoutFromClass = 0;
		numberOfAssociationsWithoutToClass = 0;
		numberOfFilteredDependenciesToLanguageConstruct = 0;
		try {
			for(FamixAssociation association : allAssociations){
				if(compliesWithFunction(association, findFunction, from, to) && compliesWithFilter(association, applyFilter)){
					if (association.from == null || association.from.equals("") || association.to == null || association.to.equals("") ||association.lineNumber == 0 || association.type == null){
						numberOfIncompleteAssociations ++;
					}
					else{
						// Filter on programming language to-values; To do: Make language dependent and configurable!  
						if((association.to.startsWith("java.")) || (association.to.startsWith("javax."))){
							numberOfFilteredDependenciesToLanguageConstruct ++;
						}
						else {
							String uniqueName = (association.from + String.valueOf(association.lineNumber) + association.to + association.type);
							fromClassPath = association.from;
							toClassPath = association.to;
							if (!result.containsKey(uniqueName)){
	
								// Check if from is existing class. If not, determine parent-class for fromClassPath.
								if((theModel.classes.containsKey(association.from))){
									FamixClass toClass = theModel.classes.get(association.from);
									if((toClass != null) && (toClass.isInnerClass) && (theModel.classes.containsKey(toClass.belongsToClass))){
										fromClassPath = toClass.belongsToClass;
									}
								} else{
									numberOfAssociationsWithoutFromClass ++;
								}
	
								// Check if to is existing class. If not, determine parent-class for toClassPath.
								if((theModel.classes.containsKey(association.to))){
									FamixClass toClass = theModel.classes.get(association.to);
									if((toClass != null) && (toClass.isInnerClass) && (theModel.classes.containsKey(toClass.belongsToClass))){
										toClassPath = toClass.belongsToClass;
									}
								} else{
									numberOfAssociationsWithoutToClass ++;
								}
								
								// Create Dependency and add to result
								DependencyDTO foundDirectDependency = new DependencyDTO(association.from, fromClassPath, association.to, toClassPath, association.type, association.lineNumber);
								result.put(uniqueName, foundDirectDependency);
							}
							else {
								numberOfDuplicateAssociations ++; }
						}	
					}
				}
				else{
					numberOfNotComplyingAssociations ++;
				}
					
				if(!preventRecursion){
					if(association.type == "ExtendsConcrete"){
						numberOfExtendsConcrete ++;
						// Pass true to prevent recursion and endless calls!
						List<DependencyDTO> extendingDirectDependencies = getDependencies(association.to, to, true);
						for(DependencyDTO extendingDirectDependency : extendingDirectDependencies){
							if(compliesWithFunction(association, FinderFunction.FROM, from, to) && isTo(extendingDirectDependency, to)){
								extendingDirectDependency.type = association.type + extendingDirectDependency.type;
								extendingDirectDependency.isIndirect = typeOfDependency(extendingDirectDependency) == DependencyType.INDIRECT;
								extendingDirectDependency.from = association.from;
								extendingDirectDependency.via = association.to;
								extendingDirectDependency.to = extendingDirectDependency.to;
								//if(!containsDependency(extendingDirectDependency, result) && !extendingDirectDependency.isIndirect)
								if (result.containsKey(extendingDirectDependency) && !extendingDirectDependency.isIndirect){
									String uniqueName = (extendingDirectDependency.from + String.valueOf(extendingDirectDependency.lineNumber) + extendingDirectDependency.to + extendingDirectDependency.type); 
									result.put(uniqueName, extendingDirectDependency);
									//result.add(extendingDirectDependency);
								}	
							}
						}
					}
				}
			}

		} catch (Exception e) {
	        this.logger.error(" Exception: "  + e);
	        e.printStackTrace();
	    }

		
		// Indirect dependency analysis disabled 2014-01-20; improve effectiveness and efficiency before it is enabled again.
		//if(!preventRecursion)
		//result.addAll(findIndirectDependencies(from, to, applyFilter));
		resultToReturn = (List<DependencyDTO>) new ArrayList<DependencyDTO>(result.values());
		this.logger.info(" Associations: filtered (language Class): " + numberOfFilteredDependenciesToLanguageConstruct + "; non-classes fromClass: " + numberOfAssociationsWithoutFromClass + "; non-classes toClass: " + numberOfAssociationsWithoutToClass);
		return resultToReturn;
	
	
	}

	private boolean determineParentClass(String toString){
		boolean returnValue = false;
		
		
		/*
		while (!returnValue){
			String[] partNames = toString.split("\\.");
			int nr = partNames.length;
			while (nr > 0){
				String s = partNames[nr];
				nr --;
			}
		}
		if((theModel.classes.get(toString) == null)){
		} */
		return returnValue;
	}
	
	public List<DependencyDTO> getAccessClassVariableInterfaceDirectDependencies(){
		List<DependencyDTO> result = new ArrayList<DependencyDTO>();
		HashMap<String, FamixInterface> allInterfaces = theModel.interfaces;
		ArrayList<FamixAssociation> allAssociations  = theModel.associations;
	
		for(String interfaceObject : allInterfaces.keySet()){
			for(FamixAssociation association : allAssociations){
				if(interfaceObject.equals(association.to) && association.type.equals("Import")){
					/*System.out.println("\n InterfaceObject: " + interfaceObject);
					System.out.println("\n AssociationTO: " + association.to);
					System.out.println(	"\n From: " + association.from +
										"\n To: " + association.to +
										"\n Type: " + association.type +
										"\n Line: " + association.lineNumber);*/
					result.add(new DependencyDTO(association.from, association.from, association.to, association.to, "AccessClassVariableInterface", false, association.lineNumber));
				}
			}
		}
		return result;
	}
	
	private List<DependencyDTO> queryCache(FinderFunction findFunction, String from, String to, String[] applyFilter, boolean preventRecursion){
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
	
	private List<DependencyDTO> findIndirectDependencies(String from, String to, String[] applyFilter){
		List<DependencyDTO> result = new ArrayList<DependencyDTO>();
		List<FamixAssociation> allAssociations = theModel.associations;
		List<DependencyDTO> dependenciesTo = new ArrayList<DependencyDTO>();
		List<DependencyDTO> dependenciesFrom = new ArrayList<DependencyDTO>();
		
		for(FamixAssociation association : allAssociations){
			if(compliesWithFunction(association, FinderFunction.TO, from, to) && compliesWithFilter(association, applyFilter)){
				DependencyDTO foundDirectDependency = new DependencyDTO(association.from, association.from, association.to, association.to, association.type, association.lineNumber);
				if(!containsDependency(foundDirectDependency, dependenciesTo)){
					dependenciesTo.add(foundDirectDependency);
				}
			}
			if(compliesWithFunction(association, FinderFunction.FROM, from, to) && compliesWithFilter(association, applyFilter)){
				DependencyDTO foundDirectDependency = new DependencyDTO(association.from, association.from, association.to, association.to, association.type, association.lineNumber);
				if(!containsDependency(foundDirectDependency, dependenciesFrom)){
					dependenciesFrom.add(foundDirectDependency);
				}
			}
		}
		
		for(DependencyDTO dependencyTo : dependenciesTo){
			for(DependencyDTO dependencyFrom : dependenciesFrom){
				if(dependencyTo.from.equals(dependencyFrom.to)){
					DependencyDTO indirectDependency = new DependencyDTO(dependencyFrom.from, dependencyFrom.from, dependencyTo.to, dependencyTo.to, dependencyFrom.type + dependencyTo.type, true, dependencyFrom.lineNumber);
					indirectDependency.via = dependencyFrom.to;
					if(isValidIndirectDependency(indirectDependency) && !containsDependency(indirectDependency, result))
						result.add(indirectDependency);
				}
			}
		}
		return result;
	}
	
	private boolean isValidIndirectDependency(DependencyDTO dependency){
    	if(dependency.type.matches("Extends.*?Extends.*"))
    		return true;
    	if(dependency.type.matches("Extends.*?Implements.*"))
    		return true;
    	if(dependency.type.matches("Implements.*?Extends.*"))
    		return true;
    	return false;
    }
	
	private DependencyType typeOfDependency(DependencyDTO dependency){
		if(dependency.type.matches("Extends.*?Extends.*"))
			return DependencyType.INDIRECT;
		if(dependency.type.matches("Extends.*?Implements.*"))
			return DependencyType.INDIRECT;
		if(dependency.type.matches("Implements.*?Extends.*"))
			return DependencyType.INDIRECT;
		return DependencyType.DIRECT;
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
	
	private boolean containsDependency(DependencyDTO find, List<DependencyDTO> dependencies) {
        for (DependencyDTO d : dependencies)
            if (find.equals(d) || compareDependency(find, d))
                return true;
        return false;
    }
	
	private boolean compareDependency(DependencyDTO compare1, DependencyDTO compare2){
		return compare1.from == compare2.from && compare1.to == compare2.to && compare1.lineNumber == compare2.lineNumber && compare1.type == compare2.type;
	}
	
    // Fill HashMap dependenciesOnFromTo 
	public void initializeDependencyHashMap(){
		this.dependenciesOnFromTo = new HashMap<String, HashMap<String, ArrayList<DependencyDTO>>>();
		DependencyDTO[] dependencies = ServiceProvider.getInstance().getAnalyseService(). getAllDependencies();
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
	            				if((matchingDependency.type == dependency.type) && (matchingDependency.lineNumber == dependency.lineNumber)){
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
