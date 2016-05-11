package husacct.analyse.presentation.reconstruct;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import husacct.ServiceProvider;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants.Algorithm;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants.RelationTypes;
import husacct.common.help.presentation.HelpableJPanel;
import husacct.common.locale.ILocaleService;

import javax.swing.JTabbedPane;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.BorderLayout;

import javax.swing.JTable;

import java.awt.GridBagLayout;

import javax.swing.JRadioButton;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.SwingConstants;

public class ApproachesTableJPanel extends HelpableJPanel {
	private static final long serialVersionUID = 1L;
	private TableColumnModel tableAllApproachesColumnModel;
	private TableColumnModel tableDistinctApproachesColumnModel;
	private ILocaleService localService;
	private String approachesConstants = AnalyseReconstructConstants.ApproachesTable.ApproachesConstants;
	private ReconstructJPanel panel;
	public JTabbedPane tabbedPane;
	public JTable tableAllApproaches;
	public JTable tableDistinctApproaches;
	public JPanel optionsPanel;
	public ButtonGroup RadioButtonsRelationType;
	public ButtonGroup radioButtonGroupTwo;

	/**
	 * Create the panel.
	 */
	public ApproachesTableJPanel(ReconstructJPanel panel) {
		super();
		localService = ServiceProvider.getInstance().getLocaleService();
		initUI();
		this.panel = panel;
	}
	
