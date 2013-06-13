package husaccttest.define;

import static org.junit.Assert.assertTrue;
import husacct.Main;
import husacct.ServiceProvider;
import husacct.common.dto.RuleTypeDTO;

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

	
	

RuleTypeDTO[] result = ServiceProvider.getInstance().getValidateService().getDefaultRuleTypesOfModule("Layer");


assertTrue(result.length>=0);
	 



}













}