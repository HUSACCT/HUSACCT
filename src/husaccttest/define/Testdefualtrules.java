package husaccttest.define;

import static org.junit.Assert.*;

import java.net.URL;
import java.util.ArrayList;

import husacct.Main;
import husacct.ServiceProvider;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ProjectDTO;
import husacct.common.dto.RuleTypeDTO;
import husacct.define.domain.Application;
import husacct.validate.ValidateServiceImpl;
import husacct.validate.domain.DomainServiceImpl;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;



public class Testdefualtrules

{
	
	
	@Before
	public void setup()
	{
		Main m = new Main(new String[]{});
		
	}

@Test
public void testChekdefualtRules()
{

	ApplicationDTO app = ServiceProvider.getInstance().getDefineService().getApplicationDetails();	
	
	app.projects.add(new ProjectDTO("test", new ArrayList<String>(), "java", "1.0", "hey", new ArrayList<AnalysedModuleDTO>()));
RuleTypeDTO[] result = ServiceProvider.getInstance().getValidateService().getDefaultRuleTypesOfModule("layer");
System.out.println(result.length);
	 



}













}