	public void initUI(){
		setLayout(new BorderLayout(0, 10));		
		optionsPanel = new JPanel();
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
		tabbedPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		tabbedPane.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e){
				if(panel != null){
					JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
					int selectedIndex = tabbedPane.getSelectedIndex();
					if(selectedIndex == 2){
						panel.setButtonVisibility(false);
					}
					else{
						panel.setButtonVisibility(true);
					}
				}
			}
			
		});
		add(tabbedPane);
				

		Object columnNames[] = getColumnNames();
		Object rows[][] = getAllApproachesRows();
		tableAllApproaches = new JTable(rows, columnNames);
		tableAllApproaches.setMinimumSize(new Dimension(600,150));
		tableAllApproachesColumnModel = tableAllApproaches.getColumnModel();
		hide(approachesConstants, tableAllApproachesColumnModel);
		Dimension tableSize = tableAllApproaches.getPreferredSize();
		
		JPanel allApproachedPanel = new JPanel();
		allApproachedPanel.setName(AnalyseReconstructConstants.ApproachesTable.PanelAllApproaches);
		JScrollPane scrollPane = new JScrollPane(tableAllApproaches);
		int tableheight = tableSize.height + (tableSize.height/tableAllApproaches.getRowCount()+5);
		scrollPane.setPreferredSize(new Dimension(tableSize.width, tableheight));
		allApproachedPanel.setLayout(new BorderLayout(0, 0));
		allApproachedPanel.add(scrollPane, BorderLayout.NORTH);
		
		allApproachedPanel.add(optionsPanel, BorderLayout.CENTER);
		GridBagLayout gbl_optionsPanel = new GridBagLayout();
		gbl_optionsPanel.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_optionsPanel.rowHeights = new int[]{0, 0, 0};
		gbl_optionsPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_optionsPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		optionsPanel.setLayout(gbl_optionsPanel);
		
		initRadioButtons();
		
		/*
		 * Generic tab
		 */
		
		JPanel distinctApproachesPanel = new JPanel();
		distinctApproachesPanel.setName(AnalyseReconstructConstants.ApproachesTable.PanelDistinctApproaches);
		distinctApproachesPanel.setLayout(new BorderLayout(0, 0));
		
		Object distinctApproachesColumns[] = getColumnNames();
		Object distinctApproachesRows[][] = getDistinctApproachesRows();
		tableDistinctApproaches = new JTable(distinctApproachesRows, distinctApproachesColumns);
		tableDistinctApproaches.setMinimumSize(new Dimension(600,150));
		tableDistinctApproachesColumnModel = tableDistinctApproaches.getColumnModel();
		hide(approachesConstants, tableDistinctApproachesColumnModel);
		Dimension distictApproachesTableSize = tableDistinctApproaches.getPreferredSize();
		
		JScrollPane distinctScrollPane = new JScrollPane(tableDistinctApproaches);
		distinctScrollPane.setPreferredSize(new Dimension(distictApproachesTableSize.width, tableDistinctApproaches.getRowHeight()*tableDistinctApproaches.getRowCount()+50));
		
		distinctApproachesPanel.setLayout(new BorderLayout(0, 0));
		distinctApproachesPanel.add(distinctScrollPane, BorderLayout.CENTER);
		
		
		String distinctApprTranslation = getTranslation(AnalyseReconstructConstants.ApproachesTable.PanelDistinctApproaches);
		String allApprTranslation = getTranslation(AnalyseReconstructConstants.ApproachesTable.PanelAllApproaches);
		
		MojoJPanel mojoPanel = new MojoJPanel(/*analyseTaskControl*/);
		tabbedPane.addTab(distinctApprTranslation, null, distinctApproachesPanel, null);
		tabbedPane.addTab(allApprTranslation, null, allApproachedPanel, null);
		tabbedPane.addTab("Mojo", null, mojoPanel.createMojoPanel(), null);
	}
	
	public void initRadioButtons(){
		String allDependenciesTranlation = getTranslation(RelationTypes.allDependencies);
		JRadioButton allDependenciesRadioButton = new JRadioButton(allDependenciesTranlation);
		allDependenciesRadioButton.setHorizontalAlignment(SwingConstants.CENTER);
		allDependenciesRadioButton.setActionCommand(RelationTypes.allDependencies);
		allDependenciesRadioButton.setSelected(true);
		GridBagConstraints gbc_allDependenciesRadioButton = new GridBagConstraints();
		gbc_allDependenciesRadioButton.anchor = GridBagConstraints.WEST;
		gbc_allDependenciesRadioButton.insets = new Insets(0, 0, 5, 5);
		gbc_allDependenciesRadioButton.gridx = 0;
		gbc_allDependenciesRadioButton.gridy = 0;
		
		String umlLinkTranslation = getTranslation(RelationTypes.umlLinks);
		JRadioButton umlLinksRadioButton = new JRadioButton(umlLinkTranslation);
		umlLinksRadioButton.setActionCommand(RelationTypes.umlLinks);
		GridBagConstraints gbc_umlLinksRadioButton = new GridBagConstraints();
		gbc_umlLinksRadioButton.anchor = GridBagConstraints.WEST;
		gbc_umlLinksRadioButton.insets = new Insets(0, 0, 5, 5);
		gbc_umlLinksRadioButton.gridx = 2;
		gbc_umlLinksRadioButton.gridy = 0;
		
		String acrdTranlation = getTranslation(RelationTypes.accessCallReferenceDependencies);
		JRadioButton accessCallReferenceDependenciesRadioButton = new JRadioButton(acrdTranlation);
		accessCallReferenceDependenciesRadioButton.setActionCommand(RelationTypes.accessCallReferenceDependencies);
		GridBagConstraints gbc_accessCallReferenceDependenciesRadioButton = new GridBagConstraints();
		gbc_accessCallReferenceDependenciesRadioButton.anchor = GridBagConstraints.WEST;
		gbc_accessCallReferenceDependenciesRadioButton.insets = new Insets(0, 0, 5, 5);
		gbc_accessCallReferenceDependenciesRadioButton.gridx = 4;
		gbc_accessCallReferenceDependenciesRadioButton.gridy = 0;
		
		JRadioButton packagesInRootRadioButton = new JRadioButton("Packages in Root (With classes only)");
		packagesInRootRadioButton.setActionCommand("PackagesInRoot");
		GridBagConstraints gbc_packagesInRootRadioButton = new GridBagConstraints();
		gbc_packagesInRootRadioButton.anchor = GridBagConstraints.WEST;
		gbc_packagesInRootRadioButton.insets = new Insets(0, 0, 0, 5);
		gbc_packagesInRootRadioButton.gridx = 0;
		gbc_packagesInRootRadioButton.gridy = 1;
		
		
		JRadioButton packagesAllWithClassesRadioButton = new JRadioButton("Packages (All with classes)");
		packagesAllWithClassesRadioButton.setActionCommand("PackagesAllWithClasses");
		GridBagConstraints gbc_packagesAllWithClassesRadioButton = new GridBagConstraints();
		gbc_packagesAllWithClassesRadioButton.anchor = GridBagConstraints.WEST;
		gbc_packagesAllWithClassesRadioButton.insets = new Insets(0, 0, 0, 5);
		gbc_packagesAllWithClassesRadioButton.gridx = 2;
		gbc_packagesAllWithClassesRadioButton.gridy = 1;
		
		JRadioButton classesRadioButton = new JRadioButton("Classes");
		classesRadioButton.setActionCommand("Classes");
		GridBagConstraints gbc_ClassesRadioButton = new GridBagConstraints();
		gbc_ClassesRadioButton.anchor = GridBagConstraints.WEST;
		gbc_ClassesRadioButton.insets = new Insets(0, 0, 0, 5);
		gbc_ClassesRadioButton.gridx = 4;
		gbc_ClassesRadioButton.gridy = 1;
		
		RadioButtonsRelationType = new ButtonGroup();
		radioButtonGroupTwo = new ButtonGroup();
		
		RadioButtonsRelationType.add(allDependenciesRadioButton);
		RadioButtonsRelationType.add(umlLinksRadioButton);
		RadioButtonsRelationType.add(accessCallReferenceDependenciesRadioButton);
		
		radioButtonGroupTwo.add(packagesInRootRadioButton);
		radioButtonGroupTwo.add(packagesAllWithClassesRadioButton);
		radioButtonGroupTwo.add(classesRadioButton);
		
		optionsPanel.add(allDependenciesRadioButton, gbc_allDependenciesRadioButton);
		optionsPanel.add(umlLinksRadioButton, gbc_umlLinksRadioButton);
		optionsPanel.add(accessCallReferenceDependenciesRadioButton, gbc_accessCallReferenceDependenciesRadioButton);
		
		optionsPanel.add(packagesInRootRadioButton, gbc_packagesInRootRadioButton);
		optionsPanel.add(packagesAllWithClassesRadioButton, gbc_packagesAllWithClassesRadioButton);
		optionsPanel.add(classesRadioButton, gbc_ClassesRadioButton);
	}

	private Object[][] getAllApproachesRows(){
		Object ApproachesRows[][] = { 
			{Algorithm.Component_HUSACCT_SelectedModule, getTranslation(Algorithm.Component_HUSACCT_SelectedModule), 10},
			{Algorithm.Gateways_HUSACCT_Root, getTranslation(Algorithm.Gateways_HUSACCT_Root), 10},
			{Algorithm.Externals_Recognition, getTranslation(Algorithm.Externals_Recognition), 10},

			{Algorithm.Layers_Goldstein_Multiple_Improved, getTranslation(Algorithm.Layers_Goldstein_Multiple_Improved), 10}, 
			{Algorithm.Layers_Goldstein_Original, getTranslation(Algorithm.Layers_Goldstein_Original), 10}, 
			{Algorithm.Layers_Goldstein_Root_Improved, getTranslation(Algorithm.Layers_Goldstein_Root_Improved), 10},
				
			{Algorithm.Layers_Scanniello_Original, getTranslation(Algorithm.Layers_Scanniello_Original), 10}, 
			{Algorithm.Layers_Scanniello_Improved, getTranslation(Algorithm.Layers_Scanniello_Improved), 10}};
				
		return ApproachesRows;
	}
	
	private Object[][] getDistinctApproachesRows(){
		Object ApproachesRows[][] = { 
				{Algorithm.Component_HUSACCT_SelectedModule, getTranslation(Algorithm.Component_HUSACCT_SelectedModule), 10},
				{Algorithm.Externals_Recognition, getTranslation(Algorithm.Externals_Recognition), 10},
				{Algorithm.Layers_Goldstein_Original, getTranslation(Algorithm.Layers_Goldstein_Original), 10},
				{Algorithm.Layers_Scanniello_Improved, getTranslation(Algorithm.Layers_Scanniello_Improved), 10}};
			return ApproachesRows;
	}
	
	
	
	private Object[] getColumnNames(){
		String approachesTranslation = localService.getTranslatedString("Approaches");
		String thresholdTranslation = localService.getTranslatedString("Threshold");
	
		Object columnNames[] = {
				approachesConstants, 
				approachesTranslation, 
				thresholdTranslation
		};
		return columnNames;
	}
	
	private String getTranslation(String translationKey){
		ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
		return localeService.getTranslatedString(translationKey);
	}
	
    private void hide(String columnName, TableColumnModel tableColumnModel) {
        int index = tableColumnModel.getColumnIndex(columnName);
        TableColumn column = tableColumnModel.getColumn(index);
        tableColumnModel.removeColumn(column);
    }
	
}
