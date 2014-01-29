package husacct.analyse.domain.famix;

import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ExternalSystemDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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

    
	public FamixDependencyFinder(FamixModel model) {
		super(model);
		this.dependencyCache = null;
		this.externalSystemCache = null;
	}
	
	public void buildCache(){
		int numberOfDependencies = getAllDependencies().size();
		this.logger.debug(new Date().toString() + " Dependencies added: " + numberOfDependencies + ", Not complying: " + numberOfNotComplyingAssociations + ", Incomplete: " + numberOfIncompleteAssociations + ", Removed duplicates: " + numberOfDuplicateAssociations + ", Extends concrete: " + numberOfExtendsConcrete);     

		//getExternalSystems().size();
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
	
	public List<DependencyDTO> getDependenciesTo(String to){
		return getDependenciesTo(to, null);
	}
	
	public List<DependencyDTO> getDependenciesTo(String to, String[] dependencyFilter){
		return findDependencies(FinderFunction.TO, "", to, dependencyFilter);
	}

	public List<ExternalSystemDTO> getExternalSystems(){
		if(externalSystemCache != null)
			return externalSystemCache;
		List<ExternalSystemDTO> externalSystems = new ArrayList<ExternalSystemDTO>();
		List<String> pathsToImports = new ArrayList<String>();
		List<String> pathsToPackages = new ArrayList<String>();
		for(String imp : theModel.imports.keySet())
			if(!pathsToImports.contains(imp))
				pathsToImports.add(imp);
		for(String clls : theModel.classes.keySet())
			if(!pathsToPackages.contains(clls))
				pathsToPackages.add(clls);
		for(String intrfc : theModel.interfaces.keySet())
			if(!pathsToPackages.contains(intrfc))
				pathsToPackages.add(intrfc);
		for(String compareString : pathsToImports)
			if(!pathsToPackages.contains(compareString)){
				ExternalSystemDTO dto = new ExternalSystemDTO();
				dto.systemName = (compareString.contains(".") && compareString.lastIndexOf('.') != compareString.length() - 1) ? compareString.substring(compareString.lastIndexOf('.')+1) : compareString;
				dto.systemPackage = compareString;
				externalSystems.add(dto);
			}
		for(ExternalSystemDTO dto : externalSystems){
			dto.fromDependencies = (ArrayList<DependencyDTO>) getDependenciesTo(dto.systemPackage);
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
		numberOfNotComplyingAssociations = 0;
		numberOfIncompleteAssociations = 0;
		numberOfDuplicateAssociations = 0;
		numberOfExtendsConcrete = 0;
		try {
			for(FamixAssociation association : allAssociations){
				if(compliesWithFunction(association, findFunction, from, to) && compliesWithFilter(association, applyFilter)){
					DependencyDTO foundDirectDependency = buildDependencyDTO(association);
					if (foundDirectDependency.from == null || foundDirectDependency.from.equals("") || foundDirectDependency.to == null || foundDirectDependency.to.equals("") ||foundDirectDependency.lineNumber == 0 || foundDirectDependency.type == null){
						numberOfIncompleteAssociations ++;
					}
					else{
					String uniqueName = (foundDirectDependency.from + String.valueOf(foundDirectDependency.lineNumber) + foundDirectDependency.to + foundDirectDependency.type); 
						if (!result.containsKey(uniqueName)){
							result.put(uniqueName, foundDirectDependency);
						}
						else {
							numberOfDuplicateAssociations ++; }
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
	        this.logger.debug(new Date().toString() + " "  + e);
	        //e.printStackTrace();
	    }

		
		// Indirect dependency analysis disabled 2014-01-20; improve effectiveness and efficiency before it is enabled again.
		//if(!preventRecursion)
		//result.addAll(findIndirectDependencies(from, to, applyFilter));
		resultToReturn = (List<DependencyDTO>) new ArrayList<DependencyDTO>(result.values());
		return resultToReturn;
	
	
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
					result.add(new DependencyDTO(association.from, association.to, "AccessClassVariableInterface", false, association.lineNumber));
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
				DependencyDTO foundDirectDependency = buildDependencyDTO(association);
				if(!containsDependency(foundDirectDependency, dependenciesTo)){
					dependenciesTo.add(foundDirectDependency);
				}
			}
			if(compliesWithFunction(association, FinderFunction.FROM, from, to) && compliesWithFilter(association, applyFilter)){
				DependencyDTO foundDirectDependency = buildDependencyDTO(association);
				if(!containsDependency(foundDirectDependency, dependenciesFrom)){
					dependenciesFrom.add(foundDirectDependency);
				}
			}
		}
		
		for(DependencyDTO dependencyTo : dependenciesTo){
			for(DependencyDTO dependencyFrom : dependenciesFrom){
				if(dependencyTo.from.equals(dependencyFrom.to)){
					DependencyDTO indirectDependency = new DependencyDTO(dependencyFrom.from, dependencyTo.to, dependencyFrom.type + dependencyTo.type, true, dependencyFrom.lineNumber);
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
	
	private DependencyDTO buildDependencyDTO(FamixAssociation association){
		return new DependencyDTO(association.from, association.to, association.type, association.lineNumber);
	}
}
