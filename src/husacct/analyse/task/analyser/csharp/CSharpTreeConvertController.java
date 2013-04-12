package husacct.analyse.task.analyser.csharp;

import static husacct.analyse.task.analyser.csharp.generators.CSharpGeneratorToolkit.*;
import husacct.analyse.infrastructure.antlr.TreePrinter;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser.compilation_unit_return;
import husacct.analyse.task.analyser.csharp.generators.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class CSharpTreeConvertController {

    CSharpUsingGenerator csUsingGenerator;
    CSharpNamespaceGenerator csNamespaceGenerator;
    CSharpClassGenerator csClassGenerator;
    CSharpStructGenerator csStructGenerator;
    CSharpEnumGenerator csEnumGenerator;
    CSharpInheritanceGenerator csInheritanceGenerator;
    CSharpAttributeGenerator csAttributeGenerator;
    CSharpMethodGeneratorController csMethodeGenerator;
    List<CommonTree> usings = new ArrayList<>();
    Stack<String> namespaceStack = new Stack<>();
    Stack<String> classNameStack = new Stack<>();

    public CSharpTreeConvertController() {
        csUsingGenerator = new CSharpUsingGenerator();
        csNamespaceGenerator = new CSharpNamespaceGenerator();
        csClassGenerator = new CSharpClassGenerator();
        csStructGenerator = new CSharpStructGenerator();
        csEnumGenerator = new CSharpEnumGenerator();
        csInheritanceGenerator = new CSharpInheritanceGenerator();
        csAttributeGenerator = new CSharpAttributeGenerator();
        csMethodeGenerator = new CSharpMethodGeneratorController();
    }

    public void delegateDomainObjectGenerators(final CSharpParser cSharpParser) throws RecognitionException {
        final CommonTree compilationCommonTree = getCompilationTree(cSharpParser);
        new TreePrinter(compilationCommonTree); //Debug functie
        delegateASTToGenerators(compilationCommonTree);
    }

    private CommonTree getCompilationTree(final CSharpParser cSharpParser) throws RecognitionException {
        final compilation_unit_return compilationUnit = cSharpParser.compilation_unit();
        return (CommonTree) compilationUnit.getTree();
    }

    private void delegateASTToGenerators(CommonTree tree) {
        if (isTreeAvailable(tree)) {
            for (int i = 0; i < tree.getChildCount(); i++) {
                CommonTree treeNode = (CommonTree) tree.getChild(i);
                int nodeType = treeNode.getType();

                switch (nodeType) {
                    case CSharpParser.USING_DIRECTIVES:
                        saveUsing(treeNode);
                        deleteTreeChild(treeNode);
                        break;
                    case CSharpParser.NAMESPACE:
                        CommonTree namespaceTree = treeNode;
                        namespaceStack.push(delegateNamespace(namespaceTree));
                        delegateASTToGenerators(namespaceTree);
                        namespaceStack.pop();
                        break;
                    case CSharpParser.CLASS:
                        CommonTree classTree = treeNode;
                        boolean isInner = classNameStack.size() > 0;
                        classNameStack.push(delegateClass(classTree, isInner));
                        if (!isInner) {
                            delegateUsings();
                        }
                        delegateASTToGenerators(classTree);
                        classNameStack.pop();
                        break;
                    case CSharpParser.EXTENDS_OR_IMPLEMENTS:
                        delegateInheritanceDefinition(treeNode);
                        deleteTreeChild(treeNode);
                        break;
                    case CSharpParser.VARIABLE_DECLARATOR:
                        delegateAttribute(treeNode);
                        deleteTreeChild(treeNode);
                        break;
                    case CSharpParser.METHOD_DECL:
                    case CSharpParser.CONSTRUCTOR_DECL:
                        delegateMethod(treeNode);
                        deleteTreeChild(treeNode);
                        break;
                    default:
                        delegateASTToGenerators(treeNode);
                }
            }
        }
    }

    private boolean isTreeAvailable(Tree tree) {
        if (tree != null) {
            return true;
        }
        return false;
    }

    private void deleteTreeChild(Tree treeNode) {
        for (int child = 0; child < treeNode.getChildCount();) {
            treeNode.deleteChild(treeNode.getChild(child).getChildIndex());
        }
    }

    private void saveUsing(CommonTree usingTree) {
        csUsingGenerator.add(usingTree);
    }

    private void delegateUsings() {
        csUsingGenerator.generateToDomain(belongsToClass(namespaceStack, classNameStack));
    }

    private String delegateNamespace(CommonTree namespaceTree) {
        return csNamespaceGenerator.generateModel(namespaceStack, namespaceTree);
    }

    private String delegateClass(CommonTree classTree, boolean isInnerClass) {
        String analysedClass;
        if (isInnerClass) {
            analysedClass = csClassGenerator.generateToModel(classTree, getParentName(namespaceStack), getParentName(classNameStack));
        } else {
            analysedClass = csClassGenerator.generateToDomain(classTree, getParentName(namespaceStack));
        }
        return analysedClass;
    }

    private void delegateInheritanceDefinition(CommonTree inheritanceTree) {
        csInheritanceGenerator.generateToDomain(inheritanceTree, belongsToClass(namespaceStack, classNameStack));
    }

    private void delegateAttribute(CommonTree attributeTree) {
        csAttributeGenerator.generateAttributeToDomain(attributeTree, getParentName(classNameStack));
    }

    private void delegateMethod(CommonTree methodTree) {
        csMethodeGenerator.generateMethodToDomain(methodTree, getParentName(classNameStack));
    }
}
