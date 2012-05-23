package husacct.analyse.task.analyser.csharp.generators;

import husacct.analyse.task.analyser.csharp.CSharpData;

public class CSharpInheritanceDefinitionGenerator extends CSharpGenerator {

	public void addToModelService(CSharpData data, int lineNumber) {
		modelService.createInheritanceDefinition(data.getInheritanceFrom(), data.getInheritanceTo(), lineNumber);
	}
}
