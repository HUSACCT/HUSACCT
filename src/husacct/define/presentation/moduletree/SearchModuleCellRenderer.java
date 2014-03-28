package husacct.define.presentation.moduletree;

import husacct.common.Resource;
import husacct.define.task.components.AbstractCombinedComponent;
import husacct.define.task.components.AbstractDefineComponent;
import husacct.define.task.components.AnalyzedModuleComponent;
import husacct.define.task.components.ComponentComponent;
import husacct.define.task.components.ExternalLibraryComponent;
import husacct.define.task.components.FacadeComponent;
import husacct.define.task.components.LayerComponent;
import husacct.define.task.components.SoftwareArchitectureComponent;
import husacct.define.task.components.SubSystemComponent;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class SearchModuleCellRenderer extends DefaultTreeCellRenderer {

	
	
	private static final long serialVersionUID = 6225304804416378425L;

    protected void checkEnabled(AbstractCombinedComponent component, JTree tree) {
	if (component instanceof AbstractDefineComponent
		&& tree instanceof CombinedModuleTree) {
	    AbstractDefineComponent abstractDefineComponent = (AbstractDefineComponent) component;
	    CombinedModuleTree moduleTree = (CombinedModuleTree) tree;
	    if (abstractDefineComponent.getModuleId() == moduleTree
		    .getSelectedModuleId()
		    || abstractDefineComponent.getModuleId() == -1L) {
		setEnabled(false);
		return;
	    }
	}
	setEnabled(true);
    }

    protected ImageIcon determineAbstractDefineComponentIcon(
	    AbstractDefineComponent component) {
	ImageIcon icon = new ImageIcon();
	if (component instanceof ComponentComponent) {
	    icon = new ImageIcon(Resource.get(Resource.ICON_COMPONENT));
	} else if (component instanceof ExternalLibraryComponent) {
	    icon = new ImageIcon(Resource.get(Resource.ICON_EXTERNALLIB_BLUE));
	} else if (component instanceof LayerComponent) {
	    icon = new ImageIcon(Resource.get(Resource.ICON_LAYER));
	} else if (component instanceof SubSystemComponent) {
	    icon = new ImageIcon(Resource.get(Resource.ICON_SUBSYSTEM));
	} else if (component instanceof SoftwareArchitectureComponent) {
	    icon = new ImageIcon(
		    Resource.get(Resource.ICON_SOFTWARE_ARCHITECTURE));
	} else if (component instanceof FacadeComponent) {
	    icon = new ImageIcon(Resource.get(Resource.ICON_FACADE));
	}
	return icon;
    }

    protected ImageIcon determineAnalyzedModuleComponentIcon(
	    AnalyzedModuleComponent component) {
	ImageIcon icon = new ImageIcon();
	if (component.getType().equals("PACKAGE")) {
	    if (component.isMapped()) {
		icon = new ImageIcon(Resource.get(Resource.ICON_PACKAGE_EMPTY));
	    } else {
		icon = new ImageIcon(Resource.get(Resource.ICON_PACKAGE));
	    }

	} else if (component.getType().equals("CLASS")) {
	    if (component.isMapped()) {
		icon = new ImageIcon(
			Resource.get(Resource.ICON_CLASS_PUBLIC_GRAY));
	    } else {
		icon = new ImageIcon(Resource.get(Resource.ICON_CLASS_PUBLIC));
	    }

	} else if (component.getType().equals("INTERFACE")) {
	    if (component.isMapped()) {
		icon = new ImageIcon(
			Resource.get(Resource.ICON_INTERFACE_PUBLIC_GRAY));
	    } else {
		icon = new ImageIcon(
			Resource.get(Resource.ICON_INTERFACE_PUBLIC));
	    }

	} else if (component.getType().equals("ENUMERATION")) {
	    if (component.isMapped()) {
		icon = new ImageIcon(
			Resource.get(Resource.ICON_ENUMERATION_GRAY));
	    } else {
		icon = new ImageIcon(Resource.get(Resource.ICON_ENUMERATION));
	    }

	} else if (component.getType().equals("EXTERNALLIBRARY")) {
	    if (component.isMapped()) {
		icon = new ImageIcon(
			Resource.get(Resource.ICON_EXTERNALLIB_GRAY));
	    } else {
		icon = new ImageIcon(Resource.get(Resource.ICON_EXTERNALLIB_GREEN));
	    }

	} else if (component.getType().equals("SUBSYSTEM")) {
	    if (component.isMapped()) {
		icon = new ImageIcon(
			Resource.get(Resource.ICON_SUBSYSTEMJAVA_GRAY));
	    } else {
		icon = new ImageIcon(Resource.get(Resource.ICON_SUBSYSTEMJAVA));
	    }

	} else if (component.getType().equals("ROOT")) {
	    icon = new ImageIcon(Resource.get(Resource.ICON_ROOT));
	} else if (component.getType().equals("REGEX")) {
	    icon = new ImageIcon(Resource.get(Resource.ICON_REGEX));
	} else if (component.getType().equals("SEARCH")) {
	    icon = new ImageIcon(Resource.get(Resource.ICON_REGEXSEARCH));
	} else if (component.getType().toLowerCase().equals("externalpackage")) {
	    if (component.isMapped()) {
		icon = new ImageIcon(
			Resource.get(Resource.ICON_EXTERNALLIB_GRAY));
	    } else {
		icon = new ImageIcon(Resource.get(Resource.ICON_EXTERNALLIB_GREEN));
	    }

	}

	return icon;
    }

    protected void determineIcon(AbstractCombinedComponent component) {
	if (component instanceof AnalyzedModuleComponent) {
	    AnalyzedModuleComponent analyzedModuleComponent = (AnalyzedModuleComponent) component;
	    setIcon(determineAnalyzedModuleComponentIcon(analyzedModuleComponent));
	} else if (component instanceof AbstractDefineComponent) {
	    AbstractDefineComponent abstractDefineComponent = (AbstractDefineComponent) component;
	    setIcon(determineAbstractDefineComponentIcon(abstractDefineComponent));
	}
    }

    /**
     * only TreeCellRenderer method Compute the String to display, and pass it
     * to the wrapped renderer
     */
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
	    boolean selected, boolean expanded, boolean leaf, int row,
	    boolean hasFocus) {
	if (value instanceof AbstractCombinedComponent) {
	    AbstractCombinedComponent component = (AbstractCombinedComponent) value;
	    super.getTreeCellRendererComponent(tree, component.getUniqueName(),
		    selected, expanded, leaf, row, hasFocus);
	    determineIcon(component);
	    checkEnabled(component, tree);
	}
	return this;
    }
}
