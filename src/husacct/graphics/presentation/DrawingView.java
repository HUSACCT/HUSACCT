package husacct.graphics.presentation;

import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.task.MouseClickListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.jhotdraw.draw.DefaultDrawingEditor;
import org.jhotdraw.draw.DefaultDrawingView;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.tool.SelectionTool;

public class DrawingView extends DefaultDrawingView {

	private static final long serialVersionUID = 7276696509798039409L;
	private static final int SingleClick = 1;
	private static final int DoubleClick = 2;

	private Drawing drawing;
	private DefaultDrawingEditor editor;
	private SelectionTool selectionTool;

	private ArrayList<MouseClickListener> listeners = new ArrayList<MouseClickListener>();
	private HashSet<Figure> previousSelection = new HashSet<Figure>();

	public DrawingView(Drawing drawing) {
		this.drawing = drawing;
		setDrawing(this.drawing);

		editor = new DefaultDrawingEditor();
		editor.add(this);

		initializeSelectionTool();
		initializeMouseListener();
		initializeKeyboardListener();
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

		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				onMouseWheel(e);
			}
		});
	}

	private void onMouseClicked(MouseEvent e) {

		handleDeselect();
		
		if (hasSelection()) {
			
			int mouseButton = e.getButton();
			int mouseClicks = e.getClickCount();
			
			if (mouseButton == MouseEvent.BUTTON1) {
				if (mouseClicks == SingleClick) {
					
					Set<Figure> selectedFigures = getSelectedFigures();
					BaseFigure[] selection = new BaseFigure[selectedFigures.size()];
					selection = selectedFigures.toArray(selection);
					
					figureSelected(selection);
				} else if (mouseClicks == DoubleClick) {
					BaseFigure figure = getFirstSelectedFigure();
					moduleZoom(figure);
				}
			}
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
		}
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

	private boolean hasSelection() {
		return getSelectedFigures().size() > 0;
	}
	
	private BaseFigure getFirstSelectedFigure() {
		
		Set<Figure> selection = getSelectedFigures();
		
		return (BaseFigure) selection.iterator().next();
	}

	private void figureSelected(BaseFigure[] figures) {
		for (MouseClickListener l : listeners) {
			l.figureSelected(figures);
		}
	}
	
	private void figureDeselected(BaseFigure[] figures) {
		for (MouseClickListener l : listeners) {
			l.figureDeselected(figures);
		}
	}

	private void moduleZoom(BaseFigure fig) {
		for (MouseClickListener l : listeners) {
			l.moduleZoom(fig);
		}
	}

	private void onMouseWheel(MouseWheelEvent e) {

	}

	private void initializeKeyboardListener() {
		addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				onKeyPressed(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
		});
	}

	protected void onKeyPressed(KeyEvent e) {

		int key = e.getKeyCode();
		
		if (key == KeyEvent.VK_BACK_SPACE) {
			
			moduleZoomOut();
		} else if (key == KeyEvent.VK_ENTER) {
			
			if (hasSelection()) {
				BaseFigure selection = getFirstSelectedFigure();
				moduleZoom(selection);
			}
		}
		
		e.consume();
	}

	private void moduleZoomOut() {
	
		for (MouseClickListener l : listeners) {
			l.moduleZoomOut();
		}
	}		
	

	public void addListener(MouseClickListener listener) {
		listeners.add(listener);
	}

	public void removeListener(MouseClickListener listener) {
		listeners.remove(listener);
	}
}
