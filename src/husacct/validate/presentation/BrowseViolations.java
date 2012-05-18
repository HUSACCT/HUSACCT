package husacct.validate.presentation;

import husacct.ServiceProvider;
import husacct.control.ILocaleChangeListener;
import husacct.validate.abstraction.language.ValidateTranslator;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.factory.message.Messagebuilder;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationHistory;
import husacct.validate.presentation.tableModels.FilterViolationsObserver;
import husacct.validate.task.TaskServiceImpl;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Map.Entry;
import java.util.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;

public class BrowseViolations extends JInternalFrame implements ILocaleChangeListener, FilterViolationsObserver, Observer {
	private JTable chooseViolationHistoryTable;
	private DefaultTableModel chooseViolationHistoryTableModel;
	private final TaskServiceImpl taskServiceImpl;
	private final ConfigurationServiceImpl configuration;
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
	private final Logger logger;
	private JRadioButton rdbtnDirect;
	private JRadioButton rdbtnIndirect;
	private JRadioButton rdbtnAll;
	private List<Violation> currentViolations;

	public BrowseViolations(TaskServiceImpl taskServiceImpl, ConfigurationServiceImpl configuration) {
		this.logger = Logger.getLogger(BrowseViolations.class);
		this.taskServiceImpl = taskServiceImpl;
		this.configuration = configuration;
		this.dateFormat = new SimpleDateFormat("dd-MM-yyyy kk:mm:ss");
		this.filterViolations = new FilterViolations(taskServiceImpl, this);
		init();
		loadModels();
		fillChooseViolationHistoryTable();
		fillViolationsTable(taskServiceImpl.getAllViolations().getValue());
		loadGUIText();
		loadInformationPanel();
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
				if(!arg0.getValueIsAdjusting() && violationsTable.getSelectedRow() > -1) {
					int row = violationsTable.convertRowIndexToModel(violationsTable.getSelectedRow());
					Violation violation = currentViolations.get(row);
					detailLineNumberLabelValue.setText("" + violation.getLinenumber());
					detailLogicalModuleLabelValue.setText(violation.getLogicalModules().getLogicalModuleFrom().getLogicalModulePath());
					String message = new Messagebuilder().createMessage(violation.getMessage());
					detailMessageLabelValue.setText(message);
				} else {
					detailLineNumberLabelValue.setText("");
					detailLogicalModuleLabelValue.setText("");
					detailMessageLabelValue.setText("");
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
				if(!e.getValueIsAdjusting() && chooseViolationHistoryTable.getSelectedRow() > -1) {
					int row = chooseViolationHistoryTable.convertRowIndexToModel(chooseViolationHistoryTable.getSelectedRow());
					selectedViolationHistory = taskServiceImpl.getViolationHistories().get(row);
					fillViolationsTable(selectedViolationHistory.getViolations());
					loadInformationPanel();
				}
			}
		});
	}

	private void fillChooseViolationHistoryTable() {
		logger.info("Testing - fillChooseViolationsHistoryTableMethod() called");
		clearChooseViolationHistoryTableModelRows();
		for(ViolationHistory violationHistory : taskServiceImpl.getViolationHistories()) {
			chooseViolationHistoryTableModel.addRow(new Object[] {dateFormat.format(violationHistory.getDate().getTime()), violationHistory.getDescription()});
		}
	}
	private void fillViolationsTable(List<Violation> violations) {
		currentViolations = violations;
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
		fillViolationsTable(getViolationsFilteredOrNormal());
	}

	public void loadAfterViolationsChanged(){
		internalAfterViolationsChanged();
		filterViolations.loadFilterValues();

	}

	public void internalAfterViolationsChanged(){
		if(selectedViolationHistory == null) {
			fillViolationsTable(taskServiceImpl.getAllViolations().getValue());
			loadInformationPanel();
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
		buttonDeleteViolationHistoryPoint.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				removeViolationHistory(arg0);
			}
		});

		buttonLatestViolations = new JButton("Current Violations");
		buttonLatestViolations.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentViolationsActionPerformed(e);
			}
		});

		buttonSaveInHistory = new JButton("Save in history");
		buttonSaveInHistory.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveInHistoryActionPerformed(e);
			}

		});
		
		JButton btnValidate = new JButton("Validate");
		btnValidate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ServiceProvider.getInstance().getValidateService().checkConformance();
			}
		});
		GroupLayout gl_leftSidePane = new GroupLayout(leftSidePane);
		gl_leftSidePane.setHorizontalGroup(
			gl_leftSidePane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_leftSidePane.createSequentialGroup()
					.addContainerGap()
					.addComponent(buttonDeleteViolationHistoryPoint, GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
					.addContainerGap())
				.addComponent(btnValidate, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
				.addComponent(buttonLatestViolations, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
				.addComponent(buttonSaveInHistory, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
				.addGroup(gl_leftSidePane.createSequentialGroup()
					.addGap(2)
					.addComponent(chooseViolationHistoryTableScrollPane, GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE))
		);
		gl_leftSidePane.setVerticalGroup(
			gl_leftSidePane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_leftSidePane.createSequentialGroup()
					.addGap(8)
					.addComponent(btnValidate, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(buttonLatestViolations, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(buttonSaveInHistory, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chooseViolationHistoryTableScrollPane, GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(buttonDeleteViolationHistoryPoint, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
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
		applyFilter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				applyFilterChanged(arg0);
			}
		});

		buttonEditFilter = new JButton("Edit Filter");
		buttonEditFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editFilterActionPerformed(e);
			}

		});
		
		rdbtnAll = new JRadioButton("All");
		
		rdbtnDirect = new JRadioButton("Direct");
		
		rdbtnIndirect = new JRadioButton("Indirect");
		GroupLayout gl_filterPane = new GroupLayout(filterPane);
		gl_filterPane.setHorizontalGroup(
			gl_filterPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_filterPane.createSequentialGroup()
					.addGroup(gl_filterPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_filterPane.createSequentialGroup()
							.addGap(26)
							.addGroup(gl_filterPane.createParallelGroup(Alignment.LEADING)
								.addComponent(buttonEditFilter)
								.addComponent(applyFilter)))
						.addGroup(gl_filterPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(rdbtnAll)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(rdbtnDirect)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(rdbtnIndirect)))
					.addContainerGap(15, Short.MAX_VALUE))
		);
		gl_filterPane.setVerticalGroup(
			gl_filterPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_filterPane.createSequentialGroup()
					.addGap(37)
					.addComponent(applyFilter)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(buttonEditFilter)
					.addPreferredGap(ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
					.addGroup(gl_filterPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(rdbtnAll)
						.addComponent(rdbtnDirect)
						.addComponent(rdbtnIndirect))
					.addContainerGap())
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
		
		ButtonGroup filterIndirectButtonGroup = new ButtonGroup();
		filterIndirectButtonGroup.add(rdbtnAll);
		filterIndirectButtonGroup.add(rdbtnDirect);
		filterIndirectButtonGroup.add(rdbtnIndirect);
		rdbtnAll.setSelected(true);
		
		rdbtnAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				updateViolationsTable();
			}
		});
		rdbtnDirect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				List<Violation> violationsIndirect = new ArrayList<Violation>();
				List<Violation> violations = getViolationsFilteredOrNormal();
				for(Violation violation : violations) {
					if(!violation.isIndirect()) {
						violationsIndirect.add(violation);
					}
				}
				fillViolationsTable(violationsIndirect);
			}
		});
		rdbtnIndirect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				List<Violation> violationsIndirect = new ArrayList<Violation>();
				List<Violation> violations = getViolationsFilteredOrNormal();
				for(Violation violation : violations) {
					if(violation.isIndirect()) {
						violationsIndirect.add(violation);
					}
				}
				fillViolationsTable(violationsIndirect);
			}
		});
		configuration.attachViolationHistoryRepositoryObserver(this);
	}

	private void loadInformationPanel() {
		informationPanel.removeAll();
		if(selectedViolationHistory == null)
			totalViolationNumber.setText("" + taskServiceImpl.getAllViolations().getValue().size());
		else 
			totalViolationNumber.setText("" + selectedViolationHistory.getViolations().size());
		for(Entry<Severity, Integer> violationPerSeverity: taskServiceImpl.getViolationsPerSeverity(selectedViolationHistory, applyFilter.isSelected()).entrySet()) {
			informationPanel.add(new JLabel(violationPerSeverity.getKey().toString()));
			informationPanel.add(new JLabel("" + violationPerSeverity.getValue()));
		}
		informationPanel.updateUI();
	}


	private void currentViolationsActionPerformed(ActionEvent e) {
		fillViolationsTable(taskServiceImpl.getAllViolations().getValue());
		chooseViolationHistoryTable.clearSelection();
		loadInformationPanel();
		selectedViolationHistory = null;
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

	private void removeViolationHistory(ActionEvent e) {
		if(selectedViolationHistory != null) {
			taskServiceImpl.removeViolationHistory(selectedViolationHistory.getDate());
			chooseViolationHistoryTable.clearSelection();
			clearViolationsTableModelRows();
			fillChooseViolationHistoryTable();
			selectedViolationHistory = null;
		} else {
			JOptionPane.showMessageDialog(null, "Select a violation history first");
		}
	}

	private void applyFilterChanged(ActionEvent e) {
		updateViolationsTable();
		loadInformationPanel();
	}

	@Override
	public void update(Observable o, Object arg) {
		fillChooseViolationHistoryTable();		
	}
	
	private List<Violation> getViolationsFilteredOrNormal() {
		List<Violation> violations;
		if(selectedViolationHistory != null) {
			violations = selectedViolationHistory.getViolations();
		} else {
			violations = taskServiceImpl.getAllViolations().getValue();
		}
		
		if(applyFilter.isSelected()) {
			violations = taskServiceImpl.applyFilterViolations(violations);
		}
		return violations;
	}
}
