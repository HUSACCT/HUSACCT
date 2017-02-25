package husacct.analyse.presentation.reconstruct.approaches;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import husacct.ServiceProvider;
import husacct.analyse.presentation.reconstruct.parameter.ReconstructArchitectureParameterPanel;
import husacct.analyse.task.AnalyseTaskControl;
import husacct.analyse.task.reconstruct.parameters.ReconstructArchitectureParameterDTO;
import husacct.common.dto.ReconstructArchitectureDTO;
import husacct.common.locale.ILocaleService;

public class ApproachesTableSelectionListener implements ListSelectionListener {
	private final ILocaleService localService = ServiceProvider.getInstance().getLocaleService();
	private JTable tableApproaches;
	private AnalyseTaskControl analyseTaskControl;
	private JTable allParameterTable;
	
	public ApproachesTableSelectionListener (JTable tableApproaches, AnalyseTaskControl analyseTaskControl, JTable allParameterTable){
		this.tableApproaches = tableApproaches;
		this.analyseTaskControl = analyseTaskControl;
		this.allParameterTable = allParameterTable;
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		int selectedRow = tableApproaches.getSelectedRow();
		if (!e.getValueIsAdjusting() && selectedRow >= 0) {
			
			
			ReconstructArchitectureDTO dto = new ReconstructArchitectureDTO();
			String approachConstant = (String) tableApproaches.getModel().getValueAt(selectedRow, 0);
			dto = analyseTaskControl.getReconstructArchitectureDTOList().getReconstructArchitectureDTO(approachConstant);
			
			HashMap<String, Object> rowData = getParameters(dto);
			DefaultTableModel defaultTableModel = new DefaultTableModel();
			defaultTableModel.addColumn(localService.getTranslatedString("Parameter"));
			defaultTableModel.addColumn(localService.getTranslatedString("Value"));
			
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
	
	private HashMap<String, Object> getParameters(ReconstructArchitectureDTO dto){		
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
}
