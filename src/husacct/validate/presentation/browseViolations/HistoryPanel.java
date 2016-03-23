package husacct.validate.presentation.browseViolations;

// This file contains deleted code from the history panel (left side panel in split panel). 
/*
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Observable;

import husacct.ServiceProvider;
import husacct.common.help.presentation.HelpableJPanel;
import husacct.control.presentation.util.LoadingDialog;
import husacct.control.task.States;
import husacct.control.task.threading.ThreadWithLoader;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.ViolationHistory;
import husacct.validate.presentation.tableModels.ViolationTable;
import husacct.validate.presentation.threadTasks.CheckConformanceTask;
import husacct.validate.presentation.threadTasks.LoadViolationHistoryPointsTask;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class HistoryPanel extends HelpableJPanel {


	// History View, disabled in version 3.2; see disabled code in initComponents()
	private JPanel historyPane;
	private JSplitPane splitPane; 
	private DefaultTableModel chooseViolationHistoryTableModel;
	private JTable chooseViolationHistoryTable;
	private JScrollPane chooseViolationHistoryTableScrollPane;
	private ViolationHistory selectedViolationHistory;
	private JButton buttonSaveInHistory;
	private JButton buttonLatestViolations;
	private JButton buttonDeleteViolationHistoryPoint;
	private JButton buttonValidateNow;
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy kk:mm:ss");


	public HistoryPanel() {
		initComponents();
		createHistoryPaneLayout();
		addListeneners();
		loadModels();
		loadText();
		fillChooseViolationHistoryTable();
	}
	
	private void initComponents() {
		historyPane = new JPanel();
		chooseViolationHistoryTableScrollPane = new JScrollPane();
		buttonDeleteViolationHistoryPoint = new JButton();
		buttonLatestViolations = new JButton();
		buttonSaveInHistory = new JButton();
		buttonValidateNow = new JButton();
		chooseViolationHistoryTable = new JTable();
		historyPane.setMinimumSize(new Dimension(200, 10));
		//splitPane = new JSplitPane();
		//getContentPane().add(splitPane, BorderLayout.CENTER);
		//.setViewportView(chooseViolationHistoryTable);
		//splitPane.setRightComponent(allViolationsPane);
	}

	private void createHistoryPaneLayout() {
		GroupLayout leftSideGroupLayout = new GroupLayout(historyPane);
		ParallelGroup horizontalLeftSideParallelGroup = leftSideGroupLayout.createParallelGroup(Alignment.LEADING);
		horizontalLeftSideParallelGroup.addComponent(buttonValidateNow, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE);
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
		sgroup1.addComponent(buttonValidateNow, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE);
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
		historyPane.setLayout(leftSideGroupLayout);
	}

	private void addListeneners() {
		chooseViolationHistoryTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && chooseViolationHistoryTable.getSelectedRow() > -1) {
					changeShownViolations();
					loadAfterChange();
					filterPane.loadAfterChange();
				}
			}
		});
		buttonValidateNow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!ServiceProvider.getInstance().getControlService().getState().contains(States.ANALYSING) && !ServiceProvider.getInstance().getControlService().getState().contains(States.VALIDATING)){
					selectedViolationHistory = null;
					ThreadWithLoader validateThread = ServiceProvider.getInstance().getControlService().getThreadWithLoader(localeService.getTranslatedString("ValidatingLoading"), new CheckConformanceTask(filterPane, buttonSaveInHistory));
					LoadingDialog currentLoader = validateThread.getLoader();
					currentLoader.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosing(WindowEvent e) {
							ServiceProvider.getInstance().getControlService().setValidate(false);
							logger.info("Stopping Thread");
						}
					});

					validateThread.run();
				}
				else {
					//TODO make an error frame that validating or analysing is already running
				}
			}
		});
		buttonSaveInHistory.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String input = JOptionPane.showInputDialog(localeService.getTranslatedString("SaveInHistoryDialog"));
				if (input != null && !input.equals("")) {
					taskServiceImpl.createHistoryPoint(input);
					buttonSaveInHistory.setEnabled(false);
					fillChooseViolationHistoryTable();
				}
			}
		});
		buttonDeleteViolationHistoryPoint.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (selectedViolationHistory != null) {
					taskServiceImpl.removeViolationHistory(selectedViolationHistory.getDate());
					chooseViolationHistoryTable.clearSelection();
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

	private void changeShownViolations() {
		ThreadWithLoader loadingThread = ServiceProvider.getInstance().getControlService().getThreadWithLoader(localeService.getTranslatedString("FilteringLoading"), new LoadViolationHistoryPointsTask(chooseViolationHistoryTable, this, taskServiceImpl, filterPane.getApplyFilter()));
		loadingThread.run();
	}

	private void clearChooseViolationHistoryTableModelRows() {
		while (chooseViolationHistoryTableModel.getRowCount() > 0) {
			chooseViolationHistoryTableModel.removeRow(0);
		}
	}

	public ViolationHistory getSelectedViolationHistory() {
		return selectedViolationHistory;
	}

	public void setSelectedViolationHistory(ViolationHistory selectedViolationHistory) {
		this.selectedViolationHistory = selectedViolationHistory;
	}

	public void reloadViolations() {
		if (selectedViolationHistory != null) {
			shownViolationsAllViolationsPanel = selectedViolationHistory.getViolations();
		} else {
			shownViolationsAllViolationsPanel = taskServiceImpl.getAllViolations().getValue();
		}
		
		shownViolationsAllViolationsPanel = filterPane.fillViolationsTable(shownViolationsAllViolationsPanel);
		if (filterPane.getApplyFilter().isSelected()) {
			shownViolationsAllViolationsPanel = filterViolations(shownViolationsAllViolationsPanel);
		}
	}
	
	public void loadInformationPanel() {
		int violationsSize;
		List<Severity> severities;
		if (selectedViolationHistory == null) {
			violationsSize = taskServiceImpl.getAllViolations().getValue().size();
			severities = taskServiceImpl.getAllSeverities();
		} else {
			violationsSize = selectedViolationHistory.getViolations().size();
			severities = selectedViolationHistory.getSeverities();
		}

		statisticsPanel.loadStatistics(taskServiceImpl.getViolationsPerSeverity(shownViolationsAllViolationsPanel, severities), violationsSize, shownViolationsAllViolationsPanel.size());
		statisticsPanel.repaint();
	}

	private void loadChooseViolationHistoryTableModel() {
		String[] columnNames = {localeService.getTranslatedString("Date"), localeService.getTranslatedString("Description")};
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
		for (ViolationHistory violationHistory : taskServiceImpl.getViolationHistories()) {
			chooseViolationHistoryTableModel.addRow(new Object[] {dateFormat.format(violationHistory.getDate().getTime()), violationHistory.getDescription()});
		}
	}

	public final void loadText() {
		violationInformationPanel.loadGuiText();
		statisticsPanel.loadAfterChange();
		setTitle(localeService.getTranslatedString("BrowseViolations"));
		buttonDeleteViolationHistoryPoint.setText(localeService.getTranslatedString("Remove"));
		buttonLatestViolations.setText(localeService.getTranslatedString("CurrentViolations"));
		buttonSaveInHistory.setText(localeService.getTranslatedString("SaveInHistory"));
		buttonValidateNow.setText(localeService.getTranslatedString("ValidateNow"));
	}

	private void loadModels() {
		loadChooseViolationHistoryTableModel();
		loadViolationsTableModel();
	}



	
}
*/