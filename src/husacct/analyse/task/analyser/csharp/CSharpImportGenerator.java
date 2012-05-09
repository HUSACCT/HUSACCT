package husacct.analyse.task.analyser.csharp;

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.tree.CommonTree;

public class CSharpImportGenerator extends CSharpGenerator{
	List<String> seperatedAndMergedUsages = new ArrayList<String>();
	
	public CSharpImportGenerator(List<CommonTree> usageTrees){
		String mergedUsages = mergeTree(usageTrees);
		seperateUsages(mergedUsages);
		
		createFamixObject(seperatedAndMergedUsages);
	}

	private String mergeTree(List<CommonTree> usageTrees) {
		String mergedUsages = "";
		for(CommonTree commonTree : usageTrees){
			mergedUsages = mergedUsages + commonTree.getText();
		}
		
		return mergedUsages;
	}
	
	private void seperateUsages(String mergedUsages) {
		String [] s = mergedUsages.split(";");
		for(String singleString : s){
			seperatedAndMergedUsages.add(singleString);
		}
	}
	
	private void createFamixObject(List<String> seperatedAndMergedUsages) {
		for(String usage : seperatedAndMergedUsages){
			modelService.createImport("", usage, 0, usage, true);
		}
	}
}
