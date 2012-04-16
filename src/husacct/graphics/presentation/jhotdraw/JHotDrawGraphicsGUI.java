package husacct.graphics.presentation.jhotdraw;

import husacct.common.dto.ViolationDTO;
import husacct.graphics.presentation.jhotdraw.figures.AbstractJHotDrawFigure;
import husacct.graphics.presentation.jhotdraw.figures.JHotDrawModuleFigure;
import husacct.graphics.presentation.jhotdraw.figures.JHotDrawRelationFigure;
import husacct.graphics.task.figures.AbstractFigure;
import husacct.graphics.task.figures.ModuleFigure;
import husacct.graphics.task.figures.RelationFigure;

import java.awt.Dimension;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.JInternalFrame;

public class JHotDrawGraphicsGUI extends husacct.graphics.presentation.GraphicsGUI
{
	private Drawing drawing;
	private DrawingView view;
	private GraphicsFrame drawTarget;
	
	public JHotDrawGraphicsGUI()
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
		ArrayList<ModuleFigure> result = new ArrayList<ModuleFigure>();
		for(JHotDrawModuleFigure jhFigure : drawing.getShownModules())
		{
			result.add((ModuleFigure)jhFigure.getFigure());
		}
		return result.toArray(new ModuleFigure[]{});
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
		AbstractJHotDrawFigure jhFigure = (new JHotDrawFigureFactory()).createFigure(figure);
		
		//TODO implement layout strategy here
		
		drawing.add(jhFigure);
	}

	@Override
	public void addRelation(RelationFigure relation, ModuleFigure from,
			ModuleFigure to)
	{
		JHotDrawFigureFactory factory = new JHotDrawFigureFactory();
		JHotDrawRelationFigure jhRelation = factory.createFigure(relation);
		JHotDrawModuleFigure jhFrom = factory.createFigure(from);
		JHotDrawModuleFigure jhTo = factory.createFigure(to);
		
		(new FigureConnectorStrategy()).connect(jhRelation, jhFrom, jhTo);
		drawing.add(jhRelation);
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
