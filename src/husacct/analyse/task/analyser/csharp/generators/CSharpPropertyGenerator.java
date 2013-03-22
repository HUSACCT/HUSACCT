package husacct.analyse.task.analyser.csharp.generators;

import java.util.Arrays;
import java.util.List;

import org.antlr.runtime.tree.CommonTree;

public class CSharpPropertyGenerator extends CSharpGenerator {

    private String accessControlQualifier;
    private String propertyName;
    private String declareType;

    public CSharpPropertyGenerator(List<CommonTree> propertyTrees, String uniqueClassName) {
        boolean classScope = false;
        boolean hadType = false;
        boolean hadName = false;
        int lineNumber = 0;
        for (CommonTree tree : propertyTrees) {
            int type = tree.getType();
            String text = tree.getText();
            if (type == STATIC) {
                classScope = true;
            }
            if (hadType) {
                hadName = checkName(type, text);
            } else {
                hadType = checkType(type, text);
            }
            if (type == EQUALS && !hadName) {
                return;
            }
            checkAccessorCollection(type, text);
            lineNumber = tree.getLine();
        }

        String uniqueName = uniqueClassName + "." + propertyName;
        modelService.createAttribute(classScope, accessControlQualifier, uniqueClassName, declareType, propertyName, uniqueName, lineNumber);
    }

    private boolean checkName(int type, String text) {
        if (type == IDENTIFIER) {
            propertyName = text;
            return true;
        }
        return false;
    }

    private boolean checkType(int type, String text) {
        if (Arrays.binarySearch(typeCollection, type) > -1) {
            declareType = text;
            return true;
        }
        return false;
    }

    private void checkAccessorCollection(int type, String text) {
        if (Arrays.binarySearch(accessorCollection, type) > -1) {
            accessControlQualifier += text + " ";
        }
    }
}
