package husacct.analyse.domain.famix;

import husacct.common.dto.DependencyDTO;
import java.util.ArrayList;
import java.util.List;

class FamixDependencyFinder extends FamixFinder{
	
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
		performQuery(FinderFunction.TO, "", to, dependencyFilter);
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
		this.removeFilter();
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
				DependencyDTO foundDependency = buildDependencyDTO(assocation, false);
				if (!result.contains(foundDependency)) result.add(foundDependency);
			}
		}
		//TODO Analyse General - Fix Indirect Dependencies
//		result.addAll(this.findIndirectDependencies(result));
		return result;
	}
	
	private List<DependencyDTO> findIndirectDependencies(List<DependencyDTO> directDependencies){
		List<DependencyDTO> result = new ArrayList<DependencyDTO>();
		for(DependencyDTO directDependency: directDependencies){
			String startFrom = directDependency.from;
			this.from = directDependency.to;
			this.currentFunction = FinderFunction.FROM;
			for(DependencyDTO indirectDependency: this.findDependencies()){
				indirectDependency.isIndirect = true;
				indirectDependency.from = startFrom;
				result.add(indirectDependency);
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
		return isFrom(association, from) && isTo(association, to);
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
	
	private DependencyDTO buildDependencyDTO(FamixAssociation association, boolean isIndirect){
		String dependencyFrom = association.from;
		String dependencyTo = association.to;
		String dependencyType = association.type;
		int dependencyLine = association.lineNumber;
		return new DependencyDTO(dependencyFrom, dependencyTo, dependencyType, isIndirect, dependencyLine);
	}
}
