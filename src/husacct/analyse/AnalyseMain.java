package husacct.analyse;

import husacct.ServiceProvider;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;
import husacct.analyse.presentation.AnalyseDebuggingFrame;
import husacct.define.IDefineService;

public class AnalyseMain {
	
	public static void main(String[] args){
		long tijd = System.nanoTime();
		ServiceProvider provider = ServiceProvider.getInstance();
		IDefineService defService = provider.getDefineService();
		String path = "c:/project/benchmark";
		
		String[] paths = new String[]{path};
		defService.createApplication("Boobies Sanders Application", paths, "C#", "1.0");
		
		IAnalyseService analyser = provider.getAnalyseService();
		analyser.analyseApplication();

		FamixCreationServiceImpl impl = new FamixCreationServiceImpl();
		System.out.println(impl.represent());
		System.out.println((double)(System.nanoTime() - tijd) / 1000000000);
		new AnalyseDebuggingFrame();
		
	}
}	