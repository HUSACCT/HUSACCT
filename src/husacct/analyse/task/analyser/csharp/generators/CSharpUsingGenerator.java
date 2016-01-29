package husacct.analyse.task.analyser.csharp.generators;

import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import java.util.ArrayList;
import org.antlr.runtime.tree.CommonErrorNode;
import org.antlr.runtime.tree.CommonTree;

public class CSharpUsingGenerator extends CSharpGenerator {

    ArrayList<UsingInformation> usingArray = new ArrayList<>();

    public void addUsings(CommonTree usingTree) {
        ArrayList<UsingInformation> usings = new ArrayList<>();
        for (int i = 0; i < usingTree.getChildCount(); i++) {
            if (usingTree.getChild(i).getType() == CSharpParser.USING_NAMESPACE_DIRECTIVE) {
                usings.add(getUsing((CommonTree) usingTree.getChild(i)));
            } else if (usingTree.getChild(i) instanceof CommonErrorNode) {
                usings.add(getUsingFromError((CommonErrorNode) usingTree.getChild(i)));
            }
        }
        usingArray.addAll(usings);
    }

    public void generateToDomain(String location) {
        String usingLocation = location;
        for (UsingInformation ui : usingArray) {
            String usingModule = removerStar(ui.module);
            String completeUsingDeclaration = ui.module;
            int lineNumber = ui.line;
            boolean isCompleteNamespace = true; //ui.isComplete;
            modelService.createImport(usingLocation, usingModule, lineNumber, completeUsingDeclaration, isCompleteNamespace);
        }
    }

    private UsingInformation getUsing(CommonTree tree) {
        String result = "";
        UsingInformation usingInfo = new UsingInformation();
        for (int i = 0; i < tree.getChildCount(); i++) {
            if (tree.getChild(i).getType() == CSharpParser.NAMESPACE_OR_TYPE_NAME) {
                result = ((CommonTree) tree.getChild(i).getChild(0)).token.getText();
                for (int j = 1; j < tree.getChild(i).getChildCount(); j++) {
                    result += getUsingPart((CommonTree) tree.getChild(i).getChild(j));
                }
            }
        }
        usingInfo.module = result;
        usingInfo.line = tree.getLine();
        usingInfo.isComplete = false;
        return usingInfo;
    }

    private String getUsingPart(CommonTree partTree) {
        String part = "";
        if (partTree.getType() == CSharpParser.NAMESPACE_OR_TYPE_PART) {
            part = "." + partTree.getChild(0).getText();
        }
        return part;
    }

    private UsingInformation getUsingFromError(CommonErrorNode commonErrorNode) {
        String usingPath = commonErrorNode.getText();
        usingPath = usingPath.replace("using ", "");
        usingPath = usingPath.replace(";", "");

        UsingInformation usingInfo = new UsingInformation();
        usingInfo.module = usingPath;
        usingInfo.line = commonErrorNode.getLine();
        usingInfo.isComplete = true;

        return usingInfo;
    }

    private String removerStar(String module) {
        return module.contains("*") ? module.substring(0, module.length() - 2) : module;
    }

    public class UsingInformation {

        String module;
        int line;
        boolean isComplete;
    }
}
