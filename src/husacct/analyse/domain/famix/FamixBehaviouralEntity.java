package husacct.analyse.domain.famix;

/**
 * The Class BehaviouralEntity. A BehaviouralEntity represents the definition in
 * source code of a behavioural abstraction, i.e. an abstraction that denotes an
 * action rather than a part of the state. Subclasses of this class represent
 * different mechanisms for defining such an entity. BehaviouralEntity is an
 * abstract class inheriting from Entity. Besides inherited attributes, it has
 * the following attributes:
 */
public class FamixBehaviouralEntity extends FamixEntity
{

	/**
	 * The access control qualifier. s a string with a language dependent
	 * interpretation, that defines who is allowed to invoke it (for instance,
	 * 'public', 'private'...).
	 */
	private String accessControlQualifier;

	/**
	 * The signature. Is a string that allows to uniquely distinguish a
	 * behavioural entity. This is necessary because there exist OO languages
	 * (i.e., C++, Java) that allow to overload methods, so that the same method
	 * name may be associated with different parameter lists, each with its own
	 * method body. The way a signature string is composed is language
	 * dependent, but it should at least include the name of the method. The UML
	 * [Booc96a] compliant notation will be used, which will typically look like
	 * (see also 4.4. Naming Conventions - p. 6)
	 * "package::subpackage::classname.methodname(parameters)" .
	 */
	private String signature;

	/**
	 * The is pure accessor. s a predicate telling whether the behavioural
	 * entity is a pure accessor. There are two kinds of accessors, a reader
	 * accessor and a writer accessor. A pure reader accessor is an entity with
	 * a single receiver parameter, only returning the value of an attribute of
	 * the class the method is defined on. A pure writer accessor is a method
	 * with one receiver parameter and one value parameter, only storing the
	 * value inside the attribute of a class. How accessor methods are
	 * recognised in source code is a language dependent issue.
	 */
	private boolean isPureAccessor;

	/**
	 * The declared return type. Is a qualifier that via interpretation outside
	 * the model refers to the type of the returned object. Typically this will
	 * be a class, a pointer or a primitive type (e.g. "int" in Java).
	 * declaredReturnType is null if the return type is not known or the empty
	 * string (i.e. "") if the BehavourialEntity does not have a return type
	 * (for instance, the C++ void; we don’t use "void", because this causes
	 * problems for languages where it is possible to define a class called
	 * "void", like for instance Smalltalk and Ada). Note that this is
	 * consistent with UML 1.1 [Booc96a]. Note that we need a language dependent
	 * interpretation to link a type name to a class name, because in most OO
	 * languages, types are not always equivalent to a class. How the declared
	 * return type may be recognised in source code and how the return type
	 * matches to a class or another type are language dependent issues.
	 * Class
	 */
	private String declaredReturnType;

	/**
	 * The declare return class. The unique name of the class that is implicit
	 * in the declaredReturnType. The declaredReturnType might be the class
	 * itself, but might also be a pointer to a class (for instance, Class* in
	 * C++) or a primitive type (such as "int" in Java), or something else
	 * depending on the language. Therefore, the declaredReturnClass will
	 * contain the name of the class which is designated already by the
	 * declaredReturnType, or the name of the class where the declaredReturnType
	 * points to, null if it is unknown if there is an implicit class in the
	 * declaredRetunType, and the empty string (i.e. "") if it is known that
	 * there is no implicit class in the declaredReturnType. What exactly is the
	 * relationship between declaredReturnClass and declaredReturnType is a
	 * language-dependent issue. Note that this is useful information for, among
	 * others, dependency analysis (a requirement for this model), hence the
	 * presence in this model.
	 * x::y::z.Class
	 */
	private String declareReturnClass;

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
	 * Gets the signature.
	 * 
	 * @return the signature
	 */
	public String getSignature()
	{
		return signature;
	}

	/**
	 * Sets the signature.
	 * 
	 * @param signature the new signature
	 */
	public void setSignature(String signature)
	{
		this.signature = signature;
	}

	/**
	 * Checks if is pure accessor.
	 * 
	 * @return true, if is pure accessor
	 */
	public boolean isPureAccessor()
	{
		return isPureAccessor;
	}

	/**
	 * Sets the pure accessor.
	 * 
	 * @param isPureAccessor the new pure accessor
	 */
	public void setPureAccessor(boolean isPureAccessor)
	{
		this.isPureAccessor = isPureAccessor;
	}

	/**
	 * Gets the declared return type.
	 * 
	 * @return the declared return type
	 */
	public String getDeclaredReturnType()
	{
		return declaredReturnType;
	}

	/**
	 * Sets the declared return type.
	 * 
	 * @param declaredReturnType the new declared return type
	 */
	public void setDeclaredReturnType(String declaredReturnType)
	{
		this.declaredReturnType = declaredReturnType;
	}

	/**
	 * Gets the declare return class.
	 * 
	 * @return the declare return class
	 */
	public String getDeclareReturnClass()
	{
		return declareReturnClass;
	}

	/**
	 * Sets the declare return class.
	 * 
	 * @param declareReturnClass the new declare return class
	 */
	public void setDeclareReturnClass(String declareReturnClass)
	{
		this.declareReturnClass = declareReturnClass;
	}
}
