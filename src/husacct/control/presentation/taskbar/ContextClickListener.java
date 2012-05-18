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
		ContextMenu menu = new ContextMenu(internalFrame);
		System.out.println(menu.getHeight());
		menu.show(e.getComponent(), e.getX(), e.getY() - 70);
	}
}
