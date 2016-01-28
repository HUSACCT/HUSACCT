package husacct.analyse.domain.famix;

class FamixUmlLink extends FamixObject {

	public String from = "";			
    public String to = "";
    public LinkType linkType = LinkType.ATTRIBUTELINK;
    public String attributeFrom = "";
	public boolean isComposite = false; // False if the type allows one value only, like Person; True in case of a generic type, or e.g. Person[].

    public String toString() {
        String s = "";
        s += "\nFrom: " + from;
        s += "\nTo: " + to;
        s += "\nType: " + linkType + ", isComposite: " + isComposite;
        s += "\n\n";
        return s;
    }
    
    public enum LinkType {
    	INHERITANCELINK,
        IMPLEMENTSLINK,
        ATTRIBUTELINK;
      }

}
