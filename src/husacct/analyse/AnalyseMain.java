package husacct.analyse;

import husacct.ServiceProvider;
import husacct.analyse.domain.IModelCreationService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;
import husacct.analyse.presentation.AnalyseDebuggingFrame;
import husacct.define.IDefineService;

import javax.swing.UIManager;

public class AnalyseMain {
	public static void main(String[] args){
		
		ServiceProvider provider = ServiceProvider.getInstance();
		IDefineService defService = provider.getDefineService();
		
		//String path = "C:\\Tools\\Workspaces\\School\\TO Blok 3 Jaar 3\\Army\\src";
		//C:\Tools\Workspaces\School\TO Blok 3 Jaar 3\Copy of Java Recognition Test\src
		//String path = "C:/Tools/Workspaces/School/TO Blok 3 Jaar 3/SimpleTestArmy/src";
		String path = "C:\\Tools\\Workspaces\\School\\TO Blok 3 Jaar 3\\Copy of Java Recognition Test\\src";
		String[] paths = new String[]{path};
		defService.createApplication("Boobies Sanders Application", paths, "Java", "1.0");
		
		IAnalyseService analyser = provider.getAnalyseService();
		analyser.analyseApplication();
		
		System.out.println(new FamixCreationServiceImpl().represent());
		
//		try {  
//	     	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());    
//	     } catch (Exception ex) { }
//		
//		new AnalyseDebuggingFrame();
	}
}	