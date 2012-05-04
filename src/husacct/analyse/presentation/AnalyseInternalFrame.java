package husacct.analyse.presentation;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import javax.swing.JPanel;

public class AnalyseInternalFrame extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	private JPanel treePanel, dependencyPanel;

	public AnalyseInternalFrame() {
		super("Analysed Application", true, true, false, true);
		setBounds(200, 200, 550, 400);

		setFrameIcon(new ImageIcon("img/husacct.png"));
		
		JTabbedPane tabPanel = new JTabbedPane(JTabbedPane.TOP);
		tabPanel.setBackground(UIManager.getColor("Panel.background"));
		getContentPane().add(tabPanel, BorderLayout.CENTER);
		
		treePanel = new ApplicationStructurePanel();
		tabPanel.addTab("Source Overview", null, treePanel, null);
		
		dependencyPanel = new DependencyPanel();
		tabPanel.addTab("Dependency Overview", null, dependencyPanel, null);
		
		setIconifiable(true);
		setTitle("Analysed Application");
		setVisible(true);
	}

}
