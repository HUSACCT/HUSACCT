package husacct.analyse.domain.famix;

/**
 * The Class Method. A Method represents the definition in source code of an
 * aspect of the behaviour of a class. What exactly constitutes such a
 * definition is a language dependent issue. Method is a concrete class
 * inheriting from BehaviouralEntity. Besides inherited attributes, it has the
 * following attributes:
 */
public class FamixMethod extends FamixBehaviouralEntity
{

	/**
	 * The belongs to class. Is a name referring to the class owning the method.
	 * It uses the uniqueName of the class as a reference.
	 */
	private String belongsToClass;

	/**
	 * The has class scope. Is a predicate telling whether the method has class
	 * scope (i.e., invoked on the class) or instance scope (i.e., invoked on an
	 * instance of that class). For example, static methods in C++ and Java have
	 * a hasClassScope attribute set to true.
	 */
	private boolean hasClassScope;

	/**
	 * The is abstract. Is a predicate telling whether the method is declared
	 * abstract, i.e. when subclasses are forced to provide an implementation
	 * for this method. Abstract methods are important in OO modelling, but how
	 * they are recognised in source code is a language dependent issue.
	 */
	private boolean isAbstract;

	/**
	 * The is constructor. Is a predicate telling whether the method is a
	 * constructor. A constructor is a method that creates an (initialised)
	 * instance of the class it is defined on. Thus a method that creates an
	 * instance of another class is not considered a constructor. How
	 * constructor methods are recognised in source code is a language dependent
	 * issue.
	 */
	private boolean isConstructor;

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
	 * Checks if is constructor.
	 * 
	 * @return true, if is constructor
	 */
	public boolean isConstructor()
	{
		return isConstructor;
	}

	/**
	 * Sets the constructor.
	 * 
	 * @param isConstructor the new constructor
	 */
	public void setConstructor(boolean isConstructor)
	{
		this.isConstructor = isConstructor;
	}
}
