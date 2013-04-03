package husacct.validate.domain.validation.logicalmodule;

import husacct.validate.domain.validation.internal_transfer_objects.Mapping;

public class LogicalModule {
	private final String logicalModulePath;
	private final String logicalModuleType;

	public LogicalModule(String logicalModulePath, String logicalModuleType) {
		this.logicalModulePath = logicalModulePath;
		this.logicalModuleType = logicalModuleType;
	}

	public LogicalModule(Mapping mapping) {
		if(mapping != null) {
			this.logicalModulePath = mapping.getLogicalPath();
			this.logicalModuleType = mapping.getLogicalPathType();
		}
		else {
			this.logicalModulePath = "";
			this.logicalModuleType = "";
		}
	}

	public String getLogicalModulePath() {
		return logicalModulePath;
	}

	public String getLogicalModuleType() {
		return logicalModuleType;
	}
}