package husacct.graphics.presentation;

import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.presentation.menubars.ContextMenu;
import husacct.graphics.util.UserInputListener;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JViewport;

import org.jhotdraw.draw.DefaultDrawingEditor;
import org.jhotdraw.draw.DefaultDrawingView;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.event.FigureSelectionEvent;
import org.jhotdraw.draw.event.FigureSelectionListener;
import org.jhotdraw.draw.tool.AbstractTool;
import org.jhotdraw.draw.tool.SelectionTool;

public class DrawingView extends DefaultDrawingView {
	
	private static final long serialVersionUID = 7276696509798039409L;
	private static final int LeftMouseButton = MouseEvent.BUTTON1;
	private static final int RightMouseButton = MouseEvent.BUTTON3;
	private static final int DoubleClick = 2;
	private static final int ScrollSpeed = 10;
	
	private final Drawing drawing;
	private final DefaultDrawingEditor editor;
	private final ContextMenu contextMenu;
	protected AbstractTool panTool;
	protected AbstractTool selectTool;
	private boolean isCtrlPressed = false;
	
	private final ArrayList<UserInputListener> listeners = new ArrayList<UserInputListener>();
	private final HashSet<Figure> previousSelection = new HashSet<Figure>();
	
	public DrawingView(Drawing givenDrawing) {
		drawing = givenDrawing;
		setDrawing(drawing);
		
		editor = new DefaultDrawingEditor();
		editor.add(this);
		
		contextMenu = new ContextMenu();
		
		initializeSelectionTool();
		editor.setTool(selectTool);
		
		initializeKeyListener();
		initializeMouseListener();
		initializeSelectionListener();
	}
	
	public void addListener(UserInputListener listener) {
		listeners.add(listener);
		contextMenu.addListener(listener);
	}
	
	public void cannotZoomOut() {
		contextMenu.setCanZoomout(false);
	}
	
	public void canZoomOut() {
		contextMenu.setCanZoomout(true);
	}
	
	public void drawingZoomChanged(double zoomFactor) {
		for (UserInputListener listener : listeners) {
			listener.setZoomSlider(zoomFactor);
			listener.drawingZoomChanged(zoomFactor);
		}
	}
	
	private void figureDeselected(BaseFigure[] figures) {
		for (UserInputListener l : listeners)
			l.figureDeselected(figures);
	}
	
	private void figureSelected(BaseFigure[] figures) {
		for (UserInputListener l : listeners)
			l.figureSelected(figures);
	}
	
	private Set<Figure> getDeltaSelection() {
		HashSet<Figure> deltaSelection = new HashSet<Figure>();
		Set<Figure> selection = getSelectedFigures();
		
		for (Figure f : previousSelection)
			if (!selection.contains(f))
				deltaSelection.add(f);
		return Collections.unmodifiableSet(deltaSelection);
	}
	
	private void handleDeselect() {
		Set<Figure> deselectedFigures = getDeltaSelection();
		if (deselectedFigures.size() > 0) {
			BaseFigure[] deselection = new BaseFigure[deselectedFigures.size()];
			deselection = deselectedFigures.toArray(deselection);
			
			figureDeselected(deselection);
			for (BaseFigure figure : deselection)
				figure.resetLayer();
		}
	}
	
	private boolean hasSelection() {
		return getSelectedFigures().size() > 0;
	}
	
	public void hideSelectedFigures() {
		Set<Figure> selection = getSelectedFigures();
		drawing.hideSelectedFigures(selection);
		clearSelection();
		contextMenu.setHasHiddenFigures(drawing.hasHiddenFigures());
	}
	
	private void initializeKeyListener() {
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 17)
					isCtrlPressed = true;
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				isCtrlPressed = false;
			}
		});
	}
	
	private void initializeMouseListener() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				DrawingView.this.onMouseClicked(e);
			}
		});
		
		addMouseWheelListener(new MouseWheelListener() {
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				DrawingView.this.onMouseScrolled(e);
			}
		});
	}
	
	protected void initializePanTool(JViewport viewport, JComponent comp) {
		panTool = new PanTool(viewport, comp);
	}
	
	private void initializeSelectionListener() {
		addFigureSelectionListener(new FigureSelectionListener() {
			@Override
			public void selectionChanged(FigureSelectionEvent evt) {
				DrawingView.this.onSelectionChanged(evt);
			}
		});
	}
	
	private void initializeSelectionTool() {
		selectTool = new SelectionTool();
	}
	
	private void moduleZoom(BaseFigure[] fig) {
		for (UserInputListener listener : listeners)
			listener.moduleZoom(fig);
		this.requestFocus();
	}
	
	private void onMouseClicked(MouseEvent e) {
		int mouseButton = e.getButton();
		int mouseClicks = e.getClickCount();
		
		handleDeselect();
		if (mouseButton == LeftMouseButton && hasSelection()) {
			BaseFigure[] selection = toFigureArray(getSelectedFigures());
			
			if (mouseClicks == DoubleClick)
				moduleZoom(selection);
			else
				for (BaseFigure figure : selection)
					figure.raiseLayer();
		} else if (mouseButton == RightMouseButton)
			contextMenu.show(this, e.getX(), e.getY());
		
		previousSelection.clear();
		previousSelection.addAll(getSelectedFigures());
	}
	
	private void onMouseScrolled(MouseWheelEvent e) {
		if (isCtrlPressed) {
			requestFocus();
			double wheelRotation = e.getWheelRotation() * -1;
			double wheelRotationFactor = wheelRotation / ScrollSpeed;
			double scaleFactor = getScaleFactor() + wheelRotationFactor;
			drawingZoomChanged(scaleFactor);
		}
	}
	
	protected void onSelectionChanged(FigureSelectionEvent evt) {
		handleDeselect();
		if (hasSelection()) {
			BaseFigure[] selection = toFigureArray(getSelectedFigures());
			for (BaseFigure selectedFig : selection)
				drawing.bringToFront(selectedFig);
			figureSelected(selection);
		}
		contextMenu.setHasSelection(hasSelection());
	}
	
	public void removeListener(UserInputListener listener) {
		listeners.remove(listener);
		contextMenu.removeListener(listener);
	}
	
	public void restoreHiddenFigures() {
		drawing.restoreHiddenFigures();
		contextMenu.setHasHiddenFigures(false);
	}
	
	public void setHasHiddenFigures(boolean setting) {
		contextMenu.setHasHiddenFigures(setting);
		contextMenu.setHasSelection(false);
	}
	
	public BaseFigure[] toFigureArray(Collection<Figure> collection) {
		return collection.toArray(new BaseFigure[collection.size()]);
	}
}
