package husacct.graphics.presentation;

import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.presentation.menubars.ContextMenu;
import husacct.graphics.util.UserInputListener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.jhotdraw.draw.DefaultDrawingEditor;
import org.jhotdraw.draw.DefaultDrawingView;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.event.FigureSelectionEvent;
import org.jhotdraw.draw.event.FigureSelectionListener;
import org.jhotdraw.draw.tool.SelectionTool;

public class DrawingView extends DefaultDrawingView {

	private static final long serialVersionUID = 7276696509798039409L;
	private static final int LeftMouseButton = MouseEvent.BUTTON1;
	private static final int RightMouseButton = MouseEvent.BUTTON3;
	private static final int DoubleClick = 2;

	private Drawing drawing;
	private DefaultDrawingEditor editor;
	private ContextMenu contextMenu;
	private SelectionTool selectionTool;

	private ArrayList<UserInputListener> listeners = new ArrayList<UserInputListener>();
	private HashSet<Figure> previousSelection = new HashSet<Figure>();

	public DrawingView(Drawing givenDrawing) {
		drawing = givenDrawing;
		setDrawing(drawing);

		editor = new DefaultDrawingEditor();
		editor.add(this);
		
		contextMenu = new ContextMenu();

		initializeSelectionTool();
		initializeMouseListener();
		initializeSelectionListener();
	}

	private void initializeSelectionTool() {
		selectionTool = new SelectionTool();
		editor.setTool(selectionTool);
	}

	private void initializeMouseListener() {
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				onMouseClicked(e);
			}
		});
	}
	
	public void canZoomOut(){
		contextMenu.setCanZoomout(true);
	}
	
	public void cannotZoomOut(){
		contextMenu.setCanZoomout(false);
	}
	
	public void setHasHiddenFigures(boolean setting){
		contextMenu.setHasHiddenFigures(setting);
		contextMenu.setHasSelection(false);
	}
	
	private void onMouseClicked(MouseEvent e) {
		int mouseButton = e.getButton();
		int mouseClicks = e.getClickCount();

		handleDeselect();
		if (mouseButton == LeftMouseButton && hasSelection()) {
			BaseFigure[] selection = toFigureArray(getSelectedFigures());

			if (mouseClicks == DoubleClick) {
				moduleZoom(selection);
			} else {
				for (BaseFigure figure : selection) {
					figure.raiseLayer();
				}
			}
		} else if (mouseButton == RightMouseButton) {
			contextMenu.show(this, e.getX(), e.getY());
		}

		previousSelection.clear();
		previousSelection.addAll(getSelectedFigures());
	}

	private void handleDeselect() {
		Set<Figure> deselectedFigures = getDeltaSelection();
		if (deselectedFigures.size() > 0) {
			BaseFigure[] deselection = new BaseFigure[deselectedFigures.size()];
			deselection = deselectedFigures.toArray(deselection);

			figureDeselected(deselection);
			for (BaseFigure figure : deselection) {
				figure.resetLayer();
			}
		}
	}

	private boolean hasSelection() {
		return getSelectedFigures().size() > 0;
	}

	private BaseFigure[] toFigureArray(Collection<Figure> collection) {
		BaseFigure[] retVal = new BaseFigure[collection.size()];
		retVal = (BaseFigure[]) collection.toArray(retVal);
		return retVal;
	}

	private Set<Figure> getDeltaSelection() {
		HashSet<Figure> deltaSelection = new HashSet<Figure>();
		Set<Figure> selection = getSelectedFigures();

		for (Figure f : previousSelection) {
			if (!selection.contains(f)) {
				deltaSelection.add(f);
			}
		}
		return Collections.unmodifiableSet(deltaSelection);
	}

	private void figureSelected(BaseFigure[] figures) {
		for (UserInputListener l : listeners) {
			l.figureSelected(figures);
		}
	}

	private void figureDeselected(BaseFigure[] figures) {
		for (UserInputListener l : listeners) {
			l.figureDeselected(figures);
		}
	}

	private void moduleZoom(BaseFigure[] fig) {
		for (UserInputListener l : listeners) {
			l.moduleZoom(fig);
		}
		requestFocus();
	}
	
	public void hideSelectedFigures() {
		Set<Figure> selection = getSelectedFigures();
		drawing.hideSelectedFigures(selection);
		clearSelection();
		contextMenu.setHasHiddenFigures(drawing.hasHiddenFigures());
	}
	
	public void restoreHiddenFigures(){
		drawing.restoreHiddenFigures();
		contextMenu.setHasHiddenFigures(false);
	}

	private void initializeSelectionListener() {
		addFigureSelectionListener(new FigureSelectionListener() {
			@Override
			public void selectionChanged(FigureSelectionEvent evt) {
				onSelectionChanged(evt);
			}
		});
	}

	protected void onSelectionChanged(FigureSelectionEvent evt) {
		handleDeselect();
		if (hasSelection()) {
			BaseFigure[] selection = toFigureArray(getSelectedFigures());
			for (BaseFigure selectedFig : selection) {
				drawing.bringToFront(selectedFig);
			}
			figureSelected(selection);
		}
		contextMenu.setHasSelection(hasSelection());
	}

	public void addListener(UserInputListener listener) {
		listeners.add(listener);
		contextMenu.addListener(listener);
	}

	public void removeListener(UserInputListener listener) {
		listeners.remove(listener);
		contextMenu.removeListener(listener);
	}
}
