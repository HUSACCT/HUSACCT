package husacct.validate.domain.validation.logicalmodule;

public class LogicalModules {
	private LogicalModule logicalModuleFrom;
	private LogicalModule logicalModuleTo;

	public LogicalModules(LogicalModule logicalModuleFrom, LogicalModule logicalModuleTo){
		this.setLogicalModuleFrom(logicalModuleFrom);
		this.setLogicalModuleTo(logicalModuleTo);
	}

	public LogicalModule getLogicalModuleFrom() {
		return logicalModuleFrom;
	}

	public void setLogicalModuleFrom(LogicalModule logicalModuleFrom) {
		this.logicalModuleFrom = logicalModuleFrom;
	}

	public LogicalModule getLogicalModuleTo() {
		return logicalModuleTo;
	}

	public void setLogicalModuleTo(LogicalModule logicalModuleTo) {
		this.logicalModuleTo = logicalModuleTo;
	}
}