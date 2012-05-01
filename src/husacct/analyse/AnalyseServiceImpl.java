package husacct.analyse;

import javax.swing.JInternalFrame;

import husacct.analyse.presentation.AnalyzePanelGetRootModules;
import husacct.analyse.task.AnalyseControlService;
import husacct.analyse.task.AnalyseControlerServiceImpl;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;

public class AnalyseServiceImpl implements IAnalyseService{

	private AnalyseControlService service = new AnalyseControlerServiceImpl();
	private AnalyseServiceStub stub;
	private boolean isAnalysed = false;
	
	public AnalyseServiceImpl(){
		stub = new AnalyseServiceStub();
	}

	@Override
	public String[] getAvailableLanguages() {
		return service.getAvailableLanguages();
	}

	@Override
	public void analyseApplication() {
		service.analyseApplication();
		this.isAnalysed = true;
	}
	
	@Override
	public boolean isAnalysed() {
		return this.isAnalysed;
	}
	
	@Override
	public JInternalFrame getJInternalFrame() {
		return new AnalyzePanelGetRootModules();
	}
	
	@Override
	public AnalysedModuleDTO[] getRootModules() {
		return service.getRootModules();
	}
	

	@Override
	public AnalysedModuleDTO[] getChildModulesInModule(String from) {
		return service.getChildModulesInModule(from);
//		return stub.getChildModulesInModule(from);
	}

	@Override
	public AnalysedModuleDTO[] getChildModulesInModule(String from, int depth) {
		return stub.getChildModulesInModule(from, depth);
	}

	@Override
	public AnalysedModuleDTO getParentModuleForModule(String child) {
		return stub.getParentModuleForModule(child);
	}

	@Override
	public DependencyDTO[] getDependencies(String from, String to) {
		return stub.getDependencies(from, to);
	}

	@Override
	public DependencyDTO[] getDependenciesFrom(String from) {
		return stub.getDependenciesFrom(from);
	}
	
	@Override
	public DependencyDTO[] getDependenciesTo(String to){
		return stub.getDependenciesTo(to);
	}

	@Override
	public DependencyDTO[] getDependencies(String from, String to, String[] dependencyFilter){
		return stub.getDependencies(from, to, dependencyFilter);
	}
	
	@Override
	public DependencyDTO[] getDependenciesFrom(String from, String[] dependencyFilter){
		return stub.getDependenciesFrom(from, dependencyFilter);
	}
	
	@Override
	public DependencyDTO[] getDependenciesTo(String to, String[] dependencyFilter){
		return stub.getDependenciesTo(to, dependencyFilter);
	}
}
