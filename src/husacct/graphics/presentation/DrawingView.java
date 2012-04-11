package husacct.graphics.presentation;

import org.jhotdraw.draw.DefaultDrawingEditor;
import org.jhotdraw.draw.DefaultDrawingView;
import org.jhotdraw.draw.event.FigureSelectionEvent;
import org.jhotdraw.draw.event.FigureSelectionListener;
import org.jhotdraw.draw.tool.SelectionTool;

public class DrawingView extends DefaultDrawingView {
	
	private static final long serialVersionUID = 7276696509798039409L;

	private Drawing drawing;
	private DefaultDrawingEditor editor;
	private SelectionTool selectionTool;
	
	public DrawingView(Drawing drawing)
	{
		this.drawing = drawing;
		this.setDrawing(this.drawing);
		
		this.editor = new DefaultDrawingEditor();
		editor.add(this);
		
		initializeSelectionTool();
	}
	
	private void initializeSelectionTool() {
		selectionTool = new SelectionTool();
			
		editor.setTool(selectionTool);
	}
}
