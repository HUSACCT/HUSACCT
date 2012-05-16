package husacct.validate.presentation;

import husacct.validate.abstraction.language.ValidateTranslator;
import husacct.validate.presentation.tableModels.FilterViolationsObserver;
import husacct.validate.task.TaskServiceImpl;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public final class FilterViolations extends JDialog  {
	private static final long serialVersionUID = -6295611607558238501L;

	private TaskServiceImpl taskServiceImpl;
	private DefaultTableModel ruletypeModelFilter, violationtypeModelFilter, pathFilterModel;
	private JTabbedPane TabbedPane;
	private JButton addPath, removePath, save, cancel;
	private JPanel filterViolationPanel, pathFilterPanel;
	private ButtonGroup filtergroup;
	private JRadioButton hideFilteredValues, showFilteredValues;
	private JScrollPane pathFilterScrollPane, ruletypepanel, violationtypePanel;
	private JTable pathFilterTable, ruletypeTable, violationtypeTable;
	private FilterViolationsObserver vilterViolationsObserver;

	private ArrayList<String> ruletypesfilter = new ArrayList<String>();
	private ArrayList<String> violationtypesfilter = new ArrayList<String>();
	private ArrayList<String> pathsfilter = new ArrayList<String>();
	private Calendar violationDate = null;

	public FilterViolations(TaskServiceImpl taskServiceImpl, FilterViolationsObserver filterViolationsObserver) {
		this.vilterViolationsObserver = filterViolationsObserver;
		this.taskServiceImpl = taskServiceImpl;
		initComponents();
		loadGUIText();
	}

	private void initComponents() {

		filtergroup = new ButtonGroup();
		TabbedPane = new JTabbedPane();
		filterViolationPanel = new JPanel();
		ruletypepanel = new JScrollPane();
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
		setAlwaysOnTop(true);
		setResizable(false);
		setModal(true);

		ruletypeTable.setAutoCreateRowSorter(true);
		ruletypeTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
		ruletypeTable.setFillsViewportHeight(true);
		ruletypeTable.getTableHeader().setResizingAllowed(false);
		ruletypeTable.getTableHeader().setReorderingAllowed(false);
		ruletypepanel.setViewportView(ruletypeTable);
		
		violationtypeTable.setFillsViewportHeight(true);
		violationtypeTable.getTableHeader().setReorderingAllowed(false);
		violationtypePanel.setViewportView(violationtypeTable);

		GroupLayout filterViolationPanelLayout = new GroupLayout(filterViolationPanel);
		filterViolationPanel.setLayout(filterViolationPanelLayout);
		filterViolationPanelLayout.setHorizontalGroup(
				filterViolationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(filterViolationPanelLayout.createSequentialGroup()
						.addComponent(ruletypepanel, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(violationtypePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE))
				);
		filterViolationPanelLayout.setVerticalGroup(
				filterViolationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(ruletypepanel, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
				.addComponent(violationtypePanel)
				);

		pathFilterTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
		pathFilterTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
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

		GroupLayout pathFilterPanelLayout = new GroupLayout(pathFilterPanel);
		pathFilterPanel.setLayout(pathFilterPanelLayout);
		pathFilterPanelLayout.setHorizontalGroup(
				pathFilterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(pathFilterPanelLayout.createSequentialGroup()
						.addComponent(pathFilterScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(pathFilterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addComponent(removePath, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(addPath, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
								.addContainerGap())
				);
		pathFilterPanelLayout.setVerticalGroup(
				pathFilterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(pathFilterScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pathFilterPanelLayout.createSequentialGroup()
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(addPath)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(removePath)
						.addContainerGap())
				);

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

		filtergroup.add(showFilteredValues);

		filtergroup.add(hideFilteredValues);
		hideFilteredValues.setSelected(true);

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(TabbedPane)
				.addGroup(layout.createSequentialGroup()
						.addComponent(hideFilteredValues)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(showFilteredValues)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(save)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(cancel)
						.addContainerGap())
				);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addComponent(TabbedPane)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(save)
										.addComponent(cancel))
										.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(hideFilteredValues)
												.addComponent(showFilteredValues))))
				);

		setSize(800, 600);
	}
	
	public void setViolationDate(Calendar date){
		violationDate = date;
	}
	
	public void loadGUIText(){
		setTitle(ValidateTranslator.getValue("TotalViolations"));
		TabbedPane.addTab(ValidateTranslator.getValue("FilterViolations"), filterViolationPanel);
		addPath.setText(ValidateTranslator.getValue("Add"));
		removePath.setText(ValidateTranslator.getValue("Remove"));
		TabbedPane.addTab(ValidateTranslator.getValue("FilterPaths"), pathFilterPanel);
		save.setText(ValidateTranslator.getValue("Save"));
		cancel.setText(ValidateTranslator.getValue("Cancel"));
		showFilteredValues.setText(ValidateTranslator.getValue("ShowSelectedValues"));
		hideFilteredValues.setText(ValidateTranslator.getValue("HideSelectedValues"));
		
		loadModels();
	}
	
	public void loadModels(){
		String[] columnNamesRuletype = {"", ValidateTranslator.getValue("Ruletypes")};
		String[] columnNamesViolationtype = {"", ValidateTranslator.getValue("Violationtypes")};
		String[] columnNamesPath = {" ", ValidateTranslator.getValue("Path")};
		
		ruletypeModelFilter = new DefaultTableModel(columnNamesRuletype, 0) {

			Class<?>[] types = new Class[]{Boolean.class, String.class};
			boolean[] canEdit = new boolean[]{true, false};

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

			Class<?>[] types = new Class[]{Boolean.class, String.class};
			boolean[] canEdit = new boolean[]{true, false};

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

			Class<?>[] types = new Class[]{Boolean.class, String.class};
			boolean[] canEdit = new boolean[]{true, true};

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
		taskServiceImpl.setFilterValues(ruletypesfilter, violationtypesfilter,
				pathsfilter, hideFilteredValues.isSelected(), violationDate);
		vilterViolationsObserver.updateViolationsTable();
		dispose();
	}
	

	private void addPathActionPerformed() {
		pathFilterModel.addRow(new Object[]{true, ""});
	}

	private void removePathActionPerformed() {
		if (pathFilterTable.getSelectedRow() > -1) {
			pathFilterModel.removeRow(pathFilterTable.getSelectedRow());
		}
	}

	private ArrayList<String> getRuletypesFilter() {
		ArrayList<String> Ruletypes = new ArrayList<String>();
		for (int i = 0; i < ruletypeModelFilter.getRowCount(); i++) {
			if ( (Boolean) ruletypeModelFilter.getValueAt(i, 0)) {
				Ruletypes.add((String) ruletypeModelFilter.getValueAt(i, 1));
			}
		}

		return Ruletypes;
	}

	private ArrayList<String> getViolationtypesFilter() {
		ArrayList<String> violationtypes = new ArrayList<String>();

		for (int i = 0; i < violationtypeModelFilter.getRowCount(); i++) {
			if ( (Boolean) violationtypeModelFilter.getValueAt(i, 0)) {
				violationtypes.add((String)
						violationtypeModelFilter.getValueAt(i, 1));
			}
		}

		return violationtypes;
	}

	private ArrayList<String> getPathFilter() {
		ArrayList<String> paths = new ArrayList<String>();

		for (int i = 0; i < pathFilterModel.getRowCount(); i++) {
			if ( (Boolean) pathFilterModel.getValueAt(i, 0)) {
				paths.add((String) pathFilterModel.getValueAt(i, 1));
			}
		}

		return paths;
	}
	
	public void loadFilterValues(){
		loadRuletypes();
		loadViolationtypes();
	}

	private void loadRuletypes(){
		while(ruletypeModelFilter.getRowCount() > 0){
			ruletypeModelFilter.removeRow(0);
		}
		ArrayList<String> ruletypes = taskServiceImpl.loadRuletypesForFilter(violationDate);
		for(String ruletype : ruletypes){
			ruletypeModelFilter.addRow(new Object[]{false, ruletype});
		}
	}

	private void loadViolationtypes(){
		while(violationtypeModelFilter.getRowCount() > 0){
			violationtypeModelFilter.removeRow(0);
		}
		ArrayList<String> violationtypes = taskServiceImpl.loadViolationtypesForFilter(violationDate);
		for(String violationtype : violationtypes){
			violationtypeModelFilter.addRow(new Object[]{false, violationtype});
		}
	}
	
}