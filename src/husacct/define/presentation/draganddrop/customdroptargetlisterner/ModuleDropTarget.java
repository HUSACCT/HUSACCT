package husacct.define.presentation.draganddrop.customdroptargetlisterner;

import husacct.define.domain.services.stateservice.StateService;
import husacct.define.presentation.draganddrop.customtransferhandlers.ModuleTrasferhandler;
import husacct.define.presentation.moduletree.AnalyzedModuleTree;
import husacct.define.presentation.moduletree.ModuleTree;
import husacct.define.presentation.utils.DragAndDropHelper;
import husacct.define.task.SoftwareUnitController;
import husacct.define.task.components.AbstractDefineComponent;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.tree.TreePath;

public class ModuleDropTarget implements DropTargetListener {
	private ModuleTree tree;
	private DropTarget target;
	private DataFlavor currentFlavor = null;
	private DataFlavor[] supportedDataFlavors = { DataFlavor.stringFlavor,
			ModuleTrasferhandler.moduleFlavours[0] };

	public ModuleDropTarget(ModuleTree tr) {
		this.tree = tr;
		this.target = new DropTarget(tree, this);
	}

	@Override
	public void dragEnter(DropTargetDragEvent arg) {
		arg.rejectDrag();

	}

	@Override
	public void dragExit(DropTargetEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dragOver(DropTargetDragEvent arg) {
		try {
			currentFlavor = null;

			for (DataFlavor resultFlavor : arg.getCurrentDataFlavors()) {
				for (DataFlavor supportedflavour : supportedDataFlavors) {
					if (resultFlavor.equals(supportedflavour)) {
						currentFlavor = supportedflavour;
					}
				}
			}
			Point p = arg.getLocation();
			TreePath path = tree.getPathForLocation(p.x, p.y);
			boolean isnull = path == null ? true : false;
			if (!isnull && currentFlavor != null) {
				arg.acceptDrag(1);
			} else {
				arg.rejectDrag();
			}

		} catch (Exception e) {
			arg.rejectDrag();
		}
	}

	@Override
	public void drop(DropTargetDropEvent arg) {

		if (currentFlavor == supportedDataFlavors[0]) {
            changeSoftwareUnitLocation(arg);
		} else if (currentFlavor == supportedDataFlavors[1]) {
			addSoftwareUnits(arg);
		}

	}

	private void addSoftwareUnits(DropTargetDropEvent arg) {
		try {

			AnalyzedModuleTree palo = (AnalyzedModuleTree) arg
					.getTransferable().getTransferData(currentFlavor);
			ArrayList<AnalyzedModuleComponent> tobesaved = new ArrayList<AnalyzedModuleComponent>();
			for (TreePath pathe : palo.getSelectionPaths()) {

				AnalyzedModuleComponent top = (AnalyzedModuleComponent) pathe
						.getLastPathComponent();
				String type = top.getType().toLowerCase();
				boolean res = ((!type.equals("root")
						&& !type.equals("application") && !type
						.equals("externalpackage")));
				if (res) {

					AnalyzedModuleComponent referencedUnit = StateService
							.instance().getAnalyzedSoftWareUnit(
									top.getUniqueName());
					if (!referencedUnit.isMapped()) {
						tobesaved.add(referencedUnit);
					}

				}
			}

			SoftwareUnitController controller = new SoftwareUnitController(
					getSelectedNode(arg).getModuleId());
			controller.save(tobesaved);

		} catch (UnsupportedFlavorException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		
	}

	private void changeSoftwareUnitLocation(DropTargetDropEvent arg) {
		try {
			
			String result = ((String) arg.getTransferable().getTransferData(
					currentFlavor));
			Object[] namesAndTypes = DragAndDropHelper.interpretObjects(result);
			ArrayList<String> names = (ArrayList<String>) namesAndTypes[0];
			ArrayList<String> types = (ArrayList<String>) namesAndTypes[1];
			SoftwareUnitController controller = new SoftwareUnitController(getSelectedNode(arg).getModuleId());
            controller.changeSoftwareUnit(getSelectedNode(arg).getModuleId(),names);
		} catch (Exception e) {

		}
		
		
		
	

	}

	private AbstractDefineComponent getSelectedNode(DropTargetDropEvent arg) {

		Point p = arg.getLocation();
		TreePath path = tree.getPathForLocation(p.x, p.y);
		AbstractDefineComponent selectedNode = (AbstractDefineComponent) path
				.getLastPathComponent();

		return selectedNode;

	}

	@Override
	public void dropActionChanged(DropTargetDragEvent arg0) {

	}

}
