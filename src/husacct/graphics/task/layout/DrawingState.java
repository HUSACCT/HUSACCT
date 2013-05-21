package husacct.graphics.task.layout;

import husacct.common.dto.AbstractDTO;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.ModuleDTO;
import husacct.graphics.presentation.Drawing;
import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.util.FigureMap;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.jhotdraw.draw.ConnectionFigure;
import org.jhotdraw.draw.Figure;

public class DrawingState {
	private Drawing drawing;
	private HashMap<String, FigureState> savedPositions;
	private FigureMap figureMap = null;
	private boolean hasHiddenFigures = false;

	public DrawingState(Drawing theDrawing) {
		drawing = theDrawing;
		savedPositions = new HashMap<String, FigureState>();
	}

	public void clear() {
		savedPositions.clear();
		hasHiddenFigures = false;
	}

	private String getFullPath(BaseFigure bf) {
		AbstractDTO dto = figureMap.getModuleDTO(bf);

		if (dto instanceof ModuleDTO) {
			ModuleDTO moduleDto = (ModuleDTO) dto;
			return moduleDto.logicalPath;
		} else {
			AnalysedModuleDTO analysedDto = (AnalysedModuleDTO) dto;
			return analysedDto.uniqueName;
		}
	}

	public boolean hasHiddenFigures() {
		return hasHiddenFigures;
	}

	public void restore(FigureMap figureMap) {
		this.figureMap = figureMap;

		restoreFigures();
		restoreLineStates();
	}

	private void restoreFigures() {
		Set<Entry<String, FigureState>> entries = savedPositions.entrySet();
		for (Entry<String, FigureState> e : entries) {
			FigureState savedState = e.getValue();

			if (figureMap.containsModule(savedState.path)) {
				BaseFigure bf = figureMap.findModuleByPath(savedState.path);
				Rectangle2D.Double bounds = savedState.position;

				Point2D.Double anchor = new Point2D.Double(bounds.x, bounds.y);
				Point2D.Double lead = new Point2D.Double(bounds.x
						+ bounds.width, bounds.y + bounds.height);

				bf.willChange();
				bf.setBounds(anchor, lead);
				bf.changed();
				if (!savedState.enabled)
					bf.setEnabled(false);
			}
		}
	}

	private void restoreLineStates() {
		for (Figure f : drawing.getChildren()) {
			BaseFigure bf = (BaseFigure) f;
			if (bf.isLine()) {
				ConnectionFigure cf = (ConnectionFigure) f;
				Figure start = cf.getStartFigure();
				Figure end = cf.getEndFigure();

				if (!start.isVisible() || !end.isVisible())
					bf.setEnabled(false);
			}
		}
	}

	public void save(FigureMap figureMap) {
		this.figureMap = figureMap;
		clear();
		List<Figure> figures = drawing.getChildren();

		for (Figure f : figures) {
			BaseFigure bf = (BaseFigure) f;
			if (!bf.isLine() && shouldSaveState(bf)) {
				FigureState state = saveFigureState(bf);
				savedPositions.put(state.path, state);
				if (!state.enabled)
					hasHiddenFigures = true;
			}
		}
	}

	private FigureState saveFigureState(BaseFigure bf) {
		FigureState retVal = new FigureState();
		retVal.path = getFullPath(bf);
		retVal.position = bf.getBounds();
		retVal.enabled = bf.isEnabled();

		return retVal;
	}

	private boolean shouldSaveState(BaseFigure bf) {
		return figureMap.getModuleDTO(bf) != null;
	}
}
