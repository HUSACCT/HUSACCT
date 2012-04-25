package husacct.validate.domain.validation.logicalmodule;

import husacct.validate.domain.validation.iternal_tranfer_objects.Mapping;

public class LogicalModule {
	private String logicalModulePath;
	private String logicalModuleType;
	
	public LogicalModule(String logicalModulePath, String logicalModuleType){
		this.logicalModulePath = logicalModulePath;
		this.logicalModuleType = logicalModuleType;
	}
	
	public LogicalModule(Mapping mapping){
		this.logicalModulePath = mapping.getLogicalPath();
		this.logicalModuleType = mapping.getLogicalPathType();
	}

	public String getLogicalModulePath() {
		return logicalModulePath;
	}

	public void setLogicalModulePath(String logicalModulePath) {
		this.logicalModulePath = logicalModulePath;
	}

	public String getLogicalModuleType() {
		return logicalModuleType;
	}

	public void setLogicalModuleType(String logicalModuleType) {
		this.logicalModuleType = logicalModuleType;
	}
}