package husacct.validate.domain.validation.module;

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
	
	public String toString() {
		return key;
	}
}