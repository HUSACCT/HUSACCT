package husaccttest.analyse;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.junit.Test;

import husacct.analyse.IAnalyseService;
import husacct.analyse.task.AnalyseTaskControl;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants.Algorithm;
import husacct.common.dto.AnalysisStatisticsDTO;
import husacct.common.locale.LocaleServiceImpl;
import husacct.control.ControlServiceImpl;
import husacct.control.task.MainController;
import husacct.control.task.WorkspaceController;
import husacct.define.IDefineService;
import junit.framework.Assert;

public class ReconstructArchitectureTest_AlgorithmTranslation {
	private static String workspacePath;
	private static ControlServiceImpl controlService;
	private static MainController mainController;
	private static WorkspaceController workspaceController;
	private static IDefineService defineService;
	private final static String workspace = "Workspace_HUSACCT_20_Arch_Without_ANTLR.xml";
	private static Logger logger;
	private static IAnalyseService analyseService = null;
	private static LocaleServiceImpl localeService;
	
	private static final String exportFile = "ExportFileAnalysedModel_HUSACCT20_WithoutAntlr.xml";
	private static String exportFilePath;
	private static AnalysisStatisticsDTO analyseStatisticsBeforeReconstruction;
	private static AnalysisStatisticsDTO analyseStatisticsAfterReconstruction;
	private static AnalyseTaskControl analyseTaskControl;
	
	@Test
	public void test() throws FileNotFoundException, IllegalArgumentException, IllegalAccessException {
		localeService = new LocaleServiceImpl();
		Field[] algorithmFields = new Algorithm().getClass().getDeclaredFields();
		
		
		File localeFile = new File("src/husacct/common/resources/locale/husacct_en.properties");
		
		Scanner scanner = new Scanner(localeFile);
		scanner.useDelimiter("\\s");
		
		for(Field f : algorithmFields){
			String fieldName = f.getName();
			String algorithmKey = (String) f.get(fieldName);
			boolean lineFound  = false;
			scanner = new Scanner(localeFile);
			
			while(scanner.hasNextLine()){
				String line = scanner.nextLine();
				
				if(line.contains(fieldName)){
					String[] keyValue = line.split(algorithmKey);
					lineFound = true;
					Assert.assertEquals(localeService.getTranslatedString(algorithmKey), keyValue[1].trim());
					break;
				}
				
				if(!lineFound && !scanner.hasNextLine()){
					fail("No translation found for " + fieldName);
				}
			}
			scanner.reset();
		}
	}

}
