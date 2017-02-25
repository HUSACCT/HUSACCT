package husacct.define.presentation.draganddrop.customdroptargetlisterner;

import husacct.define.domain.services.stateservice.StateService;
import husacct.define.presentation.draganddrop.customtransferhandlers.ModuleTrasferhandler;
import husacct.define.presentation.moduletree.AnalyzedModuleTree;
import husacct.define.presentation.tables.JTableSoftwareUnits;
import husacct.define.task.DefinitionController;
import husacct.define.task.SoftwareUnitController;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.tree.TreePath;

public class SoftwareUnitTableDropListener implements DropTargetListener {
	private JTableSoftwareUnits softwareUnitTable;
	 private DropTarget target;
	
	public SoftwareUnitTableDropListener(JTableSoftwareUnits table) {
		this.softwareUnitTable=table;
		this.target= new DropTarget(table, this);
		
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
	public void dragOver(DropTargetDragEvent arg) {
		try {
			AnalyzedModuleTree palo = (AnalyzedModuleTree)arg.getTransferable().getTransferData(ModuleTrasferhandler.moduleFlavours[0]);
		
			if (palo!=null) {
				arg.acceptDrag(1);
			} else {
				arg.rejectDrag();
			}
			
			
		} catch (UnsupportedFlavorException | IOException e) {
			arg.rejectDrag();
			
		}


    }

	@Override
	public void drop(DropTargetDropEvent arg) {
		try {
			
			AnalyzedModuleTree palo = (AnalyzedModuleTree)arg.getTransferable().getTransferData(ModuleTrasferhandler.moduleFlavours[0]);
			ArrayList<AnalyzedModuleComponent> tobesaved = new ArrayList<>();
			for (TreePath pathe : palo.getSelectionPaths()) {
				AnalyzedModuleComponent top= (AnalyzedModuleComponent)pathe.getLastPathComponent();
				//hot fix if have time will be better implemented 
				String type = top.getType().toLowerCase().trim();
				boolean res =((!type.equals("root")&&!type.equals("application")&&!type.equals("externalpackage")));
				
				
				if (res) {
			AnalyzedModuleComponent referencedUnit=	StateService.instance().getAnalyzedSoftWareUnit(top.getUniqueName());
				if (!referencedUnit.isMapped()) {
					tobesaved.add(referencedUnit);
				}
				
			}
			}
			
	
			long selectedModuleID =DefinitionController.getInstance().getSelectedModuleId();
		    SoftwareUnitController controller = new SoftwareUnitController(selectedModuleID);
			controller.save(tobesaved);
		} catch (UnsupportedFlavorException | IOException e) {
			
			e.printStackTrace();
		}

    }

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub

	}

}
