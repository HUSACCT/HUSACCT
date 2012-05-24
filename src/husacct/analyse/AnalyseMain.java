package husacct.analyse;

import husacct.ServiceProvider;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;
import husacct.analyse.presentation.AnalyseDebuggingFrame;
import husacct.define.IDefineService;

import java.io.FileWriter;
import java.io.IOException;

public class AnalyseMain {
	
	public static void main(String[] args){
		ServiceProvider provider = ServiceProvider.getInstance();
		IDefineService defService = provider.getDefineService();
		String path = "c:/project/benchmark";
		
		String[] paths = new String[]{path};
		
		IAnalyseService analyser = provider.getAnalyseService();
		analyser.analyseApplication();

	}
}	