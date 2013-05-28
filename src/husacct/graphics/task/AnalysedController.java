package husacct.graphics.task;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.AbstractDTO;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ViolationDTO;
import husacct.common.services.IServiceListener;
import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.util.DrawingDetail;
import husacct.validate.IValidateService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jhotdraw.draw.Figure;

public class AnalysedController extends DrawingController {
	private final Logger logger = Logger.getLogger(AnalysedController.class);
	protected IAnalyseService analyseService;
	protected IValidateService validateService;

	private ArrayList<BaseFigure> analysedContextFigures;

	public AnalysedController() {
		super();
		initializeServices();
	}

	@Override
	public void drawArchitecture(DrawingDetail detail) {
		super.drawArchitecture(getCurrentDrawingDetail());
		super.notifyServiceListeners();
		AbstractDTO[] modules = analyseService.getRootModules();
		resetCurrentPaths();
		if (DrawingDetail.WITH_VIOLATIONS == detail)
			showViolations();
		this.drawModulesAndLines(modules);
	}

	private void getAndDrawModulesIn(String parentName) {
		AnalysedModuleDTO[] children = analyseService
				.getChildModulesInModule(parentName);
		if (parentName.equals(""))
			drawArchitecture(getCurrentDrawingDetail());
		else if (children.length > 0) {
			setCurrentPaths(new String[] { parentName });
			this.drawModulesAndLines(children);
		} else
			logger.warn("Tried to draw modules for \"" + parentName
					+ "\", but it has no children.");
	}

	private void getAndDrawModulesIn(String[] parentNames) {
		if (parentNames.length == 0)
			drawArchitecture(getCurrentDrawingDetail());
		else {
			HashMap<String, ArrayList<AbstractDTO>> allChildren = new HashMap<String, ArrayList<AbstractDTO>>();
			ArrayList<String> compoundedNames = new ArrayList<String>();

			for (String parentName : parentNames) {
				compoundedNames.add(parentName);
				ArrayList<AbstractDTO> knownChildren = getChildrenOf(parentName);

				if (knownChildren.size() > 0)
					allChildren.put(parentName, knownChildren);
			}

			if (analysedContextFigures.size() > 0) {
				ArrayList<AbstractDTO> tmp = new ArrayList<AbstractDTO>();
				for (BaseFigure figure : analysedContextFigures)
					if (!figure.isLine() && !figure.isParent()) {
						AbstractDTO dto = getFigureMap().getModuleDTO(figure);
						if (null != dto)
							tmp.add(dto);
						else
							logger.debug(figure.getName() + " -> " + figure);
					} else if (!figure.isLine() && !figure.isModule()) {
						// NOTE: Pretty sure selected stuff that is both not a
						// module and not a line
						// is actually a ParentFigure (blue square thing)
						ArrayList<AbstractDTO> knownChildren = getChildrenOf(figure
								.getName());
						if (knownChildren.size() > 0)
							allChildren.put(figure.getName(), knownChildren);
					}
				if (tmp.size() > 0)
					allChildren.put("", tmp);
			}
			setCurrentPaths(parentNames);

			Set<String> parentNamesKeySet = allChildren.keySet();
			if (parentNamesKeySet.size() == 1) {
				String onlyParentModule = parentNamesKeySet.iterator().next();
				ArrayList<AbstractDTO> onlyParentChildren = allChildren
						.get(onlyParentModule);
				this.drawModulesAndLines(onlyParentChildren
						.toArray(new AbstractDTO[] {}));
			} else
				this.drawModulesAndLines(allChildren);
		}
	}

	private ArrayList<AbstractDTO> getChildrenOf(String parentName) {
		AbstractDTO[] children = analyseService
				.getChildModulesInModule(parentName);

		ArrayList<AbstractDTO> knownChildren = new ArrayList<AbstractDTO>();

		if (parentName.equals(""))
			drawArchitecture(getCurrentDrawingDetail());
		else if (children.length > 0) {
			knownChildren = new ArrayList<AbstractDTO>();
			for (AbstractDTO child : children)
				knownChildren.add(child);
		} else
			logger.warn("Tried to draw modules for \"" + parentName
					+ "\", but it has no children.");

		return knownChildren;
	}

	@Override
	protected DependencyDTO[] getDependenciesBetween(BaseFigure figureFrom,
			BaseFigure figureTo) {
		AnalysedModuleDTO dtoFrom = (AnalysedModuleDTO) getFigureMap()
				.getModuleDTO(figureFrom);
		AnalysedModuleDTO dtoTo = (AnalysedModuleDTO) getFigureMap()
				.getModuleDTO(figureTo);
		if (!figureFrom.equals(figureTo) && null != dtoFrom && null != dtoTo)
			return analyseService.getDependencies(dtoFrom.uniqueName,
					dtoTo.uniqueName);
		return new DependencyDTO[] {};
	}

