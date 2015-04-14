package husacct.analyse.domain.famix;

class FamixAssociation extends FamixObject {

    public String type = "";
    public String subType = "";
    public String from = "";
    public String to = "";
    public int lineNumber;
	public boolean isIndirect = false;
	public boolean isInheritanceRelated = false; // True, if the invoked method or accessed variable is inherited. Furthermore if type starts with extends

    public String toString() {
        String s = "";
        s += "Assocation Type: " + type + ", subType: " + subType + ", Indirect: " + isIndirect;
        s += "\nFrom: " + from;
        s += "\nTo: " + to;
        s += "\nLinenumber: " + lineNumber;
        s += "\n\n";
        return s;
    }
}
