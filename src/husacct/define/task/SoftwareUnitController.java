package husacct.define.task;

import husacct.ServiceProvider;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ProjectDTO;
import husacct.define.domain.SoftwareUnitDefinition;
import husacct.define.domain.services.SoftwareUnitDefinitionDomainService;
import husacct.define.domain.services.WarningMessageService;
import husacct.define.presentation.jdialog.SoftwareUnitJDialog;
import husacct.define.presentation.utils.UiDialogs;
import husacct.define.task.components.AbstractCombinedComponent;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

import java.util.Collections;
import java.util.Iterator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;


public class SoftwareUnitController extends PopUpController {

	private SoftwareUnitJDialog softwareUnitFrame;
	private Logger logger;
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
		AnalyzedModuleComponent rootComponent = new AnalyzedModuleComponent("root", "Projects", "root", "public");
		addExternalComponents(rootComponent);
		
		ApplicationDTO application = ServiceProvider.getInstance().getControlService().getApplicationDTO();
		
		if(application != null) {
			int i = 0;
			for(ProjectDTO project : application.projects) {
				AnalyzedModuleComponent projectComponent = new AnalyzedModuleComponent("project"+i, project.name, "project", "public");
				for(AnalysedModuleDTO module : project.analysedModules) {
					this.addChildComponents(projectComponent, module);
				}
				rootComponent.addChild(projectComponent);
				i++;
			}	
		}
		