	@Override
	protected ViolationDTO[] getViolationsBetween(BaseFigure figureFrom,
			BaseFigure figureTo) {
		AnalysedModuleDTO dtoFrom = (AnalysedModuleDTO) getFigureMap()
				.getModuleDTO(figureFrom);
		AnalysedModuleDTO dtoTo = (AnalysedModuleDTO) getFigureMap()
				.getModuleDTO(figureTo);
		return validateService.getViolationsByPhysicalPath(dtoFrom.uniqueName,
				dtoTo.uniqueName);
	}

	private void initializeServices() {
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		analyseService.addServiceListener(new IServiceListener() {
			@Override
			public void update() {
				AnalysedController.this.refreshDrawing();
			}
		});
		validateService = ServiceProvider.getInstance().getValidateService();
		validateService.addServiceListener(new IServiceListener() {
			@Override
			public void update() {
				if (AnalysedController.this.areViolationsShown())
					AnalysedController.this.refreshDrawing();
			}
		});
	}

	@Override
	public void moduleOpen(String[] paths) {
		super.notifyServiceListeners();
		saveSingleLevelFigurePositions();
		resetContextFigures();
		if (paths.length == 0)
			drawArchitecture(getCurrentDrawingDetail());
		else
			this.getAndDrawModulesIn(paths);
	}

	@Override
	public void moduleZoom(String zoomType){

		BaseFigure[] selection = super.getSelectedFigures();

		super.notifyServiceListeners();
		resetContextFigures();
		ArrayList<String> parentNames = sortFiguresBasedOnZoomability(selection);

		if (parentNames.size() > 0) {
			saveSingleLevelFigurePositions();
			this.getAndDrawModulesIn(parentNames.toArray(new String[] {}));
		}
	}

	@Override
	public void moduleZoom(BaseFigure[] figures) {
		super.notifyServiceListeners();
		resetContextFigures();
		ArrayList<String> parentNames = sortFiguresBasedOnZoomability(figures);

		if (parentNames.size() > 0) {
			saveSingleLevelFigurePositions();
			this.getAndDrawModulesIn(parentNames.toArray(new String[] {}));
		}
	}

	@Override
	public void moduleZoomOut() {
		super.notifyServiceListeners();
		if (getCurrentPaths().length > 0) {
			saveSingleLevelFigurePositions();
			resetContextFigures();
			String firstCurrentPaths = getCurrentPaths()[0];
			AnalysedModuleDTO parentDTO = analyseService
					.getParentModuleForModule(firstCurrentPaths);

			if (parentDTO != null)
				this.getAndDrawModulesIn(parentDTO.uniqueName);
			else
				zoomOutFailed();
		} else
			zoomOutFailed();
	}

	@Override
	public void refreshDrawing() {
		super.notifyServiceListeners();
		this.getAndDrawModulesIn(getCurrentPaths());
	}

	private void resetContextFigures() {
		analysedContextFigures = new ArrayList<BaseFigure>();
	}

	@Override
	public void showViolations() {
		if (validateService.isValidated())
			super.showViolations();
	}

	protected ArrayList<String> sortFiguresBasedOnZoomability(
			BaseFigure[] figures) {
		ArrayList<String> parentNames = new ArrayList<String>();
		for (BaseFigure figure : figures)
			if (figure.isModule() && !figure.isContext())
				try {
					AnalysedModuleDTO parentDTO = (AnalysedModuleDTO) getFigureMap()
							.getModuleDTO(figure);
					parentNames.add(parentDTO.uniqueName);
				} catch (Exception e) {
					e.printStackTrace();
					logger.warn("Could not zoom on this object: "
							+ figure.getName()
							+ ". Expected a different DTO type.");
				}
			else if (!figure.isLine() || figure.isContext()) {
				analysedContextFigures.add(figure);
				logger.warn("Figure: " + figure.getName()
						+ " is accepted as context for multizoom.");
			} else
				logger.warn("Could not zoom on this object: "
						+ figure.getName() + ". Not a module to zoom on.");

		return parentNames;
	}

	public void zoomOutFailed() {
		logger.warn("Tried to zoom out from \""
				+ getCurrentPaths()
				+ "\", but it has no parent (could be root if it's an empty string).");
		logger.debug("Reverting to the root of the application.");
		drawArchitecture(getCurrentDrawingDetail());
	}


}
