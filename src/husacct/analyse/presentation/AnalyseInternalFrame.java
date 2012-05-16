package husacct.analyse.presentation;

import husacct.analyse.abstraction.language.AnalyseTranslater;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import java.awt.BorderLayout;

public class AnalyseInternalFrame extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	private ApplicationStructurePanel treePanel;
	private DependencyPanel dependencyPanel;
	private JTabbedPane tabPanel;

	public AnalyseInternalFrame() {
		setTitle(AnalyseTranslater.getValue("WindowTitle"));
		setResizable(true);
		setBounds(200, 200, 550, 400);
		setFrameIcon(new ImageIcon("husacct/analyse/presentation/resources/husacct.png"));
		
		tabPanel = new JTabbedPane(JTabbedPane.TOP);
		tabPanel.setBackground(UIManager.getColor("Panel.background"));
		getContentPane().add(tabPanel, BorderLayout.CENTER);
		
		setIconifiable(true);
		setVisible(true);
		
		treePanel = new ApplicationStructurePanel();
		dependencyPanel = new DependencyPanel();
		tabPanel.addTab(AnalyseTranslater.getValue("SourceOverview"), null, treePanel, null);
		tabPanel.addTab(AnalyseTranslater.getValue("DependencyOverview"), null, dependencyPanel, null);
	}
	
	public void reloadText(){
		setTitle(AnalyseTranslater.getValue("WindowTitle"));
		tabPanel.setTitleAt(0, AnalyseTranslater.getValue("SourceOverview"));
		tabPanel.setTitleAt(1, AnalyseTranslater.getValue("DependencyOverview"));
		dependencyPanel.reload();
		treePanel.reload();
		tabPanel.repaint();
		this.repaint();
	}
}
