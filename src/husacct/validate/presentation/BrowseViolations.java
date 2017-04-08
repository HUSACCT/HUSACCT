package husacct.validate.presentation;

import husacct.ServiceProvider;
import husacct.common.enums.States;
import husacct.common.help.presentation.HelpableJInternalFrame;
import husacct.common.locale.ILocaleService;
import husacct.control.ILocaleChangeListener;
import husacct.control.presentation.util.LoadingDialog;
import husacct.control.task.threading.ThreadWithLoader;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.presentation.browseViolations.FilterPanel;
import husacct.validate.presentation.browseViolations.StatisticsPanel;
import husacct.validate.presentation.browseViolations.ViolationDetailsPanel;
import husacct.validate.presentation.browseViolations.ViolationPerRulePanel;
import husacct.validate.presentation.tableModels.FilterViolationsObserver;
import husacct.validate.presentation.tableModels.ViolationDataModel;
import husacct.validate.presentation.tableModels.ViolationTable;
import husacct.validate.presentation.threadTasks.CheckConformanceTask;
import husacct.validate.task.TaskServiceImpl;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

public class BrowseViolations extends HelpableJInternalFrame implements ILocaleChangeListener, FilterViolationsObserver, Observer {

	private static final long serialVersionUID = 4912981274532255799L;
	private final TaskServiceImpl taskServiceImpl;
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();

	private JTabbedPane tabPanel;
	// Violations per Rule View
	ViolationPerRulePanel violationsPerRulePanel;
	// All Violations View
	private JPanel allViolationsPanel;
	private JScrollPane statisticsScrollPane, violationsTableScrollPane, informationScrollPane;
	private ViolationTable violationTable;
	private ViolationDataModel violationTableModel;
	private FilterPanel filterPane;
	private StatisticsPanel statisticsPanel;
	private ViolationDetailsPanel violationInformationPanel;
	private List<Violation> shownViolationsInAllViolationsPanel;

	private Logger logger = Logger.getLogger(BrowseViolations.class);
	
	public BrowseViolations(TaskServiceImpl taskServiceImpl, ConfigurationServiceImpl configuration) {
		this.taskServiceImpl = taskServiceImpl;
		initComponents();
		createBaseLayout();
		addListeneners();
		loadViolationsTableModel();
		loadText();
	}

	private void initComponents() {
		// Tab All Violations
		allViolationsPanel = new JPanel();
		filterPane = new FilterPanel(this, taskServiceImpl);
		violationsTableScrollPane = new JScrollPane();
		violationInformationPanel = new ViolationDetailsPanel(taskServiceImpl);
		informationScrollPane = new JScrollPane(violationInformationPanel);
		statisticsScrollPane = new JScrollPane();
		statisticsPanel = new StatisticsPanel();
		violationTable = new ViolationTable();
		statisticsScrollPane.setBorder(null);
		statisticsScrollPane.setViewportView(statisticsPanel);
		violationsTableScrollPane.setViewportView(violationTable);

		// Tab ViolationsPerRule
		violationsPerRulePanel = new ViolationPerRulePanel(taskServiceImpl);
		
		// TabbedPanel
		tabPanel = new JTabbedPane(SwingConstants.TOP);
        tabPanel.setBackground(UIManager.getColor("Panel.background"));
        getContentPane().add(tabPanel, BorderLayout.CENTER);
        tabPanel.addTab(localeService.getTranslatedString("ViolationsPerRuleTabTitle"), null, violationsPerRulePanel, null);
        tabPanel.addTab(localeService.getTranslatedString("AllViolationsTabTitle"), null, allViolationsPanel, null);
	}

	private void createBaseLayout() {
		createAllViolationsPaneLayout();
		//createHistoryPaneLayout(); Disabled in version 3.2. See for removed code parts class HistoryPanel.
	}

