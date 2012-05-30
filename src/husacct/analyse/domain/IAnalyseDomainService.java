package husacct.analyse.domain;

import java.util.HashMap;

import org.jdom2.Element;

import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;

public interface IAnalyseDomainService {
	
	public void clearModel();
	
	public DependencyDTO[] getDependencies(String from, String to);
	public DependencyDTO[] getDependencies(String from, String to, String[] dependencyFilter);
	public DependencyDTO[] getDependenciesFrom(String from);
	public DependencyDTO[] getDependenciesFrom(String from, String[] dependencyFilter);
	public DependencyDTO[] getDependenciesTo(String to);
	public DependencyDTO[] getDependenciesTo(String to, String[] dependencyFilter);
	
	public AnalysedModuleDTO getModuleForUniqueName(String uniquename);
	public AnalysedModuleDTO[] getRootModules();
	public AnalysedModuleDTO[] getChildModulesInModule(String from); 
	public AnalysedModuleDTO[] getChildModulesInModule(String from, int depth);
	public AnalysedModuleDTO getParentModuleForModule(String child);
	
	public Element saveModel();
	public void loadModel(Element analyseElement);
	
	public HashMap<String, DependencyDTO> mapDependencies();
}
