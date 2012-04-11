package husacct.analyse.domain.famix;

/**
 * The Class Attribute. An Attribute represents the definition in source code of
 * an aspect of the state of a class. What exactly constitutes such a definition
 * is a language dependent issue. Attribute is a concrete class inheriting from
 * StructuralEntity . Besides inherited attributes, it has the following
 * attributes:
 */
public class FamixAttribute extends FamixStructuralEntity
{

	/**
	 * The belongs to class. Is a name referring to the class owning the
	 * attribute. It uses the uniqueName of the class as a reference.
	 */
	private String belongsToClass;

	/**
	 * The access control qualifier. Is a string with a language dependent
	 * interpretation, that defines who is allowed to access it (for instance,
	 * 'public', 'private'...).
	 */
	private String accessControlQualifier;

	/**
	 * The has class scope. Is a predicate telling whether the attribute has
	 * class scope (i.e., shared memory location for all instances of the class)
	 * or instance scope (i.e., separate memory location for each instance of
	 * the class). For example, static attributes in C++ and Java have a
	 * hasClassScope attribute set to true.
	 */
	private boolean hasClassScope;

	/**
	 * Gets the belongs to class.
	 * 
	 * @return the belongs to class
	 */
	public String getBelongsToClass()
	{
		return belongsToClass;
	}

	/**
	 * Sets the belongs to class.
	 * 
	 * @param belongsToClass the new belongs to class
	 */
	public void setBelongsToClass(String belongsToClass)
	{
		this.belongsToClass = belongsToClass;
	}

	/**
	 * Gets the access control qualifier.
	 * 
	 * @return the access control qualifier
	 */
	public String getAccessControlQualifier()
	{
		return accessControlQualifier;
	}

	/**
	 * Sets the access control qualifier.
	 * 
	 * @param accessControlQualifier the new access control qualifier
	 */
	public void setAccessControlQualifier(String accessControlQualifier)
	{
		this.accessControlQualifier = accessControlQualifier;
	}

	/**
	 * Checks if is checks for class scope.
	 * 
	 * @return true, if is checks for class scope
	 */
	public boolean isHasClassScope()
	{
		return hasClassScope;
	}

	/**
	 * Sets the checks for class scope.
	 * 
	 * @param hasClassScope the new checks for class scope
	 */
	public void setHasClassScope(boolean hasClassScope)
	{
		this.hasClassScope = hasClassScope;
	}

}
