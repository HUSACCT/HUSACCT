package husacct.validate.domain.validation.ruletype.legalityofdependency;

import husacct.analyse.AnalyseServiceStub;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.RuleDTO;
import husacct.validate.domain.check.CheckConformanceUtil;
import husacct.validate.domain.validation.Message;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.iternal_tranfer_objects.Mapping;
import husacct.validate.domain.validation.iternal_tranfer_objects.Mappings;
import husacct.validate.domain.validation.logicalmodule.LogicalModule;
import husacct.validate.domain.validation.logicalmodule.LogicalModules;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.domain.validation.ruletype.RuleTypes;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class IsOnlyModuleAllowedToUseRule extends RuleType{
	private final static EnumSet<RuleTypes> exceptionrules = EnumSet.of(RuleTypes.IS_ALLOWED);

	public IsOnlyModuleAllowedToUseRule(String key, String category, List<ViolationType> violationtypes) {
		super(key, category, violationtypes, exceptionrules);
	}

	@Override
	public List<Violation> check(RuleDTO appliedRule) {
		List<Violation> violations = new ArrayList<Violation>();
		//TODO replace with real implementation
		AnalyseServiceStub analysestub = new AnalyseServiceStub();

		Mappings mappings = CheckConformanceUtil.filter(appliedRule);
		List<Mapping> physicalClasspathsFrom = mappings.getMappingFrom();
		List<Mapping> physicalClasspathsTo = mappings.getMappingTo();

		for(Mapping classPathFrom : physicalClasspathsFrom){
			for(Mapping classPathTo : physicalClasspathsTo ){
				//TODO: getDependencyTo(TO) not implemented yet
				DependencyDTO[] dependencies = analysestub.getDependenciesTo(classPathTo.getPhysicalPath());
				DependencyDTO[] allowedDependencies = analysestub.getDependencies(classPathFrom.getPhysicalPath(),classPathTo.getPhysicalPath());
				for(DependencyDTO dependency: dependencies){
					for(DependencyDTO allowedDependency: allowedDependencies){
						if(dependency != allowedDependency){
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
		}
		return violations;
	}
}
