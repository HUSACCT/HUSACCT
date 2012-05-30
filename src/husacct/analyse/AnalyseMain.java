package husacct.analyse;

import javax.swing.UIManager;
import husacct.ServiceProvider;
import husacct.analyse.domain.IModelCreationService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;
import husacct.analyse.presentation.AnalyseDebuggingFrame;
import husacct.define.IDefineService;

public class AnalyseMain {
	public static void main(String[] args){
		
		ServiceProvider provider = ServiceProvider.getInstance();
		IDefineService defService = provider.getDefineService();
		
		String path = "C:\\Tools\\Workspaces\\School\\TO Blok 3 Jaar 3\\Copy of Java Recognition Test";
		String[] paths = new String[]{path};
		defService.createApplication("Boobies Sanders Application", paths, "Java", "1.0");
		
		IAnalyseService analyser = provider.getAnalyseService();
		analyser.analyseApplication();
		
		try {  
	     	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());    
	     } catch (Exception ex) { }
		
		IModelCreationService modelService = new FamixCreationServiceImpl();
		System.out.println(modelService.represent());
		new AnalyseDebuggingFrame();
	}
}	