package husacct.analyse.task.analyser.csharp;

public class CSharpInheritanceDefinitionGenerator extends CSharpGenerator {

	public void addToModelService(CSharpData data) {
		modelService.createInheritanceDefinition(data.getInheritanceFrom(), data.getInheritanceTo(), 0);
	}
}
