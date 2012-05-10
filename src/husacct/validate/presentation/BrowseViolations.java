package husacct.validate.presentation;

import husacct.validate.abstraction.language.ResourceBundles;
import husacct.validate.domain.factory.message.Messagebuilder;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.task.TaskServiceImpl;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Map.Entry;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public final class BrowseViolations extends JInternalFrame {

	private static final long serialVersionUID = -7189769674996804424L;

	private TaskServiceImpl ts;
	private FilterViolations fv;

	private JRadioButton allDependencies, directDependencies,
	indirectDependencies;
	private ButtonGroup dependencyLevel;
	private JCheckBox applyFilter;
	private JLabel dependencies,
	shownViolations, shownViolationsNumber, totalViolation,
	totalViolationNumber;
	private JPanel displayPanel, filterPanel, informationPanel;
	private JButton editFilter;
	private JScrollPane violationPanel;
	private JTable violationTable;
	private DefaultTableModel violationModel;
	private JLabel lineNumberValueLabel;
	private JLabel logicalModulesValueLabel;

	public BrowseViolations(TaskServiceImpl ts) {
		setSize(new Dimension(798, 639));
		this.ts = ts;
		this.fv = new FilterViolations(ts, this);
		initComponents();
		loadGUIText();
		violationTable.doLayout();
	}

	private void initComponents() {

		dependencyLevel = new ButtonGroup();
		violationPanel = new JScrollPane();
		violationTable = new JTable();
		shownViolationsNumber = new JLabel();
		displayPanel = new JPanel();
		dependencies = new JLabel();
		allDependencies = new JRadioButton();
		directDependencies = new JRadioButton();
		indirectDependencies = new JRadioButton();

		violationPanel.setAlignmentX(0.0F);
		violationPanel.setAlignmentY(0.0F);
		violationPanel.setAutoscrolls(true);

		violationTable.setAutoCreateRowSorter(true);
		violationTable.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
		violationTable.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		violationTable.setFillsViewportHeight(true);
		violationTable.getTableHeader().setReorderingAllowed(false);
		violationTable.setRowHeight(35);
		violationPanel.setViewportView(violationTable);
		
		shownViolationsNumber = new JLabel();
		shownViolations = new JLabel();
		totalViolationNumber = new JLabel();
		totalViolation = new JLabel();
		informationPanel = new JPanel();
		informationPanel.setLayout(new GridLayout(0, 2,0,1));
		
		shownViolationsNumber.setText("0");

		dependencyLevel.add(allDependencies);
		allDependencies.setSelected(true);

		dependencyLevel.add(directDependencies);

		dependencyLevel.add(indirectDependencies);

		GroupLayout displayPanelLayout = new GroupLayout(displayPanel);
		displayPanel.setLayout(displayPanelLayout);
		displayPanelLayout.setHorizontalGroup(
				displayPanelLayout.createParallelGroup(
						GroupLayout.Alignment.LEADING).addGroup(displayPanelLayout.
								createSequentialGroup().addContainerGap().addComponent(
										dependencies).addPreferredGap(
												LayoutStyle.ComponentPlacement.UNRELATED).addComponent(
														allDependencies).addGap(24, 24, 24).addComponent(
																directDependencies).addPreferredGap(
																		LayoutStyle.ComponentPlacement.UNRELATED).addComponent(
																				indirectDependencies).addContainerGap(GroupLayout.DEFAULT_SIZE,
																						Short.MAX_VALUE)));
		displayPanelLayout.setVerticalGroup(
				displayPanelLayout.createParallelGroup(
						GroupLayout.Alignment.LEADING).addGroup(displayPanelLayout.
								createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(
										dependencies).addComponent(allDependencies).addComponent(
												directDependencies).addComponent(indirectDependencies)));
		filterPanel = new JPanel();
		editFilter = new JButton();
		applyFilter = new JCheckBox();

		editFilter.setAutoscrolls(true);
		editFilter.setMaximumSize(new Dimension(75, 15));
		editFilter.setMinimumSize(new Dimension(75, 15));
		editFilter.setPreferredSize(new Dimension(75, 15));
		editFilter.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				fv.setVisible(true);
			}
		});

		applyFilter.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent evt) {
				loadAfterViolationsChanged();
			}
		});

		GroupLayout filterPanelLayout = new GroupLayout(filterPanel);
		filterPanelLayout.setHorizontalGroup(
				filterPanelLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(filterPanelLayout.createSequentialGroup()
						.addContainerGap()
						.addGroup(filterPanelLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(editFilter, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
								.addComponent(applyFilter))
								.addContainerGap(92, Short.MAX_VALUE))
				);
		filterPanelLayout.setVerticalGroup(
				filterPanelLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(filterPanelLayout.createSequentialGroup()
						.addContainerGap(11, Short.MAX_VALUE)
						.addComponent(applyFilter)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(editFilter, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
						.addGap(40))
				);
		filterPanel.setLayout(filterPanelLayout);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Violation details", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		GroupLayout layout = new GroupLayout(getContentPane());
		layout.setHorizontalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(filterPanel, GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE))
				.addComponent(displayPanel, GroupLayout.DEFAULT_SIZE, 782, Short.MAX_VALUE)
				.addGroup(Alignment.TRAILING, layout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 762, Short.MAX_VALUE)
					.addContainerGap())
				.addComponent(violationPanel, GroupLayout.DEFAULT_SIZE, 782, Short.MAX_VALUE)
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(filterPanel, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 141, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(displayPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(violationPanel, GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		
		JLabel lblLineNumber = new JLabel("Line number");
		
		JLabel lblLogicalModule = new JLabel("Logical Module");
		
		lineNumberValueLabel = new JLabel(" ");
		
		logicalModulesValueLabel = new JLabel(" ");
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblLineNumber)
						.addComponent(lblLogicalModule))
					.addGap(35)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(logicalModulesValueLabel)
						.addComponent(lineNumberValueLabel))
					.addContainerGap(590, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblLineNumber)
						.addComponent(lineNumberValueLabel))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblLogicalModule)
						.addComponent(logicalModulesValueLabel))
					.addContainerGap(31, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);

		
		scrollPane.setViewportView(informationPanel);

		getContentPane().setLayout(layout);
		setResizable(true);
		setClosable(true);
		setIconifiable(true);
		setMaximizable(true);
		
		
		violationTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					return;
				}
				
				if(violationTable.getSelectedRow() > -1){
					Violation v = ts.getAllViolations().get(violationTable.getSelectedRow());

					lineNumberValueLabel.setText("" + v.getLinenumber());
					logicalModulesValueLabel.setText(v.getLogicalModules().getLogicalModuleFrom().getLogicalModulePath());
				} else{
					lineNumberValueLabel.setText("");
					logicalModulesValueLabel.setText("");
				}
			}
		});
		
	}
	
	public void loadGUIText(){
		setTitle(ResourceBundles.getValue("BrowseViolations"));
		displayPanel.setBorder(BorderFactory.createTitledBorder(
				ResourceBundles.getValue("Display")));
		dependencies.setText(ResourceBundles.getValue("Dependencies") + ":");
		allDependencies.setText(ResourceBundles.getValue("All"));
		directDependencies.setText(ResourceBundles.getValue("Direct"));
		indirectDependencies.setText(ResourceBundles.getValue("Indirect"));
		filterPanel.setBorder(BorderFactory.createTitledBorder(
				ResourceBundles.getValue("Filter")));
		editFilter.setText(ResourceBundles.getValue("EditFilter"));
		applyFilter.setText(ResourceBundles.getValue("ApplyFilter"));
		informationPanel.setBorder(BorderFactory.createTitledBorder(
				ResourceBundles.getValue("Information")));
		loadModels();
		loadAfterViolationsChanged();
	}
	
	public void loadAfterViolationsChanged(){
		setViolations();
		loadInformationPanel();
	}
	
	private void loadModels(){
		String[] columnNames = {
			ResourceBundles.getValue("Source"),
			ResourceBundles.getValue("Rule"),
			ResourceBundles.getValue("DependencyKind"),
			ResourceBundles.getValue("Target"),
			ResourceBundles.getValue("Severity")};
		
		violationModel = new DefaultTableModel(columnNames, 0) {

			private static final long serialVersionUID = -6892927200143239311L;
			Class<?>[] types = new Class[]{
					String.class, String.class, String.class, String.class, String.class
			};
			boolean[] canEdit = new boolean[]{
					false, false, false, false, false
			};

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		};
		violationTable.setModel(violationModel);
		violationTable.getRowSorter().toggleSortOrder(4);
		violationTable.getRowSorter().toggleSortOrder(4);
	}
	
	private void setViolations() {
		while (violationModel.getRowCount() > 0) {
			violationModel.removeRow(0);
		}

		ArrayList<Violation> violationRows = ts.applyFilterViolations(applyFilter.isSelected());
		for (Violation violation : violationRows) {
			String message = new Messagebuilder().createMessage(violation.getMessage());
			violationModel.addRow(new Object[]{violation.getClassPathFrom(), message, ResourceBundles.getValue(violation.getViolationtypeKey()), violation.getClassPathTo(), violation.getSeverity().toString()});
		}

//		setColumnWidth(3, 50);
	}

	private void loadInformationPanel() {
		informationPanel.removeAll();
		
		totalViolation.setText(ResourceBundles.getValue("TotalViolations") + ":");
		informationPanel.add(totalViolation);

		
		totalViolationNumber.setText("" + ts.getAllViolations().size());
		informationPanel.add(totalViolationNumber);
		
		shownViolations.setText(ResourceBundles.getValue("ShownViolations") + ":");
		informationPanel.add(shownViolations);
		
		shownViolationsNumber.setText("" + violationModel.getRowCount());
		informationPanel.add(shownViolationsNumber);

		for(Entry<Severity, Integer> violationPerSeverity: ts.getViolationsPerSeverity(applyFilter.isSelected()).entrySet()) {
			informationPanel.add(new JLabel(violationPerSeverity.getKey().toString()));
			informationPanel.add(new JLabel("" + violationPerSeverity.getValue()));
		}
		
		informationPanel.updateUI();
	}

//	private void setColumnWidth(int columnIndex, int width){
//		TableColumn col = violationTable.getColumnModel().getColumn(columnIndex);
//		col.setPreferredWidth(width);
//	}
}
