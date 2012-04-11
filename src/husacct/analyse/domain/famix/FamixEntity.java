package husacct.analyse.domain.famix;

/**
 * The Class Entity.
 */
public abstract class FamixEntity extends FamixObject
{

	/**
	 * The name. Is a string that provides some human readable reference to an
	 * entity.
	 */
	private String name;

	/**
	 * The unique name. Is a string that is computed based on the name of the
	 * entity. Each class of entities must define its specific formula. The
	 * uniqueName serves as an external reference to that entity and must be
	 * unique for all entities in the model.
	 */
	private String uniqueName;

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name the new name
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Gets the unique name.
	 * 
	 * @return the unique name
	 */
	public String getUniqueName()
	{
		return uniqueName;
	}

	/**
	 * Sets the unique name.
	 * 
	 * @param uniqueName the new unique name
	 */
	public void setUniqueName(String uniqueName)
	{
		this.uniqueName = uniqueName;
	}
/*
	public boolean equals(Object object)
	{
		// TODO Auto-generated method stub
		return false;
	}
	**/
	//public abstract boolean equals(Object object);
}
