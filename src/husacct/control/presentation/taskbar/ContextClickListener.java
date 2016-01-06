package husacct.control.presentation.taskbar;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JInternalFrame;

class ContextClickListener extends MouseAdapter{
	
	private JInternalFrame internalFrame;
	
	public ContextClickListener(JInternalFrame internalFrame){
		this.internalFrame = internalFrame;
	}
	
	public void mousePressed(MouseEvent e){
		if(e.isPopupTrigger()){
			doPop(e);
		}
	}

	public void mouseReleased(MouseEvent e){
		if(e.isPopupTrigger()){
			doPop(e);
		}
	}

	private void doPop(MouseEvent e){
		ToolBarButtonContextMenu menu = new ToolBarButtonContextMenu(internalFrame);
		// TODO: calculate height from ToolBarButtonContextMenu 
		menu.show(e.getComponent(), e.getX(), e.getY() - 70);
	}
}
