package husacct.validate.presentation;

import husacct.validate.abstraction.language.ResourceBundles;
import husacct.validate.domain.validation.Violation;
import husacct.validate.task.TaskServiceImpl;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import javax.swing.*;
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
	private JLabel dependencies, highSeverity, highSeverityNumber, lowSeverity,
			lowSeverityNumber, mediumSeverity, mediumSeverityNumber,
			shownViolations, shownViolationsNumber, totalViolation,
			totalViolationNumber;
	private JPanel displayPanel, filterPanel, informationPanel;
	private JButton editFilter;
	private JScrollPane violationPanel;
	private JTable violationTable;
	private DefaultTableModel violationModel;

	public BrowseViolations(TaskServiceImpl ts) {
		this.ts = ts;
		this.fv = new FilterViolations(ts, this);
		initComponents();
		violationTable.doLayout();
		setViolations();
		totalViolationNumber.setText(ts.getAllViolations().size() + "");
	}

	private void initComponents() {

		setTitle(ResourceBundles.getValue("BrowseViolations"));
		dependencyLevel = new ButtonGroup();
		violationPanel = new JScrollPane();
		violationTable = new JTable();
		String[] columnNames = {ResourceBundles.getValue("LogicalModule"),
								ResourceBundles.getValue("Source"),
								ResourceBundles.getValue("Target"),
								ResourceBundles.getValue("LineNumber"),
								ResourceBundles.getValue("Severity"),
								ResourceBundles.getValue("Rule"),
								ResourceBundles.getValue("DependencyKind")};
		violationModel = new DefaultTableModel(columnNames, 0) {

			private static final long serialVersionUID = -6892927200143239311L;
			Class<?>[] types = new Class[]{
				String.class, String.class, String.class, Integer.class,
				Integer.class, String.class, String.class
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

		informationPanel = new JPanel();
		lowSeverity = new JLabel();
		mediumSeverity = new JLabel();
		lowSeverityNumber = new JLabel();
		mediumSeverityNumber = new JLabel();
		highSeverity = new JLabel();
		highSeverityNumber = new JLabel();
		totalViolation = new JLabel();
		shownViolations = new JLabel();
		totalViolationNumber = new JLabel();
		shownViolationsNumber = new JLabel();
		filterPanel = new JPanel();
		editFilter = new JButton();
		applyFilter = new JCheckBox();
		displayPanel = new JPanel();
		dependencies = new JLabel();
		allDependencies = new JRadioButton();
		directDependencies = new JRadioButton();
		indirectDependencies = new JRadioButton();

		violationPanel.setAlignmentX(0.0F);
		violationPanel.setAlignmentY(0.0F);
		violationPanel.setAutoscrolls(true);

		violationTable.setAutoCreateRowSorter(true);
		violationTable.setModel(violationModel);
		violationTable.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
		violationTable.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		violationTable.setFillsViewportHeight(true);
		violationTable.getRowSorter().toggleSortOrder(4);
		violationTable.getRowSorter().toggleSortOrder(4);
		violationTable.getTableHeader().setReorderingAllowed(false);
		violationPanel.setViewportView(violationTable);

		informationPanel.setBorder(BorderFactory.createTitledBorder(
				ResourceBundles.getValue("Information")));

		lowSeverity.setText(ResourceBundles.getValue("Low") + ":");

		mediumSeverity.setText(ResourceBundles.getValue("Medium") + ":");

		lowSeverityNumber.setText("0");

		mediumSeverityNumber.setText("0");

		highSeverity.setText(ResourceBundles.getValue("High") + ":");

		highSeverityNumber.setText("0");
		highSeverityNumber.setAlignmentX(0.5F);

		totalViolation.setText(ResourceBundles.getValue("TotalViolations") +
							   ":");

		shownViolations.setText(ResourceBundles.getValue("ShownViolations") +
								":");

		totalViolationNumber.setText("0");

		shownViolationsNumber.setText("0");

		filterPanel.setBorder(BorderFactory.createTitledBorder(
				ResourceBundles.getValue("Filter")));

		editFilter.setText(ResourceBundles.getValue("EditFilter"));
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

		applyFilter.setText(ResourceBundles.getValue("ApplyFilter"));
		applyFilter.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent evt) {
				setViolations();
			}
		});

		GroupLayout filterPanelLayout = new GroupLayout(filterPanel);
		filterPanel.setLayout(filterPanelLayout);
		filterPanelLayout.setHorizontalGroup(
				filterPanelLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGroup(filterPanelLayout.
				createSequentialGroup().addContainerGap().addGroup(filterPanelLayout.
				createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(
				editFilter, GroupLayout.PREFERRED_SIZE, 77,
				GroupLayout.PREFERRED_SIZE).
				addComponent(applyFilter)).addContainerGap(144, Short.MAX_VALUE)));
		filterPanelLayout.setVerticalGroup(
				filterPanelLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGroup(
				GroupLayout.Alignment.TRAILING, filterPanelLayout.
				createSequentialGroup().addGap(11, 11, 11).addComponent(
				applyFilter).addPreferredGap(
				LayoutStyle.ComponentPlacement.UNRELATED).addComponent(
				editFilter, GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE).
				addContainerGap()));

		GroupLayout informationPanelLayout = new GroupLayout(informationPanel);
		informationPanel.setLayout(informationPanelLayout);
		informationPanelLayout.setHorizontalGroup(
				informationPanelLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGroup(
				informationPanelLayout.createSequentialGroup().addContainerGap().
				addGroup(informationPanelLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGroup(informationPanelLayout.
				createSequentialGroup().addComponent(highSeverity).
				addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).
				addComponent(highSeverityNumber)).addGroup(informationPanelLayout.
				createSequentialGroup().addGroup(informationPanelLayout.
				createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(
				lowSeverity).addComponent(mediumSeverity)).addPreferredGap(
				LayoutStyle.ComponentPlacement.UNRELATED).addGroup(informationPanelLayout.
				createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(
				lowSeverityNumber).addComponent(mediumSeverityNumber)).addGap(18,
																			  18,
																			  18).
				addGroup(informationPanelLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addComponent(totalViolation,
															GroupLayout.Alignment.TRAILING).
				addComponent(shownViolations, GroupLayout.Alignment.TRAILING)).
				addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(informationPanelLayout.
				createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(
				totalViolationNumber).addComponent(shownViolationsNumber)))).
				addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 195,
								Short.MAX_VALUE).addComponent(filterPanel,
															  GroupLayout.PREFERRED_SIZE,
															  GroupLayout.DEFAULT_SIZE,
															  GroupLayout.PREFERRED_SIZE)));
		informationPanelLayout.setVerticalGroup(
				informationPanelLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGroup(
				informationPanelLayout.createSequentialGroup().addGroup(informationPanelLayout.
				createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(
				lowSeverity).addComponent(lowSeverityNumber).addComponent(
				totalViolation).addComponent(totalViolationNumber)).
				addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(informationPanelLayout.
				createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(
				mediumSeverity).addComponent(mediumSeverityNumber).addComponent(
				shownViolations).addComponent(shownViolationsNumber)).
				addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(informationPanelLayout.
				createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(
				highSeverity).addComponent(highSeverityNumber)).addContainerGap()).
				addGroup(GroupLayout.Alignment.TRAILING, informationPanelLayout.
				createSequentialGroup().addComponent(filterPanel,
													 GroupLayout.PREFERRED_SIZE,
													 GroupLayout.DEFAULT_SIZE,
													 GroupLayout.PREFERRED_SIZE).
				addContainerGap()));

		displayPanel.setBorder(BorderFactory.createTitledBorder(
				ResourceBundles.getValue("Display")));

		dependencies.setText(ResourceBundles.getValue("Dependencies") + ":");

		dependencyLevel.add(allDependencies);
		allDependencies.setSelected(true);
		allDependencies.setText(ResourceBundles.getValue("All"));

		dependencyLevel.add(directDependencies);
		directDependencies.setText(ResourceBundles.getValue("Direct"));

		dependencyLevel.add(indirectDependencies);
		indirectDependencies.setText(ResourceBundles.getValue("Indirect"));

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

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING).
				addComponent(violationPanel).addComponent(displayPanel,
														  GroupLayout.DEFAULT_SIZE,
														  GroupLayout.DEFAULT_SIZE,
														  Short.MAX_VALUE).
				addComponent(informationPanel, GroupLayout.DEFAULT_SIZE,
							 GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		layout.setVerticalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING).
				addGroup(layout.createSequentialGroup().addComponent(
				informationPanel, GroupLayout.PREFERRED_SIZE, 130,
				GroupLayout.PREFERRED_SIZE).
				addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).
				addComponent(displayPanel, GroupLayout.PREFERRED_SIZE,
							 GroupLayout.DEFAULT_SIZE,
							 GroupLayout.PREFERRED_SIZE).addPreferredGap(
				LayoutStyle.ComponentPlacement.RELATED).addComponent(
				violationPanel, GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)));

		setClosable(true);
		setIconifiable(true);
		setMaximizable(true);
		setVisible(true);
	}

	protected void setViolations() {
		int highSeverityCount = 0;
		int mediumSeverityCount = 0;
		int lowSeverityCount = 0;

		while (violationModel.getRowCount() > 0) {
			violationModel.removeRow(0);
		}

		ArrayList<Violation> violationRows = ts.filterViolations(applyFilter.isSelected());
		for (Violation violation : violationRows) {
//			violationModel.addRow(new Object[]{violation.getLogicalModules().getLogicalModuleFrom().getLogicalModulePath(), violation.getClassPathFrom(), violation.getClassPathTo(), violation.getLinenumber(), violation.getSeverityValue(), ResourceBundles.getValue(violation.getRuletypeKey()), ResourceBundles.getValue(violation.getViolationtypeKey())});
//			if (violation.getSeverityValue() == 3) {
//				lowSeverityCount++;
//			} else if (violation.getSeverityValue() == 2) {
//				mediumSeverityCount++;
//			} else {
//				lowSeverityCount++;
//			}
		}

		shownViolationsNumber.setText(violationRows.size() + "");
		highSeverityNumber.setText(highSeverityCount + "");
		mediumSeverityNumber.setText(mediumSeverityCount + "");
		lowSeverityNumber.setText(lowSeverityCount + "");
	}

	private void setColumnWidth(int columnIndex, int width){
		TableColumn col = violationTable.getColumnModel().getColumn(columnIndex);
		col.setPreferredWidth(width);
	}
}
