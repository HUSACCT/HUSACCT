package husacct.define.task.components;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.tree.TreePath;

public abstract class AbstractCombinedComponent extends Component implements
	Comparable<AbstractCombinedComponent>, Transferable {
    private static final long serialVersionUID = 1L;
    private int analyzedModuleComponentPosition = 0;
    protected ArrayList<AbstractCombinedComponent> children;
    private AnalyzedModuleComponent parent;
    protected int sizeOfChildren;
    protected String type = "";
    protected String uniqueName = "";
     static DataFlavor softwareUnitFlavour= new DataFlavor(AbstractCombinedComponent.class, "");


    public AbstractCombinedComponent() {
	super();
	children = new ArrayList<AbstractCombinedComponent>();
    }

    public abstract void addChild(AbstractCombinedComponent child);

    public abstract void addChild(int index, AbstractCombinedComponent child);

    @Override
    public int compareTo(AbstractCombinedComponent left) {

	if (left.getType().toLowerCase().equals("externalpackage")) {
	    return -1;
	} else if (getType().toLowerCase().equals("externalpackage")) {
	    return 1;
	}

	else {

	    return getUniqueName().toUpperCase().compareTo(
		    left.getUniqueName().toUpperCase());
	}
    }

    public int getAnalyzedModuleComponentPosition() {
	return analyzedModuleComponentPosition;
    }

    public abstract ArrayList<AbstractCombinedComponent> getChildren();

    public AnalyzedModuleComponent getParentofChild() {

	return parent;
    }

    public String getType() {
	return type;
    }

    public String getUniqueName() {
	return uniqueName;
    }

    public abstract void removeChild(AbstractCombinedComponent child);

    public void setAnalyzedModuleComponentPosition(
	    int analyzedModuleComponentPosition) {
	this.analyzedModuleComponentPosition = analyzedModuleComponentPosition;
    }

    public abstract void setChildren(
	    ArrayList<AbstractCombinedComponent> children);

    public void setParentOfChild(AbstractCombinedComponent parentofchild) {

	parent = (AnalyzedModuleComponent) parentofchild;
    }

    public void setType(String type) {
	this.type = type.toUpperCase();
    }

    public void setUniqueName(String uniqueName) {
	this.uniqueName = uniqueName;
    }

    public void updateChilderenPosition() {
	for (int i = 0; i < children.size(); i++) {
	    int newPosition = i;
	    children.get(i).setAnalyzedModuleComponentPosition(newPosition);

	}

    }
    @Override
    public Object getTransferData(DataFlavor arg0)
			throws UnsupportedFlavorException, IOException {
		
		return this;
	}

    @Override
	public DataFlavor[] getTransferDataFlavors() {
		
		
		return new DataFlavor[]{softwareUnitFlavour};
	}

    @Override
	public boolean isDataFlavorSupported(DataFlavor arg0) {
		// TODO Auto-generated method stub t
		
    	return true;
	}

}
