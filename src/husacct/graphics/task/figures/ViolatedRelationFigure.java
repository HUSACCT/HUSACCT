package husacct.graphics.task.figures;

import java.awt.Color;

import husacct.common.dto.ViolationDTO;

public class ViolatedRelationFigure extends RelationFigure implements
		IViolatedFigure
{
	private static final long serialVersionUID = -2679119740061602305L;

	public ViolatedRelationFigure()
	{
		super();
		
		//this.setLineThickness(1.0);
		this.setLineColor(Color.RED);
	}

	@Override
	public ViolationDTO[] getViolations()
	{
		return new ViolationDTO[]{};
	}

}
