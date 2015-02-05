package husacct.analyse.domain.famix;

class FamixInvocation extends FamixAssociation {

    public String nameOfInstance;
    public String belongsToMethod;
	public String invocationName;

    public String toString() {
        String string = "";
        string += "\n\ntype: " + super.type + "\n";
        string += "from: " + super.from + "\n";
        string += "to: " + super.to + "\n";
        string += "linenumber: " + super.lineNumber + "\n";
        string += "nameOfInstance: " + nameOfInstance + "\n";
        string += "invocationName: " + invocationName + "\n";
        string += "belongsToMethod: " + belongsToMethod + "\n";
        return string;
    }
}