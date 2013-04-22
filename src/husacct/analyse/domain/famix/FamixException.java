package husacct.analyse.domain.famix;

class FamixException extends FamixAssociation {

    public FamixException() {
        super.type = "Exception";
    }
    public String exceptionType;

    public String toString() {
        String representation = "";
        representation += "Exception from class: " + super.from;
        representation += "\nException class referred: " + super.to;
        representation += "\nLinenumber in code: " + super.lineNumber;
        representation += "\nType of exception: " + exceptionType + "\n\n";
        return representation;
    }
}
