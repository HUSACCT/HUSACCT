package husacct.analyse.presentation.reconstruct;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import husacct.ServiceProvider;
import husacct.analyse.presentation.reconstruct.parameter.ReconstructArchitectureParameterPanel;
import husacct.analyse.task.AnalyseTaskControl;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants.Algorithm;
import husacct.analyse.task.reconstruct.parameters.ReconstructArchitectureParameterDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ReconstructArchitectureDTO;
import husacct.common.help.presentation.HelpableJPanel;
import husacct.common.locale.ILocaleService;

import javax.swing.JTabbedPane;
import javax.swing.border.LineBorder;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.log4j.Logger;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.BorderLayout;

import javax.swing.JTable;

import javax.swing.ListSelectionModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.io.IOException;


public class ApproachesTableJPanel extends HelpableJPanel {
	private static final long serialVersionUID = 1L;
	private final Logger logger = Logger.getLogger(ApproachesTableJPanel.class);
	private TableColumnModel tableAllApproachesColumnModel;
	private TableColumnModel tableDistinctApproachesColumnModel;
	private ILocaleService localService;
	private String approachesConstants = AnalyseReconstructConstants.ApproachesTable.ApproachesConstants;

	private ReconstructJPanel panel;

	private AnalyseTaskControl analyseTaskControl;

	public JTabbedPane tabbedPane;
	public JTable tableAllApproaches;
	public JTable tableDistinctApproaches;
	public ButtonGroup RadioButtonsRelationType;
	public ButtonGroup radioButtonGroupTwo;
	private JTable distinctParameterTable;
	public JTable allParameterTable;

	/**
	 * Create the panel.
	 * @throws IOException 
	 */

	public ApproachesTableJPanel(AnalyseTaskControl atc, ReconstructJPanel panel) throws IOException {
		super();
		analyseTaskControl = atc;
		localService = ServiceProvider.getInstance().getLocaleService();
		initUI();
		this.panel = panel;
		logger.info("Build Reconstruct tables");
	}
	
