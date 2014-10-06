package husacct.analyse.presentation;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.locale.ILocaleService;
import husacct.control.IControlService;

import java.util.ArrayList;
import java.util.List;

public class AnalyseUIController {
	private ILocaleService husacctLocaleService = ServiceProvider.getInstance().getLocaleService();
    private IAnalyseService analyseService = ServiceProvider.getInstance().getAnalyseService();
    private IControlService controlService = ServiceProvider.getInstance().getControlService();

    public AnalyseUIController() {
    }

    public String translate(String key) {
        return husacctLocaleService.getTranslatedString(key);
    }

    public List<AnalysedModuleDTO> getRootModules() {
        List<AnalysedModuleDTO> rootModules = new ArrayList<AnalysedModuleDTO>();

        for (AnalysedModuleDTO analysedModule : analyseService.getRootModules()) {
            rootModules.add(analysedModule);
        }
        return rootModules;
    }

    public List<AnalysedModuleDTO> getModulesInModules(String currentModule) {
        List<AnalysedModuleDTO> childModules = new ArrayList<AnalysedModuleDTO>();
        for (AnalysedModuleDTO child : analyseService.getChildModulesInModule(currentModule)) {
            childModules.add(child);
        }
        return childModules;
    }

    public List<AnalysedModuleDTO> listAllModules() {
        List<AnalysedModuleDTO> allModules = new ArrayList<AnalysedModuleDTO>();
        List<AnalysedModuleDTO> rootModules = getRootModules();
        allModules.addAll(rootModules);
        for (AnalysedModuleDTO rootModule : rootModules) {
            allModules.addAll(listAllModulesInModule(rootModule.uniqueName));
        }
        return allModules;
    }

    public List<AnalysedModuleDTO> listAllModulesInModule(String uniqueModuleName) {
        List<AnalysedModuleDTO> allModulesInModule = new ArrayList<AnalysedModuleDTO>();
        if (uniqueModuleName != null && !uniqueModuleName.equals("")) {
            List<AnalysedModuleDTO> innerModules = getModulesInModules(uniqueModuleName);
            allModulesInModule.addAll(innerModules);
            for (AnalysedModuleDTO module : innerModules) {
                allModulesInModule.addAll(listAllModulesInModule(module.uniqueName));
            }
        }
        return allModulesInModule;
    }
    
    public List<DependencyDTO> listDependencies(List<AnalysedModuleDTO> from, List<AnalysedModuleDTO> to) {
        List<DependencyDTO> dependencies = new ArrayList<DependencyDTO>();
        for (AnalysedModuleDTO fromModule : from) {
            for (AnalysedModuleDTO toModule : to) {
                for (DependencyDTO dependency : analyseService.getDependencies(fromModule.uniqueName, toModule.uniqueName)) {
                    if (!dependencies.contains(dependency)) {
                        dependencies.add(dependency);
                    }
                }
            }
        }
        return dependencies;
    }

    public void exportDependencies(String path) {
        analyseService.exportDependencies(path);
    }
    
    public IControlService getControlService(){
    	return this.controlService;
    }

    public IAnalyseService getAnalyseService(){
    	return this.analyseService;
    }

}
