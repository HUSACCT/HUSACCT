package husacct.validate.presentation;

import husacct.control.ILocaleChangeListener;
import husacct.validate.abstraction.language.ValidateTranslator;
import husacct.validate.domain.factory.message.Messagebuilder;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationHistory;
import husacct.validate.presentation.tableModels.FilterViolationsObserver;
import husacct.validate.task.TaskServiceImpl;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.JCheckBox;

@SuppressWarnings("serial")
public class ViolationHistoryGUI2 extends JInternalFrame implements ListSelectionListener, ILocaleChangeListener, ActionListener, FilterViolationsObserver, ViolationHistoryRepositoryObserver {
	private JTable chooseViolationHistoryTable;
	private DefaultTableModel chooseViolationHistoryTableModel;
	private final TaskServiceImpl taskServiceImpl;
	private final SimpleDateFormat dateFormat;
	private JTable violationsTable;
	private DefaultTableModel violationsTableModel;
	private ViolationHistory selectedViolationHistory;
	private JButton buttonDeleteViolationHistoryPoint;
	private JPanel violationDetailPane;
	private JLabel detailsMessageLabel;
	private JLabel detailsLineNumberLabel;
	private JLabel detailsLogicalModuleLabel;
	private JPanel filterPane;
	private JLabel detailLineNumberLabelValue;
	private JLabel detailLogicalModuleLabelValue;
	private JLabel detailMessageLabelValue;
	private JCheckBox applyFilter;
	private JButton buttonEditFilter;
	private final FilterViolations filterViolations;

	public ViolationHistoryGUI2(TaskServiceImpl taskServiceImpl) {
		this.taskServiceImpl = taskServiceImpl;
		this.dateFormat = new SimpleDateFormat("dd-MM-yyyy kk:mm:ss");
		this.filterViolations = new FilterViolations(taskServiceImpl, this);
		init();
		loadModels();
		fillChooseViolationHistoryTable();
		updateGuiText();
	}

	private void loadModels() {
		loadChooseViolationHistoryTableModel();
		loadViolationsTableModel();
	}

