package husacct.analyse.presentation;

import husacct.ServiceProvider;
import husacct.analyse.serviceinterface.IAnalyseService;
import husacct.analyse.serviceinterface.dto.DependencyDTO;
import husacct.analyse.serviceinterface.dto.SoftwareUnitDTO;
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

    public List<SoftwareUnitDTO> getRootModules() {
        List<SoftwareUnitDTO> rootModules = new ArrayList<SoftwareUnitDTO>();

        for (SoftwareUnitDTO analysedModule : analyseService.getSoftwareUnitsInRoot()) {
            rootModules.add(analysedModule);
        }
        return rootModules;
    }

    public List<SoftwareUnitDTO> getModulesInModules(String currentModule) {
        List<SoftwareUnitDTO> childModules = new ArrayList<SoftwareUnitDTO>();
        for (SoftwareUnitDTO child : analyseService.getChildUnitsOfSoftwareUnit(currentModule)) {
            childModules.add(child);
        }
        return childModules;
    }

    public List<SoftwareUnitDTO> listAllModules() {
        List<SoftwareUnitDTO> allModules = new ArrayList<SoftwareUnitDTO>();
        List<SoftwareUnitDTO> rootModules = getRootModules();
        allModules.addAll(rootModules);
        for (SoftwareUnitDTO rootModule : rootModules) {
            allModules.addAll(listAllModulesInModule(rootModule.uniqueName));
        }
        return allModules;
    }

    public List<SoftwareUnitDTO> listAllModulesInModule(String uniqueModuleName) {
        List<SoftwareUnitDTO> allModulesInModule = new ArrayList<SoftwareUnitDTO>();
        if (uniqueModuleName != null && !uniqueModuleName.equals("")) {
            List<SoftwareUnitDTO> innerModules = getModulesInModules(uniqueModuleName);
            allModulesInModule.addAll(innerModules);
            for (SoftwareUnitDTO module : innerModules) {
                allModulesInModule.addAll(listAllModulesInModule(module.uniqueName));
            }
        }
        return allModulesInModule;
    }
    
    public List<DependencyDTO> listDependencies(List<SoftwareUnitDTO> from, List<SoftwareUnitDTO> to) {
        List<DependencyDTO> dependencies = new ArrayList<DependencyDTO>();
        for (SoftwareUnitDTO fromModule : from) {
            for (SoftwareUnitDTO toModule : to) {
                for (DependencyDTO dependency : analyseService.getDependenciesFromSoftwareUnitToSoftwareUnit(fromModule.uniqueName, toModule.uniqueName)) {
                    if (!dependencies.contains(dependency)) {
                        dependencies.add(dependency);
                    }
                }
            }
        }
        return dependencies;
    }

    public void exportDependencies(String path) {
        analyseService.createDependencyReport(path);
    }
    
    public IControlService getControlService(){
    	return this.controlService;
    }

    public IAnalyseService getAnalyseService(){
    	return this.analyseService;
    }

}
