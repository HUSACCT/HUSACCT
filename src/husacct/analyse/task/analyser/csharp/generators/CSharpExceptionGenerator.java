package husacct.analyse.task.analyser.csharp.generators;

import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import static husacct.analyse.task.analyser.csharp.generators.CSharpGeneratorToolkit.*;

import org.antlr.runtime.tree.CommonTree;

public class CSharpExceptionGenerator extends CSharpGenerator {

    private String exceptionType;
    private String fromClass;
    private String exceptionClass;
    private int lineNumber;

    public void generateExceptionToDomain(CommonTree tree, String theClass) {
        this.lineNumber = tree.getLine();
        this.fromClass = theClass;
        setExceptionType(tree);
        modelService.createException(fromClass, exceptionClass, lineNumber);
    }

    private void setExceptionType(CommonTree tree) {
        if (isCatchedException(tree)) {
            this.exceptionType = "catch";
            getCaughtExceptionClass(tree);
        } else {
            this.exceptionType = "throw";
            getThrownExceptionClass(tree);
        }
    }

    private void getThrownExceptionClass(CommonTree tree) {
        CommonTree typenameTree = findHierarchicalSequenceOfTypes(tree, CSharpParser.UNARY_EXPRESSION, CSharpParser.OBJECT_CREATION_EXPRESSION, CSharpParser.TYPE);
        exceptionClass = getTypeNameAndParts(typenameTree);
    }

    private void getCaughtExceptionClass(CommonTree tree) {
        exceptionClass = getTypeNameAndParts(tree);
    }

    private boolean isCatchedException(CommonTree tree) {
        return isOfType(tree, CSharpParser.CATCH);
    }
}
