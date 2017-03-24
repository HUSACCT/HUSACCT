package husacct.analyse.domain.famix;

import husacct.common.enums.DependencySubTypes;
import husacct.common.enums.DependencyTypes;

class FamixException extends FamixAssociation {

    public FamixException() {
        super.type = DependencyTypes.DECLARATION.toString();
        super.subType = DependencySubTypes.DECL_EXCEPTION.toString();
    }

    @Override
	public String toString() {
        String representation = "";
        representation += "\ntype: " + type + ", subType: " + super.subType;
        representation += "From class: " + super.from;
        representation += "\nException class referred: " + super.to;
        representation += "\nLinenumber in code: " + super.lineNumber;
        return representation;
    }
}
