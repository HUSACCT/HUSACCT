package husacct.analyse;

import java.io.FileWriter;
import java.io.IOException;

import husacct.ServiceProvider;
import husacct.analyse.domain.IModelCreationService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;
import husacct.analyse.domain.famix.FamixPersistencyServiceImpl;
import husacct.analyse.presentation.AnalyseDebuggingFrame;
import husacct.define.IDefineService;

import javax.swing.UIManager;

import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class AnalyseMain {
	public static void main(String[] args){
		
		//Test Comment voor presentatie build-server
		
		ServiceProvider provider = ServiceProvider.getInstance();
		IDefineService defService = provider.getDefineService();
		
		//String path = "C:\\Tools\\Workspaces\\School\\TO Blok 3 Jaar 3\\Army\\src";
		//C:\Tools\Workspaces\School\TO Blok 3 Jaar 3\Copy of Java Recognition Test\src
		//String path = "C:/Tools/Workspaces/School/TO Blok 3 Jaar 3/SimpleTestArmy/src";
		String path = "/Users/Erik/Documents/Hogeschool Utrecht/Jaar 3/Specialisatie/ThemaOpdracht/HUSACCT Develop/Java Recognition Test";
		String[] paths = new String[]{path};
		defService.createApplication("Boobies Sanders Application", paths, "Java", "1.0");
		
		IAnalyseService analyser = provider.getAnalyseService();
		analyser.analyseApplication();
		
//		System.out.println(new FamixCreationServiceImpl().represent());
		
		FamixPersistencyServiceImpl testPersistencyService = new FamixPersistencyServiceImpl();
		XMLOutputter xmlOutput = new XMLOutputter();
		xmlOutput.setFormat(Format.getPrettyFormat());
		try {
			xmlOutput.output(testPersistencyService.saveModel(), new FileWriter("/Users/Erik/Desktop/mytestje.xml"));
			System.out.println("its SAVED => Kijk in C:/ en zoek naar file.xml !! ");
		} catch (IOException e) {
			e.printStackTrace();
		}
//		testPersistencyService.loadAnalysedApplication("");
		
//		try {  
 //	     	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());    
//	     } catch (Exception ex) { }
		
//		try {  
//	     	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());    
//	     } catch (Exception ex) { }
//		
//		new AnalyseDebuggingFrame();
	}
}	