	private void createAllViolationsPaneLayout() {
		GroupLayout rightSideGroupLayout = new GroupLayout(allViolationsPanel);
		rightSideGroupLayout.setHorizontalGroup(
			rightSideGroupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(rightSideGroupLayout.createSequentialGroup()
	                .addContainerGap()
					.addGroup(rightSideGroupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(rightSideGroupLayout.createSequentialGroup()
							.addComponent(statisticsScrollPane, 507, 507, Short.MAX_VALUE)
							.addGap(10)
							.addComponent(filterPane, 300, 300, Short.MAX_VALUE))
						.addComponent(violationsTableScrollPane, GroupLayout.DEFAULT_SIZE, 817, Short.MAX_VALUE)
						.addComponent(informationScrollPane))
		                .addContainerGap()));
		rightSideGroupLayout.setVerticalGroup(
			rightSideGroupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(rightSideGroupLayout.createSequentialGroup()
	                .addContainerGap()
					.addGroup(rightSideGroupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(statisticsScrollPane, 85, 85, 85)
						.addComponent(filterPane, 85, 85, 85))
					.addGap(10)
					.addComponent(violationsTableScrollPane, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
					.addGap(10)
					.addComponent(informationScrollPane, 150, 150, 150)
		));
		allViolationsPanel.setLayout(rightSideGroupLayout);
	}

	private void addListeneners() {
		violationTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				violationInformationPanel.update(arg0, violationTable, shownViolationsInAllViolationsPanel);
			}
		});
	}

	@Override
	public void update(Observable o, Object arg) {
		reloadViolationPanelsAfterChange();
		filterPane.loadAfterChange();
	}

	@Override
	public void update(Locale newLocale) {
		reloadViolationPanelsAfterChange();
		filterPane.loadAfterChange();
	}

	@Override
	public void updateViolationTables() {
		violationsPerRulePanel.reload();
		reloadViolationPanelsAfterChange();
	}

	public void reloadViolationPanelsAfterChange() {
		violationsPerRulePanel.reload();
		reloadViolations();
		fillViolationsTable(shownViolationsInAllViolationsPanel);
		loadViolationDetailsPanel();
		updateFilterValues();
	}

	public void reloadViolations() {
		shownViolationsInAllViolationsPanel = taskServiceImpl.getAllViolations().getValue();
		shownViolationsInAllViolationsPanel = filterPane.filterViolationsOnDirectOrIndirect(shownViolationsInAllViolationsPanel);
		if (filterPane.getApplyFilter().isSelected()) {
			shownViolationsInAllViolationsPanel = filterViolations(shownViolationsInAllViolationsPanel);
		}
	}
	
	public void updateFilterValues() {
		filterPane.loadAfterChange();
	}

	public final void loadText() {
		violationInformationPanel.loadGuiText();
		statisticsPanel.loadAfterChange();
		setTitle(localeService.getTranslatedString("BrowseViolations"));
        tabPanel.setTitleAt(1, localeService.getTranslatedString("AllViolationsTabTitle"));
        tabPanel.setTitleAt(0, localeService.getTranslatedString("ViolationsPerRuleTabTitle"));

	}

	private void loadViolationsTableModel() {
		violationTableModel = new ViolationDataModel();
		violationTable.setFillsViewportHeight(true);
		violationTable.setModel(violationTableModel);
		violationTable.setAutoCreateRowSorter(true);
		violationTable.getTableHeader().setReorderingAllowed(false);
		violationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		violationTable.setColumnWidths();
	}

	public void fillViolationsTable(List<Violation> violations) {
		violationTable.setRowSorter(null);
		violationTable.setAutoCreateRowSorter(false);
		violationTable.clearSelection();
		shownViolationsInAllViolationsPanel = violations;
		violationTableModel.setData(violations);
		violationTable.revalidate();
		violationTable.setAutoCreateRowSorter(true);
	}

	public void loadViolationDetailsPanel() {
		int violationsSize;
		List<Severity> severities;
		violationsSize = taskServiceImpl.getAllViolations().getValue().size();
		severities = taskServiceImpl.getAllSeverities();
		statisticsPanel.loadStatistics(taskServiceImpl.getViolationsPerSeverity(shownViolationsInAllViolationsPanel, severities), violationsSize, shownViolationsInAllViolationsPanel.size());
		statisticsPanel.repaint();
	}

	public List<Violation> filterViolations(List<Violation> violations) {
		return taskServiceImpl.applyFilterViolations(violations);
	}

	public void applyFilterChanged(ActionEvent e) {
		final Thread updateThread = new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(1);
					fillViolationsTable(shownViolationsInAllViolationsPanel);
					loadViolationDetailsPanel();
				} catch (InterruptedException e) {
					logger.debug(e.getMessage());
				}
			}
		};
		ThreadWithLoader loadingThread = ServiceProvider.getInstance().getControlService().getThreadWithLoader(localeService.getTranslatedString("FilteringLoading"), updateThread);
		loadingThread.run();
	}

	public void validateNow() {
		boolean isAnalysing = ServiceProvider.getInstance().getControlService().getStates().contains(States.ANALYSING);
		boolean isValidating = ServiceProvider.getInstance().getControlService().getStates().contains(States.VALIDATING);
		if(!isAnalysing && !isValidating){
			ThreadWithLoader validateThread = ServiceProvider.getInstance().getControlService().getThreadWithLoader(localeService.getTranslatedString("ValidatingLoading"), new CheckConformanceTask()); // Previous to version 3.2: buttonSaveInHistory i.s.o. new JButton().
			LoadingDialog loadingDialog = validateThread.getLoadingDialog();
			if (loadingDialog != null) {
				loadingDialog.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						ServiceProvider.getInstance().getControlService().setValidating(false);
						logger.info("Stopping Thread");
					}
				});
			}
			validateThread.run();
		}
		else {
			//TODO make an error frame that validating or analysing is already running
			if (isAnalysing) {
				logger.warn(" Validate not started since state is: " + States.ANALYSING.toString());
			}
			if (isValidating) {
				logger.warn(" Validate not started since state is: " + States.VALIDATING.toString());
			}
		}
	}

}