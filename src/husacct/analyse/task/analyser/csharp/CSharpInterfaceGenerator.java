package husacct.analyse.task.analyser.csharp;

public class CSharpInterfaceGenerator extends CSharpGenerator{
	public void addToModelService(CSharpData cSharpClassData) {
		modelService.createInterface(cSharpClassData.getUniqueName(), cSharpClassData.getClassName(), cSharpClassData.getBelongsToPackage());
	}
}
