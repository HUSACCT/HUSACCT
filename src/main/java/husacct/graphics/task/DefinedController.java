package husacct.graphics.task;


import java.util.ArrayList;
import java.util.HashSet;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.AbstractDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.RuleDTO;
import husacct.common.dto.UmlLinkDTO;
import husacct.common.dto.ViolationDTO;
import husacct.define.IDefineService;
import husacct.graphics.domain.figures.BaseFigure;
import husacct.graphics.domain.figures.ModuleFigure;
import husacct.graphics.domain.figures.RelationFigure;
import husacct.validate.IValidateService;

public class DefinedController extends DrawingController {
	protected IAnalyseService			analyseService;
	protected IDefineService			defineService;
	protected IValidateService			validateService;
	
	
	public DefinedController() {
		super();
		initializeServices();
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
	protected HashSet<String> getChildenOfParent(String parentName) {
		if (parentName.equals("")) {
			parentName = "**"; // Root within Define.
		}
		ModuleDTO[] children = defineService.getModule_TheChildrenOfTheModule(parentName);
		HashSet<String> knownChildren = new HashSet<String>();
		for (ModuleDTO child : children) {
			knownChildren.add(child.logicalPath);
		}
		return knownChildren;
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
		RelationFigure dependencyFigure = null;
		if ((figureFrom != null) && (figureTo != null) && !figureFrom.getUniqueName().equals(figureTo.getUniqueName())){ 
			ArrayList<DependencyDTO> dependencies = new ArrayList<DependencyDTO>();
			HashSet<String> physicalClassPathsFrom = defineService.getModule_AllPhysicalClassPathsOfModule(figureFrom.getUniqueName());
			HashSet<String> physicalClassPathsTo = defineService.getModule_AllPhysicalClassPathsOfModule(figureTo.getUniqueName());
			for (String physicalClassPathFrom : physicalClassPathsFrom){
				for (String physicalClassPathTo : physicalClassPathsTo) {
					DependencyDTO[] foundDependencies = analyseService.getDependenciesFromClassToClass(physicalClassPathFrom, physicalClassPathTo);
					for (DependencyDTO tempDependency : foundDependencies)
						dependencies.add(tempDependency);
				}
			}
			try {
				if (dependencies.size() > 0) {
					dependencyFigure = figureFactory.createRelationFigure_Dependency(dependencies.toArray(new DependencyDTO[] {}));
				}
			} catch (Exception e) {
				logger.error(" Could not create a dependency figure." + e.getMessage());
			}
		}
		return dependencyFigure;
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
	protected UmlLinkDTO[] getUmlLinksBetween(ModuleFigure figureFrom, ModuleFigure figureTo){
		ArrayList<UmlLinkDTO> umlLinks = new ArrayList<UmlLinkDTO>();
		if ((figureFrom != null) && (figureTo != null) && !figureFrom.getUniqueName().equals(figureTo.getUniqueName())){ 
			HashSet<String> physicalClassPathsFrom = defineService.getModule_AllPhysicalClassPathsOfModule(figureFrom.getUniqueName());
			HashSet<String> physicalClassPathsTo = defineService.getModule_AllPhysicalClassPathsOfModule(figureTo.getUniqueName());
			for (String physicalClassPathFrom : physicalClassPathsFrom){
				for (String physicalClassPathTo : physicalClassPathsTo) {
					UmlLinkDTO[] foundDependencies = analyseService.getUmlLinksFromClassToToClass(physicalClassPathFrom, physicalClassPathTo);
					for (UmlLinkDTO tempDependency : foundDependencies)
						umlLinks.add(tempDependency);
				}
			}
		}
		return umlLinks.toArray(new UmlLinkDTO[] {});
	}
	
	@Override
	protected boolean hasRelationBetween(ModuleFigure figureFrom, ModuleFigure figureTo){
		boolean hasDependencies = false;
		if ((figureFrom != null) && (figureTo != null) && !figureFrom.getUniqueName().equals(figureTo.getUniqueName())){ 
			HashSet<String> physicalClassPathsFrom = defineService.getModule_AllPhysicalClassPathsOfModule(figureFrom.getUniqueName());
			HashSet<String> physicalClassPathsTo = defineService.getModule_AllPhysicalClassPathsOfModule(figureTo.getUniqueName());
			DependencyDTO[] foundDependencies;
			for (String physicalClassPathFrom : physicalClassPathsFrom){
				for (String physicalClassPathTo : physicalClassPathsTo) {
					foundDependencies = analyseService.getDependenciesFromClassToClass(physicalClassPathFrom, physicalClassPathTo);
					if (foundDependencies.length > 0) {
						return true;
					} else {
						foundDependencies = analyseService.getDependenciesFromClassToClass(physicalClassPathTo, physicalClassPathFrom);
						if (foundDependencies.length > 0) {
							return true;
						}
					}
				}
			}
		}
		return hasDependencies;		
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

	@Override
	protected RuleDTO[] getRulesBetween(BaseFigure figureFrom, BaseFigure figureTo) {
		return new RuleDTO[]{};
	}
	
}
