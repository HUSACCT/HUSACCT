package husacct.analyse;

import javax.swing.UIManager;
import husacct.ServiceProvider;
import husacct.analyse.domain.ModelCreationService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;
import husacct.analyse.presentation.AnalyseDebuggingFrame;
import husacct.define.IDefineService;

public class AnalyseMain {
	public static void main(String[] args){
		
		ServiceProvider provider = ServiceProvider.getInstance();
		IDefineService defService = provider.getDefineService();
		
		String path = "C:\\Users\\Thomas\\Documents\\My Dropbox\\School\\Themaopdracht 9\\Code\\C#";
		String[] paths = new String[]{path};
		defService.createApplication("Boobies Sanders Application", paths, "C#", "1.0");
		
		IAnalyseService analyser = provider.getAnalyseService();
		analyser.analyseApplication();
		
		ModelCreationService creation = new FamixCreationServiceImpl();
		System.out.println(creation.represent());
		
		try {  
	     	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());    
	     } catch (Exception ex) { }
		
		new AnalyseDebuggingFrame();
	}
}	