package husacct.analyse.domain.famix;

/* FamixInvocation is not used only for method calls, variable access, and references. 
 * Based on the parser data, the specific types cannot be determined, often.
 * In the post processing, the type and subtype are determined accurately.
 */
class FamixInvocation extends FamixAssociation {

    public String belongsToMethod = "";			// Unique name of the method of the from-class that contains the association-causing statement.
	public String originalToString = ""; 	// Full original to-string; may be chained/composed
	public String statement = ""; 			// Part of originalToString that causes association
    public String remainingToString = ""; 	// toRemainderChainingInvocation
	public String usedEntity = ""; 			// uniqueName of used FamixStructuralEntity, FamixBehaviouralEntity, or "" (not found)
	

    public String toString() {
        String string = "";
        string += "\n\ntype: " + super.type + ", subType: " + super.subType + "\n";
        string += "from: " + super.from + "\n";
        string += "to: " + super.to + "\n";
        string += "usedEntity: " + usedEntity + "\n";
        string += "linenumber: " + super.lineNumber + "\n";
        string += "belongsToMethod: " + belongsToMethod + "\n";
        string += "originalToString: " + originalToString + "\n";
        string += "statement: " + statement + ", remainingToString: " + remainingToString + "\n";
        string += "belongsToMethod: " + belongsToMethod + "\n";
        return string;
    }
}