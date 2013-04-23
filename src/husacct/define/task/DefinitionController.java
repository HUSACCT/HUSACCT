package husacct.define.task;

import husacct.ServiceProvider;
import husacct.define.domain.AppliedRule;
import husacct.define.domain.module.Component;
//import husacct.define.domain.module.ExternalSystem;
import husacct.define.domain.module.Facade;
import husacct.define.domain.module.Layer;
import husacct.define.domain.module.Module;
import husacct.define.domain.module.SubSystem;
import husacct.define.domain.services.AppliedRuleDomainService;
import husacct.define.domain.services.ModuleDomainService;
import husacct.define.domain.services.SoftwareUnitDefinitionDomainService;
import husacct.define.presentation.jpanel.DefinitionJPanel;
import husacct.define.presentation.utils.JPanelStatus;
import husacct.define.presentation.utils.UiDialogs;
import husacct.define.task.components.AbstractDefineComponent;
import husacct.define.task.components.DefineComponentFactory;
import husacct.define.task.components.SoftwareArchitectureComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

public class DefinitionController extends Observable implements Observer {
	
	private DefinitionJPanel definitionJPanel;
	private static DefinitionController instance;
	private List<Observer> observers;
	private Logger logger;
	private long selectedModuleId = -1;
	
	private ModuleDomainService moduleService;
	private AppliedRuleDomainService appliedRuleService;
	private SoftwareUnitDefinitionDomainService softwareUnitDefinitionDomainService;
	
	public static DefinitionController getInstance() {
		return instance == null ? (instance = new DefinitionController()) : instance;
	}
	
	public static void setInstance(DefinitionController dC){
		instance = dC;
	}

	public DefinitionController() {
		this.observers = new ArrayList<Observer>();
		this.logger = Logger.getLogger(DefinitionController.class);
		this.moduleService = new ModuleDomainService();
		this.appliedRuleService = new AppliedRuleDomainService();
		this.softwareUnitDefinitionDomainService = new SoftwareUnitDefinitionDomainService();
	}
	
	public void initSettings() {
		this.observers.clear();
		this.definitionJPanel = new DefinitionJPanel();
	}

	/**
	 * Init the user interface for creating/editting the definition.
	 * 
	 * @return JPanel The jpanel
	 */
	public JPanel initUi() {
		return definitionJPanel;
	}

	public void setSelectedModuleId(long moduleId) {
		this.selectedModuleId = moduleId;
		notifyObservers(moduleId);
	}

	public long getSelectedModuleId() {
		return selectedModuleId;
	}
	
