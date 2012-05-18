package husacct.analyse.task.analyser.csharp.generators;

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.tree.CommonTree;

public class CSharpImportGenerator extends CSharpGenerator {
	List<String> seperatedAndMergedUsages = new ArrayList<String>();
	public CSharpImportGenerator(List<CommonTree> usageTrees, String className) {
		String mergedUsages = mergeTree(usageTrees);
		seperateUsages(mergedUsages);
		for (String usage : seperatedAndMergedUsages) {
			int lineNumber = usageTrees.get(seperatedAndMergedUsages.indexOf(usage)).getLine();
			createFamixObject(usage,lineNumber, className);
		}
	}

	private void createFamixObject(String usage, int lineNumber, String className) {
		modelService.createImport(className, usage, lineNumber, usage, true);
	}

	private String mergeTree(List<CommonTree> usageTrees) {
		String mergedUsages = "";
		for (CommonTree commonTree : usageTrees) {
			mergedUsages = mergedUsages + commonTree.getText();
		}

		return mergedUsages;
	}

	private void seperateUsages(String mergedUsages) {
		String[] s = mergedUsages.split(";");
		for (String singleString : s) {
			seperatedAndMergedUsages.add(singleString);
		}
	}

}
