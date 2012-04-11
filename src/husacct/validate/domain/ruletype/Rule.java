package husacct.validate.domain.ruletype;

import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.RuleDTO;
import husacct.validate.domain.factory.RuletypesFactory;
import husacct.validate.domain.violation.Violation;
import husacct.validate.domain.violationtype.ViolationType;

import java.util.ArrayList;
import java.util.List;


public abstract class Rule {
	protected String key;
	protected String descriptionKey;
	protected String categoryKey;
	protected String exceptionKeys;
	protected List<Rule> exceptionrules;
	protected List<ViolationType> violationtypes;
	
	protected RuletypesFactory ruletypelanguagefactory;

	public Rule(String key, String categoryKey){
		this.key = key;
		this.descriptionKey = key + "Description";
		this.categoryKey = categoryKey;
	}

	public Rule(String key, String categoryKey, List<ViolationType> violationtypes){
		this.key = key;
		this.descriptionKey = key + "Description";
		this.categoryKey = categoryKey;
		this.violationtypes = violationtypes;
	}

	public String getKey(){
		return key;
	}

	public String getDescriptionKey(){
		return descriptionKey;
	}

	public String getCategoryKey(){
		return categoryKey;
	}

	public List<ViolationType> getViolationTypes(){
		return violationtypes;
	}

	//TODO add appliedrule as DTO
	public abstract List<Violation> check(RuleDTO appliedRule);
	
	protected ArrayList<Mapping> getAllClasspathsFromModule(ModuleDTO root){
		ArrayList<Mapping> classpaths = new ArrayList<Mapping>();
		
		for(String classpath : root.physicalPaths){
			classpaths.add(new Mapping(root.logicalPath, classpath));
		}		
		return getAllClasspathsFromModule(root, classpaths);
	}

	private ArrayList<Mapping> getAllClasspathsFromModule(ModuleDTO module, ArrayList<Mapping> classpaths){		
		for(ModuleDTO submodule : module.subModules){
			for(String classpath : submodule.physicalPaths){
				classpaths.add(new Mapping(submodule.logicalPath, classpath));
			}
			return getAllClasspathsFromModule(submodule, classpaths);
		}
		return classpaths;
	}
	
	protected Violation createViolation(DependencyDTO dependency, int severityValue, String ruleKey, String logicalModuleFrom, String logicalModuleTo,String logicalModuleToType, String logicalModuleFromType, boolean inDirect){
		return new Violation(dependency.lineNumber, severityValue, ruleKey, dependency.type, dependency.from, dependency.to, logicalModuleTo, logicalModuleToType, logicalModuleFrom, logicalModuleFromType, inDirect);
	}
}