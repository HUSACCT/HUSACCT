package husacct.analyse.presentation.reconstruct.approaches;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.log4j.Logger;

import husacct.ServiceProvider;
import husacct.analyse.presentation.reconstruct.EditApproachFrame;
import husacct.analyse.task.AnalyseTaskControl;
import husacct.analyse.task.reconstruct.dto.ReconstructArchitectureDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.help.presentation.HelpableJPanel;
import husacct.common.locale.ILocaleService;

public abstract class ApproachesJPanel extends HelpableJPanel implements ActionListener {
	private static final long serialVersionUID = 8208626960034851199L;
	private final ILocaleService localService = ServiceProvider.getInstance().getLocaleService();
	private final Logger logger = Logger.getLogger(ApproachesJPanel.class);
	
	private String approachesConstants = "ApproachesConstants";
	protected AnalyseTaskControl analyseTaskControl;
	protected ArrayList<ReconstructArchitectureDTO> reconstructArchitectureDTOList;
	private String selectedModuleLogicalPath = ""; // Module selected by the user during the last (not reversed) executeApproch().
	
	public JTable approachesTable;
	private TableColumnModel tableApproachesColumnModel;
	public JTable parameterTable;
	private JButton applyButton, reverseButton, clearAllButton, editApproachButton;
	private JFrame mainFrame;

	public ApproachesJPanel(JFrame mainFrame, AnalyseTaskControl analyseTaskControl) throws IOException {
		this.mainFrame = mainFrame;
		this.analyseTaskControl = analyseTaskControl;
		initUI();
	}
	
	public void initUI() throws IOException{
		setLayout(new BorderLayout());
		
		JPanel approachedPanel = new JPanel();
		approachedPanel.setName(localService.getTranslatedString("Approaches"));
		approachedPanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		//Setup AproachesTable
		Object columnNames[] = getColumnNames();
		Object rows[][] = getApproachesRows();
		approachesTable = setupApproachesTable(columnNames, rows);
		initializeReconstructArchitectureDTOList();
						
		Dimension approachestableSize = approachesTable.getPreferredSize();
		JScrollPane approachesTableScrollPane = setupScrollPane(approachestableSize, approachesTable);
		approachesTableScrollPane.setBorder(new TitledBorder(localService.getTranslatedString("Approaches")));
		
		//Setup ParameterTable
		Object[][] tempData = {{ "", "" }};
		Object[] cols = { localService.getTranslatedString("Parameter"), localService.getTranslatedString("Value") };
		parameterTable = setupParameterTable(tempData, cols);
				
		Dimension parameterTableSize = parameterTable.getPreferredSize();
		JScrollPane parameterScrollPane = setupScrollPane(parameterTableSize, parameterTable);
		parameterScrollPane.setBorder(new TitledBorder(localService.getTranslatedString("Parameters")));
		
		ListSelectionListener approachesSelectionListener = new ApproachesTableSelectionListener(approachesTable, this, parameterTable);
		approachesTable.getSelectionModel().addListSelectionListener(approachesSelectionListener);
		
		//Add table scrollPanes to the higher-level containers
		approachedPanel.add(approachesTableScrollPane);
		approachedPanel.add(parameterScrollPane);
		this.add(approachedPanel, BorderLayout.CENTER);

		//Create and add buttonPanel
		JPanel buttonPanel = setupButtonPanel();
		this.add(buttonPanel, BorderLayout.SOUTH);
	}

	private JScrollPane setupScrollPane(Dimension tableSize, JTable table) {
		JScrollPane tableScrollPane = new JScrollPane(table);
		int tableheight = tableSize.height + table.getRowHeight()*table.getRowCount()+50;
		tableScrollPane.setPreferredSize(new Dimension(tableSize.width, tableheight));
		return tableScrollPane;
	}

	private JTable setupApproachesTable(Object[] columnNames, Object[][] rows) {
		JTable table = new JTable  (rows, columnNames){
			private static final long serialVersionUID = -1304690689139412746L;
			@Override
			public boolean isCellEditable(int row, int col){
				return false;
			}
		};
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setName("tableApproaches");
		table.getTableHeader().setUI(null);
		tableApproachesColumnModel = table.getColumnModel();
		hideColumn(approachesConstants, tableApproachesColumnModel);
		return table;
	}
	
	private Object[] getColumnNames(){
		Object columnNames[] = {
				approachesConstants, 
				localService.getTranslatedString("Approaches")
		};
		return columnNames;
	}
	
	protected abstract Object[][] getApproachesRows();
	
	private void initializeReconstructArchitectureDTOList(){
		reconstructArchitectureDTOList = new ArrayList<>();
		for (int rowId = 0 ; rowId < approachesTable.getRowCount() ; rowId++ ) {
			String approachId = (String) approachesTable.getModel().getValueAt(rowId, 0);
			ReconstructArchitectureDTO raDTO = analyseTaskControl.getReconstructArchitectureDTO(approachId);
			reconstructArchitectureDTOList.add(raDTO);
		}
	}

	public ReconstructArchitectureDTO getReconstructArchitectureDTO(String approachId) {
		ReconstructArchitectureDTO foundRaDTO = null;
		for (ReconstructArchitectureDTO raDTO : reconstructArchitectureDTOList) {
			if (raDTO.approachId.equals(approachId)) {
				foundRaDTO = raDTO;
				break;
			}
		}
		return foundRaDTO;
	}
	
