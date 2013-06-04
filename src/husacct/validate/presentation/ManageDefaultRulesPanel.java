package husacct.validate.presentation;

import husacct.validate.domain.validation.module.ModuleTypes;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class ManageDefaultRulesPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JLabel test;
	private final DefaultListModel<DataLanguageHelper> rtsComponentModel;
	private JList<DataLanguageHelper> components;
	private JScrollPane componentScrollpane;

	// ==================================
	// Init panel
	// ==================================
	public ManageDefaultRulesPanel(){
		// Tmp test junk
		test = new JLabel("Tmp.");
		

		componentScrollpane = new JScrollPane();
		components = new JList<DataLanguageHelper>();
		
		rtsComponentModel = new DefaultListModel<DataLanguageHelper>();
		
		for(ModuleTypes modules : ModuleTypes.values()){
			rtsComponentModel.addElement(new DataLanguageHelper(modules.toString()));
		}
		if (!rtsComponentModel.isEmpty()) {
			components.setSelectedIndex(0);
		}

		components.setModel(rtsComponentModel);
		components.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		componentScrollpane.setViewportView(components);
		
		// Create layout
		GroupLayout layout = new GroupLayout(this);
		
		layout.setHorizontalGroup(
			layout.createSequentialGroup()
				.addComponent(componentScrollpane, 200, 200, 200)
				.addComponent(test)
		);
		layout.setVerticalGroup(
			layout.createSequentialGroup()
			.addGroup(
				layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(componentScrollpane)
					.addComponent(test)
			)
		);
		
		// Merge layout
		this.setLayout(layout);
	}
}