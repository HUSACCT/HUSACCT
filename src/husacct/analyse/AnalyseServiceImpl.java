package husacct.analyse;

import javax.swing.JInternalFrame;
import org.jdom2.Element;
import husacct.analyse.presentation.AnalyseInternalFrame;
import husacct.analyse.task.IAnalyseControlService;
import husacct.analyse.task.AnalyseControlerServiceImpl;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.savechain.ISaveable;
import husacct.common.services.ObservableService;

//TODO Add implement-clause  ISavable when the savechain is fixed
public class AnalyseServiceImpl extends ObservableService implements IAnalyseService{

	private IAnalyseControlService service;
	private AnalyseInternalFrame analyseInternalFrame;
	private boolean isAnalysed;
	
	public AnalyseServiceImpl(){
		this.service = new AnalyseControlerServiceImpl();
		this.analyseInternalFrame = null;
		this.isAnalysed = false;
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
		super.notifyServiceListeners();
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
	public DependencyDTO[] getAllDependencies(){
		return service.getAllDependencies();
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

//	@Override
//	public Element getWorkspaceData() {
//		return service.saveModel();
//	}
//
//	@Override
//	public void loadWorkspaceData(Element workspaceData) {
//		//TODO Uncomment the following line to make the loading of work working. This was excluded
//		// in the first delivery, due to memory problems in combination with coming deadlines and demo's.
//		service.loadModel(workspaceData);
//	}
}
