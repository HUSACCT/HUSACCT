package husacct.analyse.presentation.reconstruct;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import husacct.ServiceProvider;
import husacct.analyse.task.AnalyseTaskControl;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants.Algorithm;
import husacct.common.OSDetector;
import husacct.common.dto.ReconstructArchitectureDTO;
import husacct.common.help.presentation.HelpableJPanel;
import husacct.common.locale.ILocaleService;

import javax.swing.JTabbedPane;
import javax.swing.border.LineBorder;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.log4j.Logger;
import javax.swing.table.TableModel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.BorderLayout;

import javax.swing.JTable;

import javax.swing.ListSelectionModel;

import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;


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
	private JTable allParameterTable;

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
		
		//String parameterColumnNames[] = getParameterTableColumnName();
		Object[][] tempData = {
			{"test", "test"},
			{"test 1", "test 1"}
		};
		
		Object[] cols = {
			"parameter", "value"	
		};
		distinctParameterTable = new JTable(tempData, cols);
		allParameterTable = new JTable(tempData, cols);
		
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
		tableAllApproaches = new JTable(rows, columnNames);    
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
				if (!e.getValueIsAdjusting()) {
					/*int row = tableAllApproaches.getSelectedRow();
					String data = (String) tableAllApproaches.getValueAt(row, 0);
					Object[][] rowData = getParameters(data);
					TableModel model = allParameterTable.getModel();
					
					for(int i = 0; i < rowData.length; i++){
						Object[] temp = rowData[i];
						model.setValueAt(temp[0], i, 0);
						model.setValueAt(temp[1], i, 1);
					}
					
					allParameterTable.setModel(model);*/
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
		tableDistinctApproaches = new JTable(distinctApproachesRows, distinctApproachesColumns);
		tableDistinctApproaches.setMinimumSize(new Dimension(600,150));
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
				if (!e.getValueIsAdjusting()) {
					int row = tableDistinctApproaches.getSelectedRow();
					String data = (String) tableDistinctApproaches.getValueAt(row, 0);
					Object[][] rowData = getParameters(data);
					TableModel model = distinctParameterTable.getModel();
					
					for(int i = 0; i < rowData.length; i++){
						Object[] temp = rowData[i];
						model.setValueAt(temp[0], i, 0);
						model.setValueAt(temp[1], i, 1);
					}
					
					distinctParameterTable.setModel(model);
				}
			}
		});
		
		String appFolder = OSDetector.getAppFolder();;
		String filePath = appFolder + "\\thresholds.config";
		File f = new File(filePath);
		
		//needs to be moved
		if(f.exists()){
			int rowCount = tableDistinctApproaches.getModel().getRowCount() - 1;
			Scanner scanner = new Scanner(f);
			scanner.useDelimiter(";");
			int lineCounter = 0;
			
			while(scanner.hasNextLine()){
				Object line = scanner.next();
				tableDistinctApproaches.getModel().setValueAt(line, lineCounter, 2);
				lineCounter++;
			}
			scanner.close();
		} else{
			f.createNewFile();
		}
		
    	tableDistinctApproaches.getModel().addTableModelListener(new TableModelListener() {

  	      public void tableChanged(TableModelEvent e) {
  	        int rowCount = tableDistinctApproaches.getModel().getRowCount() - 1;
			
				PrintWriter writer;
				try {
					writer = new PrintWriter(f);
					 for(int i = 0; i <= rowCount; i++){
		  	        	 Object thresholdValue = tableDistinctApproaches.getModel().getValueAt(i, 2);
		  	        	 String thresholdValueString = thresholdValue.toString();
		  	        	 writer.print(";" + thresholdValueString);
		  	         }
					 writer.close();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
  	      }
  	    });
	}
	
	
	/*private Object[][] getAllApproachesRows(){
        Object ApproachesRows[][] = { 
            {Algorithm.Component_HUSACCT_SelectedModule, getTranslation(Algorithm.Component_HUSACCT_SelectedModule), 10},
            {Algorithm.Gateways_HUSACCT_Root, getTranslation(Algorithm.Gateways_HUSACCT_Root), 10},
            {Algorithm.Externals_Recognition, getTranslation(Algorithm.Externals_Recognition), 10},

            {Algorithm.Layers_Goldstein_Multiple_Improved, getTranslation(Algorithm.Layers_Goldstein_Multiple_Improved), 10}, 
            {Algorithm.Layers_Goldstein_Original, getTranslation(Algorithm.Layers_Goldstein_Original), 10}, 
            {Algorithm.Layers_Goldstein_Root_Improved, getTranslation(Algorithm.Layers_Goldstein_Root_Improved), 10},
                
            {Algorithm.Layers_Scanniello_Original, getTranslation(Algorithm.Layers_Scanniello_Original), 10}, 
            {Algorithm.Layers_Scanniello_Improved, getTranslation(Algorithm.Layers_Scanniello_Improved), 10}};
                
        return ApproachesRows;
    }*/
	
	private Object[][] getAllApproachesRows(){
		ArrayList<Object[]> approachesRowsList = new ArrayList<>();
		analyseTaskControl.createReconstructArchitectureList();
		analyseTaskControl.reconstructArchitectureListDTO.createDynamicReconstructArchitectureDTOs();
		for (ReconstructArchitectureDTO dto : analyseTaskControl.reconstructArchitectureListDTO.ReconstructArchitectureDTOList){
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
				{Algorithm.Layers_Goldstein_Original, getTranslation(Algorithm.Layers_Goldstein_Original)},
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
	
	private Object[][] getParameters(String algorithmName){		
		if(algorithmName.equals("Component and Subsystem identification")){
			Object[][] parameterRows = {
					{"test", "test"},
					
			};
			
			return parameterRows;
		} else if(algorithmName.equals("Layer identification: Goldstein - Original")){
			Object[][] parameterRows = {
					{"test 2", "test 2"},
					
			};
			
			return parameterRows;
		} else if(algorithmName.equals("External System identification")){
			Object[][] parameterRows = {
					{"test 3", "test 3"},
					
			};
			
			return parameterRows;
		} else if(algorithmName.equals("Layer identification: Scanniello - Improved")){
			
			Object[][] parameterRows = {
					{"test 4", "test 4"},
					
			};
			return parameterRows;
		} else{
			Object[][] parameterRows = {
					{"null", "null"},
					
			};
			return parameterRows;
		}
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
}