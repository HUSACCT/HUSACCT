package husacct.analyse.domain.famix;

class FamixLibrary extends FamixEntity{

	public String belongsToLibrary;
	
	@Override
	public boolean equals(Object object){
		return object instanceof FamixLibrary && super.uniqueName.equals(((FamixLibrary) object).uniqueName);
	}
	
	@Override
	public String toString(){
		String libraryRepresentation = "";
		libraryRepresentation += "\nUnique Name: " + super.uniqueName;
		libraryRepresentation += "\nBelongs to Library: " + belongsToLibrary;
		libraryRepresentation += "\nName: " + super.name + "\n\n";
		return libraryRepresentation;
	}
}
