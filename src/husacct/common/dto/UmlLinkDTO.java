package husacct.common.dto;

import husacct.common.enums.UmlLinkTypes;

//Owner: Analyse

public class UmlLinkDTO extends AbstractDTO{
	public String from = "";			
    public String to = "";
    public String attributeFrom = "";
	public boolean isComposite = false; // False if the type allows one value only, like Person; True in case of a generic type, or e.g. Person[].
    public int lineNumber;
    public String type = UmlLinkTypes.ATTRIBUTELINK.toString();

    public UmlLinkDTO() {
    }
    
    public UmlLinkDTO(String from, String to, String attributeFrom, boolean isComposite, String type) {
		this.from = from;
		this.to = to;
		this.attributeFrom = attributeFrom;
		this.isComposite = isComposite;
		this.type = type;
	}
	
	@Override
	public String toString() {
        String s = "";
        s += "\nFrom: " + from;
        s += "\nTo: " + to;
        s += "\nType: " + type + ", IsComposite: " + isComposite + ", lineNr: " + lineNumber;
        s += "\nAttributeFrom: "+ attributeFrom; 
        s += "\n\n";
        return s;
    }
}
