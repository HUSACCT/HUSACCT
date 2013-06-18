package husacct.define.presentation.draganddrop.customdroptargetlisterner;

import husacct.define.domain.services.SoftwareUnitDefinitionDomainService;
import husacct.define.domain.services.stateservice.StateService;
import husacct.define.presentation.draganddrop.customtransferhandlers.ModuleTrasferhandler;
import husacct.define.presentation.moduletree.AnalyzedModuleTree;
import husacct.define.presentation.moduletree.ModuleTree;
import husacct.define.task.SoftwareUnitController;
import husacct.define.task.components.AbstractDefineComponent;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.awt.Point;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

public class ModuleDropTarget implements DropTargetListener{
    private ModuleTree tree;
    private DropTarget target;
	public ModuleDropTarget(ModuleTree tr) {
		this.tree=tr;
		this.target= new DropTarget(tree, this);
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
	public void dragOver(DropTargetDragEvent arg) {
	try{
		Point p = arg.getLocation();
		 TreePath path = tree.getPathForLocation(p.x, p.y);
			AnalyzedModuleTree palo = (AnalyzedModuleTree)arg.getTransferable().getTransferData(ModuleTrasferhandler.moduleFlavours[0]);
		boolean isnull= path==null? true:false;
	if (!isnull&&palo!=null) {
		arg.acceptDrag(1);
	}else {
		arg.rejectDrag();
	}	
	
		
		
	}catch(Exception e)
	{
		arg.rejectDrag();
	}
	}

	@Override
	public void drop(DropTargetDropEvent arg) {
		Point p = arg.getLocation();
		 TreePath path = tree.getPathForLocation(p.x, p.y);
		AbstractDefineComponent selectedNode = (AbstractDefineComponent)path.getLastPathComponent();
		
			
			
	
		try {
			
			AnalyzedModuleTree palo = (AnalyzedModuleTree)arg.getTransferable().getTransferData(ModuleTrasferhandler.moduleFlavours[0]);
			ArrayList<AnalyzedModuleComponent> tobesaved = new ArrayList<AnalyzedModuleComponent>();
			for (TreePath pathe : palo.getSelectionPaths()) {
				AnalyzedModuleComponent top= (AnalyzedModuleComponent)pathe.getLastPathComponent();
				AnalyzedModuleComponent referencedUnit=	StateService.instance().getAnalyzedSoftWareUnit(top.getUniqueName());
				if (!referencedUnit.isMapped()) {
					tobesaved.add(referencedUnit);
				}
				
			}
			
			SoftwareUnitController controller = new SoftwareUnitController(selectedNode.getModuleId());
			controller.save(tobesaved);
		
		} catch (UnsupportedFlavorException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
			
		
		
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent arg0) {
		
		
	}

}
