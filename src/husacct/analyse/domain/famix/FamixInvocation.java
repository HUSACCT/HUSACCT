package husacct.analyse.domain.famix;

class FamixInvocation extends FamixAssociation{

//	public String invokedBy;
//	public String invokes;
//	public String base;
//	invocConstructor, accessPropertyOrField of invocMethod
	
	public String invocationType;
	public String nameOfInstance;
	public String inovcationName;
//	public String sourceFilePath;
	
	public String toString(){
		String string = "";
		string += "\n\ntype: " + super.type + "\n";
		string += "Invocation type: " + invocationType + "\n";
		string += "from: " + super.from + "\n";
		string += "to: " + super.to + "\n";
		string += "linenumber: " + super.lineNumber + "\n";
		string += "nameOfInstance: " + nameOfInstance + "\n";
		string += "inovcationName: " + inovcationName + "\n";
		return string;
	}
}