/*package husaccttest.validate;

import java.util.ArrayList;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.ProjectDTO;
import husacct.common.dto.ViolationDTO;
import husacct.define.IDefineService;
import husacct.define.domain.AppliedRule;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.module.Layer;
import husacct.define.domain.module.Module;
import husacct.define.domain.module.SubSystem;
import husacct.validate.IValidateService;

public class DefineTestSoftwareArchitectureFacade {
	private IAnalyseService analyse;
	private IDefineService define;
	private IValidateService validate;
	
	private SoftwareArchitecture softwareA;
	private Module moduleFrom;
	private Module moduleTo;
	private Module subModule1;
	private Module subModule2;
	private AppliedRule rule;
	
	private ProjectDTO testProject;
	private ArrayList<String> projectPaths;
	
	
	public DefineTestSoftwareArchitectureFacade() {
		setInstances();
		
		buildApplication();
		buildApplicationPhysicalArchitecture();
		buildApplicationLogicArchitecture();
		addAppliedRule();
	}
	
	private void setInstances() {
		SoftwareArchitecture.setInstance(new SoftwareArchitecture("TestSoftwareArchitecture", "description"));
		softwareA = SoftwareArchitecture.getInstance();
		
		ServiceProvider.getInstance().getControlService();
		analyse = ServiceProvider.getInstance().getAnalyseService();
		define = ServiceProvider.getInstance().getDefineService();
		validate = ServiceProvider.getInstance().getValidateService();
	}
	
	private void buildApplication() {
		projectPaths = new ArrayList<String>();
		projectPaths.add("presentation.legal.TwitterIlLegal");
		projectPaths.add("infrastructure.socialmedia.twitter.LegalInformation");
		projectPaths.add("infrastructure.socialmedia.twitter.TwitterFacade");
		
		ArrayList<ProjectDTO> project = new ArrayList<ProjectDTO>();
		testProject = new ProjectDTO("TEST_PROJECT", projectPaths, "Java", "1.0", "...", new ArrayList<AnalysedModuleDTO>());
		project.add(testProject);
		
		define.createApplication("TEST_APPLICATION", project, "1.0");
	}
	
	private void buildApplicationPhysicalArchitecture() {
		analyse.analyseApplication(testProject);
	}
	
	private void buildApplicationLogicArchitecture() {
		moduleFrom = new Layer("Infrastructure");
		moduleTo = new Layer("");
		subModule1 = new SubSystem("SocialMedia");
		subModule2 = new SubSystem("Twitter");
		
		moduleFrom.addSubModule(subModule1);
		subModule1.addSubModule(subModule2);
		softwareA.addModule(moduleFrom);
		softwareA.addModule(moduleTo);
	}
	
	private void addAppliedRule() {
		this.rule = new AppliedRule("FacadeConvention", "", moduleFrom, moduleTo);
		softwareA.addAppliedRule(rule);
	}
	
	public boolean checkFacadeConventionRule() {
		validate.checkConformance();
		ViolationDTO[] violations = validate.getViolationsByPhysicalPath(projectPaths.get(0), projectPaths.get(1));
		
		if(violations.length > 0) {
			return true;
		} else {
			return false;
		}
	}
}*/