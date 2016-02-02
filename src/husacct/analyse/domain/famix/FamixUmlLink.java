package husacct.analyse.domain.famix;

class FamixUmlLink extends FamixObject {

	public String from = "";			
    public String to = "";
    public String attributeFrom = "";
	public boolean isComposite = false; // False if the type allows one value only, like Person; True in case of a generic type, or e.g. Person[].
    public String linkType = "";		// The value should be the same as the value of attributeof "key" of one of the enum values of UmlLinkTypes. 
    									// Because of the persistency mechanism, the type is String and notUmlLinkTypes.

     public String toString() {
        String s = "";
        s += "\nFrom: " + from;
        s += "\nTo: " + to;
        s += "\nType: " + linkType + ", isComposite: " + isComposite;
        s += "\n\n";
        return s;
    }
}
