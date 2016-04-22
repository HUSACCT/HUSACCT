package husaccttest.analyse;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.Scanner;

import org.junit.Test;

import husacct.analyse.task.reconstruct.AnalyseReconstructConstants.Algorithm;
import husacct.common.locale.LocaleServiceImpl;
import junit.framework.Assert;

public class ReconstructArchitectureTest_AlgorithmTranslation {
	private static LocaleServiceImpl localeService;
	
	@SuppressWarnings("resource")
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
