package husacct.analyse.task.analyser.csharp;

import static husacct.analyse.infrastructure.antlr.csharp.CSharpParser.*;

import husacct.analyse.infrastructure.antlr.TreePrinter;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class CSharpTreeConvertController {
    CSharpUsingGenerator csUsingGenerator;    
    CSharpNamespaceGenerator csNamespaceGenerator;
    
    CSharpClassGenerator csClassGenerator;
    CSharpInterfaceGenerator csInterfaceGenerator;
    CSharpStructGenerator csStructGenerator;
    CSharpEnumGenerator csEnumGenerator;
    
    private String theClass = null; 
    private String thePackage = null; 
    private String currentClass = null; 
    private String parentClass = null; 
    private int classCount = 0; 

    public CSharpTreeConvertController() {
        csUsingGenerator = new CSharpUsingGenerator();
        csNamespaceGenerator = new CSharpNamespaceGenerator();
    }
    
    public void delegateDomainObjectGenerators(final CSharpParser cSharpParser)	throws RecognitionException {
		final CommonTree compilationCommonTree = getCompilationTree(cSharpParser);
		TreePrinter tp = new TreePrinter(compilationCommonTree);
                
                //code to walk through tree.
	}
    
    private CommonTree getCompilationTree(final CSharpParser cSharpParser) throws RecognitionException {
        final compilation_unit_return compilationUnit = cSharpParser.compilation_unit();
        return (CommonTree) compilationUnit.getTree();
    }

    public void delegateASTToGenerators(CSharpParser cSharpParser) throws RecognitionException { 
        CommonTree rootTree = getCompilationTree(cSharpParser);

        TreePrinter tp = new TreePrinter(rootTree);
        delegateASTToGenerators(rootTree); 
    } 

    private void delegateASTToGenerators(CommonTree rootTree) {
        handleUsings(rootTree);
        handleAsNamespace(rootTree);
    }

    private void handleAsNamespace(Tree namespaceTree) {
        for (int i = 0; i < namespaceTree.getChildCount(); i++) {
            Tree namespaceChild = namespaceTree.getChild(i);
            switch(namespaceChild.getType()){
                case NAMESPACE:
                    handleAsNamespace(namespaceChild);
                    break;
                case CLASS:
                    handleAsClass(namespaceChild);
                    break;
                case INTERFACE:
                    handleAsInterface(namespaceChild);
                    break;
                case STRUCT:
                    handleAsStruct(namespaceChild);
                    break;
                case ENUM:
                    handleAsEnum(namespaceChild);
                    break;
                case DELEGATE:
                    handleAsDelegate(namespaceChild);
                    break;
            }
        }
    }

    private void handleAsClass(Tree namespaceChild) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void handleAsInterface(Tree namespaceChild) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void handleAsStruct(Tree namespaceChild) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void handleAsEnum(Tree namespaceChild) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void handleAsDelegate(Tree namespaceChild) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void handleUsings(CommonTree rootTree) {
        Tree usingTree = rootTree.getFirstChildWithType(USING);
        CSharpUsingGenerator.generate(usingTree);
        rootTree.deleteChild(usingTree.getChildIndex());
    }
}
