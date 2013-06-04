package husaccttest.define;

import husacct.ServiceProvider;
import husacct.common.dto.CategoryDTO;
import husacct.common.dto.RuleTypeDTO;
import husacct.common.dto.ViolationTypeDTO;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.module.ExternalSystem;
import husacct.define.domain.module.Layer;
import husacct.define.domain.module.Module;
import husacct.define.domain.module.SubSystem;

import java.util.ArrayList;

public class BenchmarkTests {
	private SoftwareArchitecture sA = SoftwareArchitecture.getInstance();
	private CategoryDTO[] categories = ServiceProvider.getInstance().getValidateService().getCategories();
	

	public void setUp(){
		sA = new SoftwareArchitecture("Test architecture", "This architecture is used for testing purposes");
		SoftwareArchitecture.setInstance(sA);
		
		Module layer1 = new Layer("Presentation layer", "", 1);
		Module layer2 = new Layer("Domain layer", "", 2);
		Module layer3 = new Layer("Infrastructure layer", "", 3);
		Module externalLibrary1 = new ExternalSystem("lib", "");
		
		//layer 1
		Module module1 = new SubSystem("gui", "");
		Module module2 = new SubSystem("annotations", "");
		Module module3 = new SubSystem("exception", "");
		Module module4 = new SubSystem("upload", "");
		Module module5 = new SubSystem("legal", "");
		Module module6 = new SubSystem("post", "");
		layer1.addSubModule(module1);
		layer1.addSubModule(module2);
		layer1.addSubModule(module3);
		layer1.addSubModule(module4);
		layer1.addSubModule(module5);
		layer1.addSubModule(module6);
		
		//layer 2
		Module module7 = new SubSystem("hyves", "");
		Module module8 = new SubSystem("google_plus", "");
		Module module9 = new SubSystem("gowalla", "");
		Module module10 = new SubSystem("facebook", "");
		Module module11 = new SubSystem("flickr", "");
		Module module12 = new SubSystem("orkut", "");
		Module module13 = new SubSystem("linkedin", "");
		Module module14 = new SubSystem("music", "");
		Module module15 = new SubSystem("lastfm", "");
		Module module16 = new SubSystem("netlog", "");
		Module module17 = new SubSystem("blog", "");
		Module module18 = new SubSystem("foursquarealternative", "");
		Module module19 = new SubSystem("spotify", "");
		Module module20 = new SubSystem("stumbleupon", "");
		Module module21 = new SubSystem("locationbased", "");
		Module module22 = new SubSystem("language", "");
		Module module23 = new SubSystem("shortcharacter", "");
		Module module24 = new SubSystem("pinterest", "");
		layer2.addSubModule(module7);
		layer2.addSubModule(module8);
		layer2.addSubModule(module9);
		layer2.addSubModule(module10);
		layer2.addSubModule(module11);
		layer2.addSubModule(module12);
		layer2.addSubModule(module13);
		layer2.addSubModule(module14);
		layer2.addSubModule(module15);
		layer2.addSubModule(module16);
		layer2.addSubModule(module17);
		layer2.addSubModule(module18);
		layer2.addSubModule(module19);
		layer2.addSubModule(module20);
		layer2.addSubModule(module21);
		layer2.addSubModule(module22);
		layer2.addSubModule(module23);
		layer2.addSubModule(module24);
		
		//layer 3
		Module module25 = new SubSystem("socialmedia", "");
		Module module26 = new SubSystem("database", "");
		Module module27 = new SubSystem("asocialmedia", "");
		Module module28 = new SubSystem("blog", "");
		layer3.addSubModule(module25);
		layer3.addSubModule(module26);
		layer3.addSubModule(module27);
		layer3.addSubModule(module28);
		
		//external library 1
		
		
		sA.addSeperatedModule(layer1);
		sA.addSeperatedModule(layer2);
		sA.addSeperatedModule(layer3);
		sA.addSeperatedModule(externalLibrary1);
		
		//Rules
//		AppliedRule rule1 = new AppliedRule("IsOnlyModuleAllowedToUse", "Only the classes in package infrastructure.socialmedia are only allowed to use the JDBC library (derbyclient.jar)", 
//				getViolationTypeByRuleType("IsOnlyModuleAllowedToUse"),	"", module25, externalLibrary1, true);
		
		//sA.addAppliedRule(rule1);
	}
	
	private String[] getViolationTypeByRuleType(String ruleTypeKey){
		String[] violationTypes = new String[]{};
		for (CategoryDTO categorie : categories) {
			for (RuleTypeDTO ruleTypeDTO : categorie.ruleTypes){
				if (ruleTypeDTO.getKey().equals("ruleTypeKey")){
					ViolationTypeDTO[] dtos = ruleTypeDTO.getViolationTypes();
					ArrayList<String> violationTypeKeys = new ArrayList<String>();
					for (ViolationTypeDTO dto : dtos){
						violationTypeKeys.add(dto.key);
					}
					violationTypes = new String[violationTypeKeys.size()]; violationTypeKeys.toArray(violationTypes);
				}
			}
		}
		return violationTypes;
	}
	
}	
	
	
	
	

