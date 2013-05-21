package husacct.analyse.domain.famix;

import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ExternalSystemDTO;

import java.util.ArrayList;
import java.util.List;

class FamixDependencyFinder extends FamixFinder {
	private static enum FinderFunction { FROM, ONLY_FROM, TO, ONLY_TO, BOTH, ALL };
	private static enum DependencyType { DIRECT, INDIRECT, EXTERNAL };
    private List<DependencyDTO> dependencyCache;
    
	public FamixDependencyFinder(FamixModel model) {
		super(model);
		this.dependencyCache = null;
	}
	
	public List<DependencyDTO> getAllDependencies(){
		if(dependencyCache == null){
			dependencyCache = findDependencies(FinderFunction.ALL, "", "");
		}
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
		return externalSystems;
	}
	
	private List<DependencyDTO> findDependencies(FinderFunction findFunction, String from, String to){
		return findDependencies(findFunction, from, to, null);
	}
	
	private List<DependencyDTO> findDependencies(FinderFunction findFunction, String from, String to, String[] applyFilter){
		return findDependencies(findFunction, from, to, applyFilter, false);
	}
	
	private List<DependencyDTO> findDependencies(FinderFunction findFunction, String from, String to, String[] applyFilter, boolean preventRecursion){
		List<DependencyDTO> result = new ArrayList<DependencyDTO>();
		List<FamixAssociation> allAssociations = theModel.associations;
		for(FamixAssociation association : allAssociations){
			if(compliesWithFunction(association, findFunction, from, to) && compliesWithFilter(association, applyFilter)){
				DependencyDTO foundDirectDependency = buildDependencyDTO(association);
				if(!containsDependency(foundDirectDependency, result)){
					result.add(foundDirectDependency);
				}
			}
			if(!preventRecursion){
				if(association.type == "ExtendsConcrete"){
					// Pass true to prevent recursion and endless calls!
					List<DependencyDTO> extendingDirectDependencies = getDependencies(association.to, to, true);
					for(DependencyDTO extendingDirectDependency : extendingDirectDependencies){
						extendingDirectDependency.type = association.type + extendingDirectDependency.type;
						extendingDirectDependency.isIndirect = typeOfDependency(extendingDirectDependency) == DependencyType.INDIRECT;
						extendingDirectDependency.from = association.from;
						extendingDirectDependency.to = extendingDirectDependency.from+ " -> " + extendingDirectDependency.to;
						if(!containsDependency(extendingDirectDependency, result) && !extendingDirectDependency.isIndirect)
							result.add(extendingDirectDependency);
					}
				}
			}
		}
		result.addAll(findIndirectDependencies(from, to, applyFilter));
		return result;
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
		}
		
		for(FamixAssociation association : allAssociations){
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
			case ONLY_FROM:
				return isFrom(association, from) && !isTo(association, to);
			case TO:
				return isTo(association, to);
			case ONLY_TO:
				return !isFrom(association, from) && isTo(association, to);
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
	
	private boolean isTo(FamixAssociation association, String to){
		boolean result = to.equals("") || association.to.equals(to);
		result = result || association.to.startsWith(to + ".");
		result = result && !association.to.equals(association.from);
		return result;
	}
	
	private boolean containsDependency(DependencyDTO find, List<DependencyDTO> dependencies) {
        for (DependencyDTO d : dependencies)
            if (find.equals(d))
                return true;
        return false;
    }
	
	private DependencyDTO buildDependencyDTO(FamixAssociation association){
		return new DependencyDTO(association.from, association.to, association.type, association.lineNumber);
	}
}
