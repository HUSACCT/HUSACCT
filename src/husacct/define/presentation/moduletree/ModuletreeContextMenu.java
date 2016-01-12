package husacct.define.presentation.moduletree;

import husacct.define.presentation.jpanel.ModuleJPanel;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

public class ModuletreeContextMenu extends JPopupMenu {

    private static final long serialVersionUID = 1L;
    private JMenuItem newModule, removeModule, moveUp, moveDown;
    private JPanel parentPanel;

    private AbstractAction createNewModuleAction = new AbstractAction() {
		private static final long serialVersionUID = 648949586322816554L;
		@Override
		public void actionPerformed(ActionEvent arg0) {
		    if (parentPanel instanceof ModuleJPanel) {
			((ModuleJPanel) parentPanel).newModule();
		    }
		}
    };

    private AbstractAction moveDownAction = new AbstractAction() {
		private static final long serialVersionUID = -1396467062324885098L;
		@Override
		public void actionPerformed(ActionEvent a) {
		    if (parentPanel instanceof ModuleJPanel) {
			((ModuleJPanel) parentPanel).moveLayerDown();
		    }
		}
    };

    private AbstractAction moveUpAction = new AbstractAction() {
		private static final long serialVersionUID = 5859872060456988463L;
		@Override
		public void actionPerformed(ActionEvent a) {
		    if (parentPanel instanceof ModuleJPanel) {
			((ModuleJPanel) parentPanel).moveLayerUp();
		    }
		}
    };

    private AbstractAction removeModuleAction = new AbstractAction() {
		private static final long serialVersionUID = 3393078113848071817L;
		@Override
		public void actionPerformed(ActionEvent a) {
		    if (parentPanel instanceof ModuleJPanel) {
			((ModuleJPanel) parentPanel).removeModule();
		    }
		}
    };

    public ModuletreeContextMenu(JPanel parentPanel) {
		this.parentPanel = parentPanel;
		newModule = new JMenuItem("new module");
		removeModule = new JMenuItem("remove module");
		moveUp = new JMenuItem("level up");
		moveUp.addActionListener(moveUpAction);
		moveDown = new JMenuItem("level down");
		moveDown.addActionListener(moveDownAction);
		newModule.addActionListener(createNewModuleAction);
		removeModule.addActionListener(removeModuleAction);
    }

    public void isComponent() {
		this.add(newModule);
		this.add(removeModule);
    }

    public void isLayer() {
		isComponent();
		this.add(moveUp);
		this.add(moveDown);
    }
}
