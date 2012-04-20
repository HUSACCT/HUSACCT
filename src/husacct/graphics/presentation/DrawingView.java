package husacct.graphics.presentation;

import husacct.graphics.presentation.figures.BaseFigure;
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
	
	public DrawingView(Drawing drawing)
	{
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
		} );
		
		addMouseWheelListener(new MouseWheelListener() { 
			 public void mouseWheelMoved(MouseWheelEvent e) {
				 onMouseWheel(e);
			 }
		} );
	}
	
	private void onMouseClicked(MouseEvent e) {
		
		Set<Figure> selection = getSelectedFigures();
		
		if (!selection.isEmpty()) {
			Figure first = selection.iterator().next();
			
			if (e.getButton() == MouseEvent.BUTTON1) {
				if (e.getClickCount() == 1)
					fireModuleSelectedEvent((BaseFigure)first);
				else if (e.getClickCount() == 2) 
					fireModuleZoomInEvent((BaseFigure)first);
			}
				
		}
	}
	
	private void fireModuleSelectedEvent(BaseFigure obj) {
		for (MouseClickListener l : listeners) {
			l.moduleSelected(obj);
		}
	}
	
	private void fireModuleZoomInEvent(BaseFigure obj) {
		for (MouseClickListener l : listeners) {
			l.moduleZoom(obj);
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
