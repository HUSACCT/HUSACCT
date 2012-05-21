package husacct.analyse.domain.famix;

class FamixLocalVariable extends FamixStructuralEntity{

	public String belongsToMethod;
	
	public String toString(){
		String importRepresentation = "";
		importRepresentation += "\nname: " + super.name;
		importRepresentation += "\nuniquename: " + super.uniqueName;
		importRepresentation += "\nbelongsToClass: " + super.belongsToClass;
<<<<<<< HEAD
		importRepresentation += "\nbelongsToMethod: " + belongsToMethod;
		importRepresentation += "\ndeclareType: " + super.declareType;
=======
		importRepresentation += "\nbelongsToMethod: " + this.belongsToMethod;
		importRepresentation += "\ndeclareType: " + super.declareType;
		importRepresentation += "\nlineNumber: " + super.lineNumber;
>>>>>>> 4a0b025e09524d932a7f287ceb42721caba2a26d
		importRepresentation += "\n";
		importRepresentation += "\n";
		return importRepresentation;
	}
<<<<<<< HEAD
	
=======
>>>>>>> 4a0b025e09524d932a7f287ceb42721caba2a26d
}
