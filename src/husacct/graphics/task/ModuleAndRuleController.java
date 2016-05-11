package husacct.graphics.task;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.AbstractDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.RuleDTO;
import husacct.common.dto.ViolationDTO;
import husacct.define.IDefineService;
import husacct.graphics.domain.DrawingView;
import husacct.graphics.domain.figures.BaseFigure;
import husacct.graphics.domain.figures.ModuleFigure;
import husacct.graphics.domain.figures.RelationFigure;
import husacct.validate.IValidateService;

public class ModuleAndRuleController extends DrawingController {
	protected IAnalyseService			analyseService;
	protected IDefineService			defineService;
	protected IValidateService			validateService;
	
	
	public ModuleAndRuleController() {
		super();
		initializeServices();
	}
	
	@Override
	public DrawingView drawArchitectureTopLevel() {
		super.drawArchitectureTopLevel();
		ModuleFigure[] shownModules = drawing.getShownModules();
		ArrayList<ModuleFigure> modulesInRoot = new ArrayList<ModuleFigure>(Arrays.asList(shownModules));
		modulesInRoot.forEach(moduleFigure -> moduleFigure.setVisible(true));
		ArrayList<String> parentNames = new ArrayList<String>(); // Parent is a module to-be-zoomed-in 
		for (ModuleFigure moduleFigure : modulesInRoot){
			parentNames.add(moduleFigure.getUniqueName());
			parentFigureNameAndTypeMap.put(moduleFigure.getUniqueName(), moduleFigure.getType());
		}
		if (parentNames.size() > 0) {
			resetContextFigures(); // Needed to initialize contextFigures the first time
			this.gatherChildModuleFiguresAndContextFigures_AndDraw(parentNames.toArray(new String[] {}));
		}
		return drawingView;
	}
	
	@Override
	protected ModuleFigure getModuleFiguresByUniqueName(String uniqueName) {
		ModuleFigure moduleFigure = null;
		ModuleDTO module = defineService.getModule_ByUniqueName(uniqueName);	
		if (module != null) {
			moduleFigure = figureFactory.createModuleFigure(module);
		}
		return moduleFigure;
	}
	
	@Override
	protected ArrayList<ModuleFigure> getModuleFiguresInRoot() {
		ModuleDTO[] rootModules = defineService.getModule_AllRootModules();
		ArrayList<ModuleFigure> rootModuleFigures = new ArrayList<ModuleFigure>();
		for (AbstractDTO rootModule : rootModules) {
			ModuleFigure rootModuleFigure = figureFactory.createModuleFigure(rootModule);
			rootModuleFigures.add(rootModuleFigure);
		}
		return rootModuleFigures;
	}
	
	@Override
	protected ArrayList<ModuleFigure> getChildModuleFiguresOfParent(String parentName) {
		if (parentName.equals("")) {
			parentName = "**"; // Root within Define.
		}
		ModuleDTO[] children = defineService.getModule_TheChildrenOfTheModule(parentName);
		ArrayList<ModuleFigure> knownChildren = new ArrayList<ModuleFigure>();
		for (AbstractDTO child : children) {
			ModuleFigure moduleFigure = figureFactory.createModuleFigure(child);
			knownChildren.add(moduleFigure);
		}
		return knownChildren;
	}

	@Override
	protected RelationFigure getRelationFigureBetween(ModuleFigure figureFrom, ModuleFigure figureTo) {
		RuleDTO[] matchingRules = defineService.getRulesByLogicalPath(figureFrom.getUniqueName(), figureTo.getUniqueName());
		return figureFactory.createRelationFigure_Rule(matchingRules);
	}
	
	@Override
	protected DependencyDTO[] getDependenciesBetween(ModuleFigure figureFrom, ModuleFigure figureTo) {
		ArrayList<DependencyDTO> dependencies = new ArrayList<DependencyDTO>();
		if ((figureFrom != null) && (figureTo != null) && !figureFrom.getUniqueName().equals(figureTo.getUniqueName())){ 
			HashSet<String> physicalClassPathsFrom = defineService.getModule_AllPhysicalClassPathsOfModule(figureFrom.getUniqueName());
			HashSet<String> physicalClassPathsTo = defineService.getModule_AllPhysicalClassPathsOfModule(figureTo.getUniqueName());
			for (String physicalClassPathFrom : physicalClassPathsFrom){
				for (String physicalClassPathTo : physicalClassPathsTo) {
					DependencyDTO[] foundDependencies = analyseService.getDependenciesFromClassToClass(physicalClassPathFrom, physicalClassPathTo);
					for (DependencyDTO tempDependency : foundDependencies)
						dependencies.add(tempDependency);
				}
			}
		}
		return dependencies.toArray(new DependencyDTO[] {});
	}
	
