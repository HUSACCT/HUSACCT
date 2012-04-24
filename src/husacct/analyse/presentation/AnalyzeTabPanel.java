package husacct.analyse.presentation;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

public class AnalyzeTabPanel extends JTabbedPane{
	private static final long serialVersionUID = 1L;

	public AnalyzeTabPanel() {

		AnalyzePanelStart tabStart = new AnalyzePanelStart();
		AnalyzePanelDependencyOverview tabDependencyOverview = new AnalyzePanelDependencyOverview();

		addTab("Start", tabStart);
		//addTab("Navigator", tabNavigator);
		addTab("Analyse Services", tabDependencyOverview);

		// Analyze Button Panel Start ->  Analyze java Project  
		tabStart.analyseButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent arg0) { setSelectedIndex(1);}
		});
		// getRootDependButton Button Panel-DepOver - show packages in a new JFrame! 
		tabDependencyOverview.getRootDependButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				AnalyzePanelGetRootModules rootModulePanel = new AnalyzePanelGetRootModules(); 
				addTab("getRootModules()",rootModulePanel);
				setSelectedIndex(getTabCount() -1);
			}
 		});
		
		// getDependencyPackage Button Panel-DepOver - Get all dependency's from 
		tabDependencyOverview.getDependencyFrom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				AnalyzePanelGetDependencyFrom rootModulePanel = new AnalyzePanelGetDependencyFrom();
				addTab("getDependencyFrom(from)",rootModulePanel);
				setSelectedIndex(getTabCount() -1);
			}
 		});
	
		// getDependencyPackage Button Panel-DepOver - Get all dependency's from  en TO
		tabDependencyOverview.getDependencyFromTo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				AnalyzePanelGetDependencyFromTo rootModulePanel = new AnalyzePanelGetDependencyFromTo();
				addTab("getDependency(from, to)",rootModulePanel);
				setSelectedIndex(getTabCount() -1);
			}
 		});
		
		// getDependencyPackage Button Panel-DepOver - Get all dependency's TO
				tabDependencyOverview.getDependencyTo.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						AnalyzePanelGetDependencyTo rootModulePanel = new AnalyzePanelGetDependencyTo();
						addTab("getDependencyTo( to)",rootModulePanel);
						setSelectedIndex(getTabCount() -1);
					}
		 		});
 	} 

}