	private void loadViolationsTableModel() {
		String[] columnNames = {
				ValidateTranslator.getValue("Source"),
				ValidateTranslator.getValue("Rule"),
				ValidateTranslator.getValue("DependencyKind"),
				ValidateTranslator.getValue("Target"),
				ValidateTranslator.getValue("Severity")};

		violationsTableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return String.class;
			}

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return false;
			}
		};
		violationsTable.setModel(violationsTableModel);
		violationsTable.getRowSorter().toggleSortOrder(4);
		violationsTable.getRowSorter().toggleSortOrder(4);
	}

	private void loadChooseViolationHistoryTableModel() {
		String[] columnNames = {
				ValidateTranslator.getValue("Date"),
				ValidateTranslator.getValue("Description")};
		chooseViolationHistoryTableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return String.class;
			}
			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return false;
			}
		};
		chooseViolationHistoryTable.setModel(chooseViolationHistoryTableModel);
	}

	private void fillChooseViolationHistoryTable() {
		clearChooseViolationHistoryTableModelRows();
		for(ViolationHistory violationHistory : taskServiceImpl.getViolationHistories()) {
			chooseViolationHistoryTableModel.addRow(new Object[] {dateFormat.format(violationHistory.getDate().getTime()), violationHistory.getDescription()});
		}
	}
	private void fillViolationsTable(List<Violation> violations) {
		clearViolationsTableModelRows();
		for(Violation violation : violations) {
			violationsTableModel.addRow(new Object[] {violation.getClassPathFrom(), ValidateTranslator.getValue(violation.getRuletypeKey()), ValidateTranslator.getValue(violation.getViolationtypeKey()), violation.getClassPathTo(), violation.getSeverity().toString()});
		}
	}

	private void clearChooseViolationHistoryTableModelRows() {
		while (chooseViolationHistoryTableModel.getRowCount() > 0) {
			chooseViolationHistoryTableModel.removeRow(0);
		}
	}

	private void clearViolationsTableModelRows() {
		while (violationsTableModel.getRowCount() > 0) {
			violationsTableModel.removeRow(0);
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(!e.getValueIsAdjusting()) {
			if(e.getSource() == chooseViolationHistoryTable) {
				int row = chooseViolationHistoryTable.convertRowIndexToModel(chooseViolationHistoryTable.getSelectedRow());
				selectedViolationHistory = taskServiceImpl.getViolationHistories().get(row);
				fillViolationsTable(selectedViolationHistory.getViolations());
			} else if(e.getSource() == violationsTable) {
				int row = violationsTable.convertRowIndexToModel(violationsTable.getSelectedRow());
				Violation violation = selectedViolationHistory.getViolations().get(row);
				detailLineNumberLabelValue.setText("" + violation.getLinenumber());
				detailLogicalModuleLabelValue.setText(violation.getLogicalModules().getLogicalModuleFrom().getLogicalModulePath());
				String message = new Messagebuilder().createMessage(violation.getMessage());
				detailsMessageLabel.setText(message);
			}
		} 
	}

	@Override
	public void update(Locale newLocale) {
		updateGuiText();
	}

	private void updateGuiText() {
		loadModels();
		fillChooseViolationHistoryTable();
		if(selectedViolationHistory != null) {
			fillViolationsTable(selectedViolationHistory.getViolations());
		}
		violationDetailPane.setBorder(new TitledBorder(ValidateTranslator.getValue("Details")));
		detailsLineNumberLabel.setText(ValidateTranslator.getValue("LineNumber"));
		detailsLogicalModuleLabel.setText(ValidateTranslator.getValue("LogicalModule"));
		detailsMessageLabel.setText(ValidateTranslator.getValue("Message"));
		filterPane.setBorder(new TitledBorder(ValidateTranslator.getValue("Filter")));
		buttonDeleteViolationHistoryPoint.setText(ValidateTranslator.getValue("Remove"));
		applyFilter.setText(ValidateTranslator.getValue("ApplyFilter"));
		buttonEditFilter.setText(ValidateTranslator.getValue("EditFilter"));
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource().equals(buttonEditFilter)) {
			filterViolations.setVisible(true);
		}
	}
	
	@Override
	public void updateViolationsTable() {
	//	fillViolationsTable(ts.get)
	}
	

	@Override
	public void updateViolationHistories() {
		fillChooseViolationHistoryTable();
	}
	
	public void init() {
		setClosable(true);
		setMaximizable(true);
		setIconifiable(true);
		setTitle("ViolationHistoryGUI");
		setSize(new Dimension(800, 600));
		JSplitPane splitPane = new JSplitPane();
		getContentPane().add(splitPane, BorderLayout.CENTER);

		JPanel leftSidePane = new JPanel();
		leftSidePane.setMinimumSize(new Dimension(200, 10));
		splitPane.setLeftComponent(leftSidePane);

		JScrollPane chooseViolationHistoryTableScrollPane = new JScrollPane();

		buttonDeleteViolationHistoryPoint = new JButton("Delete TODO locale");
		GroupLayout gl_leftSidePane = new GroupLayout(leftSidePane);
		gl_leftSidePane.setHorizontalGroup(
				gl_leftSidePane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_leftSidePane.createSequentialGroup()
						.addComponent(chooseViolationHistoryTableScrollPane, GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
						.addGap(2))
						.addGroup(gl_leftSidePane.createSequentialGroup()
								.addGap(10)
								.addComponent(buttonDeleteViolationHistoryPoint, GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
								.addContainerGap())
				);
		gl_leftSidePane.setVerticalGroup(
				gl_leftSidePane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_leftSidePane.createSequentialGroup()
						.addComponent(chooseViolationHistoryTableScrollPane, GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(buttonDeleteViolationHistoryPoint, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
						.addContainerGap())
				);

		chooseViolationHistoryTable = new JTable();
		chooseViolationHistoryTableScrollPane.setViewportView(chooseViolationHistoryTable);
		leftSidePane.setLayout(gl_leftSidePane);

		JPanel rightSidePane = new JPanel();
		splitPane.setRightComponent(rightSidePane);

		filterPane = new JPanel();
		filterPane.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Filter TODO locale", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JScrollPane violationsTableScrollPane = new JScrollPane();

		violationDetailPane = new JPanel();
		violationDetailPane.setBorder(new TitledBorder(null, "Details TODO Local", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout gl_rightSidePane = new GroupLayout(rightSidePane);
		gl_rightSidePane.setHorizontalGroup(
				gl_rightSidePane.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_rightSidePane.createSequentialGroup()
						.addGroup(gl_rightSidePane.createParallelGroup(Alignment.TRAILING)
								.addComponent(violationsTableScrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 576, Short.MAX_VALUE)
								.addComponent(filterPane, GroupLayout.DEFAULT_SIZE, 576, Short.MAX_VALUE))
								.addGap(1))
								.addComponent(violationDetailPane, GroupLayout.DEFAULT_SIZE, 577, Short.MAX_VALUE)
				);
		gl_rightSidePane.setVerticalGroup(
				gl_rightSidePane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_rightSidePane.createSequentialGroup()
						.addComponent(filterPane, GroupLayout.PREFERRED_SIZE, 152, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(violationsTableScrollPane, GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
						.addGap(8)
						.addComponent(violationDetailPane, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE))
				);
		
		applyFilter = new JCheckBox("Apply Filter");
		
		buttonEditFilter = new JButton("Edit Filter");
		GroupLayout gl_filterPane = new GroupLayout(filterPane);
		gl_filterPane.setHorizontalGroup(
			gl_filterPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_filterPane.createSequentialGroup()
					.addGap(26)
					.addGroup(gl_filterPane.createParallelGroup(Alignment.LEADING)
						.addComponent(buttonEditFilter)
						.addComponent(applyFilter))
					.addContainerGap(459, Short.MAX_VALUE))
		);
		gl_filterPane.setVerticalGroup(
			gl_filterPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_filterPane.createSequentialGroup()
					.addGap(37)
					.addComponent(applyFilter)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(buttonEditFilter)
					.addContainerGap(39, Short.MAX_VALUE))
		);
		filterPane.setLayout(gl_filterPane);

		detailsLineNumberLabel = new JLabel("Line number (locale)");

		detailsLogicalModuleLabel = new JLabel("Logical module");

		detailsMessageLabel = new JLabel("Message");
		
		detailLineNumberLabelValue = new JLabel("");
		
		detailLogicalModuleLabelValue = new JLabel("");
		
		detailMessageLabelValue = new JLabel("");
		GroupLayout gl_violationDetailPane = new GroupLayout(violationDetailPane);
		gl_violationDetailPane.setHorizontalGroup(
			gl_violationDetailPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_violationDetailPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_violationDetailPane.createParallelGroup(Alignment.LEADING)
						.addComponent(detailsLogicalModuleLabel)
						.addComponent(detailsMessageLabel)
						.addGroup(gl_violationDetailPane.createSequentialGroup()
							.addComponent(detailsLineNumberLabel)
							.addGap(53)
							.addGroup(gl_violationDetailPane.createParallelGroup(Alignment.LEADING)
								.addComponent(detailLogicalModuleLabelValue)
								.addComponent(detailLineNumberLabelValue)
								.addComponent(detailMessageLabelValue))))
					.addContainerGap(397, Short.MAX_VALUE))
		);
		gl_violationDetailPane.setVerticalGroup(
			gl_violationDetailPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_violationDetailPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_violationDetailPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(detailsLineNumberLabel)
						.addComponent(detailLineNumberLabelValue))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_violationDetailPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(detailsLogicalModuleLabel)
						.addComponent(detailLogicalModuleLabelValue))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_violationDetailPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(detailsMessageLabel)
						.addComponent(detailMessageLabelValue))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		violationDetailPane.setLayout(gl_violationDetailPane);

		violationsTable = new JTable();
		violationsTableScrollPane.setViewportView(violationsTable);
		rightSidePane.setLayout(gl_rightSidePane);
		taskServiceImpl.attachViolationHistoryObserver(this);
	}

	@Override
	public void updateAll() {
		//No update at all!
	}

}
