package husacct.analyse.presentation.jpanel;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import husacct.analyse.infrastructure.antlr.java.JavaParser.classExtendsClause_return;
import husacct.common.help.presentation.HelpableJPanel;
import java.awt.FlowLayout;
import javax.swing.JTabbedPane;
import javax.swing.border.LineBorder;
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
	public JTable approachesTable;
	public JPanel optionsPanel;
	public ButtonGroup radioButtonGroup;
	public ButtonGroup radioButtonGroupTwo;

	/**
	 * Create the panel.
	 */
	public ApproachesTableJPanel() {
		super();
		initUI();
	}
	
	public void initUI(){
		setLayout(new BorderLayout(0, 0));
		
		optionsPanel = new JPanel();
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		add(tabbedPane);
				
		Object columnNames[] = { "Approaches", "threshold"};
		Object rows[][] = getApproachesRows();
		

		approachesTable = new JTable(rows, columnNames);
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
		allDependenciesRadioButton.setActionCommand("AllDependencies");
		GridBagConstraints gbc_allDependenciesRadioButton = new GridBagConstraints();
		gbc_allDependenciesRadioButton.anchor = GridBagConstraints.WEST;
		gbc_allDependenciesRadioButton.insets = new Insets(0, 0, 5, 5);
		gbc_allDependenciesRadioButton.gridx = 0;
		gbc_allDependenciesRadioButton.gridy = 0;
		
		JRadioButton umlLinksRadioButton = new JRadioButton("Uml Links");
		umlLinksRadioButton.setActionCommand("UmlLinks");
		GridBagConstraints gbc_umlLinksRadioButton = new GridBagConstraints();
		gbc_umlLinksRadioButton.anchor = GridBagConstraints.WEST;
		gbc_umlLinksRadioButton.insets = new Insets(0, 0, 5, 5);
		gbc_umlLinksRadioButton.gridx = 2;
		gbc_umlLinksRadioButton.gridy = 0;
		
		JRadioButton accessCallReferenceDependenciesRadioButton = new JRadioButton("Dependencies (Acces, Call or Reference only)");
		accessCallReferenceDependenciesRadioButton.setActionCommand("AccessCallReferenceDependencies");
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
		
		radioButtonGroup = new ButtonGroup();
		radioButtonGroupTwo = new ButtonGroup();
		
		radioButtonGroup.add(allDependenciesRadioButton);
		radioButtonGroup.add(umlLinksRadioButton);
		radioButtonGroup.add(accessCallReferenceDependenciesRadioButton);
		
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
				{"Goldstein - rootApproach", 10}, 
				{ "Goldstein - multipleLayerApproach", 10}, 
				{ "Goldstein - selectedModuleApproach", 11}, 
				{ "Goldstein - upgradedRootApproach", 10},
				{ "Scanniello - originalRoot", 10}, 
				{ "Scanniello - improvedRoot", 10}, 
				{ "Scanniello - selectedModuleApproach", 12}, 
				{"Component recognition", 10}};
		return ApproachesRows;
	}
	
}
