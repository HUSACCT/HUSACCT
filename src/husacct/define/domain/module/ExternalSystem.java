package husacct.define.domain.module;

public class ExternalSystem extends Module{
	
	public ExternalSystem()
	{
		this("", "");
	}
	
	public ExternalSystem(String name, String description)
	{
		super(name, description);
		super.type = "ExternalLibrary";
	}
}
