package husacct.validate.domain.violation;

import java.util.Date;

public class Violation implements Comparable<Violation> {
	private int linenumber;
	private int severityValue;
	private String ruletypeKey;
	private String violationtypeKey;
	private String classPathFrom;
	private String classPathTo;
	private String logicalModuleTo;
	private String logicalModuleFrom;
	private String logicalModuleFromType;
	private String logicalModuleToType;
	private String message;
	private boolean inDirect;
	private Date occured;
	
	public Violation(){
		
	}
	
	public Violation(int linenumber, int severityValue, String ruletypeKey, String violationtypeKey, String classPathFrom, String classPathTo, String logicalModuleTo, String logicalModuleToType, String logicalModuleFrom, String logicalModuleFromType, boolean inDirect){
		this.linenumber = linenumber;
		this.severityValue = severityValue;
		this.ruletypeKey = ruletypeKey;
		this.violationtypeKey = violationtypeKey;
		this.classPathFrom = classPathFrom;
		this.classPathTo = classPathTo;
		this.logicalModuleTo = logicalModuleTo;
		this.logicalModuleFrom = logicalModuleFrom;
		this.logicalModuleToType = logicalModuleToType;
		this.logicalModuleFromType = logicalModuleFromType;
		this.inDirect = inDirect;
		this.occured = new Date();
	}

	public void setLinenumber(int linenumber) {
		this.linenumber = linenumber;
	}

	public int getLinenumber() {
		return linenumber;
	}

	public void setSeverityValue(int severityValue) {
		this.severityValue = severityValue;
	}

	public int getSeverityValue() {
		return severityValue;
	}

	public void setViolationtypeKey(String violationtypeKey) {
		this.violationtypeKey = violationtypeKey;
	}

	public String getViolationtypeKey() {
		return violationtypeKey;
	}

	public void setClassPathFrom(String classPathFrom) {
		this.classPathFrom = classPathFrom;
	}

	public String getClassPathFrom() {
		return classPathFrom;
	}

	public void setClassPathTo(String classPathTo) {
		this.classPathTo = classPathTo;
	}

	public String getClassPathTo() {
		return classPathTo;
	}

	public void setLogicalModuleTo(String logicalModuleTo) {
		this.logicalModuleTo = logicalModuleTo;
	}

	public String getLogicalModuleTo() {
		return logicalModuleTo;
	}

	public void setLogicalModuleFrom(String logicalModuleFrom) {
		this.logicalModuleFrom = logicalModuleFrom;
	}

	public String getLogicalModuleFrom() {
		return logicalModuleFrom;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setIndirect(boolean inDirect) {
		this.inDirect = inDirect;
	}

	public boolean isIndirect() {
		return inDirect;
	}

	public void setOccured(Date occured) {
		this.occured = occured;
	}

	public Date getOccured() {
		return occured;
	}

	public void setRuletypeKey(String ruletypeKey) {
		this.ruletypeKey = ruletypeKey;
	}

	public String getRuletypeKey() {
		return ruletypeKey;
	}

	public String getLogicalModuleFromType() {
		return logicalModuleFromType;
	}

	public void setLogicalModuleFromType(String logicalModuleFromType) {
		this.logicalModuleFromType = logicalModuleFromType;
	}

	public String getLogicalModuleToType() {
		return logicalModuleToType;
	}

	public void setLogicalModuleToType(String logicalModuleToType) {
		this.logicalModuleToType = logicalModuleToType;
	}

	@Override
	public int compareTo(Violation o) {
		if(o.getSeverityValue() > severityValue) {
			return 1;
		}
		else {
			return 0;
		}
	}
}