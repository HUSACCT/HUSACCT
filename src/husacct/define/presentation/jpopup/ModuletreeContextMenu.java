package husacct.define.presentation.jpopup;

import husacct.define.presentation.jpanel.ModuleJPanel;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.jfree.ui.action.ActionMenuItem;

public class ModuletreeContextMenu extends JPopupMenu{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


private JMenuItem newModule,removeModule,moveUp,moveDown;
private JPanel parentPanel;

public ModuletreeContextMenu(JPanel parentPanel)
{
	this.parentPanel=parentPanel;
newModule = new JMenuItem("new module");
removeModule = new JMenuItem("remove module");
moveUp = new JMenuItem("level up");
moveUp.addActionListener(moveUpAction);
moveDown = new JMenuItem("level down");
moveDown.addActionListener(moveDownAction);
newModule.addActionListener(createNewModuleAction);
removeModule.addActionListener(removeModuleAction);


}





private AbstractAction createNewModuleAction = new AbstractAction() {
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
	if(parentPanel instanceof ModuleJPanel)
	{
       ((ModuleJPanel) parentPanel).newModule();
	}
		
	}
};



private AbstractAction removeModuleAction = new AbstractAction() {
	
	@Override
	public void actionPerformed(ActionEvent a) {
		
		
	if(parentPanel instanceof ModuleJPanel)
	{
       ((ModuleJPanel) parentPanel).removeModule();
	}
		
	}
};



private AbstractAction moveUpAction = new AbstractAction() {
	
	@Override
	public void actionPerformed(ActionEvent a) {
		
		
	if(parentPanel instanceof ModuleJPanel)
	{
       ((ModuleJPanel) parentPanel).moveLayerUp();
	}
		
	}
};

private AbstractAction moveDownAction = new AbstractAction() {
	
	@Override
	public void actionPerformed(ActionEvent a) {
		
		
	if(parentPanel instanceof ModuleJPanel)
	{
       ((ModuleJPanel) parentPanel).moveLayerDown();
	}
		
	}
};




public void isLayer() {
	isComponent();
	this.add(moveUp);
	this.add(moveDown);
	
}


public void isComponent() {
	this.add(newModule);
this.add(removeModule);
	
}

}
