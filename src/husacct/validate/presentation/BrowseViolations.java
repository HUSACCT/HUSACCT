package husacct.validate.presentation;

import husacct.ServiceProvider;
import husacct.control.ILocaleChangeListener;
import husacct.control.task.threading.ThreadWithLoader;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationHistory;
import husacct.validate.presentation.browseViolations.FilterPanel;
import husacct.validate.presentation.browseViolations.StatisticsPanel;
import husacct.validate.presentation.browseViolations.ViolationInformationPanel;
import husacct.validate.presentation.tableModels.FilterViolationsObserver;
import husacct.validate.presentation.threadTasks.CheckConformanceTask;
import husacct.validate.presentation.threadTasks.LoadViolationHistoryPointsTask;
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
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.apache.log4j.Logger;

public class BrowseViolations extends JInternalFrame implements ILocaleChangeListener, FilterViolationsObserver, Observer {

	private static final long serialVersionUID = 4912981274532255799L;
	private Logger logger = Logger.getLogger(BrowseViolations.class);
	private final TaskServiceImpl taskServiceImpl;
	private final SimpleDateFormat dateFormat;

	private JButton buttonSaveInHistory;
	private JButton buttonLatestViolations;
	private JButton buttonDeleteViolationHistoryPoint;
	private JButton buttonValidate;
	private JTable chooseViolationHistoryTable, violationsTable;
	private JScrollPane statisticsScrollPane, violationsTableScrollPane, chooseViolationHistoryTableScrollPane, informationScrollPane;
	private JPanel rightSidePane, leftSidePane;
	private JSplitPane splitPane;
	private DefaultTableModel chooseViolationHistoryTableModel, violationsTableModel;
	private ViolationHistory selectedViolationHistory;
	private FilterPanel filterPane;
	private StatisticsPanel statisticsPanel;
	private ViolationInformationPanel violationInformationPanel;
	private List<Violation> shownViolations;
	private BrowseViolations browseViolations = this;


	public BrowseViolations(TaskServiceImpl taskServiceImpl, ConfigurationServiceImpl configuration) {

		this.taskServiceImpl = taskServiceImpl;
		this.dateFormat = new SimpleDateFormat("dd-MM-yyyy kk:mm:ss");

		initComponents();
		addListeneners();
		loadModels();
		loadText();
		fillChooseViolationHistoryTable();
	}

	private void initComponents() {
		setClosable(true);
		setMaximizable(true);
		setIconifiable(true);
		setSize(new Dimension(800, 600));

		splitPane = new JSplitPane();
		leftSidePane = new JPanel();
		chooseViolationHistoryTableScrollPane = new JScrollPane();
		buttonDeleteViolationHistoryPoint = new JButton();
		buttonLatestViolations = new JButton();
		buttonSaveInHistory = new JButton();
		buttonValidate = new JButton();
		chooseViolationHistoryTable = new JTable();
		rightSidePane = new JPanel();
		filterPane = new FilterPanel(this, taskServiceImpl);
		violationsTableScrollPane = new JScrollPane();
		violationInformationPanel = new ViolationInformationPanel();
		informationScrollPane = new JScrollPane(violationInformationPanel);
		statisticsScrollPane = new JScrollPane();
		statisticsPanel = new StatisticsPanel();
		violationsTable = new JTable();

		getContentPane().add(splitPane, BorderLayout.CENTER);
		leftSidePane.setMinimumSize(new Dimension(200, 10));
		splitPane.setLeftComponent(leftSidePane);
		chooseViolationHistoryTableScrollPane.setViewportView(chooseViolationHistoryTable);
		splitPane.setRightComponent(rightSidePane);
		statisticsScrollPane.setBorder(null);
		statisticsScrollPane.setViewportView(statisticsPanel);
		violationsTableScrollPane.setViewportView(violationsTable);

		createBaseLayout();
	}

	private void createBaseLayout(){
		createLeftLayout();
		createRightLayout();
	}

