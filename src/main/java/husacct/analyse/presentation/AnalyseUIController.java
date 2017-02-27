package husacct.analyse.presentation;

import husacct.ServiceProvider;
import husacct.analyse.task.AnalyseTaskControl;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.SoftwareUnitDTO;
import husacct.common.locale.ILocaleService;
import husacct.control.IControlService;

import java.util.ArrayList;
import java.util.List;

public class AnalyseUIController {
	private ILocaleService husacctLocaleService = ServiceProvider.getInstance().getLocaleService();
    private IControlService controlService = ServiceProvider.getInstance().getControlService();
    private AnalyseTaskControl analyseTaskControl;


    public AnalyseUIController(AnalyseTaskControl atc) {
    	analyseTaskControl = atc;
    }

    public String translate(String key) {
        return husacctLocaleService.getTranslatedString(key);
    }

    public List<SoftwareUnitDTO> getRootModules() {
        List<SoftwareUnitDTO> rootModules = new ArrayList<>();

        for (SoftwareUnitDTO analysedModule : analyseTaskControl.getSoftwareUnitsInRoot()) {
            rootModules.add(analysedModule);
        }
        return rootModules;
    }

    public List<SoftwareUnitDTO> getModulesInModules(String currentModule) {
        List<SoftwareUnitDTO> childModules = new ArrayList<>();
        for (SoftwareUnitDTO child : analyseTaskControl.getChildUnitsOfSoftwareUnit(currentModule)) {
            childModules.add(child);
        }
        return childModules;
    }

    public List<SoftwareUnitDTO> listAllModules() {
        List<SoftwareUnitDTO> allModules = new ArrayList<>();
        List<SoftwareUnitDTO> rootModules = getRootModules();
        allModules.addAll(rootModules);
        for (SoftwareUnitDTO rootModule : rootModules) {
            allModules.addAll(listAllModulesInModule(rootModule.uniqueName));
        }
        return allModules;
    }

    public List<SoftwareUnitDTO> listAllModulesInModule(String uniqueModuleName) {
        List<SoftwareUnitDTO> allModulesInModule = new ArrayList<>();
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
        List<DependencyDTO> dependencies = new ArrayList<>();
        for (SoftwareUnitDTO fromModule : from) {
            for (SoftwareUnitDTO toModule : to) {
                for (DependencyDTO dependency : analyseTaskControl.getDependenciesFromSoftwareUnitToSoftwareUnit(fromModule.uniqueName, toModule.uniqueName)) {
                    if (!dependencies.contains(dependency)) {
                        dependencies.add(dependency);
                    }
                }
            }
        }
        return dependencies;
    }

    public void exportDependencies(String path) {
    	analyseTaskControl.createDependencyReport(path);
    }
    
    public IControlService getControlService(){
    	return this.controlService;
    }

    public AnalyseTaskControl getAnalyseTaskControl(){
    	return this.analyseTaskControl;
    }

}
