package husacct.analyse.presentation.jpanel;

import javax.swing.JComponent;
import javax.swing.JPanel;

import husacct.common.help.presentation.HelpableJPanel;
import java.awt.FlowLayout;
import javax.swing.JTabbedPane;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.BorderLayout;
import javax.swing.JTable;

public class ApproachesTableJPanel extends HelpableJPanel {
	private JTable table;

	/**
	 * Create the panel.
	 */
	public ApproachesTableJPanel() {
		super();
		initUI();
	}
	
	public void initUI(){
		setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		add(tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Basic", null, panel, null);
		
		table = new JTable();
		panel.add(table);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Generic", null, panel_1, null);
	}

}
