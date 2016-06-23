package husacct.analyse.presentation.reconstruct.approaches;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.log4j.Logger;

import husacct.ServiceProvider;
import husacct.analyse.presentation.reconstruct.ApproachesSettingsFrame;
import husacct.analyse.task.AnalyseTaskControl;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ReconstructArchitectureDTO;
import husacct.common.help.presentation.HelpableJPanel;
import husacct.common.locale.ILocaleService;

public class AllApproachesJPanel extends HelpableJPanel implements ActionListener {
	private static final long serialVersionUID = 8208626960034851199L;
	private static final ILocaleService localService = ServiceProvider.getInstance().getLocaleService();
	private final Logger logger = Logger.getLogger(AllApproachesJPanel.class);
	
	private String approachesConstants = AnalyseReconstructConstants.ApproachesTable.ApproachesConstants;
	private AnalyseTaskControl analyseTaskControl;
	private TableColumnModel tableAllApproachesColumnModel;
	private JButton applyButton, reverseButton, clearAllButton, settingsButton;
	
	public JTable allApproachesTable;
	public JTable allApproachesParameterTable;
	
	public AllApproachesJPanel(AnalyseTaskControl analyseTaskControl) throws IOException {
		this.analyseTaskControl = analyseTaskControl;
		initUI();
	}
	
	public void initUI() throws IOException{
		setLayout(new BorderLayout());
		
		JPanel allApproachedPanel = new JPanel();
		allApproachedPanel.setName(AnalyseReconstructConstants.ApproachesTable.PanelAllApproaches);
		allApproachedPanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		//Setup AllAproachesTable
		Object columnNames[] = getColumnNames();
		Object rows[][] = getAllApproachesRows();
		allApproachesTable = setupAllApproachesTable(columnNames, rows);
						
		Dimension allApproachestableSize = allApproachesTable.getPreferredSize();
		JScrollPane allApproachesTableScrollPane = setupAllAproachesScrollPane(allApproachestableSize, allApproachesTable);
		
		
		//Setup AllAproacheParameterTable
		Object[][] tempData = {{ "", "" }};
		Object[] cols = { "Parameter", "Value" };
		allApproachesParameterTable = setupAllAproachesParameterTable(tempData, cols);
				
		Dimension parameterTableSize = allApproachesParameterTable.getPreferredSize();
		JScrollPane allApproachesParameterScrollPane = setupAllAproachesScrollPane(parameterTableSize, allApproachesParameterTable);
		
		ListSelectionListener allApproachesSelectionListener = new ApproachesTableSelectionListener(allApproachesTable, analyseTaskControl, allApproachesParameterTable);
		allApproachesTable.getSelectionModel().addListSelectionListener(allApproachesSelectionListener);
		
		
		//Add table ScrollPanes to the Jpanel
		allApproachedPanel.add(allApproachesTableScrollPane);
		allApproachedPanel.add(allApproachesParameterScrollPane);
		
		this.add(allApproachedPanel, BorderLayout.CENTER);
		JPanel buttonPanel = setupButtonPanel();
		this.add(buttonPanel, BorderLayout.SOUTH);
	}

	private JScrollPane setupAllAproachesScrollPane(Dimension tableSize, JTable table) {
		JScrollPane tableScrollPane = new JScrollPane(table);
		int tableheight = tableSize.height + table.getRowHeight()*table.getRowCount()+50;
		tableScrollPane.setPreferredSize(new Dimension(tableSize.width, tableheight));
		return tableScrollPane;
	}

	private JTable setupAllApproachesTable(Object[] columnNames, Object[][] rows) {
		JTable table = new JTable(rows, columnNames){
			private static final long serialVersionUID = -1304690689139412746L;
			public boolean isCellEditable(int row, int col){
				return false;
			}
		};
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setName("tableAllApproaches");
		table.setMinimumSize(new Dimension(600,150));
		
		tableAllApproachesColumnModel = table.getColumnModel();
		hideColumn(approachesConstants, tableAllApproachesColumnModel);
		return table;
	}
	
	private Object[] getColumnNames(){
		String approachesTranslation = localService.getTranslatedString("Approaches");
		Object columnNames[] = {
				approachesConstants, 
				approachesTranslation
		};
		return columnNames;
	}
	
