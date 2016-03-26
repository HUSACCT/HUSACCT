package husacct.define.task;

import husacct.ServiceProvider;
import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.module.modules.Component;
import husacct.define.domain.services.AppliedRuleDomainService;
import husacct.define.domain.services.DefaultRuleDomainService;
import husacct.define.domain.services.ModuleDomainService;
import husacct.define.domain.services.SoftwareUnitDefinitionDomainService;
import husacct.define.domain.services.WarningMessageService;
import husacct.define.presentation.DefineInternalFrame;
import husacct.define.presentation.jpanel.DefinitionJPanel;
import husacct.define.presentation.utils.JPanelStatus;
import husacct.define.presentation.utils.UiDialogs;
import husacct.define.task.components.AbstractDefineComponent;
import husacct.define.task.components.AnalyzedModuleComponent;
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

		private static DefinitionController instance;

		public static DefinitionController getInstance() {
			return instance == null ? (instance = new DefinitionController())
					: instance;
		}

		public static void setInstance(DefinitionController dC) {
			instance = dC;
		}

		private DefineInternalFrame defineInternalFrame;
		private AppliedRuleDomainService appliedRuleService;
		private DefaultRuleDomainService defaultRuleService;
		private SoftwareUnitDefinitionDomainService softwareUnitDefinitionDomainService;
		private ModuleDomainService moduleService;
		private List<Observer> observersWithinDefine;
		private Logger logger;
		private long selectedModuleId = -1;

		public DefinitionController() {
			observersWithinDefine = new ArrayList<Observer>();
			logger = Logger.getLogger(DefinitionController.class);
			moduleService = new ModuleDomainService();
			appliedRuleService = new AppliedRuleDomainService();
			softwareUnitDefinitionDomainService = new SoftwareUnitDefinitionDomainService();
			defaultRuleService = new DefaultRuleDomainService();
		}

		private void addChildComponents(AbstractDefineComponent parentComponent,
				ModuleStrategy module) {
			AbstractDefineComponent childComponent = DefineComponentFactory.getDefineComponent(module);
			for (ModuleStrategy subModule : module.getSubModules()) {
				//logger.debug(module.getName() + "  ]" + module.getType());
				addChildComponents(childComponent, subModule);
			}
			parentComponent.addChild(childComponent);
		}

		@Override
		public void addObserver(Observer o) {
			if (!observersWithinDefine.contains(o)) {
				observersWithinDefine.add(o);
			}
		}

		public ArrayList<Long> getAppliedRuleIdsBySelectedModule() {
			return appliedRuleService.getAppliedRulesIdsByModuleFromId(getSelectedModuleId());
		}

		/**
		 * This function will return a hash map with the details of the requested
		 * module.
		 */
		public HashMap<String, Object> getModuleDetails(long moduleId) {
			HashMap<String, Object> moduleDetails = new HashMap<String, Object>();

			if (moduleId != -1) {
				try {
					ModuleStrategy module = moduleService.getModuleById(moduleId);
					moduleDetails.put("id", module.getId());
					moduleDetails.put("name", module.getName());
					moduleDetails.put("description", module.getDescription());
					moduleDetails.put("type", module.getType());

				} catch (Exception e) {
					logger.error("getModuleDetails() - exception: "
							+ e.getMessage());
					UiDialogs.errorDialog(getDefinitionPanel(), e.getMessage());
				}
			}
			return moduleDetails;
		}

		public String getModuleName(long moduleId) {
			String moduleName = "Root";
			if (getSelectedModuleId() != -1) {
				moduleName = moduleService.getModuleNameById(getSelectedModuleId());
			}
			return moduleName;
		}

		public AbstractDefineComponent getModuleTreeComponents() {
			JPanelStatus.getInstance("Updating Modules").start();

			SoftwareArchitectureComponent rootComponent = new SoftwareArchitectureComponent();
			ArrayList<ModuleStrategy> modules = moduleService.getSortedModules();
			for (ModuleStrategy module : modules) {

				addChildComponents(rootComponent, module);
			}

			JPanelStatus.getInstance().stop();
			return rootComponent;
		}

	    /**
	     * Returns an DefineInternalFrame with an added DefenitionJPanel.
	     */
	    public DefineInternalFrame getNewDefineInternalFrame() {
	    	defineInternalFrame = new DefineInternalFrame();
	    	clearObserversWithinDefine();
	    	return defineInternalFrame;
	        }

	    public ArrayList<String> getRegExSoftwareUnitNamesBySelectedModule() {
			return softwareUnitDefinitionDomainService
					.getRegExSoftwareUnitNames(getSelectedModuleId());
		}

		public HashMap<String, Object> getRuleDetailsByAppliedRuleId(
				long appliedRuleId) {
			AppliedRuleStrategy rule = appliedRuleService.getAppliedRuleById(appliedRuleId);
			HashMap<String, Object> ruleDetails = new HashMap<String, Object>();
			ruleDetails.put("id", rule.getId());
			ruleDetails.put("description", rule.getDescription());
			ruleDetails.put("dependencies", rule.getDependencyTypes());
			ruleDetails.put("moduleFromName", rule.getModuleFrom().getName());
			ruleDetails.put("moduleToName", rule.getModuleTo().getName());
			ruleDetails.put("enabled", rule.isEnabled());
			ruleDetails.put("regex", rule.getRegex());
			ruleDetails.put("ruleTypeKey", rule.getRuleTypeKey());
			ruleDetails.put("numberofexceptions", rule.getExceptions().size());
			return ruleDetails;
		}

		public long getSelectedModuleId() {
			return selectedModuleId;
		}

		public ArrayList<String> getSoftwareUnitNamesBySelectedModule() {
			return softwareUnitDefinitionDomainService.getSoftwareUnitNames(getSelectedModuleId());
		}

		public String getSoftwareUnitTypeBySoftwareUnitName(String softwareUnitName) {
			return softwareUnitDefinitionDomainService
					.getSoftwareUnitType(softwareUnitName);
		}

		public void clearObserversWithinDefine() {
			observersWithinDefine.clear();
		}

		public DefineInternalFrame getDefineInternalFrame() {
			if (defineInternalFrame != null) {
				return defineInternalFrame;
			} else {
				getNewDefineInternalFrame(); 
				return defineInternalFrame;
			}
		}

		public DefinitionJPanel getDefinitionPanel() {
			return getDefineInternalFrame().getDefinitionPanel();
		}

		public boolean isAnalysed() {
			return ServiceProvider.getInstance().getAnalyseService().isAnalysed();
		}

		public void moveLayerDown(long layerId) {
			logger.info("Moving layer down");
			try {
				if (layerId != -1) {
					JPanelStatus.getInstance("Moving layer down").start();
					moduleService.moveLayerDown(layerId);
					this.notifyObservers();
				}
			} catch (Exception e) {
				logger.error("moveLayerDown() - exception: " + e.getMessage());
				UiDialogs.errorDialog(getDefinitionPanel(), e.getMessage());
			} finally {
				JPanelStatus.getInstance().stop();
			}
		}

		public void moveLayerUp(long layerId) {
			logger.info("Moving layer up");
			try {
				if (layerId != -1) {
					JPanelStatus.getInstance("Moving layer up").start();
					moduleService.moveLayerUp(layerId);
					this.notifyObservers();
				}
			} catch (Exception e) {
				logger.error("moveLayerUp() - exception: " + e.getMessage());
				UiDialogs.errorDialog(getDefinitionPanel(), e.getMessage());
			} finally {
				JPanelStatus.getInstance().stop();
			}
		}

		@Override
		public void notifyObservers() {
			long moduleId = getSelectedModuleId();
			for (Observer o : observersWithinDefine) {
				o.update(this, moduleId);
			}
		}
		
		public void notifyAnalyzedObservers() {
			for (Observer o : observersWithinDefine) {
				o.update(this, "updateSoftwareTree");
			}
		}
		/**
		 * This function will load notify all to update their data
		 */
		public void notifyObservers(long moduleId) {
			for (Observer o : observersWithinDefine) {
				o.update(this, moduleId);
			}
		}

		public void passModuleToService(long selectedModuleId, ModuleStrategy module) {
			String exceptionMessage = "";
			exceptionMessage = moduleService.addModuleToParent(selectedModuleId, module);
			this.notifyObservers();

			if (!exceptionMessage.isEmpty()) {
				UiDialogs.errorDialog(getDefinitionPanel(), exceptionMessage);
			}
			else {
				logger.info("Adding module with Id: " + module.getId() + ", Name: " + module.getName());
			}
		}

		/**
		 * Remove a module by Id
		 */
		public void removeModuleById(long moduleId) {
			logger.info("Removing module with Id: " + moduleId);
			try {
				JPanelStatus.getInstance("Removing ModuleStrategy").start();
				Long parent = moduleService.getParentModuleIdByChildId(moduleId);
				moduleService.removeModuleById(moduleId);
				setSelectedModuleId(parent);
				this.notifyObservers();
			} catch (Exception e) {
				logger.error("removeModuleById(" + moduleId + ") - exception: " + e.getMessage());
				UiDialogs.errorDialog(getDefinitionPanel(), e.getMessage());
				e.printStackTrace();
			} finally {
				JPanelStatus.getInstance().stop();
			}
		}

		public void removeObserver(Observer o) {
			if (observersWithinDefine.contains(o)) {
				observersWithinDefine.remove(o);
			}
		}

		public void removeRules(List<Long> appliedRuleIds) {
			boolean mandatory = false;
			try {
				if (getSelectedModuleId() != -1L && !appliedRuleIds.isEmpty()) {
					for (long appliedRuleID : appliedRuleIds) {
						AppliedRuleStrategy rule = appliedRuleService
								.getAppliedRuleById(appliedRuleID);
						if (defaultRuleService.isMandatoryRule(rule)) {
							mandatory = true;
							UiDialogs.errorDialog(getDefinitionPanel(), ServiceProvider
									.getInstance().getLocaleService()
									.getTranslatedString("DefaultRule")
									+ "\n- " + rule.getRuleTypeKey());
							break;
						}
					}
					if (!mandatory) {
						boolean confirm = UiDialogs.confirmDialog(
								getDefinitionPanel(),
								ServiceProvider
								.getInstance()
								.getLocaleService()
								.getTranslatedString(
										"ConfirmRemoveAppliedRule"),
								"Remove?");
						if (confirm) {
							for (long appliedRuleID : appliedRuleIds) {
								logger.info("Removing rule " + appliedRuleID);
								JPanelStatus.getInstance("Removing applied rule")
								.start();
								appliedRuleService.removeAppliedRule(appliedRuleID);
							}
							this.notifyObservers();
						}
					}
				}
			} catch (Exception e) {
				logger.error("removeRule() - exception: " + e.getMessage());
				UiDialogs.errorDialog(getDefinitionPanel(), e.getMessage());
			} finally {
				JPanelStatus.getInstance().stop();
			}
		}

		/**
		 * Remove the selected software unit
		 */
		public void removeSoftwareUnits(List<String> softwareUnitNames,
				List<String> types) {
			try {
				long moduleId = getSelectedModuleId();
				int location = 0;
				boolean confirm = UiDialogs.confirmDialog(getDefinitionPanel(), ServiceProvider.getInstance().getLocaleService()
						.getTranslatedString("ConfirmRemoveSoftwareUnit"), "Remove?");
			
				for (String softwareUnit : softwareUnitNames) {
					String type = types.get(location);
					logger.info("Removing software unit " + softwareUnit + ", Type: " + type);
					if (moduleId != -1 && softwareUnit != null && !softwareUnit.equals("")) {
						if (confirm) {
							JtreeController.instance().restoreTreeItemm(softwareUnitNames, types);
							JPanelStatus.getInstance("Removing software unit").start();
							
							boolean chekHasCodelevelWarning = WarningMessageService.getInstance().isCodeLevelWarning(softwareUnit);
							if (chekHasCodelevelWarning) {
								boolean confirm2 = UiDialogs.confirmDialog(getDefinitionPanel(),
												"Your about to remove a software unit that does exist at code level",
												"Remove?");
								if (confirm2) {
									softwareUnitDefinitionDomainService.removeSoftwareUnit(moduleId, softwareUnit);
								}
							} else {
								softwareUnitDefinitionDomainService.removeSoftwareUnit(moduleId, softwareUnit);
							}
							this.notifyObservers();
							JPanelStatus.getInstance().stop();
						}
					}
					location++;
				}
			} catch (Exception e) {
				logger.error("removeSoftwareUnit() - exception: " + e.getMessage());
				e.printStackTrace();
				UiDialogs.errorDialog(getDefinitionPanel(), e.getMessage());
			} finally {
				JPanelStatus.getInstance().stop();
			}
		}

		public void setSelectedModuleId(long moduleId) {
			selectedModuleId = moduleId;
			notifyObservers(moduleId);
		}

		@Override
		public void update(Observable o, Object arg) {
			logger.info("update(" + o + ", " + arg + ")");
			long moduleId = getSelectedModuleId();
			notifyObservers(moduleId);
		}

		/**
		 * Function which will save the name and description changes to the module
		 */
		public void updateModule(String moduleName, String moduleDescription) {
			try {
				JPanelStatus.getInstance("Updating module").start();
				long moduleId = getSelectedModuleId();
				if (moduleId != -1) {
					moduleService.updateModule(moduleId, moduleName,
							moduleDescription);
				}
				this.notifyObservers();
				getDefinitionPanel().modulePanel.updateModuleTree();
			} catch (Exception e) {
				logger.error("updateModule() - exception: " + e.getMessage());
				UiDialogs.errorDialog(getDefinitionPanel(), e.getMessage());
			} finally {
				JPanelStatus.getInstance().stop();
			}
		}

		public void updateModuleType(String type) {
			moduleService.updateModuleType(getSelectedModuleId(), type);
			this.notifyObservers();
			getDefinitionPanel().modulePanel.updateModuleTree();
		}

		// Changes the name of the facade subsequently after a module with type "Component"	has its name changed. 
		public void updateFacade(String moduleName){
			logger.info("Updating facade " + moduleName +"Facade");
			try {
				JPanelStatus.getInstance("Updating facade").start();
				long moduleId = getSelectedModuleId();
				if (moduleId != -1) {
					moduleService.updateFacade(moduleId, moduleName);
				}
				this.notifyObservers();
				getDefinitionPanel().modulePanel.updateModuleTree();
			} catch (Exception e) {
				logger.error("updateFacade() - exception: " + e.getMessage());
				UiDialogs.errorDialog(getDefinitionPanel(), e.getMessage());
			} finally {
				JPanelStatus.getInstance().stop();
			}
		}

		public void addModule(String name, String description, String type) {
			ModuleStrategy module= moduleService.createNewModule(type);
			module.set(name, description);
			if (module instanceof Component) {
				ModuleStrategy facade=	moduleService.createNewModule("Facade");
				facade.set("Interface<"+name+">", "This module represents the interface(s) of the component.");
				module.addSubModule(facade);
			}
			this.passModuleToService(getSelectedModuleId(), module);
		}
		
		public boolean saveAnalzedModule(ArrayList<AnalyzedModuleComponent> units) {
			long id = DefinitionController.getInstance().getSelectedModuleId();
			SoftwareUnitController softwareUnitController = new SoftwareUnitController(id);
			softwareUnitController.save(units);
			return true;
		}


	}
