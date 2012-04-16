package husacct.graphics.presentation.jhotdraw;

import husacct.graphics.presentation.GraphicsGUI;
import husacct.graphics.presentation.jhotdraw.figures.JHotDrawModuleFigure;
import husacct.graphics.task.figures.IViolatedFigure;
import husacct.graphics.task.figures.ModuleFigure;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;

import org.jhotdraw.draw.DefaultDrawingEditor;
import org.jhotdraw.draw.DefaultDrawingView;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.tool.SelectionTool;

public class DrawingView extends DefaultDrawingView
{	
	private static final long serialVersionUID = 7276696509798039409L;

	private Drawing drawing;
	private DefaultDrawingEditor editor;
	private SelectionTool selectionTool;
	
	public DrawingView(Drawing drawing, final GraphicsGUI guiNotificationsFirer)
	{		
		this.drawing = drawing;
		this.setDrawing(this.drawing);
		this.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
				Set<Figure> figures = getSelectedFigures();
				if(figures.size() == 1)
				{
					Figure selectedFigure = figures.iterator().next();
					
					if(selectedFigure instanceof JHotDrawModuleFigure && arg0.getClickCount() == 2)
					{
						guiNotificationsFirer.fireModuleFigureZoom(
								((ModuleFigure)((JHotDrawModuleFigure)selectedFigure).getFigure()));
					}
					
					if(selectedFigure instanceof IViolatedFigure)
					{
						guiNotificationsFirer.fireViolatedFigureSelect((IViolatedFigure)selectedFigure);
					}
					
				}
			}
		});
		
		this.editor = new DefaultDrawingEditor();
		editor.add(this);
		
		initializeSelectionTool();
	}
	
	private void initializeSelectionTool() {
		selectionTool = new SelectionTool();
			
		editor.setTool(selectionTool);
	}
}
