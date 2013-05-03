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
	private String violationtypeKey;
	private String classPathFrom;
	private String classPathTo;
	private LogicalModules logicalModules;
	private Message message;
	private boolean inDirect;
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
		this.violationtypeKey = "";
		this.classPathFrom = "";
		this.classPathTo = "";
		this.inDirect = false;
		this.occured = Calendar.getInstance();
		this.logicalModules = null;
		this.message = null;
	}

	// =======================================
	// SETTERS, chain-able.
	// =======================================
	public Violation lineNumber(int lineNumber){
		this.linenumber = lineNumber;
		return this;
	}
	
	public Violation severity(Severity severity){
		this.severity = severity;
		return this;
	}
	
	public Violation ruletypeKey(String ruletypeKey){
		this.ruletypeKey = ruletypeKey;
		return this;
	}
	
	public Violation violationtypeKey(String violationtypeKey){
		this.violationtypeKey = violationtypeKey;
		return this;
	}
	
	public Violation classPathFrom(String classPathFrom){
		this.classPathFrom = classPathFrom;
		return this;
	}
	
	public Violation classPathTo(String classPathTo){
		this.classPathTo = classPathTo;
		return this;
	}
	
	// inDirect is by default false
	public Violation inDirect(boolean inDirect){
		this.inDirect = inDirect;
		return this;
	}
	
	// Occured is by default "now".
	public Violation occured(Calendar occured){
		this.occured = occured;
		return this;
	}
	
	public Violation logicalModules(LogicalModules logicalModules){
		this.logicalModules = logicalModules;
		return this;
	}
	
	public Violation message(Message message){
		this.message = message;
		return this;
	}

	// =======================================
	// GETTERS
	// =======================================
	public int getLinenumber() {
		return linenumber;
	}

	public String getViolationtypeKey() {
		return violationtypeKey;
	}

	public String getClassPathFrom() {
		return classPathFrom;
	}

	public String getClassPathTo() {
		return classPathTo;
	}

	public boolean isIndirect() {
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
}