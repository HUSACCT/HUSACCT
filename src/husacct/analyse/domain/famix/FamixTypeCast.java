package husacct.analyse.domain.famix;


/**
 * The Class TypeCast. This new association models type cast like
 * (MyClass)variable. Type casts are interesting for reengineering as they often
 * point to problems in the design of a system. There will be an instance of
 * this class for every type cast occuring in the source code, even if the cast
 * is between the same types, because we are interested in all the places where
 * casts occur. The attributes of TypeCast are:
 */
public class FamixTypeCast extends FamixAssociation
{

	/**
	 * The belongs to behaviour. Refers to the BehaviouralEntity the cast
	 * appears in.
	 */
	String belongsToBehaviour;

	/**
	 * The from type. Refers to the unique name of the type the casted
	 * expression has. This is the declared type of variable in the above
	 * example.
	 */
	String fromType;

	/**
	 * The to type. Refers to the unique name of the type the expression is
	 * casted to (MyClass in the above example).
	 */
	String toType;

	/**
	 * Gets the belongs to behaviour.
	 * 
	 * @return the belongs to behaviour
	 */
	public String getBelongsToBehaviour()
	{
		return belongsToBehaviour;
	}

	/**
	 * Sets the belongs to behaviour.
	 * 
	 * @param belongsToBehaviour the new belongs to behaviour
	 */
	public void setBelongsToBehaviour(String belongsToBehaviour)
	{
		this.belongsToBehaviour = belongsToBehaviour;
	}

	/**
	 * Gets the from type.
	 * 
	 * @return the from type
	 */
	public String getFromType()
	{
		return fromType;
	}

	/**
	 * Sets the from type.
	 * 
	 * @param fromType the new from type
	 */
	public void setFromType(String fromType)
	{
		this.fromType = fromType;
	}

	/**
	 * Gets the to type.
	 * 
	 * @return the to type
	 */
	public String getToType()
	{
		return toType;
	}

	/**
	 * Sets the to type.
	 * 
	 * @param toType the new to type
	 */
	public void setToType(String toType)
	{
		this.toType = toType;
	}
}
