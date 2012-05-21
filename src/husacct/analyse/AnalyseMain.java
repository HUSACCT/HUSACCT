package husacct.analyse;

import husacct.ServiceProvider;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;
import husacct.analyse.presentation.AnalyseDebuggingFrame;
import husacct.define.IDefineService;

public class AnalyseMain {
	public static void main(String[] args){
		
		ServiceProvider provider = ServiceProvider.getInstance();
		IDefineService defService = provider.getDefineService();
		
		String path = "C:/Users/Thomas/Downloads/Mittchel-benchmark-73d2abb/CSharpBenchmark";
		//String path = "C:\\Tools\\Workspaces\\School\\TO Blok 3 Jaar 3\\Army\\src";
		//C:\Tools\Workspaces\School\TO Blok 3 Jaar 3\Copy of Java Recognition Test\src
		//String path = "C:/Tools/Workspaces/School/TO Blok 3 Jaar 3/SimpleTestArmy/src";

		String[] paths = new String[]{path};
		defService.createApplication("Boobies Sanders Application", paths, "C#", "1.0");
		
		IAnalyseService analyser = provider.getAnalyseService();
		analyser.analyseApplication();
		
		FamixCreationServiceImpl impl = new FamixCreationServiceImpl();
		System.out.println(impl.represent());
		
		System.out.println(new FamixCreationServiceImpl().represent());
//		try {  
//	     	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());    
//	     } catch (Exception ex) { }
//		
		new AnalyseDebuggingFrame();
		
	}
}	