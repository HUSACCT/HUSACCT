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

public class AnalysedController extends DrawingController {
	private final Logger logger = Logger.getLogger(AnalysedController.class);
	protected IAnalyseService analyseService;
	protected IValidateService validateService;

	private ArrayList<BaseFigure> analysedContextFigures;

	public AnalysedController() {
		super();
		this.initializeServices();
	}

	@Override
	public void drawArchitecture(DrawingDetail detail) {
		super.drawArchitecture(this.getCurrentDrawingDetail());
		super.notifyServiceListeners();
		AbstractDTO[] modules = this.analyseService.getRootModules();
		this.resetCurrentPaths();
		if (DrawingDetail.WITH_VIOLATIONS == detail) {
			this.showViolations();
		}
		this.drawModulesAndLines(modules);
	}

	private void getAndDrawModulesIn(String parentName) {
		AnalysedModuleDTO[] children = this.analyseService
				.getChildModulesInModule(parentName);
		if (parentName.equals("")) {
			this.drawArchitecture(this.getCurrentDrawingDetail());
		} else if (children.length > 0) {
			this.setCurrentPaths(new String[] { parentName });
			this.drawModulesAndLines(children);
		} else {
			this.logger.warn("Tried to draw modules for \"" + parentName
					+ "\", but it has no children.");
		}
	}

	private void getAndDrawModulesIn(String[] parentNames) {
		if (parentNames.length == 0) {
			this.drawArchitecture(this.getCurrentDrawingDetail());
		} else {
			HashMap<String, ArrayList<AbstractDTO>> allChildren = new HashMap<String, ArrayList<AbstractDTO>>();
			for (String parentName : parentNames) {
				AbstractDTO[] children = this.analyseService
						.getChildModulesInModule(parentName);
				if (parentName.equals("")) {
					this.drawArchitecture(this.getCurrentDrawingDetail());
					continue;
				} else if (children.length > 0) {
					ArrayList<AbstractDTO> knownChildren = new ArrayList<AbstractDTO>();
					for (AbstractDTO child : children) {
						knownChildren.add(child);
					}
					allChildren.put(parentName, knownChildren);
				} else {
					this.logger.warn("Tried to draw modules for \""
							+ parentName + "\", but it has no children.");
				}
			}
			if (this.analysedContextFigures.size() > 0) {
				ArrayList<AbstractDTO> tmp = new ArrayList<AbstractDTO>();
				for (BaseFigure figure : this.analysedContextFigures) {
					if (!figure.isLine() && !figure.isParent()) {
						AbstractDTO dto = this.getFigureMap().getModuleDTO(
								figure);
						if (null != dto) {
							tmp.add(dto);
						} else {
							this.logger.debug(figure.getName() + " -> "
									+ figure);
						}
					}
				}
				if (tmp.size() > 0) {
					allChildren.put("", tmp);
				}
			}
			this.setCurrentPaths(parentNames);

			Set<String> parentNamesKeySet = allChildren.keySet();
			if (parentNamesKeySet.size() == 1) {
				String onlyParentModule = parentNamesKeySet.iterator().next();
				ArrayList<AbstractDTO> onlyParentChildren = allChildren
						.get(onlyParentModule);
				this.drawModulesAndLines(onlyParentChildren
						.toArray(new AbstractDTO[] {}));
			} else {
				this.drawModulesAndLines(allChildren);
			}
		}
	}

	@Override
	protected DependencyDTO[] getDependenciesBetween(BaseFigure figureFrom,
			BaseFigure figureTo) {
		AnalysedModuleDTO dtoFrom = (AnalysedModuleDTO) this.getFigureMap()
				.getModuleDTO(figureFrom);
		AnalysedModuleDTO dtoTo = (AnalysedModuleDTO) this.getFigureMap()
				.getModuleDTO(figureTo);
		if (!figureFrom.equals(figureTo) && null != dtoFrom && null != dtoTo) {
			return this.analyseService.getDependencies(dtoFrom.uniqueName,
					dtoTo.uniqueName);
		}
		return new DependencyDTO[] {};
	}

