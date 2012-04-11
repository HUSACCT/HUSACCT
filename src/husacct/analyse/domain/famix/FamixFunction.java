package husacct.analyse.domain.famix;

/**
 * The Class Function. A Function represents the definition in source code of an
 * aspect of global behaviour. What exactly constitutes such a definition is a
 * language dependent issue. Function is a concrete class inheriting from
 * BehaviouralEntity. Besides inherited attributes, it has the following
 * attributes:
 */
public class FamixFunction extends FamixBehaviouralEntity
{

	/**
	 * The belongs to package. Is the unique name of the package defining the
	 * scope of the function. A null belongsToPackage is allowed, it means that
	 * the function has global scope. The belongsToPackage concatenated with the
	 * name of the function must provide a unique name for that class within the
	 * model.
	 */
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
