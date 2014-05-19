package husacct.define.analyzer;

import husacct.ServiceProvider;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ProjectDTO;
import husacct.define.domain.services.WarningMessageService;
import husacct.define.domain.services.stateservice.StateService;
import husacct.define.presentation.moduletree.AnalyzedModuleTree;
import husacct.define.task.AnalysedModuleComparator;
import husacct.define.task.JtreeController;
import husacct.define.task.components.AbstractCombinedComponent;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import org.apache.log4j.Logger;

public class AnalyzedUnitComparator {
	
	private final Logger logger = Logger.getLogger(AnalyzedUnitComparator.class);


	public AnalyzedModuleComponent calucalteChanges(AbstractCombinedComponent left, AbstractCombinedComponent right) {
		ArrayList<AbstractCombinedComponent> toBeDeleted = new ArrayList<AbstractCombinedComponent>();
		ArrayList<AbstractCombinedComponent> toBeAdded = new ArrayList<AbstractCombinedComponent>();
		int leftsize = left.getChildren().size();
		int rightsize = right.getChildren().size();
		Collections.sort(left.getChildren());
		Collections.sort(right.getChildren());
		if (leftsize == rightsize) {
			isequal(left, right, toBeDeleted, toBeAdded);
		} else if (leftsize > rightsize) {
			isLessEqual(left, right, toBeDeleted, toBeAdded);
		} else if (leftsize < rightsize) {
			isequal(left, right, toBeDeleted, toBeAdded);
			isMoreEqual(left, right, toBeDeleted, toBeAdded);
		}
		
		for (AbstractCombinedComponent newAbstractCombinedComponent : toBeAdded) {
			AnalyzedModuleComponent result = (AnalyzedModuleComponent) newAbstractCombinedComponent;
			if (WarningMessageService.getInstance().hasCodeLevelWarning(result)) {
				result.freeze();
				left.addChild(result);
			} else {
				left.addChild(result);
			}
		}
		
		for (AbstractCombinedComponent remove : toBeDeleted) {
			AnalyzedModuleComponent unittoberemoved = ((AnalyzedModuleComponent) remove);
			WarningMessageService.getInstance().addCodeLevelWarning((AnalyzedModuleComponent)unittoberemoved);
			AbstractCombinedComponent parent = remove.getParentofChild();
			int index = parent.getChildren().indexOf(remove);
			parent.getChildren().remove(index);	
			unittoberemoved.removeChildFromParent();
		}
		return (AnalyzedModuleComponent) left;
	}


	private void isequal(AbstractCombinedComponent left, AbstractCombinedComponent right, ArrayList<AbstractCombinedComponent> toBeDeleted, ArrayList<AbstractCombinedComponent> toBeAdded) {
		for (int i = 0; i < left.getChildren().size(); i++) {
			compareAbstractCombinedComponent(left.getChildren().get(i), right.getChildren().get(i), toBeDeleted, toBeAdded);
			calucalteChanges(left.getChildren().get(i), right.getChildren().get(i));
		}
		Collections.sort(left.getChildren());
		Collections.sort(right.getChildren());
	}

	private void isLessEqual(AbstractCombinedComponent left, AbstractCombinedComponent right, ArrayList<AbstractCombinedComponent> toBeDeleted, ArrayList<AbstractCombinedComponent> toBeAaded) {
		int leftindex = left.getChildren().size();
		int rightindex = right.getChildren().size() - 1;
		Collections.sort(left.getChildren());
		Collections.sort(right.getChildren());
		for (int i = 0; i < leftindex; i++) {
			if (rightindex >= i) {
				if (left.getChildren().get(i).getUniqueName().equals(right.getChildren().get(i).getUniqueName())) {
					ChekifTypeChanged(left, right);
				} else {
					toBeDeleted.add(left.getChildren().get(i));
					toBeAaded.add(right.getChildren().get(i));
				}
			} else {
				toBeDeleted.add(left.getChildren().get(i));
			}
		}
	}

	private void isMoreEqual(AbstractCombinedComponent left, AbstractCombinedComponent right, ArrayList<AbstractCombinedComponent> toBeDeleted, ArrayList<AbstractCombinedComponent> toBeAdded) {
		int leftsize = left.getChildren().size();
		int rightsize = right.getChildren().size();
		for (int i = (rightsize - (rightsize - (leftsize))); i < rightsize; i++) {
			boolean isfound = false;
			for (AbstractCombinedComponent u : toBeDeleted) {
				if (u.getUniqueName().equals(right.getChildren().get(i).getUniqueName())) {
					isfound = true;
					break;
				}
			}
			if (!isfound) {
				toBeAdded.add(right.getChildren().get(i));
			}
		}

	}

