package husacct.validate.presentation;

import husacct.control.ILocaleChangeListener;
import husacct.validate.abstraction.language.ValidateTranslator;
import husacct.validate.domain.factory.message.Messagebuilder;
import husacct.validate.domain.validation.Severity;
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
import java.util.Map.Entry;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.GridLayout;

@SuppressWarnings("serial")
public class BrowseViolations extends JInternalFrame implements ILocaleChangeListener, FilterViolationsObserver, ViolationHistoryRepositoryObserver {
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
	private JPanel informationPanel;
	private JButton buttonLatestViolations;
	private JButton buttonSaveInHistory;
	private JLabel totalViolation;
	private JLabel totalViolationNumber;
	private JLabel shownViolations;
	private JLabel shownViolationsNumber;

	public BrowseViolations(TaskServiceImpl taskServiceImpl) {
		this.taskServiceImpl = taskServiceImpl;
		this.dateFormat = new SimpleDateFormat("dd-MM-yyyy kk:mm:ss");
		this.filterViolations = new FilterViolations(taskServiceImpl, this);
		init();
		loadModels();
		fillChooseViolationHistoryTable();
		fillViolationsTable(taskServiceImpl.getAllViolations().getValue());
		loadGUIText();
		loadInformationPanel(null);
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
		violationsTable.setFillsViewportHeight(true);
		violationsTable.setModel(violationsTableModel);
		violationsTable.setAutoCreateRowSorter(true);
		violationsTable.getRowSorter().toggleSortOrder(2);
		violationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		violationsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				if(!arg0.getValueIsAdjusting()) {
					int row = violationsTable.convertRowIndexToModel(violationsTable.getSelectedRow());
					Violation violation = null;
					if(selectedViolationHistory != null) {
					violation = selectedViolationHistory.getViolations().get(row);
					} else {
						violation = taskServiceImpl.getAllViolations().getValue().get(row);
					}
					detailLineNumberLabelValue.setText("" + violation.getLinenumber());
					detailLogicalModuleLabelValue.setText(violation.getLogicalModules().getLogicalModuleFrom().getLogicalModulePath());
					String message = new Messagebuilder().createMessage(violation.getMessage());
					detailsMessageLabel.setText(message);
				}
			}

		});
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
		chooseViolationHistoryTable.setFillsViewportHeight(true);
		chooseViolationHistoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		chooseViolationHistoryTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(!e.getValueIsAdjusting()) {
					int row = chooseViolationHistoryTable.convertRowIndexToModel(chooseViolationHistoryTable.getSelectedRow());
					selectedViolationHistory = taskServiceImpl.getViolationHistories().get(row);
					fillViolationsTable(selectedViolationHistory.getViolations());
					loadInformationPanel(selectedViolationHistory);
				}
			}
		});
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
	public void update(Locale newLocale) {
		loadGUIText();
	}

	public void loadGUIText() {
		loadModels();
		fillChooseViolationHistoryTable();
		if(selectedViolationHistory != null) {
			fillViolationsTable(selectedViolationHistory.getViolations());
		}
		informationPanel.setBorder(new TitledBorder(ValidateTranslator.getValue("Information")));
		violationDetailPane.setBorder(new TitledBorder(ValidateTranslator.getValue("Details")));
		detailsLineNumberLabel.setText(ValidateTranslator.getValue("LineNumber"));
		detailsLogicalModuleLabel.setText(ValidateTranslator.getValue("LogicalModule"));
		detailsMessageLabel.setText(ValidateTranslator.getValue("Message"));
		filterPane.setBorder(new TitledBorder(ValidateTranslator.getValue("Filter")));
		buttonDeleteViolationHistoryPoint.setText(ValidateTranslator.getValue("Remove"));
		applyFilter.setText(ValidateTranslator.getValue("ApplyFilter"));
		buttonEditFilter.setText(ValidateTranslator.getValue("EditFilter"));
		buttonLatestViolations.setText(ValidateTranslator.getValue("CurrentViolations"));
		buttonSaveInHistory.setText(ValidateTranslator.getValue("SaveInHistory"));
	}


	@Override
	public void updateViolationsTable() {
		if(selectedViolationHistory != null) {
			fillViolationsTable(taskServiceImpl.applyFilterViolations(selectedViolationHistory.getViolations()));
		} else {
			fillViolationsTable(taskServiceImpl.applyFilterViolations(taskServiceImpl.getAllViolations().getValue()));
		}

	}


	@Override
	public void updateViolationHistories() {
		fillChooseViolationHistoryTable();
	}


	public void loadAfterViolationsChanged(){
		internalAfterViolationsChanged();
		filterViolations.loadFilterValues();

	}

	public void internalAfterViolationsChanged(){
		if(selectedViolationHistory == null) {
			fillViolationsTable(taskServiceImpl.getAllViolations().getValue());
			loadInformationPanel(null);
		} else {

		}
		buttonSaveInHistory.setEnabled(true);
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

		buttonLatestViolations = new JButton("Current Violations");
		buttonLatestViolations.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentViolationsActionPerformed(e);
			}
		});

		buttonSaveInHistory = new JButton("Save in history");
		buttonSaveInHistory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveInHistoryActionPerformed(e);
			}

		});
		GroupLayout gl_leftSidePane = new GroupLayout(leftSidePane);
		gl_leftSidePane.setHorizontalGroup(
				gl_leftSidePane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_leftSidePane.createSequentialGroup()
						.addGroup(gl_leftSidePane.createParallelGroup(Alignment.TRAILING)
								.addComponent(buttonSaveInHistory, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
								.addComponent(chooseViolationHistoryTableScrollPane, GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
								.addComponent(buttonLatestViolations, GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE))
								.addGap(2))
								.addGroup(Alignment.LEADING, gl_leftSidePane.createSequentialGroup()
										.addGap(10)
										.addComponent(buttonDeleteViolationHistoryPoint, GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
										.addContainerGap())
				);
		gl_leftSidePane.setVerticalGroup(
				gl_leftSidePane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_leftSidePane.createSequentialGroup()
						.addGap(5)
						.addComponent(buttonLatestViolations, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
						.addGap(4)
						.addComponent(buttonSaveInHistory, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(chooseViolationHistoryTableScrollPane, GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(buttonDeleteViolationHistoryPoint, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
						.addGap(66))
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

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(null);
		GroupLayout gl_rightSidePane = new GroupLayout(rightSidePane);
		gl_rightSidePane.setHorizontalGroup(
				gl_rightSidePane.createParallelGroup(Alignment.TRAILING)
				.addComponent(violationDetailPane, GroupLayout.DEFAULT_SIZE, 818, Short.MAX_VALUE)
				.addGroup(gl_rightSidePane.createSequentialGroup()
						.addGroup(gl_rightSidePane.createParallelGroup(Alignment.TRAILING)
								.addGroup(Alignment.LEADING, gl_rightSidePane.createSequentialGroup()
										.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 330, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(filterPane, GroupLayout.PREFERRED_SIZE, 232, GroupLayout.PREFERRED_SIZE))
										.addComponent(violationsTableScrollPane, GroupLayout.DEFAULT_SIZE, 817, Short.MAX_VALUE))
										.addGap(1))
				);
		gl_rightSidePane.setVerticalGroup(
				gl_rightSidePane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_rightSidePane.createSequentialGroup()
						.addGroup(gl_rightSidePane.createParallelGroup(Alignment.BASELINE)
								.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE)
								.addComponent(filterPane, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(violationsTableScrollPane, GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
								.addGap(8)
								.addComponent(violationDetailPane, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE))
				);

		informationPanel = new JPanel();
		scrollPane.setViewportView(informationPanel);
		informationPanel.setLayout(new GridLayout(0, 2));
		informationPanel.setBorder(new TitledBorder(null, "Information panel", TitledBorder.LEADING, TitledBorder.TOP, null, null));


		totalViolation = new JLabel();
		totalViolation.setText(ValidateTranslator.getValue("TotalViolations") + ":");
		informationPanel.add(totalViolation);

		totalViolationNumber = new JLabel();
		informationPanel.add(totalViolationNumber);

		shownViolations = new JLabel();
		shownViolations.setText(ValidateTranslator.getValue("ShownViolations") + ":");
		informationPanel.add(shownViolations);

		shownViolationsNumber = new JLabel();
		informationPanel.add(shownViolationsNumber);

		applyFilter = new JCheckBox("Apply Filter");

		buttonEditFilter = new JButton("Edit Filter");
		buttonEditFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editFilterActionPerformed(e);
			}

		});
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

	private void loadInformationPanel(ViolationHistory violationHistory) {
		informationPanel.removeAll();
		if(violationHistory == null)
			totalViolationNumber.setText("" + taskServiceImpl.getAllViolations().getValue().size());
		else 
			totalViolationNumber.setText("" + violationHistory.getViolations().size());
		for(Entry<Severity, Integer> violationPerSeverity: taskServiceImpl.getViolationsPerSeverity(violationHistory, applyFilter.isSelected()).entrySet()) {
			informationPanel.add(new JLabel(violationPerSeverity.getKey().toString()));
			informationPanel.add(new JLabel("" + violationPerSeverity.getValue()));
		}
		informationPanel.updateUI();
	}

	@Override
	public void updateAll() {
		//TODO do this method!
	}
	private void currentViolationsActionPerformed(ActionEvent e) {
		fillViolationsTable(taskServiceImpl.getAllViolations().getValue());
		loadInformationPanel(null);
	}

	private void saveInHistoryActionPerformed(ActionEvent e) {
		String input = JOptionPane.showInputDialog(ValidateTranslator.getValue("SaveInHistoryDialog"));
		if(input != null && !input.equals("")) {
			taskServiceImpl.saveInHistory(input);
		}
	}

	private void editFilterActionPerformed(ActionEvent e) {
		filterViolations.setVisible(true);
	}
}
