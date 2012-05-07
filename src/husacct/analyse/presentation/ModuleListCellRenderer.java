package husacct.analyse.presentation;

import husacct.common.dto.AnalysedModuleDTO;
import java.awt.Color;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;

class ModuleListCellRenderer extends JLabel implements ListCellRenderer{

	private static final long serialVersionUID = 1L;
	private static final Color HIGHLIGHT = UIManager.getColor("Table.sortIconColor");
	private static final Color NORMAL = UIManager.getColor("Panel.background");
	
	public ModuleListCellRenderer(){
		setOpaque(true);
		setIconTextGap(10);
	}
	
	@Override
	public Component getListCellRendererComponent(JList childs, Object value, int index, boolean selected, boolean hasFocus) {
		AnalysedModuleDTO module = (AnalysedModuleDTO) value;
		setText(module.uniqueName);
		setIcon(new ImageIcon("img/module.png"));
		if(selected)setBackground(HIGHLIGHT);
		else setBackground(NORMAL);
		return this;
	}
}
