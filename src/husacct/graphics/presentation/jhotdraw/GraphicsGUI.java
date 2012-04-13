package husacct.graphics.presentation.jhotdraw;

import husacct.common.dto.ViolationDTO;
import husacct.graphics.task.figures.AbstractFigure;
import husacct.graphics.task.figures.ModuleFigure;
import husacct.graphics.task.figures.RelationFigure;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.JInternalFrame;

public class GraphicsGUI extends husacct.graphics.presentation.GraphicsGUI
{
	private Drawing drawing;
	private DrawingView view;
	private GraphicsFrame drawTarget;
	
	public GraphicsGUI()
	{
		drawing = new Drawing();
		view = new DrawingView(drawing, this);
		view.setPreferredSize(new Dimension(500,500));
		
		drawTarget = new GraphicsFrame(view);
	}

	@Override
	public JInternalFrame getGUI()
	{
		return drawTarget;
	}

	@Override
	public ModuleFigure[] getShownModuleFigures()
	{
		return drawing.getShownModules();
	}

	@Override
	public Image getImage(String type)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void add(AbstractFigure figure)
	{
		drawing.add(figure);
	}

	@Override
	public void addRelation(RelationFigure relation, ModuleFigure from,
			ModuleFigure to)
	{
		(new FigureConnectorStrategy()).connect(relation, from, to);
		drawing.add(relation);
	}

	@Override
	public void showViolations(ViolationDTO[] violations)
	{
		// TODO Auto-generated method stub
		for(ViolationDTO violation : violations)
		{
			System.out.println(violation.getErrorMessage());
		}
	}

}
