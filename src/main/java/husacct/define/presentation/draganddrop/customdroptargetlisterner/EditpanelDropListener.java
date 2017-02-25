package husacct.define.presentation.draganddrop.customdroptargetlisterner;

import husacct.define.presentation.jpanel.EditModuleJPanel;

import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.util.ArrayList;

import javax.swing.JComponent;

public class EditpanelDropListener implements DropTargetListener {

	 private DropTarget target;
	private ArrayList<DropTarget> dropTargets = new ArrayList<>();
	
	
	
	public EditpanelDropListener(EditModuleJPanel panel) {
		dropTargets.add	(new DropTarget(panel,this));
	}







	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragExit(DropTargetEvent dte) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragOver(DropTargetDragEvent dtde) {
		dtde.rejectDrag();
		
	}

	@Override
	public void drop(DropTargetDropEvent dtde) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
		
	}







	public void addTarget(JComponent component) {
		dropTargets.add(new DropTarget(component,this));
		
	}

}
