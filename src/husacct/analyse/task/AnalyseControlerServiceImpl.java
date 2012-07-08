package husacct.analyse.task;

import org.jdom2.Element;

import husacct.analyse.domain.IAnalyseDomainService;
import husacct.analyse.domain.AnalyseDomainServiceImpl;
import husacct.analyse.task.analyser.ApplicationAnalyser;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;

public class AnalyseControlerServiceImpl implements IAnalyseControlService{

	private ApplicationAnalyser analyserService; 
	private IAnalyseDomainService domainService;
	private DependencyExportController exportController;
	
	public AnalyseControlerServiceImpl(){
		this.domainService = new AnalyseDomainServiceImpl();
		this.analyserService = new ApplicationAnalyser();
		this.exportController = new DependencyExportController();
	}
	
	public void reset(){
		domainService.clearModel();
	}
	
	@Override
	public void analyseApplication(String[] paths, String programmingLanguage) {
		domainService.clearModel();
		analyserService.analyseApplication(paths, programmingLanguage);
	}
	
	@Override
	public String[] getAvailableLanguages() {
		return analyserService.getAvailableLanguages();
	}
	
	public AnalysedModuleDTO getModuleForUniqueName(String uniquename){
		return domainService.getModuleForUniqueName(uniquename);
	}
	
	@Override
	public AnalysedModuleDTO[] getRootModules() {
		return domainService.getRootModules();
	}
	

	@Override
	public AnalysedModuleDTO[] getChildModulesInModule(String from) {
		return domainService.getChildModulesInModule(from);
	}
	
	@Override
	public AnalysedModuleDTO[] getChildModulesInModule(String from, int depth) {
		return domainService.getChildModulesInModule(from, depth);
	}
	
	@Override
	public AnalysedModuleDTO getParentModuleForModule(String child) {
		return domainService.getParentModuleForModule(child);
	}
	
	@Override
	public DependencyDTO[] getAllDependencies(){
		return domainService.getAllDependencies();
	}
	
	@Override
	public DependencyDTO[] getDependencies(String from, String to) {
		return domainService.getDependencies(from, to);
	}

	@Override
	public DependencyDTO[] getDependencies(String from, String to, String[] dependencyFilter) {
		return domainService.getDependencies(from, to, dependencyFilter);
	}
	
	@Override
	public DependencyDTO[] getDependenciesFrom(String from) {
		return domainService.getDependenciesFrom(from);
	}

	@Override
	public DependencyDTO[] getDependenciesFrom(String from, String[] dependencyFilter) {
		return domainService.getDependenciesFrom(from, dependencyFilter);
	}

	@Override
	public DependencyDTO[] getDependenciesTo(String to) {
		return domainService.getDependenciesTo(to);
	}

	@Override
	public DependencyDTO[] getDependenciesTo(String to, String[] dependencyFilter) {
		return domainService.getDependenciesTo(to, dependencyFilter);
	}

	@Override
	public Element saveModel() {
		return domainService.saveModel();
	}

	@Override
	public void loadModel(Element analyseElement) {
		domainService.loadModel(analyseElement);
	}
	
	@Override
	public void exportDependencies(String path){
		exportController.export(path);
	}
}
