package husacct.define.analyzer;

import husacct.ServiceProvider;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ExternalSystemDTO;
import husacct.common.dto.ProjectDTO;
import husacct.define.domain.module.ModuleStrategy;
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

public class AnalyzedUnitComparator {

	public AnalyzedModuleComponent calucalteChanges(
			AbstractCombinedComponent left, AbstractCombinedComponent right) {

		ArrayList<AbstractCombinedComponent> toBeDeleted = new ArrayList<AbstractCombinedComponent>();
		ArrayList<AbstractCombinedComponent> toBeAaded = new ArrayList<AbstractCombinedComponent>();
		int leftsize = left.getChildren().size();
		int rightsize = right.getChildren().size();
		Collections.sort(left.getChildren());
		Collections.sort(right.getChildren());
		if (leftsize == rightsize) {

			isequal(left, right, toBeDeleted, toBeAaded);
		} else if (leftsize > rightsize) {
			isLessEqual(left, right, toBeDeleted, toBeAaded);

		} else if (leftsize < rightsize) {
			isequal(left, right, toBeDeleted, toBeAaded);
			isMoreEqual(left, right, toBeDeleted, toBeAaded);

		}

		for (AbstractCombinedComponent remove : toBeDeleted) {

			AnalyzedModuleComponent unittoberemoved = ((AnalyzedModuleComponent) remove);
			unittoberemoved.removeChildFromParent();

			if (unittoberemoved.isMapped()) {

				ModuleStrategy module = StateService.instance().getModulebySoftwareUnitUniqName(unittoberemoved.getUniqueName());
				WarningMessageService.getInstance().addCodeLevelWarning(module.getId(), unittoberemoved);
			}


			AbstractCombinedComponent parent = remove.getParentofChild();

			int index = parent.getChildren().indexOf(remove);
			parent.getChildren().remove(index);
		}

		for (AbstractCombinedComponent newAbstractCombinedComponent : toBeAaded) {

			if (WarningMessageService.getInstance().hasCodeLevelWarning((AnalyzedModuleComponent) newAbstractCombinedComponent)) {

				((AnalyzedModuleComponent) newAbstractCombinedComponent).freeze();
				left.addChild(newAbstractCombinedComponent);

			} else {

				left.addChild(newAbstractCombinedComponent);
			}
		}
		return (AnalyzedModuleComponent) left;
	}

	private void isequal(AbstractCombinedComponent left,
			AbstractCombinedComponent right,
			ArrayList<AbstractCombinedComponent> toBeDeleted,
			ArrayList<AbstractCombinedComponent> toBeAaded) {

		for (int i = 0; i < left.getChildren().size(); i++) {
			compareAbstractCombinedComponent(left.getChildren().get(i), right
					.getChildren().get(i), toBeDeleted, toBeAaded);
			calucalteChanges(left.getChildren().get(i), right.getChildren()
					.get(i));
		}
		Collections.sort(left.getChildren());
		Collections.sort(right.getChildren());
	}

