package husacct.analyse.serviceinterface.dto;

import husacct.analyse.serviceinterface.enums.UmlLinkTypes;
import husacct.common.dto.AbstractDTO;

public class UmlLinkDTO extends AbstractDTO{
	public String from = "";			
    public String to = "";
    public String attributeFrom = "";
	public boolean isComposite = false; // False if the type allows one value only, like Person; True in case of a generic type, or e.g. Person[].
    public String linkType = UmlLinkTypes.ATTRIBUTELINK.toString();

    public UmlLinkDTO() {
    }
    
    public UmlLinkDTO(String from, String to, String attributeFrom, boolean isComposite, String linkType) {
		this.from = from;
		this.to = to;
		this.attributeFrom = attributeFrom;
		this.isComposite = isComposite;
		this.linkType = linkType;
	}
	
	public String toString() {
        String s = "";
        s += "\nFrom: " + from;
        s += "\nTo: " + to;
        s += "\nAttributeFrom: "+ attributeFrom + ", Type: " + linkType + ", IsComposite: " + isComposite;
        s += "\n\n";
        return s;
    }
}
