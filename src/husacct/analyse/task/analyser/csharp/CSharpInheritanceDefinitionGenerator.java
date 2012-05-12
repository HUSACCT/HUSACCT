package husacct.analyse.task.analyser.csharp;

public class CSharpInheritanceDefinitionGenerator extends CSharpGenerator {

	public void addToModelService(CSharpData data)
	{
		System.out.println("From : " + data.getInheritanceFrom() + "To : " + data.getInheritanceTo());
				modelService.createInheritanceDefinition(data.getInheritanceFrom(), data.getInheritanceTo(), 0, "Implements");
	}
}
