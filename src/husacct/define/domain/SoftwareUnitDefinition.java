package husacct.define.domain;

public class SoftwareUnitDefinition {
	
	public enum Type
	{
		PACKAGE, CLASS, METHOD
	}

	private String name;
	private Type type;

	public SoftwareUnitDefinition(String name, Type type)
	{	
		this.name = name;
		this.type = type;
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}

	
	public Type getType()
	{
		return type;
	}
	public void setType(Type type)
	{
		this.type = type;
	}

}
