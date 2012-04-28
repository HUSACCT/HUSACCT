package husacct.analyse;

import javax.swing.UIManager;

import husacct.ServiceProvider;
import husacct.analyse.domain.ModelService;
import husacct.analyse.domain.famix.FamixModelServiceImpl;
import husacct.analyse.presentation.AnalyzeFramePackages;
import husacct.analyse.presentation.AnalyzeGUI;
import husacct.define.IDefineService;

public class AnalyseMain {
	public static void main(String[] args){
		
		//Test COmment Voor Rensie Lange Jan
		
		//Test Analyse-service
		ServiceProvider provider = ServiceProvider.getInstance();
		IDefineService defService = provider.getDefineService();
		
		String path = "/Users/Erik/Documents/Hogeschool Utrecht/Jaar 3/Specialisatie/ThemaOpdracht/HUSACCT Develop/AnalysedProject";
		String[] paths = new String[]{path};
		defService.createApplication("Boobies Sanders Application", paths, "Java", "1.0");
		
		IAnalyseService analyser = provider.getAnalyseService();
		analyser.analyseApplication();
		ModelService service = new FamixModelServiceImpl();
//		service.printModel();
		
		//Test All Query-services via output in frame!
		try {  
	     	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());    
	     } catch (Exception ex) {
	     }
		
//		new AnalyzeGUI(); 
		new AnalyzeFramePackages(service.represent());
	}
}