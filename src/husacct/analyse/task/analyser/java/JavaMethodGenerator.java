package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.JavaParser;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

class JavaMethodGenerator extends JavaGenerator{

	private static final int CONSTRUCTOR_DECLARATION = JavaParser.CONSTRUCTOR_DECL;
	private static final int MODIFIER_LIST = JavaParser.MODIFIER_LIST;
	private static final int ABSTRACT = JavaParser.ABSTRACT;
	private static final int STATIC = JavaParser.STATIC;
	private static final int PROTECTED = JavaParser.PROTECTED;
	private static final int PRIVATE = JavaParser.PRIVATE;
	private static final int PUBLIC = JavaParser.PUBLIC;
	private static final int FINAl = JavaParser.FINAL;
	private static final int THROWS_CLAUSE = JavaParser.THROWS_CLAUSE;
	
	private boolean hasClassScope;
	private boolean isAbstract = false;
	private boolean isConstructor;
	private String belongsToClass;

	private String accessControlQualifier;
	private String signature;
	private boolean isPureAccessor;  //TODO Fille isPureAccessor
	private String declaredReturnType;
	private String declareReturnClass; //TODO fill declareReturnClass if necessary

	public String name;
	public String uniqueName;

	public void generateModelObject(CommonTree methodTree, String className) {
		this.belongsToClass = className;
		fillMethodObject(methodTree);
		createMethodObject();
	}

	private void fillMethodObject(CommonTree methodTree) {
		if (methodTree != null) {
				// If constructor...
				if(methodTree.getType() == CONSTRUCTOR_DECLARATION){
					createMethodDetails(methodTree);
					isConstructor = true;
					declaredReturnType = "";
					//name = "Constructor";
					uniqueName = belongsToClass + "." + signature;
				} else {
					createMethodDetails(methodTree);
					isConstructor = false;
					uniqueName = belongsToClass + "." + signature;
				}
		}
	}

	private void createMethodDetails(Tree tree) {
		if (tree != null) {
			for (int i = 0; i < tree.getChildCount(); i++) {

				// Dit is vrij logisch, hier wordt gekeken of het een abstracte methode is.
				if(tree.getChild(i).getChild(1) != null && tree.getChild(i).getChild(1).getType() == ABSTRACT )
				{
					isAbstract = true;
				}

				// Hier wordt de return type geset, maar het object heeft een DeclaredReturnType, maar ook een DeclaredClassReturnType.
				// Misschien handig om die class eruit te halen? Want als het een class is, kun je die ook gewoon hier in setten:)
				if(tree.getChild(i).getType() == 157)
				{
					if(tree.getChild(i).getChild(0).getType() == 151)
					{
						declaredReturnType = tree.getChild(i).getChild(0).getChild(0).getText();
					} else {
						declaredReturnType = tree.getChild(i).getChild(0).getText();
					}
				}

				// De methode naam setten!
				if(tree.getChild(i).getType() == 164){
					name = tree.getChild(i).getText();
				}

				fillMethodSignature(tree, i);
				fillAccessControlQualifier(tree, i);
			}
		}
	}

	public void fillMethodSignature(Tree tree, int i)
	{		
		// Een methode signature is het volgende: http://java.about.com/od/m/g/methodsignature.htm
		String methodSignature = "";
		// Als het een constructor is (124), dan gebruik je de classname voor signature
		if(tree.getType() == CONSTRUCTOR_DECLARATION)
			methodSignature =  belongsToClass + "(";
		// Anders de methode naam.
		else
			methodSignature = name + "(";
		
		// Kijken of er een FORMAL_PARAM_LIST element is
		if(tree.getChild(i).getType() == 133)
		{
			// Als hij paramters heeft, dan gaan we ze zoeken..
			if(tree.getChild(i).getChildCount() > 0)
			{
				// Een loop voor elke parameter..
				for(int i2 = 0; i2 < tree.getChild(i).getChildCount(); i2++)
				{
					// Hebben we een FORMAL_PARAM_STD_DECL element (134)
					if(tree.getChild(i).getChild(i2).getType() == 134)
					{
						// Hebben we een TYPE element (157)
						if(tree.getChild(i).getChild(i2).getChild(1).getType() == 157)
						{
							// ALS we een QUALIFIED_TYPE_IDENT(151) hebben, dan moeten we verder in de boom, want dan 
							// beschikt de methode over een specifieke type parameter zoals String.
							// QUALIFIED_TYPE_IDENT(151) hoeft er niet te zijn, bij bijvoorbeeld een boolean.
							if(tree.getChild(i).getChild(i2).getChild(1).getChild(0).getType() == 151)
							{
								// Welke type is het argument? IDENT (164)
								if(tree.getChild(i).getChild(i2).getChild(1).getChild(0).getChild(0).getType() == 164)
								{
									methodSignature += tree.getChild(i).getChild(i2).getChild(1).getChild(0).getChild(0).getText();

								}
							// Als er GEEN QUALIFIED_TYPE_IDENT(151) is dan pakken we gelijk het type
							} else {
								methodSignature += tree.getChild(i).getChild(i2).getChild(1).getChild(0).getText();
							}
						}
					}

					// Zodra je nog niet bij de laatste parameter bent, dan zetten we een komma.
					if(i2 < tree.getChild(i).getChildCount()-1)
					{
						methodSignature += ",";
					}
				}
			}
			// Signature afsluiten en aan het object toevoegen uiteraard!
			methodSignature += ")";
			signature = methodSignature;
		}
		
		if(tree.getChild(i).getType() == THROWS_CLAUSE){
			JavaExceptionGenerator exceptionGenerator = new JavaExceptionGenerator();
			CommonTree exceptionTree = (CommonTree) tree.getChild(i);
			exceptionGenerator.generateModel(exceptionTree, this.belongsToClass);
			
			
			
			//exceptionGenerator.generateModel((CommonTree) tree.getChild(i), this.belongsToClass.toString());
			
//			System.out.println(tree.getChild(i).toStringTree());
//			
//			String fromClass = this.belongsToClass.toString();
//			
//			for(int ii = 0; ii <= tree.getChild(i).getChildCount(); ii++){
//				System.out.println(tree.getChild(i).getChild(ii).getType());
//			}
//						
			//modelService.createException(fromClass, ExceptionClass, lineNumber, declarationType)
		}
		
		
	}
	public void fillAccessControlQualifier(Tree tree, int i)
	{
		if(tree.getChild(i).getType() == MODIFIER_LIST){ //Modifier List, (public, private, static?)
			for (int childOfGivenTree = 0; childOfGivenTree < tree.getChild(i).getChildCount(); childOfGivenTree++){
				if (tree.getChild(i).getChild(childOfGivenTree).getType() == STATIC){ //90 = static
					hasClassScope = true;
				}
				else if (tree.getChild(i).getChild(childOfGivenTree).getType() == PUBLIC){ //87 = public
					accessControlQualifier = "public";
				}
				else if (tree.getChild(i).getChild(childOfGivenTree).getType() == PRIVATE){ //85 = private
					accessControlQualifier = "private";
				}
				else if (tree.getChild(i).getChild(childOfGivenTree).getType() == PROTECTED){ //86 = protected
					accessControlQualifier = "protected";
				}
			}
			if (tree.getChild(i).getChildCount() == 0 || hasClassScope && accessControlQualifier == null){
				accessControlQualifier = "package-private";
			}
		}
	}

	private void createMethodObject(){
		modelService.createMethod(name, uniqueName, accessControlQualifier, signature, isPureAccessor, declaredReturnType, belongsToClass, isConstructor, isAbstract, hasClassScope);
	}
}