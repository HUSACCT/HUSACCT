package husacct.define.task;

import husacct.ServiceProvider;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.define.domain.SoftwareUnitDefinition;
import husacct.define.domain.services.SoftwareUnitDefinitionDomainService;
import husacct.define.presentation.jdialog.SoftwareUnitJDialog;
import husacct.define.presentation.utils.UiDialogs;
import husacct.define.task.components.AbstractCombinedComponent;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;


public class SoftwareUnitController extends PopUpController {

	private SoftwareUnitJDialog softwareUnitFrame;
	private Logger logger;
private	ArrayList<AnalyzedModuleComponent> resultofsearch;
	private SoftwareUnitDefinitionDomainService softwareUnitDefinitionDomainService;
	
	public SoftwareUnitController(long moduleId) {
		logger = Logger.getLogger(SoftwareUnitController.class);
		this.setModuleId(moduleId);
		this.softwareUnitDefinitionDomainService = new SoftwareUnitDefinitionDomainService();
	}
	
	public void fillSoftwareUnitsList(ArrayList<SoftwareUnitDefinition> softwareUnitList){
		AnalysedModuleDTO[] modules = getAnalyzedModules();
		for(AnalysedModuleDTO module : modules) {
			SoftwareUnitDefinition softwareUnit = new SoftwareUnitDefinition(module.name, SoftwareUnitDefinition.Type.valueOf(module.type.toUpperCase()));
			softwareUnitList.add(softwareUnit);
		}
		
		filterAddedSoftwareUnits(softwareUnitList);
	}
	
	private void filterAddedSoftwareUnits(ArrayList<SoftwareUnitDefinition> softwareUnitList) {
		ArrayList<SoftwareUnitDefinition> addedsoftwareUnitList = this.softwareUnitDefinitionDomainService.getSoftwareUnit(currentModuleId);
		for (SoftwareUnitDefinition addedUnit : addedsoftwareUnitList){
			if (softwareUnitList.contains(addedUnit)) {
				softwareUnitList.remove(addedUnit);
			}
			
		}
	}
	
	public AnalyzedModuleComponent getSoftwareUnitTreeComponents() {
		AnalyzedModuleComponent rootComponent = new AnalyzedModuleComponent("root", "Software Units", "root", "public");
		AnalysedModuleDTO[] modules = this.getAnalyzedModules();	
		for(AnalysedModuleDTO module : modules) {
			this.addChildComponents(rootComponent, module);
		}

		return rootComponent;
	}
	
	private AnalysedModuleDTO[] getAnalyzedModules() {
		AnalysedModuleDTO[] modules = ServiceProvider.getInstance().getAnalyseService().getRootModules();
		AnalysedModuleDTO mockModule1 = new AnalysedModuleDTO("externallibrary", "test externallibrary", "externallibrary", "true");
		AnalysedModuleDTO mockModule2 = new AnalysedModuleDTO("subsystem", " test subsystem", "subsystem", "true");
		
		AnalysedModuleDTO[]	testreturnlist = new AnalysedModuleDTO[modules.length+2];
		for (int i = 0; i < modules.length; i++) {
			testreturnlist[i]=modules[i];
		}
		
		
		
		
		
		testreturnlist[modules.length]=mockModule1;
		testreturnlist[modules.length+1]=mockModule2;
		return testreturnlist;
	}
	
	private AnalysedModuleDTO[] getAnalyzedModulesWithChildren() {
		AnalysedModuleDTO[] rootModules = ServiceProvider.getInstance().getAnalyseService().getRootModules();
		
		for(AnalysedModuleDTO rootModule : rootModules) {
			this.addChildModules(rootModule);
		}
		
		return rootModules;
	}
	
	private void addChildModules(AnalysedModuleDTO module) {
		AnalysedModuleDTO[] children = ServiceProvider.getInstance().getAnalyseService().getChildModulesInModule(module.uniqueName);
		
		for(AnalysedModuleDTO subModule : children) {
			module.subModules.add(subModule);
			this.addChildModules(subModule);
		}
	}
	
	private void addChildComponents(AnalyzedModuleComponent parentComponent, AnalysedModuleDTO module) {
		AnalyzedModuleComponent childComponent = new AnalyzedModuleComponent(module.uniqueName, module.name, module.type, module.visibility);
		AnalysedModuleDTO[] children = ServiceProvider.getInstance().getAnalyseService().getChildModulesInModule(module.uniqueName);
		AnalysedModuleComparator comparator = new AnalysedModuleComparator();
        Arrays.sort(children, comparator);
		for(AnalysedModuleDTO subModule : children) {
			this.addChildComponents(childComponent, subModule);
			
		}
		parentComponent.addChild(childComponent);
	}
	
	public void save(String softwareUnit, String type) {
		save(this.getModuleId(), softwareUnit, type);
	}
	
