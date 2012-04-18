package husacct.graphics.presentation;

import husacct.graphics.presentation.decorators.DTODecorator;
import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.presentation.figures.IViolatedFigure;
import husacct.graphics.task.MouseClickListener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Set;

import org.jhotdraw.draw.DefaultDrawingEditor;
import org.jhotdraw.draw.DefaultDrawingView;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.tool.SelectionTool;

public class DrawingView extends DefaultDrawingView {

	private static final long serialVersionUID = 7276696509798039409L;

	private Drawing drawing;
	private DefaultDrawingEditor editor;
	private SelectionTool selectionTool;

	private ArrayList<MouseClickListener> listeners = new ArrayList<MouseClickListener>();

	public DrawingView(Drawing drawing) {
		this.drawing = drawing;
		setDrawing(this.drawing);

		editor = new DefaultDrawingEditor();
		editor.add(this);

		initializeSelectionTool();
		initializeMouseListener();
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

		Set<Figure> selection = getSelectedFigures();

		if (!selection.isEmpty()) {
			Figure first = selection.iterator().next();

			if (e.getButton() == MouseEvent.BUTTON1) {
				if (e.getClickCount() == 1 && first instanceof IViolatedFigure) {
					fireViolatedFigureSelected((IViolatedFigure) first);
				} else if (e.getClickCount() == 2) {
					fireModuleZoomInEvent((BaseFigure) first);
				}
			}
		}
	}

	private void fireViolatedFigureSelected(IViolatedFigure fig) {
		for (MouseClickListener l : listeners) {
			l.showViolations(fig.getViolations());
		}
	}

	private void fireModuleZoomInEvent(BaseFigure fig) {
		for (MouseClickListener l : listeners) {
			
			if (fig instanceof DTODecorator) {
				DTODecorator dec = (DTODecorator)fig;
				l.moduleZoom(dec.getDTO());			
			}
		}
	}

	private void onMouseWheel(MouseWheelEvent e) {

	}

	public void addListener(MouseClickListener listener) {
		listeners.add(listener);
	}

	public void removeListener(MouseClickListener listener) {
		listeners.remove(listener);
	}
}
