package husacct.analyse.domain.famix;

/**
 * The Class LocalVariable. A LocalVariable represents the definition in source
 * code of a variable defined locally to a behavioural entity. What exactly
 * constitutes such a definition is a language dependent issue. LocalVariable is
 * a concrete class inheriting from StructuralEntity . Besides inherited
 * attributes, it has the following attributes:
 * 
 * Example of a local variable position_: Class ScrollBar { computePosition(int
 * x,int y,int width,int height) { int position_; . . . } }
 */
public class FamixLocalVariable extends FamixStructuralEntity
{

	/**
	 * The belongs to behaviour. Is a name referring to the BehaviouralEntity
	 * owning the variable. It uses the uniqueName of this entity as a
	 * reference.
	 */
	private String belongsToBehaviour;

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
}
