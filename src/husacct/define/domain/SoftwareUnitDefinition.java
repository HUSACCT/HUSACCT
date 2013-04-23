package husacct.define.domain;

public class SoftwareUnitDefinition {
	
	public enum Type
	{
		PACKAGE, CLASS, INTERFACE,EXTERNALLIBRARY,SUBSYSTEM,REGIX
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
	
	public String toString(){
		String s = "";
		s+= name + " - " + type.toString();
		return s;
	}
	
	public boolean equals(Object o){
		boolean isEqual = false;
		if (o instanceof SoftwareUnitDefinition){
			SoftwareUnitDefinition unit = (SoftwareUnitDefinition) o;
			if (unit.getName().equals(this.name) && unit.getType() == this.type){
				isEqual = true;
			}
		}
		return isEqual;
	}
}