	@Override
	protected RuleDTO[] getRulesBetween(BaseFigure figureFrom, BaseFigure figureTo) {
		if ((figureFrom != null) && (figureTo != null) ){ 
			RuleDTO[] returnValue = defineService.getRulesByLogicalPath(figureFrom.getUniqueName(), figureTo.getUniqueName());
			return returnValue;
		}
		 else {
			return new RuleDTO[]{};
		}
	}
	
//	@Override
//	protected RuleDTO[] getRulesBetween(ModuleFigure figureFrom, ModuleFigure figureTo) {
//		ArrayList<RuleDTO> rules = new ArrayList<RuleDTO>();
//		if ((figureFrom != null) && (figureTo != null) && !figureFrom.getUniqueName().equals(figureTo.getUniqueName())){ 
//			HashSet<String> physicalClassPathsFrom = defineService.getModule_AllPhysicalClassPathsOfModule(figureFrom.getUniqueName());
//			HashSet<String> physicalClassPathsTo = defineService.getModule_AllPhysicalClassPathsOfModule(figureTo.getUniqueName());
//			for (String physicalClassPathFrom : physicalClassPathsFrom){
//				for (String physicalClassPathTo : physicalClassPathsTo) {
//					RuleDTO[] foundRules = analyseService.getDependenciesFromClassToClass(physicalClassPathFrom, physicalClassPathTo);
//					for (RuleDTO tempDependency : foundRules)
//						rules.add(tempDependency);
//				}
//			}
//		}
//		return rules.toArray(new RuleDTO[] {});
//	}
	
	@Override
	protected boolean hasRelationBetween(ModuleFigure figureFrom, ModuleFigure figureTo){
		return defineService.getRulesByLogicalPath(figureFrom.getUniqueName(), figureTo.getUniqueName()).length > 0;	
	}

	@Override
	protected RelationFigure getRelationFigureWithViolationsBetween(ModuleFigure figureFrom, ModuleFigure figureTo) {
		RelationFigure violationFigure = null;
		if ((figureFrom != null) && (figureTo != null) && !figureFrom.getUniqueName().equals(figureTo.getUniqueName())){ 
			try {
				ViolationDTO[] violations = getViolationsBetween(figureFrom, figureTo);
				DependencyDTO[] dependencies = getDependenciesBetween(figureFrom, figureTo);
				if ((violations != null) && (dependencies != null)) {
					violationFigure = figureFactory.createRelationFigure_DependencyWithViolations(dependencies, violations);
				}
			} catch (Exception e) {
				logger.error("Could not create a RelationFigure with Violations between given figures. " + e.getMessage());
			}
		} 
		return violationFigure;
	}

	@Override
	protected ViolationDTO[] getViolationsBetween(ModuleFigure figureFrom, ModuleFigure figureTo) {
		if ((figureFrom != null) && (figureTo != null) && !figureFrom.getUniqueName().equals(figureTo.getUniqueName())){ 
			ViolationDTO[] returnValue = validateService.getViolationsByLogicalPath(figureFrom.getUniqueName(), figureTo.getUniqueName());
			return returnValue;
		}
		 else {
			return new ViolationDTO[]{};
		}
	}
	
	@Override
	protected String getUniqueNameOfParentModule(String childUniqueName) {
		String parentUniqueName = defineService.getModule_TheParentOfTheModule(childUniqueName);
		if (parentUniqueName.equals("**")) {
			parentUniqueName = "";
		}
		return parentUniqueName;
	}
	
	private void initializeServices() {
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		defineService = ServiceProvider.getInstance().getDefineService();
		validateService = ServiceProvider.getInstance().getValidateService();
	}

}
