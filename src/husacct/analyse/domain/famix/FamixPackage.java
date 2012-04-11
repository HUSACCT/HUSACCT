package husacct.analyse.domain.famix;

/**
 * The Class Package. A Package represents a named sub-unit of a source code
 * model, for example namespaces in C++, and packages in Java. What exactly
 * constitutes such a sub-unit is a language dependent issue. Packages and other
 * entities can only belong to one Package. Package is a concrete class
 * inheriting from Entity. Besides inherited attributes, it has the following
 * attributes:
 */
public class FamixPackage extends FamixEntity
{

	/**
	 * The belongs to package. Is the unique name of the package containing this
	 * package. A null value represents the fact that there is no containing
	 * package.
	 */
	private String belongsToPackage;

	/**
	 * Gets the belongs to package.
	 * 
	 * @return the belongsToPackage
	 */
	public String getBelongsToPackage()
	{
		return belongsToPackage;
	}

	/**
	 * Sets the belongs to package.
	 * 
	 * @param belongsToPackage the belongsToPackage to set
	 */
	public void setBelongsToPackage(String belongsToPackage)
	{
		this.belongsToPackage = belongsToPackage;
	}
	
	@Override
	public boolean equals(Object object)
	{
		return object instanceof FamixPackage && getUniqueName().equals(((FamixPackage) object).getUniqueName());
	}

}
