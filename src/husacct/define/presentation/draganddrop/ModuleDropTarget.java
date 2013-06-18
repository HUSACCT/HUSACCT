package husacct.define.presentation.draganddrop;

import husacct.define.presentation.moduletree.ModuleTree;
import husacct.define.task.components.AbstractDefineComponent;

import java.awt.Point;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

public class ModuleDropTarget implements DropTargetListener{
    private ModuleTree tree;
    private DropTarget target;
	public ModuleDropTarget(ModuleTree tr) {
		this.tree=tr;
		this.target= new DropTarget(tree, this);
	}

	
	 private AbstractDefineComponent getNodeForEvent(DropTargetDragEvent dtde) {
		    Point p = dtde.getLocation();
		    DropTargetContext dtc = dtde.getDropTargetContext();
		    JTree tree = (JTree)dtc.getComponent();
		    TreePath path = tree.getClosestPathForLocation(p.x, p.y);
		    return (AbstractDefineComponent)path.getLastPathComponent();
		  }
	
	
	@Override
	public void dragEnter(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragExit(DropTargetEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragOver(DropTargetDragEvent arg0) {
	if (getNodeForEvent(arg0).getModuleId()==0) {
		arg0.acceptDrag(0);
	}else{
		arg0.rejectDrag();
	}	
		
	}

	@Override
	public void drop(DropTargetDropEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
