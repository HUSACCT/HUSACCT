package husacct.analyse.abstraction.mappers.javamapper.famixObjectGenerators;

import husacct.analyse.domain.famix.FamixClass;
import husacct.analyse.domain.famix.FamixMethod;
import husacct.analyse.domain.famix.FamixObject;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class JavaMethodGenerator extends JavaGenerator {
	private FamixMethod famixMethodObject;
	private FamixClass famixClassObject;
	
	public void setFamixClassObject(FamixClass famixClassObject) {
		this.famixClassObject = famixClassObject;
	}

	public JavaMethodGenerator()
	{
		this.famixMethodObject = new FamixMethod();
	}
	
	private void fillMethodObject(CommonTree methodTree) {
		if (methodTree != null) {
				
				// If constructor...
				if(methodTree.getType() == 124)
				{
					createMethodDetails(methodTree);
					famixMethodObject.setBelongsToClass(famixClassObject.getName());
					famixMethodObject.setConstructor(true);
					famixMethodObject.setDeclaredReturnType("");
					famixMethodObject.setName("Constructor");
				}else{
					createMethodDetails(methodTree);
					famixMethodObject.setBelongsToClass(famixClassObject.getName());
					famixMethodObject.setConstructor(false);
					famixMethodObject.setUniqueName(famixClassObject.getUniqueName() + "." + famixMethodObject.getSignature());
				}
			}
	}
	
	private void createMethodDetails(Tree tree) {
		if (tree != null) {
			for (int i = 0; i < tree.getChildCount(); i++) {
				
				// Dit is vrij logisch, hier wordt gekeken of het een abstracte methode is.
				if(tree.getChild(i).getChild(1) != null && tree.getChild(i).getChild(1).getType() == 53 )
				{
					famixMethodObject.setAbstract(true);
				}
				
				// Hier wordt de return type geset, maar het object heeft een DeclaredReturnType, maar ook een DeclaredClassReturnType.
				// Misschien handig om die class eruit te halen? Want als het een class is, kun je die ook gewoon hier in setten:)
				if(tree.getChild(i).getType() == 157)
				{
					if(tree.getChild(i).getChild(0).getType() == 151)
					{
						famixMethodObject.setDeclaredReturnType(tree.getChild(i).getChild(0).getChild(0).getText());
					} else {
						famixMethodObject.setDeclaredReturnType(tree.getChild(i).getChild(0).getText());
					}
				}
				
				// De methode naam setten!
				if(tree.getChild(i).getType() == 164)
				{
					famixMethodObject.setName(tree.getChild(i).getText());
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
		if(tree.getType() == 124)
			methodSignature =  famixClassObject.getName() + "(";
		// Anders de methode naam.
		else
			methodSignature = famixMethodObject.getName() + "(";
		
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
			famixMethodObject.setSignature(methodSignature);
		}
	}
	public void fillAccessControlQualifier(Tree tree, int i)
	{
		//kijken of de methode public/private static is enz
		if(tree.getChild(i).getType() == 145){ //Modifier List, verteld bijv: public/private, maar ook static
			for (int childOfGivenTree = 0; childOfGivenTree < tree.getChild(i).getChildCount(); childOfGivenTree++){
				if (tree.getChild(i).getChild(childOfGivenTree).getType() == 90){ //90 = static
					famixMethodObject.setHasClassScope(true);
				}
				else if (tree.getChild(i).getChild(childOfGivenTree).getType() == 87){ //87 = public
					famixMethodObject.setAccessControlQualifier("public");
				}
				else if (tree.getChild(i).getChild(childOfGivenTree).getType() == 85){ //85 = private
					famixMethodObject.setAccessControlQualifier("private");
				}
				else if (tree.getChild(i).getChild(childOfGivenTree).getType() == 86){ //85 = protected
					famixMethodObject.setAccessControlQualifier("protected");
				}
			}
			if (tree.getChild(i).getChildCount() == 0 || famixMethodObject.isHasClassScope() && famixMethodObject.getAccessControlQualifier() == null){
				famixMethodObject.setAccessControlQualifier("package-private");
			}
		}
	}

	@Override
	public FamixMethod generateFamix(CommonTree methodTree) {
		fillMethodObject(methodTree);
		return famixMethodObject;
	}
}
