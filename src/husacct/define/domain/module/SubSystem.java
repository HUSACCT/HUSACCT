package husacct.define.domain.module;

public class SubSystem extends Module {
	
	public SubSystem()
	{
		this("", "");
	}
	
	public SubSystem(String name, String description)
	{
		super(name, description);
		super.type = "SubSystem";
	}

}
