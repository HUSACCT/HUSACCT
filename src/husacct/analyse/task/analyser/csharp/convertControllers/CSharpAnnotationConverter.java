package husacct.analyse.task.analyser.csharp.convertControllers;

import java.util.ArrayList;

import org.antlr.runtime.tree.CommonTree;

import husacct.analyse.task.analyser.csharp.CSharpData;
import husacct.analyse.task.analyser.csharp.CSharpTreeConvertController;
import husacct.analyse.task.analyser.csharp.generators.CSharpAnnotationGenerator;
import husacct.analyse.task.analyser.csharp.generators.CSharpGenerator;

public class CSharpAnnotationConverter extends CSharpGenerator {

    private CSharpTreeConvertController cSharpTreeConvertController;
    boolean forward = false;
    boolean backward = false;
    boolean isClass = false;
    private ArrayList<CommonTree> string = new ArrayList<CommonTree>();

    public CSharpAnnotationConverter(CSharpTreeConvertController cSharpTreeConvertController) {
        this.cSharpTreeConvertController = cSharpTreeConvertController;
    }

    public boolean annotationCheck(CommonTree tree, boolean isPartOfAnnotation) {

        if (tree.getType() == CLASS) {
            isClass = true;
        }

        if (isClass && tree.getType() == 4) {
            cSharpTreeConvertController.setCurrentClassName(tree.getText());
            isClass = false;
        }

        if (tree.getType() == FORWARDSQUAREBRACKET) {
            forward = true;
        }

        if (forward && tree.getType() == BACKWARDSQUAREBRACKET) {
            backward = true;
            forward = false;
        }

        if (forward && !backward && tree.getType() == 4) {
            string.add(tree);
            isPartOfAnnotation = true;
            backward = true;
            return true;
//			new CSharpAnnotationGenerator(cSharpTreeConvertController.getCurrentClassName(),cSharpTreeConvertController.getCurrentNamespaceName(), tree.getText(), tree.getLine());
        }

        if (string.size() > 0 && !cSharpTreeConvertController.getCurrentClassName().equals("")) {
            System.out.println(cSharpTreeConvertController.getCurrentClassName());
            for (CommonTree annotation : string) {
                new CSharpAnnotationGenerator(cSharpTreeConvertController.getUniqueClassName(), cSharpTreeConvertController.getCurrentNamespaceName() + "." + annotation.getText(), annotation.getText(), annotation.getLine());
            }
            string.clear();
        }
        return isPartOfAnnotation;
    }
}
