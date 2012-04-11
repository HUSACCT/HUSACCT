package husacct.analyse.domain.famix;

/**
 * The Class InheritanceDefinition. An InheritanceDefinition represents the
 * definition in source code of an inheritance association between two classes.
 * One class then plays the role of the superclass, the other plays the role of
 * the subclass. What exactly constitutes such a definition is a language
 * dependent issue. InheritanceDefinition is a concrete class inheriting from
 * Association . Besides inherited attributes, it has the following attributes:
 */
public class FamixInheritanceDefinition extends FamixAssociation
{

	/**
	 * The subclass. Is a name referring to the class that inherits. It uses the
	 * uniqueName of the class as a reference.
	 */
	private String subclass;

	/**
	 * The superclass. Is a name referring to the class that is inherited from.
	 * It uses the uniqueName of the class as a reference.
	 */
	private String superclass;

	/**
	 * The access control qualifier. Is a string with a language dependent
	 * interpretation, that defines how subclasses access their superclasses
	 * (for instance, 'public', 'private'...).
	 */
	private String accessControlQualifier;

	/**
	 * The index. In languages with multiple inheritance, this is the position
	 * of the superclass in the list of superclasses of one subclass. Usually
	 * this will have a null value, but it may be necessary for OO languages
	 * with multiple inheritance that resolve name collisions via the order of
	 * the superclasses (e.g., CLOS).
	 */
	private int index;

	/**
	 * Gets the subclass.
	 * 
	 * @return the subclass
	 */
	public String getSubclass()
	{
		return subclass;
	}

	/**
	 * Sets the subclass.
	 * 
	 * @param subclass the new subclass
	 */
	public void setSubclass(String subclass)
	{
		this.subclass = subclass;
	}

	/**
	 * Gets the superclass.
	 * 
	 * @return the superclass
	 */
	public String getSuperclass()
	{
		return superclass;
	}

	/**
	 * Sets the superclass.
	 * 
	 * @param superclass the new superclass
	 */
	public void setSuperclass(String superclass)
	{
		this.superclass = superclass;
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
	 * Gets the index.
	 * 
	 * @return the index
	 */
	public int getIndex()
	{
		return index;
	}

	/**
	 * Sets the index.
	 * 
	 * @param index the new index
	 */
	public void setIndex(int index)
	{
		this.index = index;
	}

}