	public void saveRegExToResultTree(String regEx, String packageClass) {
		String translatedRegEx = "";
		if(regEx.startsWith("*") && regEx.endsWith("*")) {
			regEx = regEx.replace("*", "");
			translatedRegEx = regEx;
		} else if(regEx.startsWith("*")) {
			regEx = regEx.replace("*", "");
			translatedRegEx = regEx + "$";
		} else if(regEx.endsWith("*")) {
			regEx = regEx.replace("*", "");
			translatedRegEx = "^" + regEx;
		} else {
			translatedRegEx = "^" + regEx + "$";
		}
		
		Pattern regExPattern = Pattern.compile(translatedRegEx);
			
		for(Iterator<AbstractCombinedComponent> it=JtreeController.instance().GetRootOfModel().getChildren().iterator();it.hasNext();) {
			AnalyzedModuleComponent module =(AnalyzedModuleComponent)it.next();
			Matcher matcher = regExPattern.matcher(module.getName());
			
			if(packageClass.equals("P")) {
				
				if(module.getType().equals("PACKAGE")) {
					while(matcher.find()) {
						logger.info("Adding software unit to module with id " + this.getModuleId());
						try {
							JtreeController.instance().additemgetResultTree(module);
						} catch (Exception e) {
							this.logger.error(e.getMessage());
							UiDialogs.errorDialog(softwareUnitFrame, e.getMessage());
						}
					}
				}
			}
			
			else if(packageClass.equals("C")) {
				if(module.getType().equals("CLASS") || module.getType().equals("INTERFACE")) {
					while(matcher.find()) {
						logger.info("Adding software unit to module with id " + this.getModuleId());
						try {
							JtreeController.instance().additemgetResultTree(module);
						} catch (Exception e) {
							this.logger.error(e.getMessage());
							UiDialogs.errorDialog(softwareUnitFrame, e.getMessage());
						}
					}
				}
			}
			
			else if(packageClass.equals("PC")) {
				while(matcher.find()) {
					logger.info("Adding software unit to module with id " + this.getModuleId());
					try {
						JtreeController.instance().additemgetResultTree(module);
					} catch (Exception e) {
						this.logger.error(e.getMessage());
						UiDialogs.errorDialog(softwareUnitFrame, e.getMessage());
					}
				}
			}
			
			checkChildRegEx(module, regExPattern, packageClass);
		}
	}
	
	public void checkChildRegEx(AnalyzedModuleComponent childModule, Pattern pattern, String packageClass) {
		for(AbstractCombinedComponent mod : childModule.getChildren()) {
			AnalyzedModuleComponent module =(AnalyzedModuleComponent)mod;
			
			Matcher matcher = pattern.matcher(module.getName());
			
			if(packageClass.equals("P")) {
				if(module.getType().equals("PACKAGE")) {
					while(matcher.find()) {
						logger.info("Adding software unit to module with id " + this.getModuleId());
						try {
							JtreeController.instance().additemgetResultTree(module);
						} catch (Exception e) {
							this.logger.error(e.getMessage());
							UiDialogs.errorDialog(softwareUnitFrame, e.getMessage());
						}
					}
				}
			}
			
			else if(packageClass.equals("C")) {
				if(module.getType().equals("CLASS") || module.getType().equals("INTERFACE")) {
					while(matcher.find()) {
						logger.info("Adding software unit to module with id " + this.getModuleId());
						try {
							JtreeController.instance().additemgetResultTree(module);
						} catch (Exception e) {
							this.logger.error(e.getMessage());
							UiDialogs.errorDialog(softwareUnitFrame, e.getMessage());
						}
					}
				}
			}
			
			else if(packageClass.equals("PC")) {
				while(matcher.find()) {
					logger.info("Adding software unit to module with id " + this.getModuleId());
					try {
						JtreeController.instance().additemgetResultTree(module);
					} catch (Exception e) {
						this.logger.error(e.getMessage());
						UiDialogs.errorDialog(softwareUnitFrame, e.getMessage());
					}
				}
			}
			
			checkChildRegEx(module, pattern, packageClass);
		}
	}
	
	public void save(Long moduleId, String softwareUnit, String type) {
		logger.info("Adding software unit to module with id " + this.getModuleId());
		try {
			
			
			DefinitionController.getInstance().notifyObservers();
		} catch (Exception e) {
			this.logger.error(e.getMessage());
			UiDialogs.errorDialog(softwareUnitFrame, e.getMessage());
		}
	}
	
	public void save(Long moduleid,AbstractCombinedComponent component)
	{
		logger.info("Adding software unit to module with id " + this.getModuleId());
		
	}

	public void save(AnalyzedModuleComponent selectedComponent) {
		logger.info("Adding software unit to module with id " + this.getModuleId());
		try {
			
			this.softwareUnitDefinitionDomainService.addSoftwareUnit(this.getModuleId(),selectedComponent);
			
			DefinitionController.getInstance().notifyObservers();
		} catch (Exception e) {
			this.logger.error(e.getMessage());
			UiDialogs.errorDialog(softwareUnitFrame, e.getMessage());
		}
		
	}
}
