package husacct.analyse.task.analyser.csharp.generators;

import husacct.analyse.task.analyser.csharp.CSharpData;

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.tree.CommonTree;

public class CSharpExceptionGenerator extends CSharpGenerator {

    private String exceptionClass;
    private boolean forward;
    private boolean foundExceptionVariable;
    private boolean foundDot;
    private String exceptionVariable;
    private String belongsToClass;
    private ArrayList<CSharpData> allInvocations = new ArrayList<CSharpData>();

    public void generateException(List<CommonTree> tree, String belongsToClass, int lineNumber) {
        for (CommonTree node : tree) {
            exceptionClass = checkForExceptionClass(node);
            checkInvocations(node);
        }
        this.belongsToClass = belongsToClass;
        modelService.createException(belongsToClass, belongsToClass, lineNumber, exceptionClass);

        for (CSharpData invocation : allInvocations) {
            modelService.createMethodInvocation(belongsToClass, invocation.getLineNumber(), invocation.getInvocationTo(), invocation.getInvocationName(), "invocation");
        }
    }

    private String checkForExceptionClass(CommonTree node) {
        if (exceptionClass == null) {
            exceptionClass = "";
        }

        if (node.getType() == IDENTIFIER && exceptionClass.isEmpty()) {
            exceptionClass = node.getText();
        }
        return exceptionClass;
    }

    private void checkInvocations(CommonTree node) {
        if (exceptionClass != "" && node.getType() == 4 && !forward && node.getText() != exceptionClass) {
            exceptionVariable = node.getText();
        }

        if (exceptionVariable != "" && node.getType() == FORWARDCURLYBRACKET) {
            this.forward = true;
        }

        if ((forward) && (node.getType() == IDENTIFIER) && (node.getText()).equals(exceptionVariable)) {
            foundExceptionVariable = true;
        }

        if (foundExceptionVariable && node.getType() == DOT) {
            foundDot = true;
        }

        if (foundExceptionVariable && foundDot && node.getType() == IDENTIFIER) {
            foundDot = false;
            foundExceptionVariable = false;
            CSharpData data = new CSharpData();
            data.setClassName(belongsToClass);
            data.setLineNumber(node.getLine());
            data.setInvocationTo(exceptionClass);
            data.setInvocationName(node.getText());
            allInvocations.add(data);
        }
    }
}
