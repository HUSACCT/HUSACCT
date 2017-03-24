package husacct.common.enums;

//Owner: Validate

public enum ModuleTypes {
	COMPONENT("Component"),
	EXTERNAL_LIBRARY("ExternalLibrary"),
	FACADE("Facade"),
	LAYER("Layer"),
	SUBSYSTEM("SubSystem");
	
	private String key;
	
	private ModuleTypes(String key) {
		this.key = key;
	}
	
	@Override
	public String toString() {
		return key;
	}
}