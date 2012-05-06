package husacct.analyse.domain.famix;

class FamixAssociation extends FamixObject{
	
	public String type;
	public String from;
	public String to;
	public int lineNumber;
	
	public String toString(){
		String s = "";
		s+= "Assocation Type: " + type;
		s+= "\nFrom: " + from;
		s+= "\nTo: " + to;
		s+= "\nLinenumber: " + lineNumber;
		s+= "\n\n";
		return s;
	}
}
