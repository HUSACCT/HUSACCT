package husacct.analyse.presentation.jpanel;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import husacct.common.help.presentation.HelpableJPanel;
import java.awt.FlowLayout;
import javax.swing.JTabbedPane;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.BorderLayout;
import javax.swing.JTable;

public class ApproachesTableJPanel extends HelpableJPanel {
	public JTable table;

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
				
		Object columnNames[] = { "Approaches", "threshold"};
		Object rows[][] = getApproachesRows();
		table = new JTable(rows, columnNames);
		tabbedPane.addTab("Basic", null, new JScrollPane(table), null);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Generic", null, panel_1, null);
	}

	private Object[][] getApproachesRows(){
		Object ApproachesRows[][] = { { "layerApproach", 10}, { "selectedModuleApproach", 11}, { "Third Approach", 12}};
		return ApproachesRows;
	}
	
}
