package husacct.validate.presentation;

import husacct.ServiceProvider;
import husacct.control.ILocaleChangeListener;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
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

import org.apache.log4j.Logger;

public class BrowseViolations extends JInternalFrame implements ILocaleChangeListener, FilterViolationsObserver, Observer {
	private static final long serialVersionUID = 4912981274532255799L;
	private JTable chooseViolationHistoryTable;
	private DefaultTableModel chooseViolationHistoryTableModel;
	private final TaskServiceImpl taskServiceImpl;
	private final ConfigurationServiceImpl configuration;
	private final SimpleDateFormat dateFormat;
	private JTable violationsTable;
	private DefaultTableModel violationsTableModel;
	private ViolationHistory selectedViolationHistory;
	private JButton buttonDeleteViolationHistoryPoint;
	private JButton buttonValidate;
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
	private JLabel totalViolationLabel;
	private JLabel totalViolationNumber;
	private JLabel shownViolationsLabel;
	private JLabel shownViolationsNumber;
	private final Logger logger;
	private JRadioButton rdbtnDirect;
	private JRadioButton rdbtnIndirect;
	private JRadioButton rdbtnAll;
	private List<Violation> shownViolations;

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
		addListListeners();
	}

	private void loadModels() {
		loadChooseViolationHistoryTableModel();
		loadViolationsTableModel();
	}

	private void loadViolationsTableModel() {
		String[] columnNames = {
				ServiceProvider.getInstance().getControlService().getTranslatedString("Source"),
				ServiceProvider.getInstance().getControlService().getTranslatedString("Rule"),
				ServiceProvider.getInstance().getControlService().getTranslatedString("DependencyKind"),
				ServiceProvider.getInstance().getControlService().getTranslatedString("Target"),
				ServiceProvider.getInstance().getControlService().getTranslatedString("Severity")};

		violationsTableModel = new DefaultTableModel(columnNames, 0) {
			private static final long serialVersionUID = 7993526243751581611L;

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
	}

	private void loadChooseViolationHistoryTableModel() {
		String[] columnNames = {
				ServiceProvider.getInstance().getControlService().getTranslatedString("Date"),
				ServiceProvider.getInstance().getControlService().getTranslatedString("Description")};
		chooseViolationHistoryTableModel = new DefaultTableModel(columnNames, 0) {
			private static final long serialVersionUID = 5804122455086043586L;
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
	}
	
	private void addListListeners(){
		violationsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				if(!arg0.getValueIsAdjusting() && violationsTable.getSelectedRow() > -1) {
					int row = violationsTable.convertRowIndexToModel(violationsTable.getSelectedRow());
					Violation violation = shownViolations.get(row);
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
		chooseViolationHistoryTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(!e.getValueIsAdjusting() && chooseViolationHistoryTable.getSelectedRow() > -1) {
					System.out.println(e.getSource().toString());
					System.out.println(e.getSource().getClass());
					LoadingDialog loadingDialog = new LoadingDialog("Loading");
					loadingDialog.run();
					int row = chooseViolationHistoryTable.convertRowIndexToModel(chooseViolationHistoryTable.getSelectedRow());
					selectedViolationHistory = taskServiceImpl.getViolationHistories().get(row);
					fillViolationsTable(selectedViolationHistory.getViolations());
					loadInformationPanel();
					loadingDialog.dispose();
					applyFilter.setSelected(false);
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
		violationsTable.clearSelection();
		shownViolations = violations;
		clearViolationsTableModelRows();
		for(Violation violation : violations) {
			violationsTableModel.addRow(new Object[] {violation.getClassPathFrom(), ServiceProvider.getInstance().getControlService().getTranslatedString(violation.getRuletypeKey()), ServiceProvider.getInstance().getControlService().getTranslatedString(violation.getViolationtypeKey()) + ", " + getDirectKey(violation.isIndirect()), violation.getClassPathTo(), violation.getSeverity().toString()});
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
		informationPanel.setBorder(new TitledBorder(ServiceProvider.getInstance().getControlService().getTranslatedString("Information")));
		violationDetailPane.setBorder(new TitledBorder(ServiceProvider.getInstance().getControlService().getTranslatedString("Details")));
		detailsLineNumberLabel.setText(ServiceProvider.getInstance().getControlService().getTranslatedString("LineNumber"));
		detailsLogicalModuleLabel.setText(ServiceProvider.getInstance().getControlService().getTranslatedString("LogicalModule"));
		detailsMessageLabel.setText(ServiceProvider.getInstance().getControlService().getTranslatedString("Message"));
		filterPane.setBorder(new TitledBorder(ServiceProvider.getInstance().getControlService().getTranslatedString("Filter")));
		buttonDeleteViolationHistoryPoint.setText(ServiceProvider.getInstance().getControlService().getTranslatedString("Remove"));
		applyFilter.setText(ServiceProvider.getInstance().getControlService().getTranslatedString("ApplyFilter"));
		buttonEditFilter.setText(ServiceProvider.getInstance().getControlService().getTranslatedString("EditFilter"));
		buttonLatestViolations.setText(ServiceProvider.getInstance().getControlService().getTranslatedString("CurrentViolations"));
		buttonSaveInHistory.setText(ServiceProvider.getInstance().getControlService().getTranslatedString("SaveInHistory"));
		buttonValidate.setText(ServiceProvider.getInstance().getControlService().getTranslatedString("Validate"));
		loadInformationPanel();
	}


	@Override
	public void updateViolationsTable() {
		fillViolationsTable(getViolationsFilteredOrNormal());
	}

	public void loadAfterViolationsChanged(){
		internalAfterViolationsChanged();
		filterViolations.loadFilterValues();

	}
	
	public String getDirectKey(boolean b) {
		if(b)
			return "indirect";
		return "direct";
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
		
		buttonValidate = new JButton("Validate");
		buttonValidate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ServiceProvider.getInstance().getValidateService().checkConformance();
			}
		});
		
		
		GroupLayout leftSideGroupLayout = new GroupLayout(leftSidePane);
		ParallelGroup horizontalLeftSideParallelGroup = leftSideGroupLayout.createParallelGroup(Alignment.LEADING);
		horizontalLeftSideParallelGroup.addComponent(buttonValidate, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE);
		horizontalLeftSideParallelGroup.addComponent(buttonLatestViolations, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE);
		horizontalLeftSideParallelGroup.addComponent(buttonSaveInHistory, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE);
		SequentialGroup sequentialGroup = leftSideGroupLayout.createSequentialGroup();
		sequentialGroup.addContainerGap();
		sequentialGroup.addComponent(buttonDeleteViolationHistoryPoint, GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE);
		sequentialGroup.addContainerGap();
		SequentialGroup seqGroup2 = leftSideGroupLayout.createSequentialGroup();
		seqGroup2.addGap(2);
		seqGroup2.addComponent(chooseViolationHistoryTableScrollPane, GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE);
		horizontalLeftSideParallelGroup.addGroup(sequentialGroup);
		horizontalLeftSideParallelGroup.addGroup(seqGroup2);
		leftSideGroupLayout.setHorizontalGroup(horizontalLeftSideParallelGroup);
		
		ParallelGroup verticalLeftSideGroup = leftSideGroupLayout.createParallelGroup(Alignment.TRAILING);
		SequentialGroup sgroup1 = leftSideGroupLayout.createSequentialGroup();
		sgroup1.addGap(8);
		sgroup1.addComponent(buttonValidate, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE);
		sgroup1.addPreferredGap(ComponentPlacement.RELATED);
		sgroup1.addComponent(buttonLatestViolations, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE);
		sgroup1.addPreferredGap(ComponentPlacement.RELATED);
		sgroup1.addComponent(buttonSaveInHistory, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE);
		sgroup1.addPreferredGap(ComponentPlacement.RELATED);
		sgroup1.addComponent(chooseViolationHistoryTableScrollPane, GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE);
		sgroup1.addPreferredGap(ComponentPlacement.RELATED);
		sgroup1.addComponent(buttonDeleteViolationHistoryPoint, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE);
		sgroup1.addContainerGap();
		verticalLeftSideGroup.addGroup(sgroup1);
		leftSideGroupLayout.setVerticalGroup(verticalLeftSideGroup);

		chooseViolationHistoryTable = new JTable();
		chooseViolationHistoryTableScrollPane.setViewportView(chooseViolationHistoryTable);
		leftSidePane.setLayout(leftSideGroupLayout);

		JPanel rightSidePane = new JPanel();
		splitPane.setRightComponent(rightSidePane);

		filterPane = new JPanel();
		filterPane.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Filter TODO locale", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JScrollPane violationsTableScrollPane = new JScrollPane();

		violationDetailPane = new JPanel();
		violationDetailPane.setBorder(new TitledBorder(null, "Details TODO Local", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(null);
		
		GroupLayout rightSideGroupLayout = new GroupLayout(rightSidePane);	
		
		rightSideGroupLayout.setHorizontalGroup(
				rightSideGroupLayout.createParallelGroup(Alignment.TRAILING)
				.addComponent(violationDetailPane, GroupLayout.DEFAULT_SIZE, 818, Short.MAX_VALUE)
				.addGroup(rightSideGroupLayout.createSequentialGroup()
						.addGroup(rightSideGroupLayout.createParallelGroup(Alignment.TRAILING)
								.addGroup(Alignment.LEADING, rightSideGroupLayout.createSequentialGroup()
										.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 330, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(filterPane, GroupLayout.PREFERRED_SIZE, 232, GroupLayout.PREFERRED_SIZE))
										.addComponent(violationsTableScrollPane, GroupLayout.DEFAULT_SIZE, 817, Short.MAX_VALUE))
										.addGap(1))
				);
		
		
		rightSideGroupLayout.setVerticalGroup(
				rightSideGroupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(rightSideGroupLayout.createSequentialGroup()
						.addGroup(rightSideGroupLayout.createParallelGroup(Alignment.BASELINE)
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


		totalViolationLabel = new JLabel();
		totalViolationLabel.setText(ServiceProvider.getInstance().getControlService().getTranslatedString("TotalViolations") + ":");
		informationPanel.add(totalViolationLabel);

		totalViolationNumber = new JLabel();
		informationPanel.add(totalViolationNumber);

		shownViolationsLabel = new JLabel();
		shownViolationsLabel.setText(ServiceProvider.getInstance().getControlService().getTranslatedString("ShownViolations") + ":");
		informationPanel.add(shownViolationsLabel);

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
		rightSidePane.setLayout(rightSideGroupLayout);
		
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
		
		shownViolationsNumber.setText("" + shownViolations.size());
		
		informationPanel.add(totalViolationLabel);
		informationPanel.add(totalViolationNumber);
		
		informationPanel.add(shownViolationsLabel);
		informationPanel.add(shownViolationsNumber);
		
		for(Entry<Severity, Integer> violationPerSeverity: taskServiceImpl.getViolationsPerSeverity(selectedViolationHistory, applyFilter.isSelected()).entrySet()) {
			informationPanel.add(new JLabel(violationPerSeverity.getKey().toString()));
			informationPanel.add(new JLabel("" + violationPerSeverity.getValue()));
		}
		informationPanel.updateUI();
	}


	private void currentViolationsActionPerformed(ActionEvent e) {
		fillViolationsTable(taskServiceImpl.getAllViolations().getValue());
		chooseViolationHistoryTable.clearSelection();
		selectedViolationHistory = null;
		loadInformationPanel();
	}

	private void saveInHistoryActionPerformed(ActionEvent e) {
		String input = JOptionPane.showInputDialog(ServiceProvider.getInstance().getControlService().getTranslatedString("SaveInHistoryDialog"));
		if(input != null && !input.equals("")) {
			taskServiceImpl.createHistoryPoint(input);
			buttonSaveInHistory.setEnabled(false);
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
