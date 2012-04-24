package husacct.validate.domain.ruletype.legalityofdependency;

import husacct.common.dto.DependencyDTO;
import husacct.common.dto.RuleDTO;
import husacct.validate.domain.ruletype.Mapping;
import husacct.validate.domain.ruletype.Rule;
import husacct.validate.domain.violation.Violation;
import husacct.validate.domain.violationtype.ViolationType;

import java.util.ArrayList;
import java.util.List;

import husacct.analyse.AnalyseServiceStub;

public class IsNotAllowedToUseRule extends Rule {
	public IsNotAllowedToUseRule(String key, String category, List<ViolationType> violationtypes) {
		super(key, category, violationtypes);
	}

	@Override
	public List<Violation> check(RuleDTO appliedRule) {
		List<Violation> violations = new ArrayList<Violation>();
		AnalyseServiceStub analysestub = new AnalyseServiceStub();
		String mainLogicalModuleFrom = appliedRule.moduleFrom.logicalPath;
		String mainLogicalModuleFromType = appliedRule.moduleFrom.type;
		String mainLogicalModuleTo = appliedRule.moduleTo.logicalPath;
		String mainLogicalModuleToType = appliedRule.moduleTo.type;

		ArrayList<Mapping> physicalClasspathsFrom = getAllClasspathsFromModule(appliedRule.moduleFrom);
		ArrayList<Mapping> physicalClasspathsTo = getAllClasspathsFromModule(appliedRule.moduleTo);


		for(Mapping classPathFrom : physicalClasspathsFrom){
			for(Mapping classPathTo : physicalClasspathsTo){
				DependencyDTO[] dependencies = analysestub.getDependencies(classPathFrom.getPhysicalPath(), classPathTo.getPhysicalPath());
				for(DependencyDTO dependency: dependencies){
					Violation violation = createViolation(dependency, 1, this.key, mainLogicalModuleFrom, mainLogicalModuleTo, mainLogicalModuleFromType, mainLogicalModuleToType, false);
					violations.add(violation);
				}
			}
		}		
		return violations;		
	}
}