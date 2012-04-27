package husacct.validate.domain.validation.ruletype.legalityofdependency;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import husacct.analyse.AnalyseServiceStub;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.RuleDTO;
import husacct.define.DefineServiceStub;
import husacct.validate.domain.check.CheckConformanceUtil;
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

public class SkipCallRule extends RuleType {
	private final static EnumSet<RuleTypes> exceptionrules = EnumSet.of(RuleTypes.IS_ALLOWED);
	
	public SkipCallRule(String key, String category, List<ViolationType> violationtypes, Severity severity) {
		super(key, category, violationtypes, exceptionrules, severity);
	}

	@Override
	public List<Violation> check(RuleDTO appliedRule) {
		List<Violation> violations = new ArrayList<Violation>();
		List<List<Mapping>> toModules = new ArrayList<List<Mapping>>();
		//TODO replace with real implementation
		AnalyseServiceStub analysestub = new AnalyseServiceStub();
		DefineServiceStub definestub = new DefineServiceStub();
		
		Mappings mappings = CheckConformanceUtil.filter(appliedRule);
		List<Mapping> moduleFrom = mappings.getMappingFrom();
		
		List<ModuleDTO> allModules = Arrays.asList(definestub.getRootModules());
		
		for (ModuleDTO module :allModules){
			if(module.logicalPath == appliedRule.moduleFrom.logicalPath){
				toModules = toModules(allModules,allModules.indexOf(module));
			}
		}		
		
		for(List<Mapping> moduleTo : toModules){
			for(Mapping classPathFrom : moduleTo){
				for(Mapping classPathTo : moduleFrom ){
					DependencyDTO[] dependencies = analysestub.getDependencies(classPathFrom.getPhysicalPath(),classPathTo.getPhysicalPath());	
					for(DependencyDTO dependency: dependencies){
						Message message = new Message(appliedRule);
	
						LogicalModule logicalModuleFrom = new LogicalModule(classPathFrom);
						LogicalModule logicalModuleTo = new LogicalModule(classPathTo);
						LogicalModules logicalModules = new LogicalModules(logicalModuleFrom, logicalModuleTo);
	
						//TODO: retrieve severity for this ruletype
						Violation violation = createViolation(dependency, 1, this.key, logicalModules, false, message);
						violations.add(violation);
					}
				}					
			}				
		}
		
		return violations;
	}
	
	private List<List<Mapping>> toModules(List<ModuleDTO> allModules, int fromModuleNumber){
		List<List<Mapping>> returnList = new ArrayList<List<Mapping>>();
		for(ModuleDTO module : allModules){
			if(allModules.indexOf(module) > fromModuleNumber+1)
			returnList.add(CheckConformanceUtil.getAllModulesFromLayer(allModules.get(allModules.indexOf(module))));
		}		
		return returnList;
		
	}
}