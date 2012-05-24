package husacct.validate.domain.validation;

import husacct.validate.domain.validation.logicalmodule.LogicalModules;

import java.util.Calendar;

public class Violation {
	
	private int linenumber;
	private Severity severity;
	private String ruletypeKey;
	private String violationtypeKey;
	private String classPathFrom;
	private String classPathTo;
	private LogicalModules logicalModules;
	private Message message; 
	private boolean inDirect;
	private Calendar occured;
	
	public Violation(){
		
	}
	
	public Violation(int linenumber, Severity severity, String ruletypeKey, String violationtypeKey, String classPathFrom, String classPathTo, boolean inDirect, Message message, LogicalModules logicalModules){
		this.linenumber = linenumber;
		this.setSeverity(severity);
		this.ruletypeKey = ruletypeKey;
		this.violationtypeKey = violationtypeKey;
		this.classPathFrom = classPathFrom;
		this.classPathTo = classPathTo;
		this.inDirect = inDirect;
		this.occured = Calendar.getInstance();
		this.logicalModules = logicalModules;
		this.message = message;
	}

	public void setLinenumber(int linenumber) {
		this.linenumber = linenumber;
	}

	public int getLinenumber() {
		return linenumber;
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

	public void setIndirect(boolean inDirect) {
		this.inDirect = inDirect;
	}

	public boolean isIndirect() {
		return inDirect;
	}

	public void setOccured(Calendar occured) {
		this.occured = occured;
	}

	public Calendar getOccured() {
		return occured;
	}

	public void setRuletypeKey(String ruletypeKey) {
		this.ruletypeKey = ruletypeKey;
	}

	public String getRuletypeKey() {
		return ruletypeKey;
	}


	public LogicalModules getLogicalModules() {
		return logicalModules;
	}

	public void setLogicalModules(LogicalModules logicalModules) {
		this.logicalModules = logicalModules;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public Message getMessage() {
		return message;
	}

	public Severity getSeverity() {
		return severity;
	}

	public void setSeverity(Severity severity) {
		this.severity = severity;
	}
}