		Collections.sort(rootComponent.getChildren());
		rootComponent.updateChilderenPosition();
		return rootComponent;
	}
	
	private AnalysedModuleDTO[] getAnalyzedModules() {
		AnalysedModuleDTO[] modules = ServiceProvider.getInstance().getAnalyseService().getRootModules();
		return modules;
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
		parentComponent.registerchildrenSize();
	}
	
	public void addExternalComponents(AnalyzedModuleComponent root){
		AnalyzedModuleComponent rootOfExterexternalLibrary = new AnalyzedModuleComponent("external library","externallibrary", "externalpackage", "public");
		AnalyzedModuleComponent mockModule1 = new AnalyzedModuleComponent("externallibrary", "test externallibrary", "externallibrary", "true");
		AnalyzedModuleComponent mockModule2 = new AnalyzedModuleComponent("subsystem", " test subsystem", "subsystem", "true");
		rootOfExterexternalLibrary.addChild(mockModule1);
		rootOfExterexternalLibrary.addChild(mockModule2);
		rootOfExterexternalLibrary.registerchildrenSize();
		root.addChild(rootOfExterexternalLibrary);
		
	}
	public void save(String softwareUnit, String type) {
		save(this.getModuleId(), softwareUnit, type);
	}
	
	public void saveRegExToResultTree(String regEx, String packageClass) {
		String translatedRegEx = "";
		boolean packagesOnly = false;
		boolean innerClass = false;
		String start = "^";
		String end = "$";

		if(regEx.startsWith("*.") && regEx.endsWith(".*")) {
			packagesOnly = true;
			regEx = regEx.replace("*.", "");
			regEx = regEx.replace(".*", "");
			if(regEx.contains(".")) {
				innerClass = true;
				StringTokenizer stringTokenizer = new StringTokenizer(regEx, ".");
				while (stringTokenizer.hasMoreElements()) {
					translatedRegEx = translatedRegEx + start + stringTokenizer.nextToken() + end + ".";
				}
				translatedRegEx = translatedRegEx.replaceAll("\\.(?!.*\\.)","");
			}
			else
				translatedRegEx = start + regEx + end;
		} else if(regEx.startsWith("*.")) {
			packagesOnly = true;
			regEx = regEx.replace("*.", "");
			if(regEx.contains(".")) {
				innerClass = true;
				StringTokenizer stringTokenizer = new StringTokenizer(regEx, ".");
				while (stringTokenizer.hasMoreElements()) {
					translatedRegEx = translatedRegEx + start + stringTokenizer.nextToken() + end + ".";
				}
				translatedRegEx = translatedRegEx.replaceAll("\\.(?!.*\\.)","");
			}
			else
				translatedRegEx = start + regEx + end;
		} else if(regEx.endsWith(".*")) {
			packagesOnly = true;
			regEx = regEx.replace(".*", "");
			if(regEx.contains(".")) {
				innerClass = true;
				StringTokenizer stringTokenizer = new StringTokenizer(regEx, ".");
				while (stringTokenizer.hasMoreElements()) {
					translatedRegEx = translatedRegEx + start + stringTokenizer.nextToken() + end + ".";
				}
				translatedRegEx = translatedRegEx.replaceAll("\\.(?!.*\\.)","");
			}
			else
				translatedRegEx = start + regEx + end;
		} else if(regEx.startsWith("*") && regEx.endsWith("*")) {
			regEx = regEx.replace("*", "");
			translatedRegEx = regEx;
		} else if(regEx.startsWith("*")) {
			regEx = regEx.replace("*", "");
			translatedRegEx = regEx + end;
		} else if(regEx.endsWith("*")) {
			regEx = regEx.replace("*", "");
			translatedRegEx = start + regEx;
		} else {
			translatedRegEx = start + regEx + end;
		}
		
		Pattern regExPattern = null;

		for(Iterator<AbstractCombinedComponent> it=JtreeController.instance().getRootOfModel().getChildren().iterator();it.hasNext();) {
			AnalyzedModuleComponent module =(AnalyzedModuleComponent)it.next();

			if(!packagesOnly && !innerClass) {
				regExPattern = Pattern.compile(translatedRegEx);
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
								
								this.logger.error(e.getStackTrace());
								UiDialogs.errorDialog(softwareUnitFrame, e.getMessage());
							}
						}
					}
				}

				else if(packageClass.equals("PC")) {
					while(matcher.find()) {
					//	logger.info("Adding software unit to module with id " + this.getModuleId());
						try {
							
							
							
							JtreeController.instance().additemgetResultTree(module);
						} catch (Exception e) {
							//this.logger.error(e.getMessage());
							UiDialogs.errorDialog(softwareUnitFrame, e.getMessage());
						}
					}
				}
				checkChildRegEx(module, regExPattern, packageClass, packagesOnly, false);
			}

			else if(packagesOnly && !innerClass) {
				regExPattern = Pattern.compile(translatedRegEx);
				Matcher matcher = regExPattern.matcher(module.getName());
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
				checkChildRegEx(module, regExPattern, packageClass, packagesOnly, false);
			}
			
			else if(innerClass) {
				StringTokenizer stringTokenizer = new StringTokenizer(translatedRegEx, ".");
				String firstPackage = stringTokenizer.nextToken();
				regExPattern = Pattern.compile(firstPackage);
				Matcher matcher = regExPattern.matcher(module.getName());
				if(matcher.find()) {
					translatedRegEx = translatedRegEx.replace(firstPackage + ".", "");
					regExPattern = Pattern.compile(translatedRegEx);
					checkChildRegEx(module, regExPattern, packageClass, packagesOnly, true);
				}
			}
		}
	}

	public void checkChildRegEx(AnalyzedModuleComponent childModule, Pattern pattern, String packageClass, boolean packagesOnly, boolean innerClass) {
		String translatedRegEx = pattern.pattern();
		String nextPackage = "";
		if(innerClass) {
			StringTokenizer stringTokenizer = new StringTokenizer(translatedRegEx, ".");
			nextPackage = stringTokenizer.nextToken();
			pattern = Pattern.compile(nextPackage);
		}
		
		for(AbstractCombinedComponent mod : childModule.getChildren()) {
			AnalyzedModuleComponent module =(AnalyzedModuleComponent)mod;

			if(!packagesOnly && !innerClass) {
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
				checkChildRegEx(module, pattern, packageClass, packagesOnly, false);
			}
			
			else if(packagesOnly && !innerClass) {
				Matcher matcher = pattern.matcher(module.getName());
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
				checkChildRegEx(module, pattern, packageClass, packagesOnly, false);
			}
			
			else if(innerClass) {
				Matcher matcher = pattern.matcher(module.getName());
				if(matcher.find()) {
					translatedRegEx = translatedRegEx.replace(nextPackage, "");
					if(translatedRegEx.startsWith(".")) {
						translatedRegEx = translatedRegEx.substring(1, translatedRegEx.length());
						pattern = Pattern.compile(translatedRegEx);
						checkChildRegEx(module, pattern, packageClass, packagesOnly, true);
					}
					else {
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
		}
	}
	
	public void saveRegEx(ArrayList<AnalyzedModuleComponent> selectedComponents, String regExName) {
		logger.info("Adding software unit to module with id " + this.getModuleId() + " to regex " + regExName);
		
		try {
			
			this.softwareUnitDefinitionDomainService.addSoftwareUnitToRegex(this.getModuleId(), selectedComponents, regExName);
			
			DefinitionController.getInstance().notifyObservers();
		} catch (Exception e) {
			this.logger.error(e.getMessage());
			UiDialogs.errorDialog(softwareUnitFrame, e.getMessage());
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

	public boolean save(AnalyzedModuleComponent selectedComponent) {
		logger.info("Adding software unit to module with id " + this.getModuleId());
		try {
			if(!selectedComponent.isComplete())
			{
				UiDialogs.errorDialog(softwareUnitFrame, "Inconsistency detected: an unit of  \n name: "+selectedComponent.getName()+" type: "+selectedComponent.getType()+" has been already mapped");
				this.logger.error("Inconsistancy detected");
				return false;
			}else if (selectedComponent.getType().toLowerCase().equals("package")&& selectedComponent.isMapped()) {
				UiDialogs.errorDialog(softwareUnitFrame, "The package  \n name: "+selectedComponent.getName()+" type: "+selectedComponent.getType()+" has been already mapped");
				this.logger.error("Inconsistancy detected");
				return false;
			}
			else{
			this.softwareUnitDefinitionDomainService.addSoftwareUnit(this.getModuleId(),selectedComponent);
			}
			DefinitionController.getInstance().notifyObservers();
			return true;
		} catch (Exception e) {
			this.logger.error(e.getMessage());
			return false;
		}
		
	}

	public void editRegEx(ArrayList<AnalyzedModuleComponent> components,String editingRegEx) {
		
		JtreeController.instance().editRegex(this.getModuleId(),components, editingRegEx);
		
		
		
		
	}
}