	private void createLeftLayout(){
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

		leftSidePane.setLayout(leftSideGroupLayout);
	}

	private void createRightLayout(){
		GroupLayout rightSideGroupLayout = new GroupLayout(rightSidePane);	

		rightSideGroupLayout.setHorizontalGroup(
				rightSideGroupLayout.createParallelGroup(Alignment.TRAILING)
				.addComponent(informationScrollPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(rightSideGroupLayout.createSequentialGroup()
						.addGroup(rightSideGroupLayout.createParallelGroup(Alignment.TRAILING)
								.addGroup(Alignment.LEADING, rightSideGroupLayout.createSequentialGroup()
										.addComponent(statisticsScrollPane, GroupLayout.PREFERRED_SIZE, 330, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(filterPane, GroupLayout.PREFERRED_SIZE, 232, GroupLayout.PREFERRED_SIZE))
										.addComponent(violationsTableScrollPane, GroupLayout.DEFAULT_SIZE, 817, Short.MAX_VALUE))
										.addGap(1))
				);


		rightSideGroupLayout.setVerticalGroup(
				rightSideGroupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(rightSideGroupLayout.createSequentialGroup()
						.addGroup(rightSideGroupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(statisticsScrollPane, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE)
								.addComponent(filterPane, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(violationsTableScrollPane, GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
								.addGap(8)
								.addComponent(informationScrollPane, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
				);


		rightSidePane.setLayout(rightSideGroupLayout);
	}

	private void addListeneners(){
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
					loadAfterChange();
					filterPane.loadAfterChange();
				}
			}
		});
		buttonValidate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ThreadWithLoader validateThread = ServiceProvider.getInstance().getControlService().getThreadWithLoader(ServiceProvider.getInstance().getControlService().getTranslatedString("ValidatingLoading"), new CheckConformanceTask(browseViolations, filterPane, buttonSaveInHistory));
				validateThread.run();
			}
		});
		buttonSaveInHistory.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String input = JOptionPane.showInputDialog(ServiceProvider.getInstance().getControlService().getTranslatedString("SaveInHistoryDialog"));
				if(input != null && !input.equals("")) {
					taskServiceImpl.createHistoryPoint(input);
					buttonSaveInHistory.setEnabled(false);
					fillChooseViolationHistoryTable();
				}
			}
		});
		buttonDeleteViolationHistoryPoint.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
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
		});
		buttonLatestViolations.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chooseViolationHistoryTable.clearSelection();
				selectedViolationHistory = null;
				loadAfterChange();
				filterPane.loadAfterChange();
			}
		});
	}

	@Override
	public void update(Observable o, Object arg) {
		loadAfterChange();
		filterPane.loadAfterChange();
		buttonSaveInHistory.setEnabled(true);
	}

	@Override
	public void update(Locale newLocale) {
		loadAfterChange();
		filterPane.loadAfterChange();
	}

	@Override
	public void updateViolationsTable() {
		loadAfterChange();
	}

	public void loadAfterChange(){
		shownViolations = getViolationsFilteredOrNormal();
		fillViolationsTable(shownViolations);
		loadInformationPanel();
	}
	
	public void updateFilterValues(){
		filterPane.loadAfterChange();
	}
	
	public final void loadText(){
		violationInformationPanel.loadGuiText();
		statisticsPanel.loadAfterChange();
		setTitle(ServiceProvider.getInstance().getControlService().getTranslatedString("BrowseViolations"));
		buttonDeleteViolationHistoryPoint.setText(ServiceProvider.getInstance().getControlService().getTranslatedString("Remove"));
		buttonLatestViolations.setText(ServiceProvider.getInstance().getControlService().getTranslatedString("CurrentViolations"));
		buttonSaveInHistory.setText(ServiceProvider.getInstance().getControlService().getTranslatedString("SaveInHistory"));
		buttonValidate.setText(ServiceProvider.getInstance().getControlService().getTranslatedString("Validate"));
	}

	private void loadModels(){
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
		violationsTable.getTableHeader().setReorderingAllowed(false);
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

	private void fillChooseViolationHistoryTable() {
		clearChooseViolationHistoryTableModelRows();
		for(ViolationHistory violationHistory : taskServiceImpl.getViolationHistories()) {
			chooseViolationHistoryTableModel.addRow(new Object[] {dateFormat.format(violationHistory.getDate().getTime()), violationHistory.getDescription()});
		}
	}
	public void fillViolationsTable(List<Violation> violations) {
		RowSorter<? extends TableModel> rowsorter = violationsTable.getRowSorter();
		violationsTable.setRowSorter(null);
		violationsTable.setAutoCreateRowSorter(false);
		violationsTable.clearSelection();
		clearViolationsTableModelRows();
		for(Violation violation : violations) {
			String violationtypeString = "";
			if(!violation.getViolationtypeKey().isEmpty()){
				if(!violation.getViolationtypeKey().equals("VisibilityConvention")) {
					violationtypeString = ServiceProvider.getInstance().getControlService().getTranslatedString(violation.getViolationtypeKey()) + ", " + (violation.isIndirect() ? "Indirect" : "Direct");
				} else {
					violationtypeString = ServiceProvider.getInstance().getControlService().getTranslatedString(violation.getViolationtypeKey());
				}
			}
			violationsTableModel.addRow(new Object[] {violation.getClassPathFrom(), ServiceProvider.getInstance().getControlService().getTranslatedString(violation.getRuletypeKey()), violationtypeString, violation.getClassPathTo(), violation.getSeverity().toString()});
			violationsTable.revalidate();
		}
		violationsTable.repaint();
		violationsTable.setRowSorter(rowsorter);
	}

	public void loadInformationPanel() {
		int violationsSize;
		List<Severity> severities;
		if(selectedViolationHistory == null){
			violationsSize = taskServiceImpl.getAllViolations().getValue().size();
			severities = taskServiceImpl.getAllSeverities();
		} else{ 
			violationsSize = selectedViolationHistory.getViolations().size();
			severities = selectedViolationHistory.getSeverities();
		}		

		statisticsPanel.loadStatistics(taskServiceImpl.getViolationsPerSeverity(shownViolations, severities), violationsSize, shownViolations.size());
		statisticsPanel.repaint();
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

	public void applyFilterChanged(ActionEvent e) {
		final Thread updateThread = new Thread() { 
			@Override 
			public void run() {
				try {
					Thread.sleep(1);
					updateViolationsTable();
				} catch (InterruptedException e) {
					logger.debug(e.getMessage());
				}
			}
		};
		ThreadWithLoader loadingThread = ServiceProvider.getInstance().getControlService().getThreadWithLoader(ServiceProvider.getInstance().getControlService().getTranslatedString("FilteringLoading"), updateThread);
		loadingThread.run();
	}

	private void changeShownViolations(){
		ThreadWithLoader loadingThread = ServiceProvider.getInstance().getControlService().getThreadWithLoader(ServiceProvider.getInstance().getControlService().getTranslatedString("FilteringLoading"), new LoadViolationHistoryPointsTask(chooseViolationHistoryTable, this, taskServiceImpl, filterPane.getApplyFilter()));
		loadingThread.run();
	}

	private void clearChooseViolationHistoryTableModelRows() {
		while (chooseViolationHistoryTableModel.getRowCount() > 0) {
			chooseViolationHistoryTableModel.removeRow(0);
		}
	}

	private void clearViolationsTableModelRows() {
		while (violationsTableModel.getRowCount() > 0) {
			violationsTableModel.removeRow(0);
			violationsTable.revalidate();
		}
	}

	public ViolationHistory getSelectedViolationHistory() {
		return selectedViolationHistory;
	}

	public void setSelectedViolationHistory(ViolationHistory selectedViolationHistory) {
		this.selectedViolationHistory = selectedViolationHistory;
	}
}
