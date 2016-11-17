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

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.log4j.Logger;

import husacct.ServiceProvider;
import husacct.analyse.presentation.reconstruct.EditApproachFrame;
import husacct.analyse.task.AnalyseTaskControl;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ReconstructArchitectureDTO;
import husacct.common.help.presentation.HelpableJPanel;
import husacct.common.locale.ILocaleService;

public class AllApproachesJPanel extends HelpableJPanel implements ActionListener {
	private static final long serialVersionUID = 8208626960034851199L;
	private final ILocaleService localService = ServiceProvider.getInstance().getLocaleService();
	private final Logger logger = Logger.getLogger(AllApproachesJPanel.class);
	
	private String approachesConstants = "ApproachesConstants";
	private AnalyseTaskControl analyseTaskControl;
	private TableColumnModel tableAllApproachesColumnModel;
	private JButton applyButton, reverseButton, clearAllButton, editApproachButton;
	
	public JTable allApproachesTable;
	public JTable parameterTable;
	
	public AllApproachesJPanel(AnalyseTaskControl analyseTaskControl) throws IOException {
		this.analyseTaskControl = analyseTaskControl;
		initUI();
	}
	
	public void initUI() throws IOException{
		setLayout(new BorderLayout());
		
		JPanel allApproachedPanel = new JPanel();
		allApproachedPanel.setName(localService.getTranslatedString("Approaches"));
		//allApproachedPanel.setBorder(new TitledBorder(localService.getTranslatedString("Approaches")));
		allApproachedPanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		//Setup AllAproachesTable
		Object columnNames[] = getColumnNames();
		Object rows[][] = getAllApproachesRows();
		allApproachesTable = setupAllApproachesTable(columnNames, rows);
						
		Dimension allApproachestableSize = allApproachesTable.getPreferredSize();
		JScrollPane allApproachesTableScrollPane = setupScrollPane(allApproachestableSize, allApproachesTable);
		allApproachesTableScrollPane.setBorder(new TitledBorder(localService.getTranslatedString("Approaches")));
		
		//Setup ParameterTable
		/* JPanel parametersPanel = new JPanel();
		parametersPanel.setBorder(new TitledBorder(localService.getTranslatedString("Parameters")));
		parametersPanel.setLayout(new GridLayout(0, 1, 0, 0)); */

		Object[][] tempData = {{ "", "" }};
		Object[] cols = { localService.getTranslatedString("Parameter"), localService.getTranslatedString("Value") };
		parameterTable = setupParameterTable(tempData, cols);
				
		Dimension parameterTableSize = parameterTable.getPreferredSize();
		JScrollPane allApproachesParameterScrollPane = setupScrollPane(parameterTableSize, parameterTable);
		allApproachesParameterScrollPane.setBorder(new TitledBorder(localService.getTranslatedString("Parameters")));
		
		ListSelectionListener allApproachesSelectionListener = new ApproachesTableSelectionListener(allApproachesTable, analyseTaskControl, parameterTable);
		allApproachesTable.getSelectionModel().addListSelectionListener(allApproachesSelectionListener);
		
		
		//Add table ScrollPanes to the Jpanel
		allApproachedPanel.add(allApproachesTableScrollPane);
		allApproachedPanel.add(allApproachesParameterScrollPane);
		
		this.add(allApproachedPanel, BorderLayout.CENTER);
		//this.add(parametersPanel, BorderLayout.CENTER);
		JPanel buttonPanel = setupButtonPanel();
		this.add(buttonPanel, BorderLayout.SOUTH);
	}

	private JScrollPane setupScrollPane(Dimension tableSize, JTable table) {
		JScrollPane tableScrollPane = new JScrollPane(table);
		int tableheight = tableSize.height + table.getRowHeight()*table.getRowCount()+50;
		tableScrollPane.setPreferredSize(new Dimension(tableSize.width, tableheight));
		return tableScrollPane;
	}

	private JTable setupAllApproachesTable(Object[] columnNames, Object[][] rows) {
		JTable table = new JTable  (rows, columnNames){
			private static final long serialVersionUID = -1304690689139412746L;
			public boolean isCellEditable(int row, int col){
				return false;
			}
		};
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setName("tableAllApproaches");
		table.getTableHeader().setUI(null);
		tableAllApproachesColumnModel = table.getColumnModel();
		hideColumn(approachesConstants, tableAllApproachesColumnModel);
		return table;
	}
	
	private Object[] getColumnNames(){
		Object columnNames[] = {
				approachesConstants, 
				localService.getTranslatedString("Approaches")
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
		if (action.getSource() == editApproachButton){
			int selectedRow = allApproachesTable.getSelectedRow();
			if (selectedRow >= 0){
				String approachConstant = (String) allApproachesTable.getModel().getValueAt(selectedRow, 0);
				ReconstructArchitectureDTO reconstructArchitectureDTO = analyseTaskControl.getReconstructArchitectureDTOList().getReconstructArchitectureDTO(approachConstant);
				if (!reconstructArchitectureDTO.parameterDTOs.isEmpty()){
					new EditApproachFrame(analyseTaskControl, reconstructArchitectureDTO, this);
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
