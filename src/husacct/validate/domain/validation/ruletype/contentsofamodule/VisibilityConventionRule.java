package husacct.validate.domain.validation.ruletype.contentsofamodule;

import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.RuleDTO;
import husacct.validate.domain.check.CheckConformanceUtil;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.factory.violationtype.ViolationTypeFactory;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.iternal_tranfer_objects.Mapping;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.domain.validation.ruletype.RuleTypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class VisibilityConventionRule extends RuleType {
	private final static EnumSet<RuleTypes> exceptionrules = EnumSet.of(RuleTypes.VISIBILITY_CONVENTION);
	
	public VisibilityConventionRule(String key, String category, List<ViolationType> violationtypes, Severity severity) {
		super(key, category, violationtypes, exceptionrules, severity);
	}
	
	@Override
	public List<Violation> check(ConfigurationServiceImpl configuration, RuleDTO rootRule, RuleDTO currentRule) {		
		this.violations = new ArrayList<Violation>();
		this.violationtypefactory = new ViolationTypeFactory().getViolationTypeFactory(configuration);
		
		this.mappings = CheckConformanceUtil.filter(currentRule);
		this.physicalClasspathsFrom = mappings.getMappingFrom();
		
		int violationCounter=0;
		for(Mapping physicalClasspathFrom : physicalClasspathsFrom ){
			for(AnalysedModuleDTO analyzedModule : analyseService.getChildModulesInModule(physicalClasspathFrom.getPhysicalPath())){
				for(String violationKey : currentRule.violationTypeKeys){
					if(!analyzedModule.visibility.toLowerCase().equals(violationKey.toLowerCase())){
						violationCounter++;
					}
					if(violationCounter < currentRule.violationTypeKeys.length){
						System.out.println("violtion!");
					}
					violationCounter =0;
				}
			}
		}
		
		return Collections.emptyList();
	}
}