	public boolean addLayer(long selectedModuleId, String layerName, String layerDescription){
		logger.info("Adding layer " + layerName);
		try {
			JPanelStatus.getInstance("Adding Layer").start();
			Layer newLayer = new Layer(layerName, layerDescription);
			this.passModuleToService(selectedModuleId, newLayer);
			return true;
		} catch (Exception e) {
			logger.error("addLayer(" + layerName + ") - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage());
			return false;
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}
	
	public boolean addSubSystem(long selectedModuleId, String moduleName, String moduleDescription){
		logger.info("Adding subsystem " + moduleName);
		try {
			JPanelStatus.getInstance("Adding subsystem").start();
			SubSystem newModule = new SubSystem(moduleName, moduleDescription);
			this.passModuleToService(selectedModuleId, newModule);
			return true;
		} catch (Exception e) {
			logger.error("addSubSystem(" + moduleName + ") - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage());
			return false;
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}
	
	public boolean addComponent(long selectedModuleId, String componentName, String componentDescription,boolean ifWithFacade){
		logger.info("Adding component " + "Facade"+componentName);
		logger.info("Adding component " + componentName);
		try {
			JPanelStatus.getInstance("Adding component").start();
			Component newComponent = new Component(componentName, componentDescription);
			if (ifWithFacade) {
				Facade f= new Facade();
			f.setName("Facade"+componentName);
			newComponent.addSubModule(f);
			
			}
			
			
			
			
			
			
			this.passModuleToService(selectedModuleId, newComponent);
			
			return true;
		} catch (Exception e) {
			logger.error("addComponent(" + componentName + ") - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage());
			return false;
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}
	
	public boolean addExternalLibrary(long selectedModuleId, String libraryName, String libraryDescription){
		logger.info("Adding external library " + libraryName);
		try {
			JPanelStatus.getInstance("Adding external library").start();
			//ExternalSystem newComponent = new ExternalSystem(libraryName, libraryDescription);
			//this.passModuleToService(selectedModuleId, newComponent);
			return true;
		} catch (Exception e) {
			logger.error("addExternalLibrary(" + libraryName + ") - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage());
			return false;
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}
	
	private void passModuleToService(long selectedModuleId, Module module) {
		if(selectedModuleId == -1) {
			this.moduleService.addModuleToRoot(module);
		} else {
			logger.debug("Adding child");
			this.moduleService.addModuleToParent(selectedModuleId, module);
		}
		this.notifyObservers();
	}

	/**
	 * Remove a module by Id
	 */
	public void removeModuleById(long moduleId) {
		logger.info("Removing module by Id " + moduleId);
		try {
			JPanelStatus.getInstance("Removing Module").start();
			this.moduleService.removeModuleById(moduleId);
			this.setSelectedModuleId(-1);
			this.notifyObservers();
		} catch (Exception e) {
			logger.error("removeModuleById(" + moduleId + ") - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage());
			System.out.println(e.getStackTrace());
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}
	
	public void moveLayerUp(long layerId) {
		logger.info("Moving layer up");
		try {
			if (layerId != -1) {
				JPanelStatus.getInstance("Moving layer up").start();
				this.moduleService.moveLayerUp(layerId);
				this.notifyObservers();
			}
		} catch (Exception e) {
			logger.error("moveLayerUp() - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage());
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}
	
	public void moveLayerDown(long layerId) {
		logger.info("Moving layer down");
		try {
			if (layerId != -1) {
				JPanelStatus.getInstance("Moving layer down").start();
				this.moduleService.moveLayerDown(layerId);
				this.notifyObservers();
			}
		} catch (Exception e) {
			logger.error("moveLayerDown() - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage());
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}

	/**
	 * Remove the selected software unit
	 */
	public void removeSoftwareUnits(List<String> softwareUnitNames, List<String> types) {
		try {
			long moduleId = getSelectedModuleId();
			
			boolean confirm = UiDialogs.confirmDialog(definitionJPanel, ServiceProvider.getInstance().getLocaleService().getTranslatedString("ConfirmRemoveSoftwareUnit"), "Remove?");
			
			for(String softwareUnit : softwareUnitNames) {
				for(String type : types) {
				logger.info("Removing software unit " + softwareUnit);

				if (moduleId != -1 && softwareUnit != null && !softwareUnit.equals("")) {
					if (confirm) {
						// Remove the software unit
						logger.info("getting type:" + type);

						JPanelStatus.getInstance("Removing software unit").start();
						if(type.toUpperCase().equals("REGEX")) {
							
							this.softwareUnitDefinitionDomainService.removeRegExSoftwareUnit(moduleId, softwareUnit);
							this.notifyObservers();
						}else{
							this.softwareUnitDefinitionDomainService.removeSoftwareUnit(moduleId, softwareUnit);
						//Update the software unit table
						this.notifyObservers();
						}
						}
				}
			} 
			}
		} catch (Exception e) {
			logger.error("removeSoftwareUnit() - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage());
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}
	
	public void removeRule(long appliedRuleId) {
		logger.info("Removing rule " + appliedRuleId);
		try {
			long moduleId = getSelectedModuleId();

			if (moduleId != -1 && appliedRuleId != -1L) {
				boolean confirm = UiDialogs.confirmDialog(definitionJPanel, ServiceProvider.getInstance().getLocaleService().getTranslatedString("ConfirmRemoveAppliedRule"), "Remove?");
				if (confirm) {
					// Remove the software unit
					JPanelStatus.getInstance("Removing applied rule").start();
					this.appliedRuleService.removeAppliedRule(appliedRuleId);

					// Update the applied rules table
					this.notifyObservers();
				}
			}
		} catch (Exception e) {
			logger.error("removeRule() - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage());
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}

	/**
	 * Function which will save the name and description changes to the module
	 */
	public void updateModule(String moduleName, String moduleDescription) {
		logger.info("Updating module " + moduleName);
		try {
			JPanelStatus.getInstance("Updating module").start();
			long moduleId = getSelectedModuleId();
			if (moduleId != -1) {
				this.moduleService.updateModule(moduleId, moduleName, moduleDescription);
			}
			this.notifyObservers();
		} catch (Exception e) {
			logger.error("updateModule() - exception: " + e.getMessage());
			UiDialogs.errorDialog(definitionJPanel, e.getMessage());
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}
	
	public AbstractDefineComponent getModuleTreeComponents() {
		JPanelStatus.getInstance("Updating Modules").start();
		
		SoftwareArchitectureComponent rootComponent = new SoftwareArchitectureComponent();
		ArrayList<Module> modules = this.moduleService.getSortedModules();
		for (Module module : modules) {
			this.addChildComponents(rootComponent, module);
		}

		JPanelStatus.getInstance().stop();
		return rootComponent;
	}
	
	private void addChildComponents(AbstractDefineComponent parentComponent, Module module) {
		AbstractDefineComponent childComponent = DefineComponentFactory.getDefineComponent(module);
		for(Module subModule : module.getSubModules()) {
			logger.debug(module.getName()+"  ]"+module.getType());
			this.addChildComponents(childComponent, subModule);
		}
		parentComponent.addChild(childComponent);
	}

	
	public String getModuleName(long moduleId){
		String moduleName = "Root";
		if (this.getSelectedModuleId() != -1){
			moduleName = this.moduleService.getModuleNameById(this.getSelectedModuleId());
		}
		return moduleName;
	}
	
	/**
	 * This function will return a hash map with the details of the requested module.
	 */
	public HashMap<String, Object> getModuleDetails(long moduleId) {
		HashMap<String, Object> moduleDetails = new HashMap<String, Object>();

		if (moduleId != -1) {
			try {
				Module module = this.moduleService.getModuleById(moduleId);
				moduleDetails.put("id", module.getId());
				moduleDetails.put("name", module.getName());
				moduleDetails.put("description", module.getDescription());
				moduleDetails.put("type", module.getType());
				
			} catch (Exception e) {
				logger.error("getModuleDetails() - exception: " + e.getMessage());
				UiDialogs.errorDialog(definitionJPanel, e.getMessage());
			}
		}
		return moduleDetails;
	}
	
	public void update(Observable o, Object arg) {
		logger.info("update(" + o + ", " + arg + ")");
		long moduleId = getSelectedModuleId();
		notifyObservers(moduleId);
	}
	
	@Override
	public void notifyObservers(){
		long moduleId = getSelectedModuleId();
		for (Observer o : this.observers){
			o.update(this, moduleId);
		}
	}
	
	/**
	 * This function will load notify all to update their data
	 */
	public void notifyObservers(long moduleId){
		for (Observer o : this.observers){
			o.update(this, moduleId);
		}
	}
	
	public void addObserver(Observer o){
		if (!this.observers.contains(o)){
			this.observers.add(o);
		}
	}
	
	public void removeObserver(Observer o){
		if (this.observers.contains(o)){
			this.observers.remove(o);
		}
	}

	public ArrayList<Long> getAppliedRuleIdsBySelectedModule() {
		return this.appliedRuleService.getAppliedRulesIdsByModuleFromId(getSelectedModuleId());
	}
	
	public HashMap<String, Object> getRuleDetailsByAppliedRuleId(long appliedRuleId){
		AppliedRule rule = this.appliedRuleService.getAppliedRuleById(appliedRuleId);
		HashMap<String, Object> ruleDetails = new HashMap<String, Object>();
		ruleDetails.put("id", rule.getId());
		ruleDetails.put("description", rule.getDescription());
		ruleDetails.put("dependencies", rule.getDependencies());
		ruleDetails.put("moduleFromName", rule.getModuleFrom().getName());
		ruleDetails.put("moduleToName", rule.getModuleTo().getName());
		ruleDetails.put("enabled", rule.isEnabled());
		ruleDetails.put("regex", rule.getRegex());
		ruleDetails.put("ruleTypeKey", rule.getRuleType());
		ruleDetails.put("numberofexceptions", rule.getExceptions().size());
		return ruleDetails;
	}
	
	public ArrayList<String> getSoftwareUnitNamesBySelectedModule() {
		return this.softwareUnitDefinitionDomainService.getSoftwareUnitNames(getSelectedModuleId());
	}
	
	public ArrayList<String> getRegExSoftwareUnitNamesBySelectedModule() {
		return this.softwareUnitDefinitionDomainService.getRegExSoftwareUnitNames(getSelectedModuleId());
	}
	
	public String getSoftwareUnitTypeBySoftwareUnitName(String softwareUnitName){
		return this.softwareUnitDefinitionDomainService.getSoftwareUnitType(softwareUnitName);
	}
	
	public boolean isAnalysed(){
		return ServiceProvider.getInstance().getAnalyseService().isAnalysed();
	}
}
