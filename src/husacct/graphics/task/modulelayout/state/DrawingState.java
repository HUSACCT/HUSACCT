package husacct.graphics.task.modulelayout.state;

import husacct.graphics.domain.Drawing;
import husacct.graphics.domain.figures.BaseFigure;
import husacct.graphics.domain.figures.ModuleFigure;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.jhotdraw.draw.ConnectionFigure;
import org.jhotdraw.draw.Figure;

public class DrawingState {
	private Drawing							drawing;
	private HashMap<String, FigureState>	savedPositions;
	private boolean							hasHiddenFigures	= false;
	
	public DrawingState(Drawing theDrawing) {
		drawing = theDrawing;
		savedPositions = new HashMap<String, FigureState>();
	}
	
	public void clear() {
		savedPositions.clear();
		hasHiddenFigures = false;
	}
	
	private String getFullPath(BaseFigure bf) {
		String uniqueName = "";
		if (bf instanceof ModuleFigure) {
			return bf.getUniqueName();
		} else {
			return uniqueName;
		}
	}
	
	public boolean hasHiddenFigures() {
		return hasHiddenFigures;
	}
	
	public void restore() {
		restoreFigures();
		restoreLineStates();
	}
	
	private void restoreFigures() {
		Set<Entry<String, FigureState>> entries = savedPositions.entrySet();
		for (Entry<String, FigureState> e : entries) {
			FigureState savedState = e.getValue();
/*			To do: invent new working mechanism. 
			if (figureMap.containsModule(savedState.path)) {
				BaseFigure bf = figureMap.findModuleByPath(savedState.path);
				Rectangle2D.Double bounds = savedState.position;
				
				Point2D.Double anchor = new Point2D.Double(bounds.x, bounds.y);
				Point2D.Double lead = new Point2D.Double(bounds.x
						+ bounds.width, bounds.y + bounds.height);
				
				bf.willChange();
				bf.setBounds(anchor, lead);
				bf.changed();
				if (!savedState.enabled) bf.setEnabled(false);
			}
*/		}
	}
	
	private void restoreLineStates() {
		for (Figure f : drawing.getChildren()) {
			BaseFigure bf = (BaseFigure) f;
			if (bf.isLine()) {
				ConnectionFigure cf = (ConnectionFigure) f;
				Figure start = cf.getStartFigure();
				Figure end = cf.getEndFigure();
				
				if (!start.isVisible() || !end.isVisible()) bf
						.setEnabled(false);
			}
		}
	}
	
	public void save() {
		clear();
		List<Figure> figures = drawing.getChildren();
		
		for (Figure f : figures) {
			BaseFigure bf = (BaseFigure) f;
			if (bf.isModule()) {
				FigureState state = saveFigureState(bf);
				savedPositions.put(state.path, state);
				if (!state.enabled) hasHiddenFigures = true;
			}
		}
	}
	
	private FigureState saveFigureState(BaseFigure bf) {
		FigureState output = new FigureState();
		output.path = getFullPath(bf);
		output.position = bf.getBounds();
		output.enabled = bf.isEnabled();
		
		return output;
	}
	
}
