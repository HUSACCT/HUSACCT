package husacct.analyse.presentation;

import java.util.ArrayList;
import java.util.List;
import husacct.analyse.task.IAnalyseControlService;
import husacct.analyse.task.AnalyseControlerServiceImpl;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;

public class AnalyseUIController {

	private IAnalyseControlService analyseTaskService = new AnalyseControlerServiceImpl();
	
	public AnalyseUIController(){
		this.analyseTaskService = new AnalyseControlerServiceImpl();
	}
		
	public List<AnalysedModuleDTO> getRootModules(){
		List<AnalysedModuleDTO> rootModules = new ArrayList<AnalysedModuleDTO>();
		for(AnalysedModuleDTO analysedModule: analyseTaskService.getRootModules()){
			rootModules.add(analysedModule);
		}
		return rootModules;
	}
	
	public List<AnalysedModuleDTO> getModulesInModules(String currentModule){
		List<AnalysedModuleDTO> childModules = new ArrayList<AnalysedModuleDTO>();
		for(AnalysedModuleDTO child: analyseTaskService.getChildModulesInModule(currentModule)){
			childModules.add(child);
		}
		return childModules;
	}
	
	public List<AnalysedModuleDTO> listAllModules(){
		List<AnalysedModuleDTO> allModules = new ArrayList<AnalysedModuleDTO>();
		List<AnalysedModuleDTO> rootModules = getRootModules();
		allModules.addAll(rootModules);
		for(AnalysedModuleDTO rootModule: rootModules){
			allModules.addAll(listAllModulesInModule(rootModule.uniqueName));
		}
		return allModules;
	}
	
	public List<AnalysedModuleDTO> listAllModulesInModule(String uniqueModuleName){
		List<AnalysedModuleDTO> allModulesInModule = new ArrayList<AnalysedModuleDTO>();
		if(uniqueModuleName != null && !uniqueModuleName.equals("")){
			List<AnalysedModuleDTO> innerModules = getModulesInModules(uniqueModuleName);
			allModulesInModule.addAll(innerModules);
			for(AnalysedModuleDTO module: innerModules) {
				allModulesInModule.addAll(listAllModulesInModule(module.uniqueName));
			}
		}
		return allModulesInModule;
	}
	
	public List<DependencyDTO> listDependencies(List<AnalysedModuleDTO> from, List<AnalysedModuleDTO> to){
		List<DependencyDTO> dependencies = new ArrayList<DependencyDTO>();
		for(AnalysedModuleDTO fromModule: from){
			for(AnalysedModuleDTO toModule: to){
				for(DependencyDTO dependency: analyseTaskService.getDependencies(fromModule.uniqueName, toModule.uniqueName)){
					if(!dependencies.contains(dependency)) dependencies.add(dependency);
				}
			}
		}
		return dependencies;
	}
}
