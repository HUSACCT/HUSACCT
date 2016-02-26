package husacct.graphics.task;


import husacct.ServiceProvider;
import husacct.analyse.serviceinterface.IAnalyseService;
import husacct.analyse.serviceinterface.dto.DependencyDTO;
import husacct.analyse.serviceinterface.dto.SoftwareUnitDTO;
import husacct.common.dto.AbstractDTO;
import husacct.common.dto.ViolationDTO;
import husacct.graphics.domain.figures.BaseFigure;
import husacct.graphics.domain.figures.ModuleFigure;
import husacct.validate.IValidateService;

import java.util.ArrayList;

public class AnalysedController extends DrawingController {
	protected IAnalyseService		analyseService;
	protected IValidateService		validateService;


	public AnalysedController() {
		super();
		initializeServices();
	}

	@Override
	protected ArrayList<ModuleFigure> getModuleFiguresInRoot() {
		SoftwareUnitDTO[] rootModules = analyseService.getSoftwareUnitsInRoot();
		ArrayList<ModuleFigure> rootModuleFigures = new ArrayList<ModuleFigure>();
		for (SoftwareUnitDTO rootModule : rootModules) {
			if (rootModule.name.toLowerCase().equals("xlibraries")){
				rootModule.type = "library";
			}
			ModuleFigure rootModuleFigure = figureFactory.createModuleFigure(rootModule);
			rootModuleFigures.add(rootModuleFigure);
		}
		return rootModuleFigures;
	}
	
	@Override
	protected ArrayList<ModuleFigure> getChildModuleFiguresOfParent(String parentName) {
		SoftwareUnitDTO[] children = analyseService.getChildUnitsOfSoftwareUnit(parentName);
		ArrayList<ModuleFigure> knownChildren = new ArrayList<ModuleFigure>();
		for (AbstractDTO child : children) {
			ModuleFigure moduleFigure = figureFactory.createModuleFigure(child);
			knownChildren.add(moduleFigure);
		}
		return knownChildren;
	}

	@Override
	protected DependencyDTO[] getDependenciesBetween(BaseFigure figureFrom, BaseFigure figureTo) {
		if ((figureFrom != null) && (figureTo != null) && !figureFrom.getUniqueName().equals(figureTo.getUniqueName())){ 
			return analyseService.getDependenciesFromSoftwareUnitToSoftwareUnit(figureFrom.getUniqueName(), figureTo.getUniqueName());
		}
		return new DependencyDTO[] {};
	}
	
	@Override
	protected boolean hasDependencyBetween(BaseFigure figureFrom, BaseFigure figureTo){
		boolean b = false;
		if ((figureFrom != null) && (figureTo != null) && !figureFrom.getUniqueName().equals(figureTo.getUniqueName())){ 
			if((analyseService.getDependenciesFromSoftwareUnitToSoftwareUnit(figureFrom.getUniqueName(), figureTo.getUniqueName()).length > 0) || 
					(analyseService.getDependenciesFromSoftwareUnitToSoftwareUnit(figureTo.getUniqueName(), figureFrom.getUniqueName()).length > 0)){
				b = true;
			}
		}
		return b;		
	}

	@Override
	protected ViolationDTO[] getViolationsBetween(BaseFigure figureFrom, BaseFigure figureTo) {
		if ((figureFrom != null) && (figureTo != null) && !figureFrom.getUniqueName().equals(figureTo.getUniqueName())){ 
			return validateService.getViolationsByPhysicalPath(figureFrom.getUniqueName(), figureTo.getUniqueName());
		} else {
			return new ViolationDTO[]{};
		}
	}

	@Override
	protected String getUniqueNameOfParent(String childUniqueName) {
		String parentUniqueName = "";
		SoftwareUnitDTO parentDTO = analyseService.getParentUnitOfSoftwareUnit(childUniqueName);
		if (parentDTO != null) { 
			parentUniqueName = parentDTO.uniqueName;
		}
		return parentUniqueName;
	}
	
	private void initializeServices() {
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		validateService = ServiceProvider.getInstance().getValidateService();
	}

}
