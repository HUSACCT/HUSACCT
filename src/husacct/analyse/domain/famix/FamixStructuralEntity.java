package husacct.analyse.domain.famix;

/**
 * The Class StructuralEntity. A StructuralEntity represents the definition in
 * source code of a structural entity, i.e. it denotes an aspect of the state of
 * a system. The different kinds of structural entities mainly differ in
 * lifetime: some have the same lifetime as the entity they belong to, e.g. an
 * attribute and a class, some have a lifetime that is the same as the whole
 * system, e.g. a global variable. Subclasses of this class represent different
 * mechanisms for defining such an entity. StructuralEntity is an abstract class
 * inheriting from Entity. Besides inherited attributes, it has the following
 * attributes:
 */
public class FamixStructuralEntity extends FamixEntity
{

	/**
	 * The decalre type. Is a qualifier that via interpretation outside the
	 * model refers to the type of the defined structure. Typically this will be
	 * a class, a pointer or a primitive type (e.g. "int" in Java). declaredType
	 * is null if the return type is not known or the empty string (i.e. "") if
	 * the StructuralEntity does not have a return type (for instance, the C++
	 * void; we don’t use "void", because this causes problems for languages
	 * where it is possible to define a class called "void", like for instance
	 * Smalltalk and Ada). Note that this is consistent with UML 1.1 [Booc96a].
	 * Note that we need a language dependent interpretation to link a type name
	 * to a class name, because in most OO languages, types are not always
	 * equivalent to a class. How the declaredType may be recognised in source
	 * code and how the type matches to a class are language dependent issue.
	 */
	private String declareType;

	/**
	 * The declare class. The unique name of the class that is implicit in the
	 * declaredType. The declaredType might be the class itself, but might also
	 * be a pointer to a class (for instance, Class* in C++) or a primitive type
	 * (such as "int" in Java), or something else depending on the language.
	 * Therefore, the declaredClass will contain the name of the class which is
	 * designated already by the declaredType, or the name of the class where
	 * the declaredType points to, null if it is unknown if there is an implicit
	 * class in the declared type, and the empty string (i.e. "") if it is known
	 * that there is no implicit class in the declaredReturnType. What exactly
	 * is the relationship between declaredClass and declaredType is a
	 * language-dependent issue. Note that this is useful information for, among
	 * others, dependency analysis (a requirement for this model), hence the
	 * presence in this model.
	 */
	private String declareClass;

	/**
	 * Gets the decalre type.
	 * 
	 * @return the decalre type
	 */
	public String getDeclareType()
	{
		return declareType;
	}

	/**
	 * Sets the decalre type.
	 * 
	 * @param decalreType the new decalre type
	 */
	public void setDeclareType(String decalreType)
	{
		this.declareType = decalreType;
	}

	/**
	 * Gets the declare class.
	 * 
	 * @return the declare class
	 */
	public String getDeclareClass()
	{
		return declareClass;
	}

	/**
	 * Sets the declare class.
	 * 
	 * @param declareClass the new declare class
	 */
	public void setDeclareClass(String declareClass)
	{
		this.declareClass = declareClass;
	}
}
