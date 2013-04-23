package husacct.analyse.domain.famix;

class FamixInvocation extends FamixAssociation {

    public String nameOfInstance;
    public String inovcationName;
    public String belongsToMethod;

    public String toString() {
        String string = "";
        string += "\n\ntype: " + super.type + "\n";
        string += "from: " + super.from + "\n";
        string += "to: " + super.to + "\n";
        string += "linenumber: " + super.lineNumber + "\n";
        string += "nameOfInstance: " + nameOfInstance + "\n";
        string += "inovcationName: " + inovcationName + "\n";
        string += "belongsToMethod: " + belongsToMethod + "\n";
        return string;
    }
}