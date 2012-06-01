package husacct.analyse;

import husacct.ServiceProvider;
import husacct.define.IDefineService;

public class PerformanceTestMain {

	public static void main(String[] args){
		IDefineService defineService = ServiceProvider.getInstance().getDefineService();
		String path = "/Users/Erik/Documents/Hogeschool Utrecht/Jaar 3/Specialisatie/ThemaOpdracht/HUSACCT Develop/Java Recognition Test";
		defineService.createApplication("Erik", new String[]{path}, "Java", "1.0");
		
		IAnalyseService analyseService = new AnalyseServiceImpl();
		analyseService.analyseApplication();
		
		analyseService.getAllDependencies();
	}
	
}
