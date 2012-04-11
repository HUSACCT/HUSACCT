package husacct.analyse.domain.famix;

/**
 * The Class Argument. An Argument represents the passing of an argument when
 * invoking a BehaviouralEntity. What exactly constitutes such a definition is a
 * language dependent issue. The model distinguishes between two kind of
 * arguments, an ExpressionArgument or an AccessArgument. The former means that
 * some complex expression is passed, in that case the contents of the
 * expression is not further specified. The latter means that a reference to a
 * StructuralEntity is passed, thus involving an Access to the corresponding
 * structural entity, hence a reference to the corresponding Access is stored
 * within the AccessArgument. Both ExpressionArgument and AccessArgument are
 * concrete classes inheriting from Argument. Argument is an abstract class
 * inheriting from Object. Besides inherited attributes, Argument has the
 * following attributes:
 */
public class FamixArgument extends FamixObject
{

	/**
	 * The index. The position of the argument in the list of arguments.
	 * Language plug-ins should specify what the position of a argument is and
	 * this should be consistent the position attribute of FormalParameter (see
	 * page 21).
	 */
	private int index;

	/**
	 * The is receiver. Is a predicate telling whether this argument plays the
	 * role of the receiver in the containing invocation. Knowing which argument
	 * plays the role of the receiver may help resolving polymorph invocations.
	 */
	private boolean isReceiver;

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

	/**
	 * Checks if is receiver.
	 * 
	 * @return true, if is receiver
	 */
	public boolean isReceiver()
	{
		return isReceiver;
	}

	/**
	 * Sets the receiver.
	 * 
	 * @param isReceiver the new receiver
	 */
	public void setReceiver(boolean isReceiver)
	{
		this.isReceiver = isReceiver;
	}
}
