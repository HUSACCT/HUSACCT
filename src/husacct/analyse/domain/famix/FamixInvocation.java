package husacct.analyse.domain.famix;

class FamixInvocation extends FamixAssociation{

	public String invokedBy;
	public String invokes;
	public String base;
			
	public int lineNumber;
	public String sourceFilePath;
	
	public String toString(){
		String string = "";
		string += "Invoked By: " + invokedBy;
		string += "\nInvokes: " + invokes;
		string += "\nBase: " + base;
		string += "\n lineNumber: " + lineNumber;
		string += "\nSourcefilePath: " + sourceFilePath;
		return string;
	}
}
