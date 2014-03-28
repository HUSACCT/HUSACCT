package husacct.define.task;

import husacct.ServiceProvider;
import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.module.modules.Component;
import husacct.define.domain.module.modules.Facade;
import husacct.define.domain.services.AppliedRuleDomainService;
import husacct.define.domain.services.DefaultRuleDomainService;
import husacct.define.domain.services.ModuleDomainService;
import husacct.define.domain.services.SoftwareUnitDefinitionDomainService;
import husacct.define.domain.services.WarningMessageService;
import husacct.define.domain.services.stateservice.StateService;
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

		private static DefinitionController instance;

		public static DefinitionController getInstance() {
			return instance == null ? (instance = new DefinitionController())
					: instance;
		}

		public static void setInstance(DefinitionController dC) {
			instance = dC;
		}

		private AppliedRuleDomainService appliedRuleService;
		private DefaultRuleDomainService defaultRuleService;

		private DefinitionJPanel definitionJPanel;
		private Logger logger;
		private ModuleDomainService moduleService;
		private List<Observer> observers;

		private long selectedModuleId = -1;

		private SoftwareUnitDefinitionDomainService softwareUnitDefinitionDomainService;

		public DefinitionController() {
			observers = new ArrayList<Observer>();
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
			if (!observers.contains(o)) {
				observers.add(o);
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
					UiDialogs.errorDialog(definitionJPanel, e.getMessage());
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
			ruleDetails.put("dependencies", rule.getDependencies());
			ruleDetails.put("moduleFromName", rule.getModuleFrom().getName());
			ruleDetails.put("moduleToName", rule.getModuleTo().getName());
			ruleDetails.put("enabled", rule.isEnabled());
			ruleDetails.put("regex", rule.getRegex());
			ruleDetails.put("ruleTypeKey", rule.getRuleType());
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

		public void initSettings() {
			observers.clear();
			definitionJPanel = new DefinitionJPanel();
		}

		/**
		 * Init the user interface for creating/editting the definition.
		 * 
		 * @return JPanel The jpanel
		 */
		public JPanel initUi() {
			return definitionJPanel;
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
				UiDialogs.errorDialog(definitionJPanel, e.getMessage());
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
				UiDialogs.errorDialog(definitionJPanel, e.getMessage());
			} finally {
				JPanelStatus.getInstance().stop();
			}
		}

		@Override
		public void notifyObservers() {
			long moduleId = getSelectedModuleId();
			for (Observer o : observers) {
				o.update(this, moduleId);
			}
		}
		
		public void notifyAnalyzedObservers() {
			long moduleId = getSelectedModuleId();
			for (Observer o : observers) {
				o.update(this, "updateSoftwareTree");
			}
		}
		/**
		 * This function will load notify all to update their data
		 */
		public void notifyObservers(long moduleId) {
			for (Observer o : observers) {
				o.update(this, moduleId);
			}
		}

		public void passModuleToService(long selectedModuleId, ModuleStrategy module) {
			String ExceptionMessage = "";
			
			if (selectedModuleId == -1) {
				moduleService.addModuleToRoot(module);
			} else {
				ExceptionMessage = moduleService.addNewModuleToParent(selectedModuleId, module);
			}
			this.notifyObservers();

			if (!ExceptionMessage.isEmpty()) {
				UiDialogs.errorDialog(definitionJPanel, ExceptionMessage);
			}
			else {
				logger.info("Adding module with id and name: " + module.getId() + module.getName());
			}
		}

		/**
		 * Remove a module by Id
		 */
		public void removeModuleById(long moduleId) {
			logger.info("Removing module by Id " + moduleId);
			try {
				JPanelStatus.getInstance("Removing ModuleStrategy").start();
				moduleService.removeModuleById(moduleId);
				setSelectedModuleId(0);
				this.notifyObservers();
			} catch (Exception e) {
				logger.error("removeModuleById(" + moduleId + ") - exception: "
						+ e.getMessage());
				UiDialogs.errorDialog(definitionJPanel, e.getMessage());
				e.printStackTrace();

			} finally {
				JPanelStatus.getInstance().stop();
			}
		}

		public void removeObserver(Observer o) {
			if (observers.contains(o)) {
				observers.remove(o);
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
							UiDialogs.errorDialog(definitionJPanel, ServiceProvider
									.getInstance().getLocaleService()
									.getTranslatedString("DefaultRule")
									+ "\n- " + rule.getRuleType());
							break;
						}
					}
					if (!mandatory) {
						boolean confirm = UiDialogs.confirmDialog(
								definitionJPanel,
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
				UiDialogs.errorDialog(definitionJPanel, e.getMessage());
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
				boolean confirm = UiDialogs.confirmDialog(definitionJPanel, ServiceProvider.getInstance().getLocaleService()
						.getTranslatedString("ConfirmRemoveSoftwareUnit"), "Remove?");
			
				for (String softwareUnit : softwareUnitNames) {
					String type = types.get(location);
					logger.info("Removing software unit " + softwareUnit);
					if (moduleId != -1 && softwareUnit != null && !softwareUnit.equals("")) {
						if (confirm) {
							JtreeController.instance().restoreTreeItemm(softwareUnitNames, types);
							JPanelStatus.getInstance("Removing software unit").start();
							
							boolean chekHasCodelevelWarning = WarningMessageService.getInstance().isCodeLevelWarning(softwareUnit);
							if (chekHasCodelevelWarning) {
								boolean confirm2 = UiDialogs.confirmDialog(definitionJPanel,
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
				UiDialogs.errorDialog(definitionJPanel, e.getMessage());
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
			} catch (Exception e) {
				logger.error("updateModule() - exception: " + e.getMessage());
				UiDialogs.errorDialog(definitionJPanel, e.getMessage());
			} finally {
				JPanelStatus.getInstance().stop();
			}
		}

		public void updateModuleType(String moduleName, String moduleDescription,
				String type) {
			moduleService.updateModuleType(getSelectedModuleId(), moduleName,
					moduleDescription, type);
			this.notifyObservers();
		}

		public void updateFacade(String moduleName){
			logger.info("Updating facade " + moduleName +"Facade");
			try {
				JPanelStatus.getInstance("Updating facade").start();
				long moduleId = getSelectedModuleId();
				if (moduleId != -1) {
					moduleService.updateFacade(moduleId, moduleName);
				}
				this.notifyObservers();
			} catch (Exception e) {
				logger.error("updateFacade() - exception: " + e.getMessage());
				UiDialogs.errorDialog(definitionJPanel, e.getMessage());
			} finally {
				JPanelStatus.getInstance().stop();
			}
		}

		public void addModule(String name, String description, String type) {
			ModuleStrategy module= moduleService.createNewModule(type);
			module.set(name, description);
			if (module instanceof Component) {
			
				ModuleStrategy facade=	moduleService.createNewModule("Facade");
				facade.set("Facade<"+name+">", "this is the Facade of your Component");
				module.addSubModule(facade);
			}
			
			this.passModuleToService(getSelectedModuleId(), module);
			
			
		}
	}
