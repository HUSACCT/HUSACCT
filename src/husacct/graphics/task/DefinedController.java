package husacct.graphics.task;


import husacct.ServiceProvider;
import husacct.analyse.serviceinterface.IAnalyseService;
import husacct.analyse.serviceinterface.dto.DependencyDTO;
import husacct.common.dto.AbstractDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ViolationDTO;
import husacct.define.IDefineService;
import husacct.graphics.domain.figures.BaseFigure;
import husacct.graphics.domain.figures.ModuleFigure;
import husacct.validate.IValidateService;

import java.util.ArrayList;
import java.util.HashSet;

public class DefinedController extends DrawingController {
	protected IAnalyseService			analyseService;
	protected IDefineService			defineService;
	protected IValidateService			validateService;
	
	
	public DefinedController() {
		super();
		initializeServices();
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
	protected DependencyDTO[] getDependenciesBetween(BaseFigure figureFrom, BaseFigure figureTo) {
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
	protected boolean hasDependencyBetween(BaseFigure figureFrom, BaseFigure figureTo){
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
	protected ViolationDTO[] getViolationsBetween(BaseFigure figureFrom, BaseFigure figureTo) {
		if ((figureFrom != null) && (figureTo != null) && !figureFrom.getUniqueName().equals(figureTo.getUniqueName())){ 
			ViolationDTO[] returnValue = validateService.getViolationsByLogicalPath(figureFrom.getUniqueName(), figureTo.getUniqueName());
			return returnValue;
		}
		 else {
			return new ViolationDTO[]{};
		}
	}
	
	@Override
	protected String getUniqueNameOfParent(String childUniqueName) {
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
