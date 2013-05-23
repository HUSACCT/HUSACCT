package husacct.define.domain;

public class SoftwareUnitDefinition {
	
	public enum Type{
		PACKAGE, CLASS, INTERFACE,EXTERNALLIBRARY,SUBSYSTEM,REGEX
	}

	private String name;
	private Type type;

	public SoftwareUnitDefinition(String name, Type type){	
		this.name = name;
		this.type = type;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public Type getType(){
		return type;
	}
	public void setType(Type type){
		this.type = type;
	}
	
	public String toString(){
		return name + " - " + type.toString();
	}
	
	public boolean equals(Object o){
		if (o instanceof SoftwareUnitDefinition){
			SoftwareUnitDefinition unit = (SoftwareUnitDefinition) o;
			if (unit.getName().equals(this.name) && unit.getType() == this.type){
				return true;
			}
		}
		return false;
	}
}