	private void isLessEqual(AbstractCombinedComponent left,
			AbstractCombinedComponent right,
			ArrayList<AbstractCombinedComponent> toBeDeleted,
			ArrayList<AbstractCombinedComponent> toBeAaded) {
		int leftindex = left.getChildren().size();
		int rightindex = right.getChildren().size() - 1;
		Collections.sort(left.getChildren());
		Collections.sort(right.getChildren());
		for (int i = 0; i < leftindex; i++) {

			if (rightindex >= i) {

				if (left.getChildren().get(i).getUniqueName()
						.equals(right.getChildren().get(i).getUniqueName())) {
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

	private void isMoreEqual(AbstractCombinedComponent left,
			AbstractCombinedComponent right,
			ArrayList<AbstractCombinedComponent> toBeDeleted,
			ArrayList<AbstractCombinedComponent> toBeAaded) {

		int leftsize = left.getChildren().size();
		int rightsize = right.getChildren().size();

		for (int i = (rightsize - (rightsize - leftsize)); i < rightsize; i++) {

			boolean isfound = false;
			for (AbstractCombinedComponent u : toBeDeleted) {
				if (u.getUniqueName().equals(
						right.getChildren().get(i).getUniqueName())) {
					isfound = true;
					break;
				}
			}
			if (!isfound) {

				toBeAaded.add(right.getChildren().get(i));
			}
		}

	}

	private void compareAbstractCombinedComponent(
			AbstractCombinedComponent left, AbstractCombinedComponent right,
			ArrayList<AbstractCombinedComponent> toBeDeleted,
			ArrayList<AbstractCombinedComponent> toBeAaded) {

		String AbstractCombinedComponentL = left.getUniqueName();
		String AbstractCombinedComponentR = right.getUniqueName();

		if (AbstractCombinedComponentL.equals(AbstractCombinedComponentR)) {

			ChekifTypeChanged(left, right);

		} else if (!AbstractCombinedComponentL
				.equals(AbstractCombinedComponentR)) {
			toBeDeleted.add(left);
			toBeAaded.add(right);
		}
		Collections.sort(left.getChildren());
		Collections.sort(right.getChildren());
	}

	private void ChekifTypeChanged(AbstractCombinedComponent left,
			AbstractCombinedComponent right) {

		if (!left.getType().equals(right.getType())) {

			left.setType(right.getType());
		}

	}

	public AnalyzedModuleComponent getSoftwareUnitTreeComponents() {
		StateService.instance().getAnalzedModuleRegistry().reset();
		JtreeController.instance().setLoadState(true);
		AnalyzedModuleComponent rootComponent = new AnalyzedModuleComponent(
				"root", "Application", "application", "public");
		addExternalComponents(rootComponent);

		ApplicationDTO application = ServiceProvider.getInstance()
				.getControlService().getApplicationDTO();

		for (ProjectDTO project : application.projects) {
			AnalyzedModuleComponent projectComponent = new AnalyzedModuleComponent(
					project.name, project.name, "root", "public");
			for (AnalysedModuleDTO module : project.analysedModules) {

				this.addChildComponents(projectComponent, module);
			}
			rootComponent.addChild(projectComponent);
		}

		Collections.sort(rootComponent.getChildren());

		return rootComponent;
	}

	private void addChildComponents(AnalyzedModuleComponent parentComponent,
			AnalysedModuleDTO module) {
		AnalyzedModuleComponent childComponent = new AnalyzedModuleComponent(
				module.uniqueName, module.name, module.type, module.visibility);

		AnalysedModuleDTO[] children = ServiceProvider.getInstance()
				.getAnalyseService().getChildModulesInModule(module.uniqueName);
		AnalysedModuleComparator comparator = new AnalysedModuleComparator();
		Arrays.sort(children, comparator);
		for (AnalysedModuleDTO subModule : children) {

			this.addChildComponents(childComponent, subModule);
		}


		parentComponent.addChild(childComponent);
		parentComponent.registerchildrenSize();
	}

	private void addExternalComponents(AnalyzedModuleComponent root) {
		AnalyzedModuleComponent rootOfExterexternalLibrary = new AnalyzedModuleComponent(
				"external library", "External Systems", "externalpackage",
				"public");
		
		ExternalSystemDTO[] externalSystems = ServiceProvider.getInstance()
				.getAnalyseService().getExternalSystems();
		for (ExternalSystemDTO exe : externalSystems) {
			if (exe.systemPackage.startsWith("java.")) {
				AnalyzedModuleComponent javalib = new AnalyzedModuleComponent(
						exe.systemPackage, exe.systemName, "externallibrary",
						"public");
				rootOfExterexternalLibrary.addChild(javalib);
			} else {
				AnalyzedModuleComponent subsystem = new AnalyzedModuleComponent(
						exe.systemPackage, exe.systemName, "subsystem",
						"public");
				rootOfExterexternalLibrary.addChild(subsystem);
			}

		}

		root.addChild(rootOfExterexternalLibrary);

	}

	public AnalyzedModuleComponent getRootModel() {
		if (!JtreeController.instance().isLoaded()|| !ServiceProvider.getInstance().getControlService().isPreAnalysed()) {

			if(!ServiceProvider.getInstance().getControlService().isPreAnalysed())
			{
				AnalyzedModuleComponent root= JtreeController.instance().getRootOfModel();
				WarningMessageService.getInstance().registerNotMappedUnits(root);
				return root;
			}
			JtreeController.instance().setLoadState(true);
			JtreeController.instance().setCurrentTree(new AnalyzedModuleTree(getSoftwareUnitTreeComponents()));
			AnalyzedModuleComponent root= JtreeController.instance().getRootOfModel();
			WarningMessageService.getInstance().registerNotMappedUnits(root);
			return root;

		} else {
			AnalyzedModuleComponent left = JtreeController.instance()
					.getRootOfModel();
			AnalyzedModuleComponent right = getSoftwareUnitTreeComponents();
			calucalteChanges(left, right);
			WarningMessageService.getInstance().registerNotMappedUnits(left);
			WarningMessageService.getInstance().updateWarnings();
			return left;
		}
	}
}