package husacct.graphics.presentation.figures;

import java.awt.Color;

public class ViolatedRelationFigure extends RelationFigure
{
	private static final long serialVersionUID = -2679119740061602305L;

	public ViolatedRelationFigure(String name)
	{
		super(name);
		
		//this.setLineThickness(1.0);
		this.setLineColor(Color.RED);
	}

}