	@Override
	protected ViolationDTO[] getViolationsBetween(BaseFigure figureFrom,
			BaseFigure figureTo) {
		AnalysedModuleDTO dtoFrom = (AnalysedModuleDTO) this.getFigureMap()
				.getModuleDTO(figureFrom);
		AnalysedModuleDTO dtoTo = (AnalysedModuleDTO) this.getFigureMap()
				.getModuleDTO(figureTo);
		return this.validateService.getViolationsByPhysicalPath(
				dtoFrom.uniqueName, dtoTo.uniqueName);
	}

	private void initializeServices() {
		this.analyseService = ServiceProvider.getInstance().getAnalyseService();
		this.analyseService.addServiceListener(new IServiceListener() {
			@Override
			public void update() {
				AnalysedController.this.refreshDrawing();
			}
		});
		this.validateService = ServiceProvider.getInstance()
				.getValidateService();
		this.validateService.addServiceListener(new IServiceListener() {
			@Override
			public void update() {
				if (AnalysedController.this.areViolationsShown()) {
					AnalysedController.this.refreshDrawing();
				}
			}
		});
	}

	@Override
	public void moduleOpen(String[] paths) {
		super.notifyServiceListeners();
		this.resetContextFigures();
		if (paths.length == 0) {
			this.drawArchitecture(this.getCurrentDrawingDetail());
		} else {
			this.getAndDrawModulesIn(paths);
		}
	}

	@Override
	public void moduleZoom(BaseFigure[] figures) {
		super.notifyServiceListeners();
		this.resetContextFigures();
		ArrayList<String> parentNames = this
				.sortFiguresBasedOnZoomability(figures);

		if (parentNames.size() > 0) {
			this.saveSingleLevelFigurePositions();
			this.getAndDrawModulesIn(parentNames.toArray(new String[] {}));
		}
	}

	@Override
	public void moduleZoomOut() {
		super.notifyServiceListeners();
		if (this.getCurrentPaths().length > 0) {
			this.saveSingleLevelFigurePositions();
			this.resetContextFigures();
			String firstCurrentPaths = this.getCurrentPaths()[0];
			AnalysedModuleDTO parentDTO = this.analyseService
					.getParentModuleForModule(firstCurrentPaths);
			if (null != parentDTO) {
				this.getAndDrawModulesIn(parentDTO.uniqueName);
			} else {
				this.logger
						.warn("Tried to zoom out from \""
								+ this.getCurrentPaths()
								+ "\", but it has no parent (could be root if it's an empty string).");
				this.logger.debug("Reverting to the root of the application.");
				this.drawArchitecture(this.getCurrentDrawingDetail());
			}
		} else {
			this.logger
					.warn("Tried to zoom out from \""
							+ this.getCurrentPaths()
							+ "\", but it has no parent (could be root if it's an empty string).");
			this.logger.debug("Reverting to the root of the application.");
			this.drawArchitecture(this.getCurrentDrawingDetail());
		}
	}

	@Override
	public void refreshDrawing() {
		super.notifyServiceListeners();
		this.getAndDrawModulesIn(this.getCurrentPaths());
	}

	private void resetContextFigures() {
		this.analysedContextFigures = new ArrayList<BaseFigure>();
	}

	@Override
	public void showViolations() {
		if (this.validateService.isValidated()) {
			super.showViolations();
		}
	}

	protected ArrayList<String> sortFiguresBasedOnZoomability(
			BaseFigure[] figures) {
		ArrayList<String> parentNames = new ArrayList<String>();
		for (BaseFigure figure : figures) {
			if (figure.isModule() && !figure.isContext()) {
				try {
					AnalysedModuleDTO parentDTO = (AnalysedModuleDTO) this
							.getFigureMap().getModuleDTO(figure);
					parentNames.add(parentDTO.uniqueName);
				} catch (Exception e) {
					e.printStackTrace();
					this.logger.warn("Could not zoom on this object: "
							+ figure.getName()
							+ ". Expected a different DTO type.");
				}
			} else if (!figure.isLine() || figure.isContext()) {
				this.analysedContextFigures.add(figure);
				this.logger.warn("Figure: " + figure.getName()
						+ " is accepted as context for multizoom.");
			} else {
				this.logger.warn("Could not zoom on this object: "
						+ figure.getName() + ". Not a module to zoom on.");
			}
		}

		return parentNames;
	}
}
