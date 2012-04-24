package husacct.analyse.task.analyser.java;

import husacct.analyse.domain.ModelService;
import husacct.analyse.domain.famix.FamixModelServiceImpl;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;


public class JavaAttributeGenerator {
	
	private Boolean classScope = false;
	private String AccesControlQualifier;
	private String belongsToClass; 
	
	private String declareClass; //voorbeeld: package.package.class
	private String declareType;     //int, string, objectnaam etc
	

	private String name;
	
	private ModelService modelService = new FamixModelServiceImpl();

	private final static int STATIC = 90;
	private final static int MODIFIER_LIST = 145;
	private final static int QUALIFIED_TYPE_IDENT = 151;
	private final static int TYPE = 157;
	private final static int VAR_DECLARATOR_LIST = 162;
	private final static int IDENT = 164;
	
	
	public void generateModel(Tree attributeTree, String belongsToClass) {
		this.belongsToClass = belongsToClass;
		walkThroughAST(attributeTree);
		createAttributeObject();
	}

	private void walkThroughAST(Tree tree) {
		for(int i = 0; i < tree.getChildCount(); i++){
			Tree child = tree.getChild(i);
			int treeType = child.getType();
			if(treeType == MODIFIER_LIST){
				setAccesControllQualifier(tree);
				setClassScope(child);
			}else if(treeType == TYPE){
				setDeclareType(child);		
			}else if(treeType == VAR_DECLARATOR_LIST){
				setAttributeName(child);	
			}
			walkThroughAST(child);
		}
	}

	private void createAttributeObject(){
		modelService.createAttribute(classScope, AccesControlQualifier, belongsToClass, declareType, name, belongsToClass + "." + name);
	}

	private void setAttributeName(Tree tree) {
		for(int i = 0; i < tree.getChildCount(); i++){
			Tree child = tree.getChild(i);
			int treeType = child.getType();
			if(treeType == IDENT){
				name = child.getText();
				break;
			} 		
			setAttributeName((CommonTree) tree.getChild(i));
		}
	}

	//pakt nog geen primitieve types op.
	private void setDeclareType(Tree typeTree) {
		
		Tree child = typeTree.getChild(0);
		Tree declaretype = child.getChild(0);
		if(child.getType() != QUALIFIED_TYPE_IDENT){
			declareType = child.getText();
		}else{
			declareType = declaretype.getText();
		}
	}

	private void setAccesControllQualifier(Tree tree) {
		Tree ModifierList = tree.getChild(0);
		Tree Modifier = ModifierList.getChild(0);
		if(Modifier != null){
			AccesControlQualifier = Modifier.getText();			
		}else{
			AccesControlQualifier = "Package";
		}
	}

	private void setClassScope(Tree ModifierList){
		for(int i = 0; i < ModifierList.getChildCount(); i++){
			if(ModifierList.getChild(i).getType() == STATIC){
				classScope = true;
				break;
			}
		}
	}

}