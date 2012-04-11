package husacct.analyse.domain.famix;

/**
 * The Class Invocation. An Invocation represents the definition in source code
 * of a BehaviouralEntity invoking another BehaviouralEntity. What exactly
 * constitutes such a definition is a language dependent issue. However, when
 * the same behavioural entity is invoked more than once in a method body, then
 * parsers should generate a separate invocation-association for each
 * occurrence. It is important to note that due to polymorphism, there exists at
 * parse time a one-to-many relationship between the invocation and the actual
 * entity invoked: a method, for instance, might be defined on a certain class,
 * but at runtime actually invoked on an instance of a subclass of this class.
 * This explains the presence of the base attribute and the candidates
 * aggregation. Invocation is a concrete class inheriting from Association.
 * Besides inherited attributes, it has the following attributes:
 */
public class FamixInvocation extends FamixAssociation
{

	/**
	 * The invoked by. Is a name referring to the BehaviouralEntity doing the
	 * invocation. It uses the uniqueName of the entity as a reference.
	 */
	private String invokedBy;

	/**
	 * The invokes. Is a qualifier holding the signature of the
	 * BehaviouralEntity invoked. Due to polymorphism, the signature of the
	 * invoked BehaviouralEntity is not enough to assess which BehaviouralEntity
	 * is actually invoked. Further analysis based on the arguments is
	 * necessary. Concatenated with the base attribute this attribute
	 * constitutes the unique name of a behavioural entity.
	 */
	private String invokes;

	/**
	 * The base. Is the unique name of the entity where the invoked entity is
	 * defined on. Null means unknown and an empty string means the attribute
	 * has no base (the invoked entity may be a global function). Together with
	 * the invokes attribute, this attribute constitutes the unique name of a
	 * behavioural entity.
	 */
	private String base;
	
	/**
	 * NON-FAMIX Linenumber in sourcefile of invocation 
	 */
	
	private int lineNumber;
	
	/**
	 * NON-FAMIX path of sourceFile
	 */

	private String sourceFilePath;
	
	// private String canidatesAt // TODO uitzoeken

	public int getLineNumber()
	{
		return lineNumber;
	}

	public void setLineNumber(int lineNumber)
	{
		this.lineNumber = lineNumber;
	}

	public String getSourceFilePath()
	{
		return sourceFilePath;
	}

	public void setSourceFilePath(String sourceFilePath)
	{
		this.sourceFilePath = sourceFilePath;
	}

	/**
	 * Gets the invoked by.
	 * 
	 * @return the invoked by
	 */
	public String getInvokedBy()
	{
		return invokedBy;
	}

	/**
	 * Sets the invoked by.
	 * 
	 * @param invokedBy the new invoked by
	 */
	public void setInvokedBy(String invokedBy)
	{
		this.invokedBy = invokedBy;
	}

	/**
	 * Gets the invokes.
	 * 
	 * @return the invokes
	 */
	public String getInvokes()
	{
		return invokes;
	}

	/**
	 * Sets the invokes.
	 * 
	 * @param invokes the new invokes
	 */
	public void setInvokes(String invokes)
	{
		this.invokes = invokes;
	}

	/**
	 * Gets the base.
	 * 
	 * @return the base
	 */
	public String getBase()
	{
		return base;
	}

	/**
	 * Sets the base.
	 * 
	 * @param base the new base
	 */
	public void setBase(String base)
	{
		this.base = base;
	}
}
