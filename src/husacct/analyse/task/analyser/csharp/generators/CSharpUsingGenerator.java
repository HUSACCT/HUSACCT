package husacct.analyse.task.analyser.csharp.generators;

import java.util.List;
import org.antlr.runtime.tree.CommonTree;

public class CSharpUsingGenerator extends CSharpGenerator {

    private String usingClass;
    private String usingModule;
    private String completeUsingDeclaration;
    private boolean isCompleteNamespaceUsing;
    private int lineNumber;

    public void generateToDomain(CommonTree usingTree, String className) {
        this.usingClass = className;
        fillUsingObject(usingTree);
        modelService.createImport(usingClass, usingModule, lineNumber, completeUsingDeclaration, isCompleteNamespaceUsing);
    }

    private void fillUsingObject(CommonTree usingTree) {
        String importDetails = createImportDetails(usingTree, "--");
        String declaration = convertToImportDeclaration(importDetails, "--");

        this.lineNumber = usingTree.getLine();
        this.completeUsingDeclaration = declaration;
        this.isCompleteNamespaceUsing = isPackageImport(declaration);
        if (isCompleteNamespaceUsing) {
            usingModule = removeStar(declaration);
        } else {
            usingModule = declaration;
        }
    }

    private String createImportDetails(CommonTree importTree, String detailSeperator) {
        String details = "";
        @SuppressWarnings("unchecked")
        List<CommonTree> importDetail = (List<CommonTree>) importTree.getChildren();
        if (importDetail != null) {
            if (importDetail.size() < 2) {
                if (!isDot(importDetail.get(0))) {
                    details += importDetail.get(0).getText() + detailSeperator;
                }
                details += createImportDetails(importDetail.get(0), detailSeperator);
            } else {
                for (CommonTree singleDetail : importDetail) {
                    if (!isDot(singleDetail)) {
                        if (isStar(singleDetail)) {
                            details += "*" + detailSeperator;
                        } else {
                            details += singleDetail.getText() + detailSeperator;
                        }
                    }
                    details += createImportDetails(singleDetail, detailSeperator);
                }
            }
        }
        return details;
    }

    private String convertToImportDeclaration(String walkedImportDescription, String detailSeporator) {
        String[] items = walkedImportDescription.split(detailSeporator);
        String declaration = "";
        for (int itemCount = 0; itemCount < items.length; itemCount++) {
            if (itemCount > 0) {
                declaration += ".";
            }
            declaration += items[itemCount];
        }
        return declaration;
    }

    private boolean isPackageImport(String importString) {
        return importString.contains("*");
    }

    private boolean isDot(CommonTree treeChild) {
        return treeChild.getText().equals(".");
    }

    private boolean isStar(CommonTree treeChild) {
        return treeChild.getText().equals(".*");
    }

    private String removeStar(String declaration) {
        return declaration.replace(".*", "");
    }
}
