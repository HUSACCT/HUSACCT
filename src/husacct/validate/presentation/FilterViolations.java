package husacct.validate.presentation;

import husacct.validate.abstraction.language.ResourceBundles;
import husacct.validate.task.TaskServiceImpl;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FilterViolations extends JFrame {

	private static final long serialVersionUID = -6295611607558238501L;
	
	private TaskServiceImpl ts;
	private BrowseViolations bv;
	private DefaultTableModel ruletypeModelFilter, violationtypeModelFilter, pathFilterModel;
	private JTabbedPane TabbedPane;
	private JButton addPath, removePath, save, cancel;
	private JPanel filterViolationPanel, pathFilterPanel;
	private ButtonGroup filtergroup;
	private JRadioButton hideFilteredValues, showFilteredValues;
	private JScrollPane pathFilterScrollPane, ruletypepanel, violationtypePanel;
	private JTable pathFilterTable, ruletypeTable, violationtypeTable;

	private ArrayList<String> ruletypesfilter = new ArrayList<String>();
	private ArrayList<String> violationtypesfilter = new ArrayList<String>();
	private ArrayList<String> pathsfilter = new ArrayList<String>();

	public FilterViolations(TaskServiceImpl ts, BrowseViolations bv) {
		this.ts = ts;
		this.bv = bv;
		String[] columnNamesRuletype = {"", ResourceBundles.getValue("Ruletypes")};
		ruletypeModelFilter = new DefaultTableModel(columnNamesRuletype, 0) {

			private static final long serialVersionUID = -2752815747553087143L;
			
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

		String[] columnNamesViolationtype = {"", ResourceBundles.getValue("Violationtypes")};
		violationtypeModelFilter = new DefaultTableModel(columnNamesViolationtype, 0) {

			private static final long serialVersionUID = -2076057432618819613L;
			
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

		String[] columnNamesPath = {" ", ResourceBundles.getValue("Path")};
		pathFilterModel = new DefaultTableModel(columnNamesPath, 0) {

			private static final long serialVersionUID = 8399838627659517010L;
			
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
		initComponents();
		loadRuletypes();
		loadViolationtypes();
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
		setTitle(ResourceBundles.getValue("TotalViolations"));
		setAlwaysOnTop(true);
		setResizable(false);

		ruletypeTable.setAutoCreateRowSorter(true);
		ruletypeTable.setModel(ruletypeModelFilter);
		ruletypeTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
		ruletypeTable.setFillsViewportHeight(true);
		ruletypeTable.getTableHeader().setResizingAllowed(false);
		ruletypeTable.getTableHeader().setReorderingAllowed(false);
		ruletypepanel.setViewportView(ruletypeTable);

		violationtypeTable.setModel(violationtypeModelFilter);
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

		TabbedPane.addTab(ResourceBundles.getValue("FilterViolations"), filterViolationPanel);

		pathFilterTable.setModel(pathFilterModel);
		pathFilterTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
		pathFilterTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		pathFilterScrollPane.setViewportView(pathFilterTable);

		addPath.setText(ResourceBundles.getValue("Add"));
		addPath.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				addPathActionPerformed();
			}
		});

		removePath.setText(ResourceBundles.getValue("Remove"));
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

		TabbedPane.addTab(ResourceBundles.getValue("FilterPaths"), pathFilterPanel);

		save.setText(ResourceBundles.getValue("Save"));
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				saveActionPerformed();
			}
		});

		cancel.setText(ResourceBundles.getValue("Cancel"));
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				cancelActionPerformed();
			}
		});

		filtergroup.add(showFilteredValues);
		showFilteredValues.setText(ResourceBundles.getValue("ShowSelectedValues"));

		filtergroup.add(hideFilteredValues);
		hideFilteredValues.setSelected(true);
		hideFilteredValues.setText(ResourceBundles.getValue("HideSelectedValues"));

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

		pack();
	}// </editor-fold>

	private void cancelActionPerformed() {
		dispose();
	}

	private void saveActionPerformed() {
		ruletypesfilter = getRuletypesFilter();
		violationtypesfilter = getViolationtypesFilter();
		pathsfilter = getPathFilter();
		ts.setFilterValues(ruletypesfilter, violationtypesfilter,
				pathsfilter, hideFilteredValues.isSelected());
		bv.setViolations();
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

	private void loadRuletypes(){
		ArrayList<String> ruletypes = ts.loadRuletypes();
		for(String ruletype : ruletypes){
			ruletypeModelFilter.addRow(new Object[]{false, ruletype});
		}
	}

	private void loadViolationtypes(){
		ArrayList<String> violationtypes = ts.loadViolationtypes();
		for(String violationtype : violationtypes){
			violationtypeModelFilter.addRow(new Object[]{false, violationtype});
		}
	}
}