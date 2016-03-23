package husacct.validate.domain.validation;

import husacct.validate.domain.validation.logicalmodule.LogicalModules;

import java.util.Calendar;

public class Violation {

	// =======================================
	// VARIABLES
	// =======================================
	private int linenumber;
	private Severity severity;
	private String ruletypeKey;
	private String violationTypeKey;
	private String classPathFrom;
	private String classPathTo;
	private LogicalModules logicalModules;
	private Message message;
	private String dependencySubType;
	private boolean inDirect;
	private boolean isInheritanceRelated; // True, if the invoked method or accessed variable is inherited. Furthermore if type starts with extends. 
	private boolean isInnerClassRelated; // True, if the from-class or to-class is an inner class
	private Calendar occured;

	// =======================================
	// CONSTRUCTOR
	// =======================================
	// Violation constructor, chain config to
	// create new violation:
	// Validation error = new Validation().LineNumber(100).Message("");
	// ect...
	// =======================================
	public Violation(){
		this.linenumber = 0;
		this.severity = null;
		this.ruletypeKey = "";
		this.violationTypeKey = "";
		this.classPathFrom = "";
		this.classPathTo = "";
		this.dependencySubType = "";
		this.inDirect = false;
		this.isInheritanceRelated = false;
		this.isInnerClassRelated = false;
		this.occured = Calendar.getInstance(); 	// Occurred is by default "now".
		this.logicalModules = null;
		this.message = null;
	}

	// =======================================
	// SETTERS, chain-able.
	// =======================================
	public Violation setLineNumber(int lineNumber){
		this.linenumber = lineNumber;
		return this;
	}
	
	public Violation setSeverity(Severity severity){
		this.severity = severity;
		return this;
	}
	
	public Violation setRuletypeKey(String ruletypeKey){
		this.ruletypeKey = ruletypeKey;
		return this;
	}
	
	public Violation setViolationTypeKey(String violationTypeKey){
		this.violationTypeKey = violationTypeKey;
		return this;
	}
	
	public Violation setClassPathFrom(String classPathFrom){
		this.classPathFrom = classPathFrom;
		return this;
	}
	
	public Violation setClassPathTo(String classPathTo){
		this.classPathTo = classPathTo;
		return this;
	}
	
	public Violation setdependencySubType(String dependencySubType){
		this.dependencySubType = dependencySubType;
		return this;
	}
	
	public Violation setIsInheritanceRelated(boolean isInheritanceRelated){
		this.isInheritanceRelated = isInheritanceRelated;
		return this;
	}
	
	public Violation setIsInnerClassRelated(boolean isInnerClassRelated){
		this.isInnerClassRelated = isInnerClassRelated;
		return this;
	}
	
	public Violation setInDirect(boolean inDirect){
		this.inDirect = inDirect;
		return this;
	}
	
	public Violation setOccured(Calendar occured){
		this.occured = occured;
		return this;
	}
	
	public Violation setLogicalModules(LogicalModules logicalModules){
		this.logicalModules = logicalModules;
		return this;
	}
	
	public Violation setMessage(Message message){
		this.message = message;
		return this;
	}

	// =======================================
	// GETTERS
	// =======================================
	public int getLinenumber() {
		return linenumber;
	}

	public String getViolationTypeKey() {
		return violationTypeKey;
	}

	public String getClassPathFrom() {
		return classPathFrom;
	}

	public String getClassPathTo() {
		return classPathTo;
	}

	public String getDependencySubType() {
		return dependencySubType;
	}

	public boolean getIsInheritanceRelated() {
		return isInheritanceRelated;
	}

	public boolean getIsInnerClassRelated() {
		return isInnerClassRelated;
	}

	public boolean getIsIndirect() {
		return inDirect;
	}

	public Calendar getOccured() {
		return occured;
	}

	public String getRuletypeKey() {
		return ruletypeKey;
	}

	public LogicalModules getLogicalModules() {
		return logicalModules;
	}

	public Message getMessage() {
		return message;
	}

	public Severity getSeverity() {
		return severity;
	}
	
    public String toString() {
        String representation = "";
        representation += "\nfromClasspath: " + classPathFrom;
        representation += "\ntoClasspath: " + classPathTo;
        representation += "\nlogicalModuleFrom: " + logicalModules.getLogicalModuleFrom().getLogicalModulePath();
        representation += "\nlogicalModuleTo: " + logicalModules.getLogicalModuleTo().getLogicalModulePath();
        representation += "\nruleType: " + ruletypeKey;
        representation += ", line: " + linenumber;
        representation += ", violationType: " + violationTypeKey + ", subType: " + dependencySubType;
        representation += ", indirect: " + inDirect + ", isInheritanceRelated: " + isInheritanceRelated + ", isInnerClassRelated: " + isInnerClassRelated;
        representation += "\n";
        return representation;
    }
}