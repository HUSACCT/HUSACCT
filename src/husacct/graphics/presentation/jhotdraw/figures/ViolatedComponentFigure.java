package husacct.graphics.presentation.jhotdraw.figures;

import java.awt.Color;

import husacct.common.dto.ViolationDTO;
import husacct.graphics.task.figures.IViolatedFigure;
import husacct.graphics.task.figures.ViolatedModuleFigure;

@SuppressWarnings("serial")
public class ViolatedComponentFigure extends ComponentFigure implements
		IViolatedFigure
{

	public ViolatedComponentFigure(ViolatedModuleFigure moduleFigure)
	{
		super(moduleFigure);
	}
	
	protected void initializeComponents()
	{
		super.initializeComponents();

		this.setLineColor(Color.RED);
		//this.setLineThickness(2.0);
	}

	@Override
	public ViolationDTO[] getViolations()
	{
		return ((ViolatedModuleFigure)this.getFigure()).getViolations();
	}

}
