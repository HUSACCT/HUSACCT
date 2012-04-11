package husacct.analyse;

import husacct.analyse.domain.famix.FamixModel;
import husacct.analyse.domain.famix.FamixObject;
import husacct.analyse.task.AnalyseControlService;
import husacct.analyse.task.AnalyseControlerServiceImpl;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import javax.naming.directory.InvalidAttributesException;

public class AnalyseServiceImpl implements IAnalyseService{

	private AnalyseControlService service = new AnalyseControlerServiceImpl();
	private AnalyseServiceStub stub;
	private FamixModel famixModel;

	public AnalyseServiceImpl(){
		stub = new AnalyseServiceStub();
		famixModel = new FamixModel();
	}

	@Override
	public String[] getAvailableLanguages() {
		return service.getAvailableLanguages();
	}

	@Override
	public void analyseApplication() {
		List<FamixObject> famixObjects = service.analyseApplication();
		try {
			for (FamixObject famixObject : famixObjects) {
				famixModel.addObject(famixObject);
			}
		} catch (InvalidAttributesException e) {
			e.printStackTrace();
		}
		System.out.println(famixModel);
	}

	@Override
	public DependencyDTO[] getDependency(String from, String to) {
		return stub.getDependency(from, to);
	}

	@Override
	public DependencyDTO[] getDependency(String from) {
		return stub.getDependency(from);
	}

	@Override
	public AnalysedModuleDTO[] getRootModules() {
		return stub.getRootModules();
	}

	@Override
	public AnalysedModuleDTO[] getChildModulesInModule(String from) {
		return stub.getChildModulesInModule(from);
	}

	@Override
	public AnalysedModuleDTO[] getChildModulesInModule(String from, int depth) {
		return stub.getChildModulesInModule(from, depth);
	}

	@Override
	public AnalysedModuleDTO getParentModuleForModule(String child) {
		return stub.getParentModuleForModule(child);
	}
}
