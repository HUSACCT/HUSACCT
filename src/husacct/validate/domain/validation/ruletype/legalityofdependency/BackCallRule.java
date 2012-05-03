package husacct.validate.domain.validation.ruletype.legalityofdependency;

import husacct.analyse.AnalyseServiceStub;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.RuleDTO;
import husacct.define.DefineServiceStub;
import husacct.validate.domain.ConfigurationServiceImpl;
import husacct.validate.domain.check.CheckConformanceUtil;
import husacct.validate.domain.factory.violationtype.java.ViolationTypeFactory;
import husacct.validate.domain.validation.Message;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.iternal_tranfer_objects.Mapping;
import husacct.validate.domain.validation.iternal_tranfer_objects.Mappings;
import husacct.validate.domain.validation.logicalmodule.LogicalModule;
import husacct.validate.domain.validation.logicalmodule.LogicalModules;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.domain.validation.ruletype.RuleTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public class BackCallRule extends RuleType {
	private final static EnumSet<RuleTypes> exceptionrules = EnumSet.of(RuleTypes.IS_ALLOWED);
	
	public BackCallRule(String key, String category, List<ViolationType> violationtypes, Severity severity) {
		super(key, category, violationtypes, exceptionrules, severity);
	}

	@Override
	public List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO appliedRule) {
		List<Violation> violations = new ArrayList<Violation>();
		List<List<Mapping>> toModules = new ArrayList<List<Mapping>>();
		violationtypefactory = new ViolationTypeFactory().getViolationTypeFactory(configuration);
		//TODO replace with real implementation
		AnalyseServiceStub analysestub = new AnalyseServiceStub();
		DefineServiceStub definestub = new DefineServiceStub();
		
		Mappings mappings = CheckConformanceUtil.filter(appliedRule);
		List<Mapping> moduleFrom = mappings.getMappingFrom();
		
		List<ModuleDTO> allModules = Arrays.asList(definestub.getRootModules());
		int counter = 0;
		
		for (ModuleDTO module :allModules){
			counter++;
			if(module.type.toLowerCase().equals("layer")){
				if(module.logicalPath.toLowerCase().equals(appliedRule.moduleFrom.logicalPath.toLowerCase()))
				toModules = getModulesTo(allModules, counter);
				
			}
		}			
		
		for(List<Mapping> moduleTo : toModules){
			for(Mapping classPathFrom : moduleFrom){
				for(Mapping classPathTo : moduleTo ){
					DependencyDTO[] dependencies = analysestub.getDependencies(classPathFrom.getPhysicalPath(),classPathTo.getPhysicalPath());	
					for(DependencyDTO dependency: dependencies){
						Message message = new Message(appliedRule);
	
						LogicalModule logicalModuleFrom = new LogicalModule(classPathFrom);
						LogicalModule logicalModuleTo = new LogicalModule(classPathTo);
						LogicalModules logicalModules = new LogicalModules(logicalModuleFrom, logicalModuleTo);
	
						final Severity violationTypeSeverity = getViolationTypeSeverity(dependency.type);
						Severity severity = CheckConformanceUtil.getSeverity(configuration, super.severity, violationTypeSeverity);
						Violation violation = createViolation(dependency, 1, this.key, logicalModules, false, message, severity);
						violations.add(violation);
					}
				}					
			}				
		}
		
		return violations;
	}
	
	private List<List<Mapping>> getModulesTo(List<ModuleDTO> allModules, int counter){
		List<List<Mapping>> returnList = new ArrayList<List<Mapping>>();
		for(int i=0 ; i<counter ; i++){
			returnList.add(CheckConformanceUtil.getAllModulesFromLayer(allModules.get(i)));
		}		
		return returnList;
		
	}
}
