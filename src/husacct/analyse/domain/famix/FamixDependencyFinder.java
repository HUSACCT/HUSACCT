package husacct.analyse.domain.famix;

import husacct.common.dto.DependencyDTO;
import java.util.ArrayList;
import java.util.List;

class FamixDependencyFinder extends FamixFinder{

	private static final String EXTENDS_ABSTRCT = "extendsAbstract";
	private static final String EXTENDS_CONCRETE = "extendsConcrete";
	private static final String EXTENDS_LIBRARY = "extendsLibrary";
	
	private static enum FinderFunction{FROM, TO, BOTH};
	private FinderFunction currentFunction;
	
	private boolean filtered;
	private String[] filter;
	private String from = "", to = "";
	private List<DependencyDTO> currentResult;
	
	public FamixDependencyFinder(FamixModel model) {
		super(model);
		this.currentResult = new ArrayList<DependencyDTO>();
		this.filter = new String[]{};
		this.filtered = false;
		this.currentFunction = FinderFunction.BOTH;
	}
	
	public List<DependencyDTO> getDependencies(String from, String to) {
		performQuery(FinderFunction.BOTH, from, to);
		return this.currentResult;
	}

	public List<DependencyDTO> getDependencies(String from, String to, String[] dependencyFilter) {
		performQuery(FinderFunction.BOTH, from, to, dependencyFilter);
		return this.currentResult;
	}

	public List<DependencyDTO> getDependenciesFrom(String from, String[] dependencyFilter) {
		performQuery(FinderFunction.FROM, from, "", dependencyFilter);
		return this.currentResult;
	}
	
	public List<DependencyDTO> getDependenciesFrom(String from) {
		performQuery(FinderFunction.FROM, from, "");
		return this.currentResult;
	}

	public List<DependencyDTO> getDependenciesTo(String to) {
		performQuery(FinderFunction.TO, "", to);
		return this.currentResult;
	}

	public List<DependencyDTO> getDependenciesTo(String to, String[] dependencyFilter) {
		performQuery(FinderFunction.TO, "", to);
		return this.currentResult;
	}
	
	private void performQuery(FinderFunction function, String argumentFrom, String argumentTo, String[] applyFilter){
		this.filter = applyFilter;
		this.filtered = true;
		performQuery(function, argumentFrom, argumentTo);
	}
	
	private void performQuery(FinderFunction function, String argumentFrom, String argumentTo){
		this.currentFunction = function;
		this.from = argumentFrom;
		this.to = argumentTo;
		this.currentResult = findDependencies();
		removeFilter();
	}
	
	private void removeFilter(){
		this.filtered = false;
		this.filter = new String[]{};
	}
	
	private List<DependencyDTO> findDependencies(){
		List<DependencyDTO> result = new ArrayList<DependencyDTO>();
		List<FamixAssociation> allAssocations = theModel.associations;
		for(FamixAssociation assocation: allAssocations){
			if(compliesWithFunction(assocation) && compliesWithFilter(assocation)){
				DependencyDTO foundDependency = buildDependencyDTO(assocation);
				result.add(foundDependency);
			}
		}
		return result;
	}
	
	private boolean compliesWithFunction(FamixAssociation association){
		switch(this.currentFunction){
			case BOTH: return connectsBoth(association, from, to);
			case FROM: return isFrom(association, from);
			case TO: return isTo(association, to);
		}
		return false;
	}
	
	private boolean compliesWithFilter(FamixAssociation association){
		if(!filtered) return true;
		for(String value: filter){
			if(association.type.equals(value)) return true;
		}
		return false;
	}
	
	private boolean connectsBoth(FamixAssociation association, String from, String to){
		return association.from.startsWith(from) && association.to.startsWith(to);
	}
	
	private boolean isFrom(FamixAssociation association, String from){
		return association.from.startsWith(from);
	}
	
	private boolean isTo(FamixAssociation association, String to){
		return association.to.startsWith(to);
	}
	
	private DependencyDTO buildDependencyDTO(FamixAssociation association){
		String dependencyFrom = association.from;
		String dependencyTo = association.to;
		String dependencyType = determineType(association);
		int dependencyLine = association.lineNumber;
		return new DependencyDTO(dependencyFrom, dependencyTo, dependencyType, dependencyLine);
	}
	
	private String determineType(FamixAssociation assocation){
		String type = assocation.type;
		if(type.equals("extends")){
			FamixClass theClass = getClassForUniqueName(assocation.to);
			if(theClass == null) type = EXTENDS_LIBRARY;
			else if(theClass.isAbstract) type = EXTENDS_ABSTRCT;
			else type = EXTENDS_CONCRETE;
		}
		return type;
	}
	
	private FamixClass getClassForUniqueName(String uniqueName){
		return theModel.classes.get(uniqueName);
	}

}
