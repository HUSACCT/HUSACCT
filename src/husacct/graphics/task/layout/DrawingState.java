package husacct.graphics.task.layout;

import husacct.common.dto.AbstractDTO;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.ModuleDTO;
import husacct.graphics.presentation.Drawing;
import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.task.FigureMap;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;

import org.jhotdraw.draw.Figure;

public class DrawingState {
	private Drawing drawing;
	private HashMap<String, Rectangle2D.Double> savedPositions;
	private FigureMap figureMap = null;

	private boolean debugPrint = false;
	private String nameFilter = "Main";

	public DrawingState(Drawing theDrawing) {
		drawing = theDrawing;
		savedPositions = new HashMap<String, Rectangle2D.Double>();
	}

	public void clear() {
		savedPositions.clear();
	}

	public void save(FigureMap figureMap) {
		this.figureMap = figureMap;
		clear();

		List<Figure> figures = drawing.getChildren();
		for (Figure f : figures) {
			BaseFigure bf = (BaseFigure) f;

			if (!bf.isLine()) {
				try{
					String fullPath = getFullPath(bf);
					Rectangle2D.Double bounds = bf.getBounds();
					savedPositions.put(fullPath, bounds);
	
					printFigure(bf, bounds);
				}catch(NullPointerException e){
					// Figure not found in drawing, because of mulitzoom
				}
			}
		}
	}

	private String getFullPath(BaseFigure bf) throws NullPointerException {
		AbstractDTO dto = figureMap.getModuleDTO(bf);

		if (dto instanceof ModuleDTO) {
			ModuleDTO moduleDto = (ModuleDTO) dto;
			return moduleDto.logicalPath;
		} else {
			AnalysedModuleDTO analysedDto = (AnalysedModuleDTO) dto;
			return analysedDto.uniqueName;
		}
	}

	private void printFigure(BaseFigure bf, Rectangle2D.Double bounds) {
		if (debugPrint) {
			String rect = String.format(Locale.US, "[x=%1.2f,y=%1.2f,w=%1.2f,h=%1.2f]", bounds.x, bounds.y,
					bounds.width, bounds.height);

			if (nameFilter.isEmpty() || (!nameFilter.isEmpty() && nameFilter.equals(bf.getName())))
				System.out.println(String.format("%s: %s", bf.getName(), rect));
		}
	}

	public void restore(FigureMap figureMap) {
		this.figureMap = figureMap;

		Set<Entry<String, Rectangle2D.Double>> entries = savedPositions.entrySet();
		for (Entry<String, Rectangle2D.Double> e : entries) {
			String name = e.getKey();
			Rectangle2D.Double bounds = e.getValue();

			if (figureMap.containsModule(name)) {
				BaseFigure bf = figureMap.findModuleByPath(name);
				printFigure(bf, bounds);

				Point2D.Double anchor = new Point2D.Double(bounds.x, bounds.y);
				Point2D.Double lead = new Point2D.Double(bounds.x + bounds.width, bounds.y + bounds.height);

				bf.willChange();
				bf.setBounds(anchor, lead);
				bf.changed();
			}
		}
	}
}
