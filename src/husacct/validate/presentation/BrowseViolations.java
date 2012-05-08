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
import javax.swing.*;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

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

	public BrowseViolations(TaskServiceImpl ts) {
		setSize(new Dimension(800, 600));
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
		violationTable.setRowHeight(200);
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
				setViolations();
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

		GroupLayout layout = new GroupLayout(getContentPane());
		layout.setHorizontalGroup(
				layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(filterPanel, GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE))
						.addComponent(violationPanel, GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE)
						.addComponent(displayPanel, GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE)
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
								.addComponent(violationPanel, GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE))
				);

		
		scrollPane.setViewportView(informationPanel);

		getContentPane().setLayout(layout);
		setResizable(true);
		setClosable(true);
		setIconifiable(true);
		setMaximizable(true);
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
		createInformationPanel();
	}
	
	private void loadModels(){
		String[] columnNames = {
			ResourceBundles.getValue("LogicalModule"),
			ResourceBundles.getValue("Source"),
			ResourceBundles.getValue("Rule"),
			ResourceBundles.getValue("LineNumber"),
			ResourceBundles.getValue("DependencyKind"),
			ResourceBundles.getValue("Target"),
			ResourceBundles.getValue("Severity")};
		
		violationModel = new DefaultTableModel(columnNames, 0) {

			private static final long serialVersionUID = -6892927200143239311L;
			Class<?>[] types = new Class[]{
					String.class, String.class, String.class, Integer.class,
					String.class, String.class, String.class
			};
			boolean[] canEdit = new boolean[]{
					false, false, false, false, false, false, false
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
		setViolations();
	}

	public void createInformationPanel() {
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

	protected void setViolations() {

		while (violationModel.getRowCount() > 0) {
			violationModel.removeRow(0);
		}

		ArrayList<Violation> violationRows = ts.applyFilterViolations(applyFilter.isSelected());
		for (Violation violation : violationRows) {
			String message = new Messagebuilder().createMessage(violation.getMessage());
			violationModel.addRow(new Object[]{violation.getLogicalModules().getLogicalModuleFrom().getLogicalModulePath(), violation.getClassPathFrom(), message, violation.getLinenumber(), ResourceBundles.getValue(violation.getViolationtypeKey()), violation.getClassPathTo(), violation.getSeverity().toString()});
		}

		setColumnWidth(3, 50);

		createInformationPanel();
	}

	private void setColumnWidth(int columnIndex, int width){
		TableColumn col = violationTable.getColumnModel().getColumn(columnIndex);
		col.setPreferredWidth(width);
	}
}
