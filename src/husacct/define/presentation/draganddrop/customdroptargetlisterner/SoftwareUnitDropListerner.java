package husacct.define.presentation.draganddrop.customdroptargetlisterner;

import husacct.define.presentation.moduletree.AnalyzedModuleTree;
import husacct.define.presentation.moduletree.ModuleTree;
import husacct.define.task.components.AbstractDefineComponent;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.awt.Point;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

public class SoftwareUnitDropListerner  implements DropTargetListener{
	private AnalyzedModuleTree  tree;
    private DropTarget target;
	public SoftwareUnitDropListerner (AnalyzedModuleTree tr) {
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
	public void dragEnter(DropTargetDragEvent arg) {
	
		 Point p = arg.getLocation();
		 TreePath path = tree.getPathForLocation(p.x, p.y);
		 AnalyzedModuleComponent check = (AnalyzedModuleComponent)path.getLastPathComponent();
		 String type = check.getType().toLowerCase();
		 if (type.equals("root")||type.equals("application")||type.equals("externalpackage")) {
			arg.rejectDrag();
		 }
		
	}

	@Override
	public void dragExit(DropTargetEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragOver(DropTargetDragEvent arg0) {
	
		arg0.rejectDrag();
	
		
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
