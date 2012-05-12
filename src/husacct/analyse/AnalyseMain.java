package husacct.analyse;

import husacct.ServiceProvider;
import husacct.analyse.domain.ModelCreationService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;
import husacct.analyse.presentation.AnalyseDebuggingFrame;
import husacct.define.IDefineService;

import javax.swing.UIManager;

public class AnalyseMain {
	public static void main(String[] args){
		
		ServiceProvider provider = ServiceProvider.getInstance();
		IDefineService defService = provider.getDefineService();
		
		String path = "/home/thomas/Downloads/Mittchel-benchmark-73d2abb/CSharpBenchmark";

		String[] paths = new String[]{path};
		defService.createApplication("Boobies Sanders Application", paths, "C#", "1.0");
		
		IAnalyseService analyser = provider.getAnalyseService();
		analyser.analyseApplication();
		
		ModelCreationService creation = new FamixCreationServiceImpl();
		//System.out.println(creation.represent());
		
		
//		ModelQueryService queryService = new FamixQueryServiceImpl();
		//Testing the getChildModulesInModule(String child, int dept):
//		List<AnalysedModuleDTO> foundModules = new ArrayList<AnalysedModuleDTO>();
//		for(AnalysedModuleDTO module: queryService.getChildModulesInModule("declarations", 2)){
//			foundModules.add(module);
//		}
//		printRecursive(foundModules, "");
		
		try {  
	     	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());    
	     } catch (Exception ex) { }
		
		ModelCreationService modelService = new FamixCreationServiceImpl();
		System.out.println(modelService.represent());
		new AnalyseDebuggingFrame();

	}
	//Testing the getChildModulesInModule(String child, int dept):
//	private static void printRecursive(List<AnalysedModuleDTO> modules, String indent){
//		if(modules != null && !modules.isEmpty()){
//			for(AnalysedModuleDTO module: modules){
//				System.out.println("Module:\n" + module.toString());
//				printRecursive(module.subModules, "\t");
//			}
//		}
//	}
}	