	private void compareAbstractCombinedComponent(AbstractCombinedComponent left, AbstractCombinedComponent right, ArrayList<AbstractCombinedComponent> toBeDeleted, ArrayList<AbstractCombinedComponent> toBeAaded) {
		String AbstractCombinedComponentL = left.getUniqueName();
		String AbstractCombinedComponentR = right.getUniqueName();
		if (AbstractCombinedComponentL.equals(AbstractCombinedComponentR)) {
			ChekifTypeChanged(left, right);
		} else if (!AbstractCombinedComponentL.equals(AbstractCombinedComponentR)) {
			toBeDeleted.add(left);
			toBeAaded.add(right);
		}
		Collections.sort(left.getChildren());
		Collections.sort(right.getChildren());
	}

	private void ChekifTypeChanged(AbstractCombinedComponent left, AbstractCombinedComponent right) {
		if (!left.getType().equals(right.getType())) {
			left.setType(right.getType());
		}
	}

	public AnalyzedModuleComponent getSoftwareUnitTreeComponents() {
		StateService.instance().getAnalzedModuleRegistry().reset();
		JtreeController.instance().setLoadState(true);
		AnalyzedModuleComponent rootComponent = new AnalyzedModuleComponent("root", "Application", "application", "public");
		ApplicationDTO application = ServiceProvider.getInstance().getControlService().getApplicationDTO();
		for (ProjectDTO project : application.projects) {
			AnalyzedModuleComponent projectComponent = new AnalyzedModuleComponent(project.name, project.name, "root", "public");
			AnalysedModuleDTO[] moduleList = ServiceProvider.getInstance().getAnalyseService().getRootModules();
			for (AnalysedModuleDTO module : moduleList) {
            	this.addChildComponents(projectComponent, module);
			}
			rootComponent.addChild(projectComponent);
		}
		Collections.sort(rootComponent.getChildren());
		return rootComponent;
	}

	private void addChildComponents(AnalyzedModuleComponent parentComponent, AnalysedModuleDTO module) {
		AnalyzedModuleComponent childComponent = new AnalyzedModuleComponent(module.uniqueName, module.name, module.type, module.visibility);
		AnalysedModuleDTO[] children = ServiceProvider.getInstance().getAnalyseService().getChildModulesInModule(module.uniqueName);
		AnalysedModuleComparator comparator = new AnalysedModuleComparator();
		Arrays.sort(children, comparator);
		for (int i = 0 ; i < children.length; i++) {
			if (children[i] != null){
				this.addChildComponents(childComponent, children[i]);
			}
			else{
				this.logger.warn(new Date().toString() + " Null-child in parent: " + module.uniqueName);
			}
		}
		parentComponent.addChild(childComponent);
		parentComponent.registerchildrenSize();
	}

	public AnalyzedModuleComponent getRootModel() {
		if (!JtreeController.instance().isLoaded() || !ServiceProvider.getInstance().getControlService().isPreAnalysed()) {
			if (!ServiceProvider.getInstance().getControlService().isPreAnalysed()) {
				AnalyzedModuleComponent root = JtreeController.instance().getRootOfModel();
				WarningMessageService.getInstance().resetNotAnalyzed();
				WarningMessageService.getInstance().registerNotMappedUnits(root);
				StateService.instance().registerImportedData();
				return root;
			}
			JtreeController.instance().setLoadState(true);
			JtreeController.instance().setCurrentTree(new AnalyzedModuleTree(getSoftwareUnitTreeComponents()));
			AnalyzedModuleComponent root = JtreeController.instance().getRootOfModel();
			WarningMessageService.getInstance().resetNotAnalyzed();
			WarningMessageService.getInstance().registerNotMappedUnits(root);
			StateService.instance().registerImportedData();
			return root;
		} else {
			AnalyzedModuleComponent left = JtreeController.instance().getRootOfModel();
		    AnalyzedModuleComponent right = getSoftwareUnitTreeComponents();
			//calucalteChanges(left, right); // Disabled 2014-04-20, because it caused exceptions.
			WarningMessageService.getInstance().resetNotAnalyzed();
			WarningMessageService.getInstance().registerNotMappedUnits(right);
			WarningMessageService.getInstance().updateWarnings();
			StateService.instance().registerImportedData();
			return left;
		}
	}
}