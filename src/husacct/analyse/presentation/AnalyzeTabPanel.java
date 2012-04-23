package husacct.analyse.presentation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTabbedPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

public class AnalyzeTabPanel extends JTabbedPane implements	TreeSelectionListener {
	private static final long serialVersionUID = 1L;

	public AnalyzeTabPanel() {

		AnalyzePanelStart tabStart = new AnalyzePanelStart();
		AnalyzePanelNavigator tabNavigator = new AnalyzePanelNavigator();
		AnalyzePanelDependencyOverview tabDependencyOverview = new AnalyzePanelDependencyOverview();

		addTab("Start", tabStart);
		addTab("Navigator", tabNavigator);
		addTab("Dependency Overview", tabDependencyOverview);

		// Analyze Button Panel Start ->  Analyze java Project  
		tabStart.analyseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) { setSelectedIndex(1);}
		});
		// showPackage Button Panel-DepOver - show packages in a new JFrame! 
		tabDependencyOverview.showPackages.addActionListener(new ActionListener() {
 					@Override
					public void actionPerformed(ActionEvent arg0) {
 						AnalyzeFramePackages packageFrame = new AnalyzeFramePackages();
					}
 		});
		
 	}

	public void createTabs() { 
	}

	@Override
	public void valueChanged(TreeSelectionEvent arg0) {
		// TODO Tree bewerkingen ?

	}

}
