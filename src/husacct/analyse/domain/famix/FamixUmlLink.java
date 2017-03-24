package husacct.analyse.domain.famix;

class FamixUmlLink extends FamixObject {

	public String from = "";			
    public String to = "";
    public String attributeFrom = "";
	public boolean isComposite = false; // False if the type allows one value only, like Person; True in case of a generic type, or e.g. Person[].
    public int lineNumber;
    public String type = "";		// The value should be the same as the value of attributeof "key" of one of the enum values of UmlLinkTypes. 
    									// Because of the persistency mechanism, the type is String and notUmlLinkTypes.

     @Override
	public String toString() {
        String s = "";
        s += "\nFrom: " + from;
        s += "\nTo: " + to;
        s += "\nType: " + type + ", isComposite: " + isComposite + ", lineNr: " + lineNumber;
        s += "\nAttributeFrom: "+ attributeFrom; 
        s += "\n\n";
        return s;
    }
}
