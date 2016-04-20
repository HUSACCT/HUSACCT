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
import sun.misc.Service;

import javax.swing.JTabbedPane;
import javax.swing.border.LineBorder;
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
import java.util.Map;

import javax.swing.SwingConstants;
import javax.swing.ScrollPaneConstants;

public class ApproachesTableJPanel extends HelpableJPanel {
	private static final long serialVersionUID = 1L;
	public JTable approachesTable;
	private TableColumnModel tableColumnModule;
	public JPanel optionsPanel;
	public ButtonGroup RadioButtonsRelationType;
	public ButtonGroup radioButtonGroupTwo;
	private ILocaleService localService;
	/**
	 * Create the panel.
	 */
	public ApproachesTableJPanel() {
		super();
		localService = ServiceProvider.getInstance().getLocaleService();
		initUI();
	}
	
	public void initUI(){
		setLayout(new BorderLayout(0, 0));		
		optionsPanel = new JPanel();
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
		tabbedPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		add(tabbedPane);
				
		String approachesTranslation = localService.getTranslatedString("Approaches");
		String thresholdTranslation = localService.getTranslatedString("Threshold");
		Object columnNames[] = {"ApproachConstants", approachesTranslation, thresholdTranslation};
		Object rows[][] = getApproachesRows();
		approachesTable = new JTable(rows, columnNames);
		approachesTable.setMinimumSize(new Dimension(600,150));
		tableColumnModule = approachesTable.getColumnModel();
		hide("ApproachConstants");
		Dimension tableSize = approachesTable.getPreferredSize();
		
		JPanel approachedPane = new JPanel();
		JScrollPane scrollPane = new JScrollPane(approachesTable);
		scrollPane.setPreferredSize(new Dimension(tableSize.width, approachesTable.getRowHeight()*approachesTable.getRowCount()+50));
		tabbedPane.addTab("Basic", null, approachedPane, null);
		approachedPane.setLayout(new BorderLayout(0, 0));
		approachedPane.add(scrollPane, BorderLayout.NORTH);
		
		approachedPane.add(optionsPanel, BorderLayout.CENTER);
		GridBagLayout gbl_optionsPanel = new GridBagLayout();
		gbl_optionsPanel.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_optionsPanel.rowHeights = new int[]{0, 0, 0};
		gbl_optionsPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_optionsPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		optionsPanel.setLayout(gbl_optionsPanel);
		
		initRadioButtons();
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Generic", null, panel, null);
	}
	
	public void initRadioButtons(){
		JRadioButton allDependenciesRadioButton = new JRadioButton("All Dependencies");
		allDependenciesRadioButton.setHorizontalAlignment(SwingConstants.CENTER);
		allDependenciesRadioButton.setActionCommand(RelationTypes.allDependencies);
		GridBagConstraints gbc_allDependenciesRadioButton = new GridBagConstraints();
		gbc_allDependenciesRadioButton.anchor = GridBagConstraints.WEST;
		gbc_allDependenciesRadioButton.insets = new Insets(0, 0, 5, 5);
		gbc_allDependenciesRadioButton.gridx = 0;
		gbc_allDependenciesRadioButton.gridy = 0;
		
		JRadioButton umlLinksRadioButton = new JRadioButton("Uml Links");
		umlLinksRadioButton.setActionCommand(RelationTypes.umlLinks);
		GridBagConstraints gbc_umlLinksRadioButton = new GridBagConstraints();
		gbc_umlLinksRadioButton.anchor = GridBagConstraints.WEST;
		gbc_umlLinksRadioButton.insets = new Insets(0, 0, 5, 5);
		gbc_umlLinksRadioButton.gridx = 2;
		gbc_umlLinksRadioButton.gridy = 0;
		
		JRadioButton accessCallReferenceDependenciesRadioButton = new JRadioButton("Dependencies (Acces, Call or Reference only)");
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

	private Object[][] getApproachesRows(){
		
				
		Object ApproachesRows[][] = { 
			{Algorithm.Layers_Goldstein_Multiple_Improved, getTranslation(Algorithm.Layers_Goldstein_Multiple_Improved), 10}, 
			{Algorithm.Layers_Goldstein_Original, getTranslation(Algorithm.Layers_Goldstein_Original), 10}, 
			{Algorithm.Layers_Goldstein_Root_Improved, getTranslation(Algorithm.Layers_Goldstein_Root_Improved), 10},
				
			{Algorithm.Layers_Scanniello_Original, getTranslation(Algorithm.Layers_Scanniello_Original), 10}, 
			{Algorithm.Layers_Scanniello_Improved, getTranslation(Algorithm.Layers_Scanniello_Improved), 10}, 
				
			{Algorithm.Component_HUSACCT_SelectedModule, getTranslation(Algorithm.Component_HUSACCT_SelectedModule), 10},
			{Algorithm.Gateways_HUSACCT_Root, "Gateways HUSACCT Root", 10}};
		return ApproachesRows;
	}
	
	private String getTranslation(String translationKey){
		ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
		return localeService.getTranslatedString(translationKey);
	}
	
    private void hide(String columnName) {
        int index = tableColumnModule.getColumnIndex(columnName);
        TableColumn column = tableColumnModule.getColumn(index);
        tableColumnModule.removeColumn(column);
    }
	
}
