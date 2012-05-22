package husacct.analyse;

import javax.swing.JInternalFrame;
import husacct.analyse.presentation.AnalyseInternalFrame;
import husacct.analyse.task.IAnalyseControlService;
import husacct.analyse.task.AnalyseControlerServiceImpl;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;

public class AnalyseServiceImpl implements IAnalyseService{

	private IAnalyseControlService service = new AnalyseControlerServiceImpl();
	private AnalyseServiceStub stub;
	private AnalyseInternalFrame analyseInternalFrame;
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
		this.analyseInternalFrame = new AnalyseInternalFrame();
		this.isAnalysed = true;
	}
	
	@Override
	public boolean isAnalysed() {
		return this.isAnalysed;
	}
	
	@Override
	public JInternalFrame getJInternalFrame() {
		if(analyseInternalFrame == null) analyseInternalFrame = new AnalyseInternalFrame();
		return analyseInternalFrame;
	}
	
	@Override
	public AnalysedModuleDTO getModuleForUniqueName(String uniquename) {
		return service.getModuleForUniqueName(uniquename);
	}
	
	@Override
	public AnalysedModuleDTO[] getRootModules() {
		return service.getRootModules();
	}
	

	@Override
	public AnalysedModuleDTO[] getChildModulesInModule(String from) {
		return service.getChildModulesInModule(from);
	}

	@Override
	public AnalysedModuleDTO[] getChildModulesInModule(String from, int depth) {
		return service.getChildModulesInModule(from, depth);
	}

	@Override
	public AnalysedModuleDTO getParentModuleForModule(String child) {
		return service.getParentModuleForModule(child);
	}

	@Override
	public DependencyDTO[] getDependencies(String from, String to) {
		return service.getDependencies(from, to);
	}
	
	@Override
	public DependencyDTO[] getDependencies(String from, String to, String[] dependencyFilter){
		return service.getDependencies(from, to, dependencyFilter);
	}

	@Override
	public DependencyDTO[] getDependenciesFrom(String from) {
		return service.getDependenciesFrom(from);
	}
	
	@Override
	public DependencyDTO[] getDependenciesFrom(String from, String[] dependencyFilter){
		return service.getDependenciesFrom(from, dependencyFilter);
	}
	
	@Override
	public DependencyDTO[] getDependenciesTo(String to){
		return service.getDependenciesTo(to);
	}

	@Override
	public DependencyDTO[] getDependenciesTo(String to, String[] dependencyFilter){
		return service.getDependenciesTo(to, dependencyFilter);
	}
}
