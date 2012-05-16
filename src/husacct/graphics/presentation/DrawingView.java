package husacct.graphics.presentation;

import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.task.UserInputListener;

import java.awt.event.KeyEvent;
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
	// private static final int SingleClick = 1;
	private static final int DoubleClick = 2;

	private Drawing drawing;
	private DefaultDrawingEditor editor;
	private SelectionTool selectionTool;

	private ArrayList<UserInputListener> listeners = new ArrayList<UserInputListener>();
	private HashSet<Figure> previousSelection = new HashSet<Figure>();

	public DrawingView(Drawing drawing) {
		this.drawing = drawing;
		setDrawing(this.drawing);

		editor = new DefaultDrawingEditor();
		editor.add(this);

		initializeSelectionTool();
		initializeMouseListener();
		initializeSelectionListener();

		// FIXME: Keyboard listeners contain bugs. Fix before re-enabling
		// initializeKeyboardListener();
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

	private void onMouseClicked(MouseEvent e) {
		handleDeselect();
		if (hasSelection()) {
			int mouseButton = e.getButton();
			int mouseClicks = e.getClickCount();

			if (mouseButton == MouseEvent.BUTTON1) {
				BaseFigure[] selection = toFigureArray(getSelectedFigures());

				if (mouseClicks == DoubleClick) {
					moduleZoom(selection);
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

			// show the selected figures on top
			for (BaseFigure selectedFig : selection) {
				this.drawing.bringToFront(selectedFig);
				// TODO also raise connection figures pointing to and from the
				// selected figure(s)
			}

			figureSelected(selection);
		}
	}

	// TODO: DO NOT REMOVE THIS FUNCTION. IT IS DISABLED BECAUSE IT CONTAINS
	// BUGS
	// NOT BECAUSE IT IS UNWANTED CODE
	// private void initializeKeyboardListener() {
	// addKeyListener(new KeyListener() {
	// @Override
	// public void keyPressed(KeyEvent e) {
	// onKeyPressed(e);
	// }
	//
	// @Override
	// public void keyReleased(KeyEvent e) {
	// }
	//
	// @Override
	// public void keyTyped(KeyEvent e) {
	// }
	// });
	// }

	protected void onKeyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_BACK_SPACE) {
			moduleZoomOut();
		} else if (key == KeyEvent.VK_ENTER) {
			if (hasSelection()) {
				BaseFigure[] selection = toFigureArray(getSelectedFigures());
				moduleZoom(selection);
			}
		}
		e.consume();
	}

	private void moduleZoomOut() {
		for (UserInputListener l : listeners) {
			l.moduleZoomOut();
		}
	}

	public void addListener(UserInputListener listener) {
		listeners.add(listener);
	}

	public void removeListener(UserInputListener listener) {
		listeners.remove(listener);
	}
}
