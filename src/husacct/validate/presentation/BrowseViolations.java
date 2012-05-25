package husacct.validate.presentation;

import husacct.ServiceProvider;
import husacct.control.ILocaleChangeListener;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationHistory;
import husacct.validate.presentation.browseViolations.FilterPanel;
import husacct.validate.presentation.browseViolations.StatisticsPanel;
import husacct.validate.presentation.browseViolations.ViolationInformationPanel;
import husacct.validate.presentation.tableModels.FilterViolationsObserver;
import husacct.validate.task.TaskServiceImpl;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class BrowseViolations extends JInternalFrame implements ILocaleChangeListener, FilterViolationsObserver, Observer {
	private static final long serialVersionUID = 4912981274532255799L;
	private JTable chooseViolationHistoryTable, violationsTable;
	private DefaultTableModel chooseViolationHistoryTableModel, violationsTableModel;
	private final TaskServiceImpl taskServiceImpl;
	private final ConfigurationServiceImpl configuration;
	private final SimpleDateFormat dateFormat;
	private ViolationHistory selectedViolationHistory;
	private FilterPanel filterPane;
	private StatisticsPanel statisticsPanel;
	private ViolationInformationPanel violationInformationPanel;
	
	private final FilterViolations filterViolations;
	private JButton buttonSaveInHistory, buttonLatestViolations, buttonDeleteViolationHistoryPoint,
	buttonValidate;
	
	private List<Violation> shownViolations;
	private BrowseViolations thisScreen = this;

	public BrowseViolations(TaskServiceImpl taskServiceImpl, ConfigurationServiceImpl configuration) {
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

	private void addListListeners() {
		violationsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				violationInformationPanel.update(arg0, violationsTable, shownViolations);
			}

		});
		chooseViolationHistoryTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(!e.getValueIsAdjusting() && chooseViolationHistoryTable.getSelectedRow() > -1) {			
					changeShownViolations();
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
	public void fillViolationsTable(List<Violation> violations) {
		violationsTable.clearSelection();
		shownViolations = violations;
		clearViolationsTableModelRows();
		for(Violation violation : violations) {
			String violationtypeString = "";
			if(!violation.getViolationtypeKey().isEmpty()){
				violationtypeString = ServiceProvider.getInstance().getControlService().getTranslatedString(violation.getViolationtypeKey()) + ", " + getDirectKey(violation.isIndirect());
			}
			violationsTableModel.addRow(new Object[] {violation.getClassPathFrom(), ServiceProvider.getInstance().getControlService().getTranslatedString(violation.getRuletypeKey()), violationtypeString, violation.getClassPathTo(), violation.getSeverity().toString()});

		} 
		loadInformationPanel();
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
		violationInformationPanel.loadGuiText();
		statisticsPanel.loadAfterChange();
		filterPane.loadAfterChange();
		buttonDeleteViolationHistoryPoint.setText(ServiceProvider.getInstance().getControlService().getTranslatedString("Remove"));
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
				//TODO: add LoadingDialog from next merge
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

		filterPane = new FilterPanel(this);

		JScrollPane violationsTableScrollPane = new JScrollPane();

		violationInformationPanel = new ViolationInformationPanel();

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(null);

		GroupLayout rightSideGroupLayout = new GroupLayout(rightSidePane);	

		rightSideGroupLayout.setHorizontalGroup(
				rightSideGroupLayout.createParallelGroup(Alignment.TRAILING)
				.addComponent(violationInformationPanel, GroupLayout.DEFAULT_SIZE, 818, Short.MAX_VALUE)
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
								.addComponent(violationInformationPanel, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE))
				);

		statisticsPanel = new StatisticsPanel(taskServiceImpl);
		scrollPane.setViewportView(statisticsPanel);
		

		
		
		
		

		violationsTable = new JTable();
		violationsTableScrollPane.setViewportView(violationsTable);
		rightSidePane.setLayout(rightSideGroupLayout);

		
		configuration.attachViolationHistoryRepositoryObserver(this);
	}

	protected void loadInformationPanel() {
		int violationsSize = 0;
		if(selectedViolationHistory == null){
			violationsSize = taskServiceImpl.getAllViolations().getValue().size();
		} else{ 
			violationsSize = selectedViolationHistory.getViolations().size();
		}
		
		statisticsPanel.loadStatistics(taskServiceImpl.getViolationsPerSeverity(shownViolations), violationsSize, shownViolations.size());
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

	public void editFilterActionPerformed(ActionEvent e) {
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

	public void applyFilterChanged(ActionEvent e) {
		//TODO: add LoadingDialog from next merge
		updateViolationsTable();
		loadInformationPanel();

	}

	@Override
	public void update(Observable o, Object arg) {
		fillChooseViolationHistoryTable();		
	}

	public List<Violation> getViolationsFilteredOrNormal() {
		List<Violation> violations;
		if(selectedViolationHistory != null) {
			violations = selectedViolationHistory.getViolations();
		} else {
			violations = taskServiceImpl.getAllViolations().getValue();
		}

		if(filterPane.getApplyFilter().isSelected()) {
			violations = taskServiceImpl.applyFilterViolations(violations);
		}
		return violations;
	}
	protected void setSelectedViolationHistory(ViolationHistory v) {
		selectedViolationHistory = v;
	}

	public ViolationHistory getSelectedViolationHistory() {
		return selectedViolationHistory;
	}

	private void changeShownViolations() {
		//TODO: add LoadingDialog from next merge
		LoadViolationHistoryPointsTask loadViolationsHistoryTask = new LoadViolationHistoryPointsTask(chooseViolationHistoryTable, thisScreen, taskServiceImpl, filterPane.getApplyFilter());
		Thread th = new Thread(loadViolationsHistoryTask);
		th.start();
	}

	public void update(){
		try{
			int row = chooseViolationHistoryTable.convertRowIndexToModel(chooseViolationHistoryTable.getSelectedRow());
			selectedViolationHistory = taskServiceImpl.getViolationHistories().get(row);
			fillViolationsTable(selectedViolationHistory.getViolations());
			loadInformationPanel();
			filterPane.getApplyFilter().setSelected(false);
		} catch(Exception e){

		}
	}
}
