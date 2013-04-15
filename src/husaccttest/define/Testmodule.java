package husaccttest.define;
import husacct.define.*;
import husacct.define.domain.module.Module;
public class Testmodule {

@org.junit.Test
public void Test()
{
	Module  m1= new Module();
	m1.setId(3);
	
	m1.addSubModule(new Module());
	
}

}