	private Object[][] getAllApproachesRows(){
		ArrayList<Object[]> approachesRowsList = new ArrayList<>();
		analyseTaskControl.createReconstructArchitectureList();
		analyseTaskControl.getReconstructArchitectureDTOList().createDynamicReconstructArchitectureDTOs();
		for (ReconstructArchitectureDTO dto : analyseTaskControl.getReconstructArchitectureDTOList().reconstructArchitectureDTOList){
			Object[] rowObject = {dto.approachConstant, dto.getTranslation()};
			approachesRowsList.add(rowObject);
		}
		
		Object[][] approachesRows = new Object[approachesRowsList.size()][];
		approachesRows = approachesRowsList.toArray(approachesRows);
		return approachesRows;
	}
	
	private void hideColumn(String columnName, TableColumnModel tableColumnModel) {
		int index = tableColumnModel.getColumnIndex(columnName);
		TableColumn column = tableColumnModel.getColumn(index);
		tableColumnModel.removeColumn(column);
	}
	
	private JTable setupAllAproachesParameterTable(Object[][] rows, Object[] columns){
		JTable table = new JTable(rows, columns);
		table.setEnabled(false);
		return table;
		
	}
	
	private JPanel setupButtonPanel(){
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		
		String applyTranslation = localService.getTranslatedString("Apply");
		applyButton = new JButton(applyTranslation);
		buttonPanel.add(applyButton);
		applyButton.setPreferredSize(new Dimension(100, 40));
		applyButton.addActionListener(this);

		String reverseTranslation = localService.getTranslatedString("Reverse");
		reverseButton = new JButton(reverseTranslation);
		buttonPanel.add(reverseButton);
		reverseButton.setPreferredSize(new Dimension(reverseButton.getPreferredSize().width, 40));
		reverseButton.addActionListener(this);
		
		String clearAllTranslation = localService.getTranslatedString("ClearAll");
		clearAllButton = new JButton(clearAllTranslation);
		buttonPanel.add(clearAllButton);
		clearAllButton.setPreferredSize(new Dimension(100, 40));
		clearAllButton.addActionListener(this);
				
		settingsButton = new JButton("Settings");
		buttonPanel.add(settingsButton);
		settingsButton.setPreferredSize(new Dimension(100, 40));
		settingsButton.addActionListener(this);
		
		return buttonPanel;
	}

	@Override
	public void actionPerformed(ActionEvent action) {
		if (action.getSource() == applyButton) {
			try{
																		
				int selectedRow = allApproachesTable.getSelectedRow();
				if (selectedRow >= 0){
					executeApproach(allApproachesTable, selectedRow);
				}
				else{
					logger.warn("No Approache selected");
				}
			}
			catch(Exception e){
				logger.error("Approaches Apply Exception: " + e);
			}
		}
		if (action.getSource() == reverseButton) {
			allApproachesTable.clearSelection();
			analyseTaskControl.reconstructArchitecture_Reverse();
			ServiceProvider.getInstance().getDefineService().getSarService().updateModulePanel();
		}
		if (action.getSource() == clearAllButton) {
			allApproachesTable.clearSelection();
			analyseTaskControl.reconstructArchitecture_ClearAll();
			ServiceProvider.getInstance().getDefineService().getSarService().updateModulePanel();
		}
		if (action.getSource() == settingsButton){
			int selectedRow = allApproachesTable.getSelectedRow();
			if (selectedRow >= 0){
				String approachConstant = (String) allApproachesTable.getModel().getValueAt(selectedRow, 0);
				ReconstructArchitectureDTO reconstructArchitectureDTO = analyseTaskControl.getReconstructArchitectureDTOList().getReconstructArchitectureDTO(approachConstant);
				if (!reconstructArchitectureDTO.parameterDTOs.isEmpty()){
					new ApproachesSettingsFrame(analyseTaskControl, reconstructArchitectureDTO, this);
				}
				else{
					JOptionPane.showMessageDialog(this, "this approach has no settings");
				}
			}
			else{
				logger.warn("No Approache selected");
			}
		}	
	}
	
	private void executeApproach(JTable approachesTable, int selectedRow){
		ReconstructArchitectureDTO reconstructArchitectureDTO = new ReconstructArchitectureDTO();
		ModuleDTO selectedModule = getSelectedModule();
		String approachConstant = (String) approachesTable.getModel().getValueAt(selectedRow, 0);
		reconstructArchitectureDTO = analyseTaskControl.getReconstructArchitectureDTOList().getReconstructArchitectureDTO(approachConstant);
		reconstructArchitectureDTO.setSelectedModule(selectedModule);
		analyseTaskControl.reconstructArchitecture_Execute(reconstructArchitectureDTO);
		ServiceProvider.getInstance().getDefineService().getSarService().updateModulePanel();
	}
	
	private ModuleDTO getSelectedModule(){
		return ServiceProvider.getInstance().getDefineService().getSarService().getModule_SelectedInGUI();
	}
	
}
