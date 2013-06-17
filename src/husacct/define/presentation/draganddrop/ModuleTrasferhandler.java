package husacct.define.presentation.draganddrop;

import husacct.define.task.components.AbstractCombinedComponent;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.TreePath;

public class ModuleTrasferhandler  extends  TransferHandler{

	public static DataFlavor[] moduleFlavours= {new DataFlavor(AbstractCombinedComponent.class, "mainComponent")};
    

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

        System.out.println(line.getUniqueName());
     
        return true;
    }
}


