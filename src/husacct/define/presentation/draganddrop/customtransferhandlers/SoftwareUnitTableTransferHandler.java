package husacct.define.presentation.draganddrop.customtransferhandlers;

import husacct.define.task.components.AbstractCombinedComponent;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.TreePath;

public class SoftwareUnitTableTransferHandler extends TransferHandler implements Transferable{
	
	
	public static DataFlavor[] moduleFlavours= {new DataFlavor(AbstractCombinedComponent.class, "mainComponent")};
	
	
	
	
	@Override
	public boolean canImport(TransferSupport support) {
	        if (!support.isDrop()) {
	            return false;
	        }

	        return support.isDataFlavorSupported(moduleFlavours[0]);
	    }

	    @Override
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


	    
	    
	    
	    
	    @Override
		public int getSourceActions(JComponent c) {
	    	return TransferHandler.COPY;
	    	}
	    	
	    
	    @Override
		public boolean canImport(JComponent comp, DataFlavor flavor[]) {
	    	if (!(comp instanceof JLabel) && !(comp instanceof AbstractButton)) {
	    
	    	}	
	    	return false;
	    	}
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    @Override
		public Transferable createTransferable(JComponent comp) {
	    	// Clear
	    
	   
	    	return this;
	    	
	    }
	    
	    
	    
	    
	    
	    
	    
	    @Override
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
			return null;
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



	

}
