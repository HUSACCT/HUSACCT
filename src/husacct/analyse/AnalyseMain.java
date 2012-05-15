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
		

		String path = "C:/Users/MvH/benchmark/CSharpBenchmark";
		//String path = "C:\\Users\\Thomas\\Downloads\\Mittchel-benchmark-73d2abb\\CSharpBenchmark";
		String[] paths = new String[]{path};
		defService.createApplication("Boobies Sanders Application", paths, "C#", "1.0");
		
		IAnalyseService analyser = provider.getAnalyseService();
		analyser.analyseApplication();
		
		new FamixCreationServiceImpl();
		
		try {  
	     	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());    
	     } catch (Exception ex) { }
		
		ModelCreationService modelService = new FamixCreationServiceImpl();
		System.out.println(modelService.represent());
		new AnalyseDebuggingFrame();

	}
}	