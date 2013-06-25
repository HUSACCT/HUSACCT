package husacct.define.domain.softwareunit;

public class SoftwareUnitDefinition {

    public enum Type {
    	CLASS, EXTERNALLIBRARY, INTERFACE, PACKAGE, REGEX, SUBSYSTEM, LIBRARY
    }

    private String name;
    private Type type;

    public SoftwareUnitDefinition(String name, Type type) {
	this.name = name;
	this.type = type;
    }

    @Override
    public boolean equals(Object o) {
	if (o instanceof SoftwareUnitDefinition) {
	    SoftwareUnitDefinition unit = (SoftwareUnitDefinition) o;
	    if (unit.getName().equals(name) && unit.getType() == type) {
		return true;
	    }
	}
	return false;
    }

    public String getName() {
	return name;
    }

    public Type getType() {
	return type;
    }

    public void setName(String name) {
	this.name = name;
    }

    public void setType(Type type) {
	this.type = type;
    }

    @Override
    public String toString() {
	return name + " - " + type.toString();
    }
}
