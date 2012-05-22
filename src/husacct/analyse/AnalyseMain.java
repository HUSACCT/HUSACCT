package husacct.analyse;

import husacct.ServiceProvider;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;
import husacct.analyse.presentation.AnalyseDebuggingFrame;
import husacct.define.IDefineService;

public class AnalyseMain {
	public static long now = 0;
	public static long antlr = 0;
	public static long parserlexer = 0;
	public static long generator = 0;
	public static long convert = 0;
	public static long stream = 0;
	public static long stringStream = 0;
	public static long charstream = 0;
	public static long lexer;
	public static long parser;
	public static long buffer;
	
	public static void main(String[] args){
		ServiceProvider provider = ServiceProvider.getInstance();
		IDefineService defService = provider.getDefineService();
		String path = "/home/thomas/Downloads/Mittchel-benchmark-73d2abb";
		
		String[] paths = new String[]{path};
		defService.createApplication("Boobies Sanders Application", paths, "C#", "1.0");
		
		IAnalyseService analyser = provider.getAnalyseService();
		analyser.analyseApplication();

		FamixCreationServiceImpl impl = new FamixCreationServiceImpl();
		System.out.println(impl.represent());

		new AnalyseDebuggingFrame();
		
	}
}	