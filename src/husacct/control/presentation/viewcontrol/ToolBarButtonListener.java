package husacct.control.presentation.viewcontrol;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class ToolBarButtonListener extends MouseAdapter{
	
	private InternalFrameController internalFrameController;
	
	public ToolBarButtonListener(InternalFrameController internalFrameController){
		this.internalFrameController = internalFrameController;
	}
	
	@Override
	public void mousePressed(MouseEvent e){
		if(e.isPopupTrigger()){
			doPop(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e){
		if(e.isPopupTrigger()){
			doPop(e);
		}
	}

	private void doPop(MouseEvent e){
		ToolBarButtonContextMenu menu = new ToolBarButtonContextMenu(internalFrameController);
		menu.show(e.getComponent(), e.getX(), e.getY() - 70);
	}
}
