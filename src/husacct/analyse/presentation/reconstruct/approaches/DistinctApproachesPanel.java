package husacct.analyse.presentation.reconstruct.approaches;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.log4j.Logger;

import husacct.ServiceProvider;
import husacct.analyse.presentation.reconstruct.parameter.ReconstructArchitectureParameterPanel;
import husacct.analyse.task.AnalyseTaskControl;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants.Algorithm;
import husacct.analyse.task.reconstruct.parameters.ReconstructArchitectureParameterDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ReconstructArchitectureDTO;
import husacct.common.help.presentation.HelpableJPanel;
import husacct.common.locale.ILocaleService;

public class DistinctApproachesPanel extends HelpableJPanel implements ActionListener{
	private static final long serialVersionUID = 10903678111609565L;
	private static final ILocaleService localService = ServiceProvider.getInstance().getLocaleService();
	private final Logger logger = Logger.getLogger(AllApproachesJPanel.class);
	private String approachesConstants = "ApproachesConstants";
	private JTable distinctApproachesTable;
	private JButton applyButton, reverseButton, clearAllButton;
	private AnalyseTaskControl analyseTaskControl;
	
	public DistinctApproachesPanel(AnalyseTaskControl analyseTaskControl) throws IOException {
		this.analyseTaskControl = analyseTaskControl;
		initUI();
	}
	
	public void initUI() throws IOException{
		setLayout(new BorderLayout());
		
		JPanel distinctApproachesPanel = new JPanel();
		distinctApproachesPanel.setName(getTranslation("DistinctApproaches"));
		distinctApproachesPanel.setLayout(new BorderLayout(0, 0));
		
		Object distinctApproachesColumns[] = getColumnNames();
		Object distinctApproachesRows[][] = getDistinctApproachesRows();
		distinctApproachesTable = setupDistinctApproachesTable(distinctApproachesRows, distinctApproachesColumns);
		
		Dimension distictApproachesTableSize = distinctApproachesTable.getPreferredSize();
		JScrollPane distinctScrollPane = setupDistictScrollPane(distictApproachesTableSize, distinctApproachesTable);
		
		distinctApproachesPanel.add(distinctScrollPane, BorderLayout.NORTH);
		
		this.add(distinctApproachesPanel, BorderLayout.CENTER);
		JPanel buttonPanel = setupButtonPanel();
		this.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	private Object[] getColumnNames(){
		String approachesTranslation = localService.getTranslatedString("Approaches");
		Object columnNames[] = {
				approachesConstants, 
				approachesTranslation
		};
		return columnNames;
	}
	private Object[][] getDistinctApproachesRows(){
		Object distinctApproachesRows[][] = { 
				{Algorithm.Component_HUSACCT_SelectedModule, getTranslation(Algorithm.Component_HUSACCT_SelectedModule)},
				{Algorithm.Externals_Recognition, getTranslation(Algorithm.Externals_Recognition)},
				{Algorithm.Layers_Goldstein_HUSACCT_SelectedModule, getTranslation(Algorithm.Layers_Goldstein_HUSACCT_SelectedModule)},
				{Algorithm.Layers_HUSACCT_SelectedModule, getTranslation(Algorithm.Layers_HUSACCT_SelectedModule)},
				{Algorithm.Layers_Scanniello_Improved, getTranslation(Algorithm.Layers_Scanniello_Improved)}};
		return distinctApproachesRows;
	}
	private String getTranslation(String translationKey){
		ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
		return localeService.getTranslatedString(translationKey);
	}
	
	
	private JTable setupDistinctApproachesTable(Object[][] rows, Object[] columns){
		JTable table = new JTable(rows, columns){
			private static final long serialVersionUID = -7444877138370325551L;

			public boolean isCellEditable(int row, int col){
				return false;
			}
		};
		
		table.setMinimumSize(new Dimension(600,150));
		table.setName("tableDistinctApproaches");
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		TableColumnModel tableColumnModel = table.getColumnModel();
		hideColumn(approachesConstants, tableColumnModel);
		
		return table;
	}
	private void hideColumn(String columnName, TableColumnModel tableColumnModel) {
		int index = tableColumnModel.getColumnIndex(columnName);
		TableColumn column = tableColumnModel.getColumn(index);
		tableColumnModel.removeColumn(column);
	}
	
	private JScrollPane setupDistictScrollPane(Dimension tableSize, JTable table) {
		JScrollPane distictTableScrollPane = new JScrollPane(table);
		int tableheight = tableSize.height + table.getRowHeight()*table.getRowCount()+50;
		distictTableScrollPane.setPreferredSize(new Dimension(tableSize.width, tableheight));
		return distictTableScrollPane;
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
		
		return buttonPanel;
	}

	@Override
	public void actionPerformed(ActionEvent action) {
		if (action.getSource() == applyButton) {
			try{
																		
				int selectedRow = distinctApproachesTable.getSelectedRow();
				if (selectedRow >= 0){
					executeDistinctApproache(distinctApproachesTable, selectedRow);
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
			distinctApproachesTable.clearSelection();
			analyseTaskControl.reconstructArchitecture_Reverse();
			ServiceProvider.getInstance().getDefineService().getSarService().updateModulePanel();
		}
		if (action.getSource() == clearAllButton) {
			distinctApproachesTable.clearSelection();
			analyseTaskControl.reconstructArchitecture_ClearAll();
			ServiceProvider.getInstance().getDefineService().getSarService().updateModulePanel();
		}
	}
	
	private void executeDistinctApproache(JTable approachesTable, int selectedRow){
		ReconstructArchitectureDTO reconstructArchitectureDTO = new ReconstructArchitectureDTO();
		ModuleDTO selectedModule = getSelectedModule();
		String approachConstant = (String) approachesTable.getModel().getValueAt(selectedRow, 0);
		reconstructArchitectureDTO = createDefaultReconstructArchitectureDTO(approachConstant);
		reconstructArchitectureDTO.setSelectedModule(selectedModule);
		reconstructArchitectureDTO.approachConstant = approachConstant;
		analyseTaskControl.reconstructArchitecture_Execute(reconstructArchitectureDTO);
		ServiceProvider.getInstance().getDefineService().getSarService().updateModulePanel();
	}
	
	private ModuleDTO getSelectedModule(){
		return ServiceProvider.getInstance().getDefineService().getSarService().getModule_SelectedInGUI();
	}

	private ReconstructArchitectureDTO createDefaultReconstructArchitectureDTO(String approachConstant){
		ReconstructArchitectureDTO customDTO = analyseTaskControl.getReconstructArchitectureDTOList().getReconstructArchitectureDTO(approachConstant);
		ReconstructArchitectureDTO defaultDTO = new ReconstructArchitectureDTO();
		for (ReconstructArchitectureParameterDTO parameterDTO : customDTO.parameterDTOs){
			ReconstructArchitectureParameterPanel.setValueOfReconstructArchitectureDTO(parameterDTO.parameterConstant, defaultDTO, parameterDTO.defaultValue);
		}
		return defaultDTO;
		
	}
}
