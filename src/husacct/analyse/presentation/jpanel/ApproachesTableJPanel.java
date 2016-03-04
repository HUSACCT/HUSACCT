package husacct.analyse.presentation.jpanel;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

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

public class ApproachesTableJPanel extends HelpableJPanel {
	public JTable approachesTable;
	public JPanel optionsPanel;

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
		gbl_optionsPanel.rowHeights = new int[]{0, 0};
		gbl_optionsPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_optionsPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		optionsPanel.setLayout(gbl_optionsPanel);
		
		initRadioButtons();
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Generic", null, panel, null);
	}
	
	public void initRadioButtons(){
		JRadioButton layersAtRootLevelRadioButton = new JRadioButton("Layers At Root Level");
		GridBagConstraints gbc_layersAtRootLevelRadioButton = new GridBagConstraints();
		gbc_layersAtRootLevelRadioButton.insets = new Insets(0, 0, 0, 5);
		gbc_layersAtRootLevelRadioButton.gridx = 0;
		gbc_layersAtRootLevelRadioButton.gridy = 0;
		
		JRadioButton multiLayeredRadioButton = new JRadioButton("Multi-Layered");
		GridBagConstraints gbc_multiLayeredRadioButton = new GridBagConstraints();
		gbc_multiLayeredRadioButton.insets = new Insets(0, 0, 0, 5);
		gbc_multiLayeredRadioButton.gridx = 2;
		gbc_multiLayeredRadioButton.gridy = 0;
		
		JRadioButton layersWithinSelectedModuleRadioButton = new JRadioButton("Layers Within Selected Module");
		GridBagConstraints gbc_layersWithinSelectedModuleRadioButton = new GridBagConstraints();
		gbc_layersWithinSelectedModuleRadioButton.insets = new Insets(0, 0, 0, 5);
		gbc_layersWithinSelectedModuleRadioButton.gridx = 4;
		gbc_layersWithinSelectedModuleRadioButton.gridy = 0;
		
		ButtonGroup radioButtonGroup = new ButtonGroup();
		radioButtonGroup.add(layersAtRootLevelRadioButton);
		radioButtonGroup.add(multiLayeredRadioButton);
		radioButtonGroup.add(layersWithinSelectedModuleRadioButton);
		
		optionsPanel.add(layersWithinSelectedModuleRadioButton, gbc_layersWithinSelectedModuleRadioButton);
		optionsPanel.add(multiLayeredRadioButton, gbc_multiLayeredRadioButton);
		optionsPanel.add(layersAtRootLevelRadioButton, gbc_layersAtRootLevelRadioButton);
	}

	private Object[][] getApproachesRows(){
		Object ApproachesRows[][] = { { "layerApproach", 10}, { "selectedModuleApproach", 11}, { "Third Approach", 12}};
		return ApproachesRows;
	}
	
}
