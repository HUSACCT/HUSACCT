package husacct.validate.presentation;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.control.ControlServiceImpl;
import husacct.control.presentation.util.DialogUtils;
import husacct.validate.domain.validation.internaltransferobjects.PathDTO;
import husacct.validate.presentation.tableModels.FilterViolationsObserver;
import husacct.validate.task.TaskServiceImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle;
import javax.swing.table.DefaultTableModel;

public final class FilterViolations extends JDialog {

	private static final long serialVersionUID = -6295611607558238501L;
	@SuppressWarnings("unused")
	private boolean selectedFilterValues = true;
	private TaskServiceImpl taskServiceImpl;
	private DefaultTableModel ruletypeModelFilter, violationtypeModelFilter, pathFilterModel;
	private JTabbedPane tabbedPane;
	private JButton addPath, removePath, save, cancel;
	private JPanel filterViolationPanel, pathFilterPanel;
	private ButtonGroup filtergroup;
	private JRadioButton hideFilteredValues, showFilteredValues;
	private JScrollPane pathFilterScrollPane, ruletypePanel, violationtypePanel;
	private JTable pathFilterTable, ruletypeTable, violationtypeTable;
	private FilterViolationsObserver filterViolationsObserver;
	private ArrayList<String> ruletypesfilter = new ArrayList<String>();
	private ArrayList<String> violationtypesfilter = new ArrayList<String>();
	private ArrayList<String> pathsfilter = new ArrayList<String>();
	private Calendar violationDate = Calendar.getInstance();
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();

	public FilterViolations(TaskServiceImpl taskServiceImpl, FilterViolationsObserver filterViolationsObserver) {
		super(((ControlServiceImpl) ServiceProvider.getInstance().getControlService()).getMainController().getMainGui(), true);
		this.filterViolationsObserver = filterViolationsObserver;
		this.taskServiceImpl = taskServiceImpl;
		initComponents();
		loadGUIText();
	}