	public void initUI() throws IOException{		
		setLayout(new BorderLayout(0, 10));
		
				Object[][] tempData = {
			{"", ""},
		};
		
		Object[] cols = {
			"Parameter", "Value"	
		};
		distinctParameterTable = new JTable(tempData, getParameterTableColumnName());
		distinctParameterTable.setEnabled(false);
		distinctParameterTable.setVisible(false);
		allParameterTable = new JTable(tempData, cols);
		allParameterTable.setEnabled(false);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
		tabbedPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		tabbedPane.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e){
				if(panel != null){
					JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
					Component selectedTappedPane = tabbedPane.getSelectedComponent();
					JPanel selectedPanel = (JPanel) selectedTappedPane;
					if(selectedPanel.getName().equals("Mojo")){
						panel.setButtonVisibility(false);
					}
					else if (selectedPanel.getName().equals("DistinctApproaches")){
						panel.setDistinctApproachesVisibility();
					}
					else{
						panel.setButtonVisibility(true);
					}
				}
			}
			
		});
		add(tabbedPane);
				

		JPanel allApproachedPanel = new JPanel();
		allApproachedPanel.setName(AnalyseReconstructConstants.ApproachesTable.PanelAllApproaches);
		allApproachedPanel.setLayout(new BorderLayout(0, 0));
		
		Object columnNames[] = getColumnNames();
		Object rows[][] = getAllApproachesRows();
		tableAllApproaches = new JTable(rows, columnNames){
			public boolean isCellEditable(int row, int col){
				return false;
			}
		};
		tableAllApproaches.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableAllApproaches.setName("tableAllApproaches");
		tableAllApproaches.setMinimumSize(new Dimension(600,150));
		tableAllApproachesColumnModel = tableAllApproaches.getColumnModel();
		hide(approachesConstants, tableAllApproachesColumnModel);
		Dimension tableSize = tableAllApproaches.getPreferredSize();
		
		JScrollPane allApproachesTableScrollPane = new JScrollPane(tableAllApproaches);
		int tableheight = tableSize.height + tableAllApproaches.getRowHeight()*tableAllApproaches.getRowCount()+50;
		allApproachesTableScrollPane.setPreferredSize(new Dimension(tableSize.width, tableheight));
		allApproachedPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane allApproachesParameterTable = new JScrollPane(allParameterTable);
		allApproachesParameterTable.setPreferredSize(new Dimension(225, 114));
		allApproachedPanel.add(allApproachesParameterTable, BorderLayout.CENTER);
		allApproachedPanel.add(allApproachesTableScrollPane, BorderLayout.NORTH);
		
		tableAllApproaches.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int selectedRow = tableAllApproaches.getSelectedRow();
				if (!e.getValueIsAdjusting() && selectedRow >= 0) {
					
					
					ReconstructArchitectureDTO dto = new ReconstructArchitectureDTO();
					ModuleDTO selectedModule = getSelectedModule();
					String approachConstant = (String) tableAllApproaches.getModel().getValueAt(selectedRow, 0);
					dto = analyseTaskControl.getReconstructArchitectureDTOList().getReconstructArchitectureDTO(approachConstant);
					dto.setSelectedModule(selectedModule);
					
					HashMap<String, Object> rowData = getParameters(dto);
					DefaultTableModel defaultTableModel = new DefaultTableModel();
					defaultTableModel.addColumn("Parameter");
					defaultTableModel.addColumn("Value");
					
					Iterator<Entry<String, Object>> iterator = rowData.entrySet().iterator();
					int i = 0;
					while(iterator.hasNext()){
						Object[] empty = {"", ""};
						defaultTableModel.addRow(empty);
						HashMap.Entry<String, Object> entry = (HashMap.Entry<String, Object>) iterator.next();
						Object[] temp = {entry.getKey(), entry.getValue()};
						defaultTableModel.setValueAt(temp[0], i, 0);
						defaultTableModel.setValueAt(temp[1], i, 1);
						i++;
					}
					
					allParameterTable.setModel(defaultTableModel);
				}
			}
		});
		
		/*
		 * Generic tab
		 */
		
		JPanel distinctApproachesPanel = new JPanel();
		distinctApproachesPanel.setName(AnalyseReconstructConstants.ApproachesTable.PanelDistinctApproaches);
		distinctApproachesPanel.setLayout(new BorderLayout(0, 0));
		
		Object distinctApproachesColumns[] = getColumnNames();
		Object distinctApproachesRows[][] = getDistinctApproachesRows();
		
		tableDistinctApproaches = new JTable(distinctApproachesRows, distinctApproachesColumns){
			public boolean isCellEditable(int row, int col){
				return false;
			}
		};
		
		tableDistinctApproaches.setMinimumSize(new Dimension(600,150));
		tableDistinctApproaches.setName("tableDistinctApproaches");
		tableDistinctApproachesColumnModel = tableDistinctApproaches.getColumnModel();
		tableDistinctApproaches.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		hide(approachesConstants, tableDistinctApproachesColumnModel);
		Dimension distictApproachesTableSize = tableDistinctApproaches.getPreferredSize();
		
		JScrollPane distinctScrollPane = new JScrollPane(tableDistinctApproaches);
		distinctScrollPane.setPreferredSize(new Dimension(distictApproachesTableSize.width, tableDistinctApproaches.getRowHeight()*tableDistinctApproaches.getRowCount()+50));
		
		distinctApproachesPanel.setLayout(new BorderLayout(0, 0));
		distinctApproachesPanel.add(distinctScrollPane, BorderLayout.NORTH);
		
		
		String distinctApprTranslation = getTranslation(AnalyseReconstructConstants.ApproachesTable.PanelDistinctApproaches);
		String allApprTranslation = getTranslation(AnalyseReconstructConstants.ApproachesTable.PanelAllApproaches);
		
		MojoJPanel mojoPanel = new MojoJPanel(/*analyseTaskControl*/);
		tabbedPane.addTab(distinctApprTranslation, null, distinctApproachesPanel, null);
		tabbedPane.addTab(allApprTranslation, null, allApproachedPanel, null);
		tabbedPane.addTab("Mojo",null, mojoPanel.createMojoPanel(),null);
		
		JScrollPane parameterTableScrollPane = new JScrollPane(distinctParameterTable);
		distinctApproachesPanel.add(parameterTableScrollPane, BorderLayout.CENTER);
		parameterTableScrollPane.setPreferredSize(new Dimension(225, 114));
		
		tableDistinctApproaches.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int selectedRow = tableDistinctApproaches.getSelectedRow();
				if (!e.getValueIsAdjusting() && selectedRow >=0 ) {
					
					ReconstructArchitectureDTO dto = new ReconstructArchitectureDTO();
					ModuleDTO selectedModule = getSelectedModule();
					String approachConstant = (String) tableDistinctApproaches.getModel().getValueAt(selectedRow, 0);
					dto = analyseTaskControl.getReconstructArchitectureDTOList().getReconstructArchitectureDTO(approachConstant);
					dto.setSelectedModule(selectedModule);
					
					HashMap<String, Object> rowData = getParameters(dto);
					DefaultTableModel defaultTableModel = new DefaultTableModel();
					defaultTableModel.addColumn("Parameter");
					defaultTableModel.addColumn("Value");
					
					Iterator<Entry<String, Object>> iterator = rowData.entrySet().iterator();
					int i = 0;
					while(iterator.hasNext()){
						Object[] empty = {"", ""};
						defaultTableModel.addRow(empty);
						HashMap.Entry<String, Object> entry = (HashMap.Entry<String, Object>) iterator.next();
						Object[] temp = {entry.getKey(), entry.getValue()};
						defaultTableModel.setValueAt(temp[0], i, 0);
						defaultTableModel.setValueAt(temp[1], i, 1);
						i++;
					}
					
					distinctParameterTable.setModel(defaultTableModel);
				}
			}
		});
	}
	
	private Object[][] getAllApproachesRows(){
		ArrayList<Object[]> approachesRowsList = new ArrayList<>();
		analyseTaskControl.createReconstructArchitectureList();
		analyseTaskControl.getReconstructArchitectureDTOList().createDynamicReconstructArchitectureDTOs();
		for (ReconstructArchitectureDTO dto : analyseTaskControl.getReconstructArchitectureDTOList().ReconstructArchitectureDTOList){
			Object[] rowObject = {dto.approachConstant, dto.getTranslation()};
			approachesRowsList.add(rowObject);
		}
		
		Object[][] approachesRows = new Object[approachesRowsList.size()][];
		approachesRows = approachesRowsList.toArray(approachesRows);
		return approachesRows;
	}
	
	private Object[][] getDistinctApproachesRows(){
		Object ApproachesRows[][] = { 
				{Algorithm.Component_HUSACCT_SelectedModule, getTranslation(Algorithm.Component_HUSACCT_SelectedModule)},
				{Algorithm.Externals_Recognition, getTranslation(Algorithm.Externals_Recognition)},
				{Algorithm.Layers_Goldstein_HUSACCT_SelectedModule, getTranslation(Algorithm.Layers_Goldstein_HUSACCT_SelectedModule)},
				{Algorithm.Layers_HUSACCT_SelectedModule, getTranslation(Algorithm.Layers_HUSACCT_SelectedModule)},
				{Algorithm.Layers_Scanniello_Improved, getTranslation(Algorithm.Layers_Scanniello_Improved)}};
			return ApproachesRows;
	}
	
	
	
	private Object[] getColumnNames(){
		String approachesTranslation = localService.getTranslatedString("Approaches");
	
		Object columnNames[] = {
				approachesConstants, 
				approachesTranslation
		};
		return columnNames;
	}
	
	private HashMap<String, Object> getParameters(ReconstructArchitectureDTO dto){		
		String selectedModuleName = dto.getSelectedModule().name;
		
		if(selectedModuleName.equals("") || selectedModuleName.equals("SoftwareArchitecture")){
			selectedModuleName = getTranslation("Root");
		}
		
		HashMap<String, Object> parameterRows = new HashMap<>();
		
		for(ReconstructArchitectureParameterDTO parameterDTO : dto.parameterDTOs){
			Object value = ReconstructArchitectureParameterPanel.getValueFromReconstructArchitectureDTO(parameterDTO.parameterConstant, dto);
				
			if(value != null && !value.toString().isEmpty()){
				parameterRows.put(parameterDTO.parameterConstant, value);
			} else{
				parameterRows.put(parameterDTO.parameterConstant, parameterDTO.defaultValue);
			}
		}
		
		return parameterRows;
	}
	
	private String[] getParameterTableColumnName(){
		String columnNames[] = {
				"Parameter", "Value"
		};
		
		return columnNames;
	}
	
	private String getTranslation(String translationKey){
		ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
		return localeService.getTranslatedString(translationKey);
	}
	
    private void hide(String columnName, TableColumnModel tableColumnModel) {
        int index = tableColumnModel.getColumnIndex(columnName);
        TableColumn column = tableColumnModel.getColumn(index);
        tableColumnModel.removeColumn(column);
    }

	private ModuleDTO getSelectedModule(){
		return ServiceProvider.getInstance().getDefineService().getSarService().getModule_SelectedInGUI();
	}
}