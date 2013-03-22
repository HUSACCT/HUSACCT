package husacct.analyse.task.analyser.csharp.generators;

public class CSharpAnnotationGenerator extends CSharpGenerator {

    public CSharpAnnotationGenerator(String belongs, String typeName, String uniqueName, int lineNum) {
        modelService.createAnnotation(belongs, typeName, typeName, uniqueName, lineNum);
    }
}