	public void updateReconstructArchitectureDTO(ReconstructArchitectureDTO newDTO){
		for(ReconstructArchitectureDTO oldDTO : reconstructArchitectureDTOList){
			if (oldDTO.approachId.equals(newDTO.approachId)){
				oldDTO = newDTO;
				break;
			}
		}
	}

	private void hideColumn(String columnName, TableColumnModel tableColumnModel) {
		int index = tableColumnModel.getColumnIndex(columnName);
		TableColumn column = tableColumnModel.getColumn(index);
		tableColumnModel.removeColumn(column);
	}
	
	private JTable setupParameterTable(Object[][] rows, Object[] columns){
		JTable table = new JTable(rows, columns);
		table.setEnabled(false);
		Color background = new Color(242, 242, 242);
		table.setBackground(background);
		return table;
		
	}
	
	private JPanel setupButtonPanel(){
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		
		String applyTranslation = localService.getTranslatedString("Apply");
		applyButton = new JButton(applyTranslation);
		buttonPanel.add(applyButton);
		applyButton.setPreferredSize(new Dimension(140, 40));
		applyButton.addActionListener(this);

		String reverseTranslation = localService.getTranslatedString("Reverse");
		reverseButton = new JButton(reverseTranslation);
		buttonPanel.add(reverseButton);
		reverseButton.setPreferredSize(new Dimension(140, 40));
		reverseButton.addActionListener(this);
		
		String clearAllTranslation = localService.getTranslatedString("ClearAll");
		clearAllButton = new JButton(clearAllTranslation);
		buttonPanel.add(clearAllButton);
		clearAllButton.setPreferredSize(new Dimension(140, 40));
		clearAllButton.addActionListener(this);
				
		String editApproach = localService.getTranslatedString("EditApproach");
		editApproachButton = new JButton(editApproach);
		buttonPanel.add(editApproachButton);
		editApproachButton.setPreferredSize(new Dimension(140, 40));
		editApproachButton.addActionListener(this);
		
		return buttonPanel;
	}

	@Override
	public void actionPerformed(ActionEvent action) {
		if (action.getSource() == applyButton) {
			try{
																		
				int selectedRow = approachesTable.getSelectedRow();
				if (selectedRow >= 0){
					executeApproach(approachesTable, selectedRow);
				}
				else{
					//logger.warn("No Approach selected");
				}
			}
			catch(Exception e){
				logger.error("Approaches Apply Exception: " + e);
			}
		}
		if (action.getSource() == reverseButton) {
			approachesTable.clearSelection();
			analyseTaskControl.reconstructArchitecture_Reverse();
			ServiceProvider.getInstance().getDefineService().getSarService().updateModulePanel(selectedModuleLogicalPath);
			selectedModuleLogicalPath = "";
		}
		if (action.getSource() == clearAllButton) {
			approachesTable.clearSelection();
			analyseTaskControl.reconstructArchitecture_ClearAll();
			ServiceProvider.getInstance().getDefineService().getSarService().updateModulePanel("");
			selectedModuleLogicalPath = "";
		}
		if (action.getSource() == editApproachButton){
			int selectedRow = approachesTable.getSelectedRow();
			if (selectedRow >= 0){
				String approachId = (String) approachesTable.getModel().getValueAt(selectedRow, 0);
				ReconstructArchitectureDTO reconstructArchitectureDTO = getReconstructArchitectureDTO(approachId);
				if ((reconstructArchitectureDTO != null) && !reconstructArchitectureDTO.parameterDTOs.isEmpty()){
					new EditApproachFrame(this.mainFrame, reconstructArchitectureDTO, this);
				}
				else{
					JOptionPane.showMessageDialog(this, localService.getTranslatedString("ApproachWithoutParametersWarning"));
				}
			}
			else{
				JOptionPane.showMessageDialog(this, localService.getTranslatedString("NoApproachSelectedWarning"));
			}
		}	
	}
	
	private void executeApproach(JTable approachesTable, int selectedRow){
		ModuleDTO selectedModule = getSelectedModule();
		if ((selectedModule != null) && (selectedModule.logicalPath != null)) {
			selectedModuleLogicalPath = selectedModule.logicalPath;
		}
		String approachId = (String) approachesTable.getModel().getValueAt(selectedRow, 0);
		ReconstructArchitectureDTO reconstructArchitectureDTO = getReconstructArchitectureDTO(approachId);
		if (reconstructArchitectureDTO != null) {
			reconstructArchitectureDTO.setSelectedModule(selectedModule);
			analyseTaskControl.reconstructArchitecture_Execute(reconstructArchitectureDTO);
		}
		ServiceProvider.getInstance().getDefineService().getSarService().updateModulePanel(selectedModuleLogicalPath);
	}
	
	private ModuleDTO getSelectedModule(){
		return ServiceProvider.getInstance().getDefineService().getSarService().getModule_SelectedInGUI();
	}
	
	protected String getTranslation(String translationKey){ 
 		ILocaleService localeService = ServiceProvider.getInstance().getLocaleService(); 
 		return localeService.getTranslatedString(translationKey); 
 	} 
	

	
}
