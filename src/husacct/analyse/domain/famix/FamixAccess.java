package husacct.analyse.domain.famix;

/**
 * The Class Access. An Access represents the definition in source code of a
 * BehaviouralEntity accessing a StructuralEntity. Depending on the level of
 * extraction (see Table 1, p. 7), that StructuralEntity may be an attribute, a
 * local variable, an argument, a global variable.... What exactly constitutes
 * such a definition is a language dependent issue. However, when the same
 * structural entity is accessed more than once in a method body, then parsers
 * should generate a separate access-association for each occurrence.
 */
class FamixAccess extends FamixAssociation
{

	/**
	 * The accesses. s a name referring to the variable being accessed. It uses
	 * the uniqueName of the variable as a reference.
	 */
	private String accesses;

	/**
	 * The accessed in. Is a name referring to the method doing the access. It
	 * uses the uniqueName of the method as a reference.
	 */
	private String accessedIn;

	/**
	 * The is accessed l value. Is a predicate telling whether the value was
	 * accessed as Lvalue, i.e. a location value or a value on the left side of
	 * an assignment. When the predicate is true, the memory location denoted by
	 * the variable might change its value; false means that the contents of the
	 * memory location is read; null means that it is unknown. Note that LValue
	 * is the inverse of RValue.
	 */
	private boolean isAccessedLValue = false;

	/**
	 * Gets the accesses.
	 * 
	 * @return the accesses
	 */
	public String getAccesses()
	{
		return accesses;
	}

	/**
	 * Sets the accesses.
	 * 
	 * @param accesses the new accesses
	 */
	public void setAccesses(String accesses)
	{
		this.accesses = accesses;
	}

	/**
	 * Gets the accessed in.
	 * 
	 * @return the accessed in
	 */
	public String getAccessedIn()
	{
		return accessedIn;
	}

	/**
	 * Sets the accessed in.
	 * 
	 * @param accessedIn the new accessed in
	 */
	public void setAccessedIn(String accessedIn)
	{
		this.accessedIn = accessedIn;
	}

	/**
	 * Checks if is accessed l value.
	 * 
	 * @return true, if is accessed l value
	 */
	public boolean isAccessedLValue()
	{
		return isAccessedLValue;
	}

	/**
	 * Sets the accessed l value.
	 * 
	 * @param isAccessedLValue the new accessed l value
	 */
	public void setAccessedLValue(boolean isAccessedLValue)
	{
		this.isAccessedLValue = isAccessedLValue;
	}
}
