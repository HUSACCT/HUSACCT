package husacct.control.presentation.log;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.control.task.MainController;

import java.awt.Dimension;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

//TODO: Make this an JInternalFrame
@SuppressWarnings("serial")
public class AnalysisHistoryOverviewFrame extends JFrame{

	private MainController mainController;
	private JTable analysisTable;
	private DefaultTableModel analysisTableModel;
	
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	
	public AnalysisHistoryOverviewFrame(MainController mainController){
		//super(mainController.getMainGui(), true);
		this.setTitle(localeService.getTranslatedString("ApplicationAnalysisHistory"));
		this.mainController = mainController;
		this.setup();
		//this.setLoaders();
		this.addComponents();
		this.setResizable(true);
		this.setVisible(true);
		this.setSize(800, 400);
	}
	
	private void setup(){
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(new Dimension(500, 380));
		this.setLocationRelativeTo(getRootPane());
	}
	
	private void addComponents(){
		addTable();
	}
	
	private void addTable(){
		String workspace = mainController.getWorkspaceController().getCurrentWorkspace().getName();
		String application = ServiceProvider.getInstance().getDefineService().getApplicationDetails().name;
		String project = ServiceProvider.getInstance().getDefineService().getApplicationDetails().projects.get(0).name;
		HashMap<String, HashMap<String, String>> tableData = mainController.getLogController().getApplicationHistoryFromFile(workspace, application, project);
		
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
		analysisTableModel.addColumn(localeService.getTranslatedString("Violations"));

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
		    		analysisData.get("dependencies"), 
		    		analysisData.get("violations")
		    });
		}
		
		this.add(new JScrollPane(analysisTable));
	}
}