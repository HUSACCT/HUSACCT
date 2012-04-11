package husacct.analyse.domain.famix;

/**
 * The Class GlobalVariable. A GlobalVariable represents the definition in
 * source code of a variable with a lifetime equal to the lifetime of a running
 * system, and which is globally accessible. What exactly constitutes such a
 * definition is a language dependent issue. GlobalVariable is a concrete class
 * inheriting from StructuralEntity . Besides inherited attributes, it has the
 * following attributes:
 */
public class FamixGlobalVariable extends FamixStructuralEntity
{

	/** The belongs to package. */
	private String belongsToPackage;

	/**
	 * Gets the belongs to package.
	 * 
	 * @return the belongs to package
	 */
	public String getBelongsToPackage()
	{
		return belongsToPackage;
	}

	/**
	 * Sets the belongs to package.
	 * 
	 * @param belongsToPackage the new belongs to package
	 */
	public void setBelongsToPackage(String belongsToPackage)
	{
		this.belongsToPackage = belongsToPackage;
	}
}
