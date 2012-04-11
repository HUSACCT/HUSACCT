package husacct.analyse.presentation;

import husacct.analyse.AnalyseServiceStub;
import husacct.analyse.domain.famix.FamixModel;
import husacct.analyse.domain.famix.FamixObject;
import husacct.analyse.domain.famix.FamixPackage;
import husacct.analyse.task.AnalyseControlService;
import husacct.analyse.task.AnalyseControlerServiceImpl;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;

import java.util.List;

import javax.naming.directory.InvalidAttributesException;

public class analyseController {

	
	private AnalyseControlService service = new AnalyseControlerServiceImpl();
	private AnalyseServiceStub stub;
	private FamixModel famixModel;

	public analyseController(){
		stub = new AnalyseServiceStub();
		famixModel = new FamixModel();
	}
 
	public String[] getAvailableLanguages() {
		return service.getAvailableLanguages();
	}
 
	public String  analyseApplication() {
		List<FamixObject> famixObjects = service.analyseApplication();
		try {
			for (FamixObject famixObject : famixObjects) {
				
				if(famixObject instanceof FamixPackage){
//					 System.out.println(famixObject.toString());
				} 
				famixModel.addObject(famixObject);
			}
//			System.out.println(famixModel);
		} catch (InvalidAttributesException e) {
			e.printStackTrace();
		}
		
		return famixModel.toString();
	}

 
	 
	public FamixModel getCompleteModel(){
		return famixModel;
	}

	
}
