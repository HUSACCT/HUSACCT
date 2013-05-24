package husacct.analyse.task.analyser.csharp.generators;

import static husacct.analyse.task.analyser.csharp.generators.CSharpGeneratorToolkit.*;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import org.antlr.runtime.tree.CommonTree;

public class CSharpLoopGenerator extends CSharpGenerator {

    private String packageAndClassName;
    private String belongsToMethod;
    
    CSharpAttributeAndLocalVariableGenerator csAttribueteAndLocalVariableGenerator = new CSharpAttributeAndLocalVariableGenerator();
    CSharpInvocationMethodGenerator csInvocationMethodGenerator;
    CSharpBlockScopeGenerator csBlockScopeGenerator;

    public void generateToDomainFromLoop(CommonTree loopTree, String belongsToClass, String belongsToMethod) {
        packageAndClassName = belongsToClass;
        this.belongsToMethod = belongsToMethod;
        
        switch(loopTree.getType()){
            case CSharpParser.FOR:
            case CSharpParser.WHILE:
			case CSharpParser.DO:
                walkForOrWhileTree(loopTree);
                break;
            case CSharpParser.FOREACH:
                walkForEachTree(loopTree);
                break;
            default:
                System.err.println(this.getClass().getName() + " no supported loop");
        }
    }

    private void walkForOrWhileTree(CommonTree loopTree) {
        for(int i = 0; i < loopTree.getChildCount(); i++){
            CommonTree child = (CommonTree) loopTree.getChild(i);
            
            switch(child.getType()){
                case CSharpParser.LOCAL_VARIABLE_DECLARATOR:
                	delegateLocalVariable(child);
                    csAttribueteAndLocalVariableGenerator.generateLocalVariableToDomain(child, packageAndClassName, this.belongsToMethod);
                    deleteTreeChild(child);
                    break;
                case CSharpParser.METHOD_INVOCATION:
                	delegateInvocationMethod(child);
                    deleteTreeChild(child);
                    break;
                case CSharpParser.BLOCK:
                	delegateBlockScope(child);
                    deleteTreeChild(child);
                    break;
				default:
					walkForOrWhileTree(child);
					break;
            }
        }
    }

   private void delegateLocalVariable(CommonTree child) {
		if (child.toStringTree().contains("= >")) {
			CSharpLamdaGenerator csLamdaGenerator = new CSharpLamdaGenerator();
			csLamdaGenerator.delegateLambdaToBuffer((CommonTree)child, packageAndClassName, belongsToMethod);
		} else {
			CSharpAttributeAndLocalVariableGenerator csharpLocalVariableGenerator = new CSharpAttributeAndLocalVariableGenerator();
			csharpLocalVariableGenerator.generateLocalVariableToDomain(child, this.packageAndClassName, this.belongsToMethod);
		}
	}

   private void delegateInvocationMethod(CommonTree child) {
    	csInvocationMethodGenerator = new CSharpInvocationMethodGenerator(this.packageAndClassName);
    	csInvocationMethodGenerator.generateMethodInvocToDomain((CommonTree) child, this.belongsToMethod);
	}
   
   private void delegateBlockScope(CommonTree child) {
	   csBlockScopeGenerator = new CSharpBlockScopeGenerator();
     csBlockScopeGenerator.walkThroughBlockScope(child, this.packageAndClassName, this.belongsToMethod);
	}

	private void walkForEachTree(CommonTree loopTree) {
        for(int childCount = 0; childCount < loopTree.getChildCount(); childCount++){
			CommonTree child = (CommonTree) loopTree.getChild(childCount);
			
			switch (child.getType()) {
				case CSharpParser.TYPE:
					if (hasChild(child, CSharpParser.NAMESPACE_OR_TYPE_NAME)) {
						
						CommonTree typeIdentTree = walkTree(child, CSharpParser.NAMESPACE_OR_TYPE_NAME);
						String type = walkTree(typeIdentTree, CSharpParser.IDENTIFIER).getText();
						
						for(int typeChild = 1; typeChild < typeIdentTree.getChildCount(); typeChild++) {
							CommonTree typePartTree = (CommonTree) typeIdentTree.getChild(typeChild);
							if(isOfType(typePartTree, CSharpParser.NAMESPACE_OR_TYPE_PART)) {
								type += type.equals("") ? "" : ".";
								type += walkTree(typePartTree, CSharpParser.IDENTIFIER);
							}
						}

						int lineNumber = walkTree(typeIdentTree, CSharpParser.IDENTIFIER).getLine();
						if (hasNextChild(loopTree, childCount)) {
							csAttribueteAndLocalVariableGenerator.generateLocalLoopVariable(packageAndClassName, belongsToMethod, type, loopTree.getChild(childCount + 1).getText(), lineNumber);
						}
					} else if (loopTree.getChild(childCount + 1).getType() == CSharpParser.IDENTIFIER) {
						int lineNumber = loopTree.getChild(childCount + 1).getLine();
						csAttribueteAndLocalVariableGenerator.generateLocalLoopVariable(packageAndClassName, belongsToMethod, child.getChild(0).getText(), loopTree.getChild(childCount + 1).getText(), lineNumber);
					} else {
						System.err.println(this.getClass().getName() + " no type found");
					}
					break;
				case CSharpParser.CAST_EXPRESSION:
					deleteTreeChild(child.getChild(0)); //no need to know this
					break;
				case CSharpParser.METHOD_INVOCATION:
					delegateInvocationMethod(child);
					deleteTreeChild(child);
					break;
				case CSharpParser.BLOCK:
					delegateBlockScope(child);
                    deleteTreeChild(child);
					break;
				default:
					walkForOrWhileTree(child);
					break;
			}
		}
    }

	private boolean hasNextChild(CommonTree tree, int currentChild) {
		if (tree.getChild(currentChild + 1) != null)
		{
			return true;
		}
		return false;
	}
}
