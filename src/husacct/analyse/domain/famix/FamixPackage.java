package husacct.analyse.domain.famix;

public class FamixPackage extends FamixEntity{

	public String belongsToPackage;
	
	@Override
	public boolean equals(Object object){
		return object instanceof FamixPackage && super.uniqueName.equals(((FamixPackage) object).uniqueName);
	}
}
