package husacct.graphics.presentation.jhotdraw.figures;

import java.awt.Color;

import husacct.common.dto.ViolationDTO;
import husacct.graphics.task.figures.IViolatedFigure;
import husacct.graphics.task.figures.ViolatedRelationFigure;

public class JHotDrawViolatedRelationFigure extends JHotDrawRelationFigure 
		implements IViolatedFigure
{
	private static final long serialVersionUID = -2679119740061602305L;

	public JHotDrawViolatedRelationFigure(ViolatedRelationFigure figure)
	{
		super(figure);
		
		//this.setLineThickness(1.0);
		this.setLineColor(Color.RED);
	}

	@Override
	public ViolationDTO[] getViolations()
	{
		return new ViolationDTO[]{};
	}

}
