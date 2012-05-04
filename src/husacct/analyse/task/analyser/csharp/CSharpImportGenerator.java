package husacct.analyse.task.analyser.csharp;

import husacct.analyse.domain.famix.FamixCreationServiceImpl;

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.tree.CommonTree;

public class CSharpImportGenerator {
	List<String> seperatedAndMergedUsages = new ArrayList<String>();
	
	public CSharpImportGenerator(List<CommonTree> usageTrees, String classname){
		String mergedUsages = mergeTree(usageTrees);
		seperateUsages(mergedUsages);
		
		createFamixObject(seperatedAndMergedUsages, classname);
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
	
	private void createFamixObject(List<String> seperatedAndMergedUsages, String classname) {
		FamixCreationServiceImpl model = new FamixCreationServiceImpl();
		
		for(String usage : seperatedAndMergedUsages){
			System.out.print("bla");
			model.createImport(classname, usage, 0, usage, true);
		}
	}
}
