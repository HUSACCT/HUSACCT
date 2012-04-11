package husacct.graphics.presentation;

import husacct.common.dto.ModuleDTO;
import husacct.graphics.task.IDrawingListener;

import java.util.ArrayList;

public class Drawing extends org.jhotdraw.draw.DefaultDrawing
{
	private static final long serialVersionUID = 3212318618672284266L;
	
	private ArrayList<IDrawingListener> drawingListeners;
	
	public Drawing()
	{
		super();
		
		this.drawingListeners = new ArrayList<IDrawingListener>();
	}

	public ModuleDTO[] getShownModules()
	{
		ModuleDTO[] result = new ModuleDTO[]{};
		
		return (new ArrayList<ModuleDTO>()).toArray(result);
	}
	
	public void addDrawingListener(IDrawingListener listener)
	{
		this.drawingListeners.add(listener);
	}
}
