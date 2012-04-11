package husacct.analyse.domain.famix;

/**
 * The Class FormalParameter. A FormalParameter represents the definition in
 * source code of a formal parameter, i.e. the declaration of what a behavioural
 * entity expects as an argument. What exactly constitutes such a definition is
 * a language dependent issue. FormalParameter is a concrete class inheriting
 * from StructuralEntity . Besides inherited attributes, it has the following
 * attributes:
 */
public class FamixFormalParameter extends FamixStructuralEntity
{

	/**
	 * The belongs to behaviour. s a name referring to the BehaviouralEntity
	 * owning the variable. It uses the uniqueName of this entity as a
	 * reference.
	 */
	private String belongsToBehaviour;

	/**
	 * The position. The position of the parameter in the list of parameters.
	 * Language plug-ins should specify what the position of a parameter is and
	 * this should be consistent the position attribute of Argument (see page
	 * 25).
	 */
	private int position;

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
	 * Gets the position.
	 * 
	 * @return the position
	 */
	public int getPosition()
	{
		return position;
	}

	/**
	 * Sets the position.
	 * 
	 * @param position the new position
	 */
	public void setPosition(int position)
	{
		this.position = position;
	}
}