	private void initComponents() {
		filtergroup = new ButtonGroup();
		tabbedPane = new JTabbedPane();
		filterViolationPanel = new JPanel();
		ruletypePanel = new JScrollPane();
		ruletypeTable = new JTable();
		violationtypePanel = new JScrollPane();
		violationtypeTable = new JTable();
		pathFilterPanel = new JPanel();
		pathFilterScrollPane = new JScrollPane();
		pathFilterTable = new JTable();
		addPath = new JButton();
		removePath = new JButton();
		save = new JButton();
		cancel = new JButton();
		showFilteredValues = new JRadioButton();
		hideFilteredValues = new JRadioButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setModal(true);
		DialogUtils.alignCenter(this);

		ruletypeTable.setAutoCreateRowSorter(true);
		ruletypeTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
		ruletypeTable.setFillsViewportHeight(true);
		ruletypeTable.getTableHeader().setResizingAllowed(false);
		ruletypeTable.getTableHeader().setReorderingAllowed(false);
		ruletypePanel.setViewportView(ruletypeTable);

		violationtypeTable.setFillsViewportHeight(true);
		violationtypeTable.getTableHeader().setReorderingAllowed(false);
		violationtypePanel.setViewportView(violationtypeTable);

		pathFilterTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
		pathFilterTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		pathFilterTable.setFillsViewportHeight(true);
		pathFilterTable.getTableHeader().setResizingAllowed(false);
		pathFilterTable.getTableHeader().setReorderingAllowed(false);
		pathFilterScrollPane.setViewportView(pathFilterTable);

		addPath.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				addPathActionPerformed();
			}
		});

		removePath.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				removePathActionPerformed();
			}
		});

		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				saveActionPerformed();
			}
		});

		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				cancelActionPerformed();
			}
		});
		
		hideFilteredValues.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				setSelectedFilterValues(false);
			}
		});
		
		showFilteredValues.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				setSelectedFilterValues(true);
			}
		});

		filtergroup.add(showFilteredValues);

		filtergroup.add(hideFilteredValues);
		hideFilteredValues.setSelected(true);

		createFilterViolationPanelLayout();
		createPathFilterPanelLayout();
		createBaseLayout();
		setSize(800, 600);
	}
	
	private void setSelectedFilterValues(boolean value) {
		this.selectedFilterValues = value;
	}

	private void createFilterViolationPanelLayout() {
		GroupLayout filterViolationPanelLayout = new GroupLayout(filterViolationPanel);

		GroupLayout.SequentialGroup horizontalFilterViolationGroup = filterViolationPanelLayout.createSequentialGroup();
		horizontalFilterViolationGroup.addComponent(ruletypePanel);
		horizontalFilterViolationGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		horizontalFilterViolationGroup.addComponent(violationtypePanel);

		filterViolationPanelLayout.setHorizontalGroup(horizontalFilterViolationGroup);

		GroupLayout.ParallelGroup verticalFilterViolationGroup = filterViolationPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING);
		verticalFilterViolationGroup.addComponent(ruletypePanel);
		verticalFilterViolationGroup.addComponent(violationtypePanel);

		filterViolationPanelLayout.setVerticalGroup(verticalFilterViolationGroup);
		filterViolationPanel.setLayout(filterViolationPanelLayout);
	}

	private void createPathFilterPanelLayout() {
		GroupLayout pathFilterPanelLayout = new GroupLayout(pathFilterPanel);

		GroupLayout.ParallelGroup horizontalButtonPathGroup = pathFilterPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false);
		horizontalButtonPathGroup.addComponent(removePath, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
		horizontalButtonPathGroup.addComponent(addPath, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);

		GroupLayout.SequentialGroup horizontalPanePathGroup = pathFilterPanelLayout.createSequentialGroup();
		horizontalPanePathGroup.addComponent(pathFilterScrollPane);
		horizontalPanePathGroup.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED);
		horizontalPanePathGroup.addGroup(horizontalButtonPathGroup);
		horizontalPanePathGroup.addContainerGap();

		pathFilterPanelLayout.setHorizontalGroup(horizontalPanePathGroup);

		GroupLayout.SequentialGroup verticalButtonPathGroup = pathFilterPanelLayout.createSequentialGroup();
		verticalButtonPathGroup.addComponent(addPath);
		verticalButtonPathGroup.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED);
		verticalButtonPathGroup.addComponent(removePath);
		verticalButtonPathGroup.addContainerGap();

		GroupLayout.ParallelGroup verticalPanePathGroup = pathFilterPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING);
		verticalPanePathGroup.addComponent(pathFilterScrollPane);
		verticalPanePathGroup.addGroup(verticalButtonPathGroup);

		pathFilterPanelLayout.setVerticalGroup(verticalPanePathGroup);
		pathFilterPanel.setLayout(pathFilterPanelLayout);
	}

	private void createBaseLayout() {
		GroupLayout layout = new GroupLayout(getContentPane());

		GroupLayout.SequentialGroup horizontalButtonGroup = layout.createSequentialGroup();
		horizontalButtonGroup.addComponent(hideFilteredValues);
		horizontalButtonGroup.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED);
		horizontalButtonGroup.addComponent(showFilteredValues);
		horizontalButtonGroup.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED);
		horizontalButtonGroup.addComponent(save);
		horizontalButtonGroup.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED);
		horizontalButtonGroup.addComponent(cancel);
		horizontalButtonGroup.addContainerGap();

		GroupLayout.ParallelGroup horizontalPaneGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		horizontalPaneGroup.addComponent(tabbedPane);
		horizontalPaneGroup.addGroup(horizontalButtonGroup);

		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(horizontalPaneGroup);

		GroupLayout.ParallelGroup verticalRadioButtonGroup = layout.createParallelGroup(GroupLayout.Alignment.TRAILING);
		verticalRadioButtonGroup.addComponent(hideFilteredValues);
		verticalRadioButtonGroup.addComponent(showFilteredValues);

		GroupLayout.ParallelGroup verticalButtonGroup = layout.createParallelGroup(GroupLayout.Alignment.TRAILING);
		verticalButtonGroup.addComponent(save);
		verticalButtonGroup.addComponent(cancel);
		verticalButtonGroup.addGroup(verticalRadioButtonGroup);

		GroupLayout.SequentialGroup verticalPaneGroup = layout.createSequentialGroup();
		verticalPaneGroup.addComponent(tabbedPane);
		verticalPaneGroup.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED);
		verticalPaneGroup.addGroup(verticalButtonGroup);

		layout.setVerticalGroup(verticalPaneGroup);
	}

	public void setViolationDate(Calendar date) {
		violationDate = date;
	}

	public void loadGUIText() {
		setTitle(localeService.getTranslatedString("TotalViolations"));
		tabbedPane.addTab(localeService.getTranslatedString("FilterViolations"), filterViolationPanel);
		addPath.setText(localeService.getTranslatedString("Add"));
		removePath.setText(localeService.getTranslatedString("Remove"));
		tabbedPane.addTab(localeService.getTranslatedString("FilterPaths"), pathFilterPanel);
		save.setText(localeService.getTranslatedString("Save"));
		cancel.setText(localeService.getTranslatedString("Cancel"));
		showFilteredValues.setText(localeService.getTranslatedString("ShowSelectedValues"));
		hideFilteredValues.setText(localeService.getTranslatedString("HideSelectedValues"));

		loadModels();
	}

	public void loadModels() {
		String[] columnNamesRuletype = {"", localeService.getTranslatedString("Ruletypes")};
		String[] columnNamesViolationtype = {"", localeService.getTranslatedString("Violationtypes")};
		String[] columnNamesPath = {" ", localeService.getTranslatedString("Path")};

		ruletypeModelFilter = new DefaultTableModel(columnNamesRuletype, 0) {
			private static final long serialVersionUID = -7173080075671054375L;
			Class<?>[] types = new Class[] {Boolean.class, String.class};
			boolean[] canEdit = new boolean[] {true, false};

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		};

		violationtypeModelFilter = new DefaultTableModel(columnNamesViolationtype, 0) {
			private static final long serialVersionUID = -9191282154177444964L;
			Class<?>[] types = new Class[] {Boolean.class, String.class};
			boolean[] canEdit = new boolean[] {true, false};

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		};

		pathFilterModel = new DefaultTableModel(columnNamesPath, 0) {
			private static final long serialVersionUID = 1832644249597223838L;
			Class<?>[] types = new Class[] {Boolean.class, String.class};
			boolean[] canEdit = new boolean[] {true, true};

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		};

		ruletypeTable.setModel(ruletypeModelFilter);
		violationtypeTable.setModel(violationtypeModelFilter);
		pathFilterTable.setModel(pathFilterModel);

		loadFilterValues();
	}

	private void cancelActionPerformed() {
		dispose();
	}

	private void saveActionPerformed() {
		ruletypesfilter = getRuletypesFilter();
		violationtypesfilter = getViolationtypesFilter();
		pathsfilter = getPathFilter();
		if (!checkPathsNames()) {
			return;
		}
		PathDTO dto = new PathDTO(ruletypesfilter, violationtypesfilter, pathsfilter);
		taskServiceImpl.setFilterValues(dto, hideFilteredValues.isSelected(), violationDate);
		filterViolationsObserver.updateViolationsTable();
		dispose();
	}

	private void addPathActionPerformed() {
		pathFilterModel.addRow(new Object[] {true, ""});
	}

	private void removePathActionPerformed() {
		if (pathFilterTable.getSelectedRow() > -1) {
			pathFilterModel.removeRow(pathFilterTable.getSelectedRow());
		}
	}

	private ArrayList<String> getRuletypesFilter() {
		ArrayList<String> Ruletypes = new ArrayList<String>();
		for (int i = 0; i < ruletypeModelFilter.getRowCount(); i++) {
			if ((Boolean) ruletypeModelFilter.getValueAt(i, 0)) {
				Ruletypes.add((String) ruletypeModelFilter.getValueAt(i, 1));
			}
		}

		return Ruletypes;
	}

	private ArrayList<String> getViolationtypesFilter() {
		ArrayList<String> violationtypes = new ArrayList<String>();

		for (int i = 0; i < violationtypeModelFilter.getRowCount(); i++) {
			if ((Boolean) violationtypeModelFilter.getValueAt(i, 0)) {
				violationtypes.add((String) violationtypeModelFilter.getValueAt(i, 1));
			}
		}

		return violationtypes;
	}

	private ArrayList<String> getPathFilter() {
		ArrayList<String> paths = new ArrayList<String>();

		for (int i = 0; i < pathFilterModel.getRowCount(); i++) {
			if ((Boolean) pathFilterModel.getValueAt(i, 0)) {
				paths.add((String) pathFilterModel.getValueAt(i, 1));
			}
		}

		return paths;
	}

	public void loadFilterValues() {
		loadRuletypes();
		loadViolationtypes();
	}

	private void loadRuletypes() {
		ArrayList<String> enabledRuleTypes = taskServiceImpl.getEnabledFilterRuleTypes();
		while (ruletypeModelFilter.getRowCount() > 0) {
			ruletypeModelFilter.removeRow(0);
		}
		ArrayList<String> ruletypes = taskServiceImpl.loadRuletypesForFilter(violationDate);
		boolean isEnabled;
		for (String ruletype : ruletypes) {
			isEnabled = false;
			if (enabledRuleTypes.contains(ruletype)){
				isEnabled = true;
			}
			ruletypeModelFilter.addRow(new Object[] {isEnabled, ruletype});
		}
	}

	private void loadViolationtypes() {
		ArrayList<String> enabledViolations = taskServiceImpl.getEnabledFilterViolations();
		while (violationtypeModelFilter.getRowCount() > 0) {
			violationtypeModelFilter.removeRow(0);
		}
		ArrayList<String> violationtypes = taskServiceImpl.loadViolationtypesForFilter(violationDate);
		boolean isEnabled;
		for (String violationtype : violationtypes) {
			if (!violationtype.isEmpty()) {
				isEnabled = false;
				if (enabledViolations.contains(violationtype)){
					isEnabled = true;
				}
				violationtypeModelFilter.addRow(new Object[] {isEnabled, violationtype});
			}
		}
	}

	private boolean checkPathsNames() {
		if (pathsfilter.isEmpty()) {
			return true;
		}
		boolean returnValue = true;
		for (String path : pathsfilter) {
			if (path.isEmpty()) {
				returnValue = false;
			}
		}
		if (!returnValue) {
			ServiceProvider.getInstance().getControlService().showInfoMessage(String.format(localeService.getTranslatedString("EmptyField"), ServiceProvider.getInstance().getLocaleService().getTranslatedString("Path")));
		}
		return returnValue;
	}
}