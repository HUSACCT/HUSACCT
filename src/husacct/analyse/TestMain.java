package husacct.analyse;

import javax.swing.UIManager;
import husacct.ServiceProvider;
import husacct.analyse.domain.ModelCreationService;
import husacct.analyse.domain.ModelQueryService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;
import husacct.analyse.domain.famix.FamixQueryServiceImpl;
import husacct.analyse.presentation.AnalyseDebuggingFrame;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.define.IDefineService;

public class TestMain {
	public static void main(String[] args){
		
		ServiceProvider provider = ServiceProvider.getInstance();
		IDefineService defService = provider.getDefineService();
		
		//String path = "C:\\Tools\\Workspaces\\School\\TO Blok 3 Jaar 3\\Army\\src";
		String path = "/Users/Erik/Documents/Hogeschool Utrecht/Jaar 3/Specialisatie/ThemaOpdracht/HUSACCT Develop/InnerClassTest";
		String[] paths = new String[]{path};
		defService.createApplication("Boobies Sanders Application", paths, "Java", "1.0");
		
		IAnalyseService analyser = provider.getAnalyseService();
		analyser.analyseApplication();
		
		ModelQueryService queryService = new FamixQueryServiceImpl();
		for(AnalysedModuleDTO dto: queryService.getChildModulesInModule("annotation", 2)){
		}
		ModelCreationService creation = new FamixCreationServiceImpl();
		System.out.println(creation.represent());
		
		
//		ModelQueryService queryService = new FamixQueryServiceImpl();
		//Testing the getChildModulesInModule(String child, int dept):
//		List<AnalysedModuleDTO> foundModules = new ArrayList<AnalysedModuleDTO>();
//		for(AnalysedModuleDTO module: queryService.getChildModulesInModule("declarations", 2)){
//			foundModules.add(module);
//		}
//		printRecursive(foundModules, "");
		
//		try {  
//	     	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());    
//	     } catch (Exception ex) { }
//		
//		new AnalyseDebuggingFrame();
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