package husacct.validate.domain.validation.logicalmodule;

public class LogicalModules {
	private final LogicalModule logicalModuleFrom;
	private final LogicalModule logicalModuleTo;

	public LogicalModules(LogicalModule logicalModuleFrom, LogicalModule logicalModuleTo){
		this.logicalModuleFrom = logicalModuleFrom;
		this.logicalModuleTo = logicalModuleTo;
	}

	public LogicalModules(LogicalModule logicalModuleFrom){
		this.logicalModuleFrom = logicalModuleFrom;
		this.logicalModuleTo = new LogicalModule("","");
	}

	public LogicalModule getLogicalModuleFrom() {
		return logicalModuleFrom;
	}

	public LogicalModule getLogicalModuleTo() {
		return logicalModuleTo;
	}
}