package husacct.analyse.domain.famix;

/**
 * The Class Class. A Class represents the definition of a class in source code.
 * What exactly constitutes such a definition is a language dependent issue.
 * Class is a concrete class inheriting from Entity. Besides inherited
 * attributes, it has the following attributes:
 */
public class FamixClass extends FamixEntity
{

	/**
	 * The is abstract. Is a predicate telling whether the class is declared
	 * abstract. Abstract classes are important in OO modelling, but how they
	 * are recognised in source code is a language dependent issue.
	 */
	boolean isAbstract;

	/**
	 * The belongs to package. Is the unique name of the package defining the
	 * scope of the class. A null belongsToPackage is allowed, it means that the
	 * class has global scope. The belongsToPackage concatenated with the name
	 * of the class must provide a unique name for that class within the model.
	 */
	String belongsToPackage;

	/**
	 * Checks if is abstract.
	 * 
	 * @return true, if is abstract
	 */
	public boolean isAbstract()
	{
		return isAbstract;
	}

	/**
	 * Sets the abstract.
	 * 
	 * @param isAbstract the new abstract
	 */
	public void setAbstract(boolean isAbstract)
	{
		this.isAbstract = isAbstract;
	}

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
	
	@Override
	public boolean equals(Object object)
	{
		return object instanceof FamixClass && getUniqueName().equals(((FamixClass) object).getUniqueName());
	}
}
