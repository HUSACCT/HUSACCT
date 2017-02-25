package husacct.control.presentation.log;

import husacct.ServiceProvider;
import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ProjectDTO;
import husacct.common.locale.ILocaleService;
import husacct.control.presentation.util.DialogUtils;
import husacct.control.task.MainController;

import java.awt.Dimension;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class ApplicationAnalysisHistoryOverviewFrame extends JDialog{

	private MainController mainController;
	private JTable analysisTable;
	private DefaultTableModel analysisTableModel;
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	
	public ApplicationAnalysisHistoryOverviewFrame(MainController mainController){
		super(mainController.getMainGui(), true);
		this.setTitle(localeService.getTranslatedString("ApplicationAnalysisHistory"));
		this.mainController = mainController;
		this.setup();
		this.addComponents();
		this.setResizable(true);
		this.setVisible(true);
	}
	
	private void setup(){
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(new Dimension(1000, 400));
		DialogUtils.alignCenter(this);
	}
	
	private void addComponents(){
		addTable();
	}
	
	private void addTable(){
		String workspace = mainController.getWorkspaceController().getCurrentWorkspace().getName();
		ApplicationDTO applicationDTO = ServiceProvider.getInstance().getDefineService().getApplicationDetails();
		String application = applicationDTO.name;
		ArrayList<ProjectDTO> projects = applicationDTO.projects;
		HashMap<String, HashMap<String, String>> tableData = mainController.getApplicationAnalysisHistoryLogController().getApplicationHistoryFromFile(workspace, application, projects);
		
		analysisTableModel = new DefaultTableModel();
		analysisTable = new JTable(analysisTableModel){
			public boolean isCellEditable(int rowIndex, int colIndex) {
				return false;
			}
		};

		analysisTableModel.addColumn(localeService.getTranslatedString("Application"));
		analysisTableModel.addColumn(localeService.getTranslatedString("Path"));
		analysisTableModel.addColumn(localeService.getTranslatedString("DateTime"));
		analysisTableModel.addColumn(localeService.getTranslatedString("Packages"));
		analysisTableModel.addColumn(localeService.getTranslatedString("Classes"));
		analysisTableModel.addColumn(localeService.getTranslatedString("Interfaces"));
		analysisTableModel.addColumn(localeService.getTranslatedString("Dependencies"));
		
		analysisTable.getTableHeader().setReorderingAllowed(false);
		analysisTable.getTableHeader().setResizingAllowed(true);
		analysisTable.setAutoCreateRowSorter(true);
		analysisTable.getRowSorter().toggleSortOrder(2); analysisTable.getRowSorter().toggleSortOrder(2);	//Sort by date/time, newest on top
		
		for (Entry<String, HashMap<String, String>> entry : tableData.entrySet()) {
		    Long analysisTimestampLong = Long.parseLong(entry.getKey());
			Date analysisTimestampDate = new Date(analysisTimestampLong*1000);
			DateFormat analysisTimestampFormat = new SimpleDateFormat("dd-MM-yyyy hh:MM:ss");
			
		    HashMap<String, String> analysisData = entry.getValue();
		    analysisTableModel.addRow(new Object[]{
		    		analysisData.get("application"), 
		    		analysisData.get("path"), 
		    		analysisTimestampFormat.format(analysisTimestampDate),
		    		analysisData.get("packages"), 
		    		analysisData.get("classes"), 
		    		analysisData.get("interfaces"), 
		    		analysisData.get("dependencies")
		    });
		}
		
		this.add(new JScrollPane(analysisTable));
	}
}