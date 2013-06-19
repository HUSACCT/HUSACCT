package husacct.define.presentation.draganddrop.customtransferhandlers;

import husacct.define.task.components.AbstractCombinedComponent;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.TreePath;

public class ModuleTrasferhandler  extends  TransferHandler implements Transferable,DropTargetListener{

	public static DataFlavor[] moduleFlavours= {new DataFlavor(AbstractCombinedComponent.class, "mainComponent")};
    private TreePath[] data;
    JTree tree;

    public boolean canImport(TransferSupport support) {
        if (!support.isDrop()) {
            return false;
        }

        return support.isDataFlavorSupported(moduleFlavours[0]);
    }

    public boolean importData(TransferSupport support) {
        if (!canImport(support)) {
          return false;
        }

        Transferable transferable = support.getTransferable();
        AbstractCombinedComponent line;
        try {
         line = (AbstractCombinedComponent) transferable.getTransferData(moduleFlavours[0]);
        } catch (Exception e) {
          return false;
        }

        JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
        TreePath path = dl.getPath();

      
     
        return false;
    }


    
    
    
    
    public int getSourceActions(JComponent c) {
    	return TransferHandler.COPY;
    	}
    	
    
    public boolean canImport(JComponent comp, DataFlavor flavor[]) {
    	if (!(comp instanceof JLabel) && !(comp instanceof AbstractButton)) {
    
    	}	
    	return false;
    	}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public Transferable createTransferable(JComponent comp) {
    	// Clear
    	data = null;
    	tree=null;
    	if (comp instanceof JTree) {
    	tree = (JTree) comp;
    	data=tree.getSelectionPaths();
   
    	return this;
    	}
    	
    	return null;
    	}
    
    
    
    
    
    
    
    
    public boolean importData(JComponent comp, Transferable t) {
    	
    	if (comp instanceof JLabel) {
    	JLabel label = (JLabel)comp;
    	if (t.isDataFlavorSupported(moduleFlavours[0])) {
    	try {
    try {
		JTree 	tr = (JTree)t.getTransferData(moduleFlavours[0]);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    	
   
    	return true;
    	
    	
    	} catch (UnsupportedFlavorException ignored) {
    	} 
 
    	}
    	
    	  return true;
    	}
    
    return false;
    }  
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    @Override
	public Object getTransferData(DataFlavor arg0)
			throws UnsupportedFlavorException, IOException {
		// TODO Auto-generated method stub
		return tree;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		// TODO Auto-generated method stub
		return moduleFlavours;
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor arg) {
		// TODO Auto-generated method stub
		return moduleFlavours[0].equals(arg);
	}



	@Override
	public void dragEnter(DropTargetDragEvent arg0) {
	

	}

	@Override
	public void dragExit(DropTargetEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dragOver(DropTargetDragEvent arg) {
		arg.rejectDrag();

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