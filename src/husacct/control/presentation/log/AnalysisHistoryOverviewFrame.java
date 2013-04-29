package husacct.control.presentation.log;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.control.task.MainController;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class AnalysisHistoryOverviewFrame extends JFrame{

	private MainController mainController;
	private JTable analysisTable;
	private DefaultTableModel analysisTableModel;
	private JScrollPane analysisHistoryOverviewContainer;
	
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	
	public AnalysisHistoryOverviewFrame(MainController mainController){
		//super(mainController.getMainGui(), true);
		this.setTitle(localeService.getTranslatedString("ApplicationAnalysisHistory"));
		this.mainController = mainController;
		this.setup();
		//this.setLoaders();
		this.addComponents();
		this.setListeners();
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
		/*loaderList = new JList(loaderListData.toArray());
		loaderList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		loaderList.setLayoutOrientation(JList.VERTICAL);
		loaderList.setVisibleRowCount(-1);
		JScrollPane listScrollPane = new JScrollPane(loaderList);
		listScrollPane.setAlignmentX(LEFT_ALIGNMENT);
		
		openPanel = new JPanel();
		openPanel.setLayout(new BoxLayout(openPanel, BoxLayout.Y_AXIS));
		
		loaderPanelContainer = new JPanel();
		loaderPanelContainer.setPreferredSize(new Dimension(350, 300));
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		openButton = new JButton(localeService.getTranslatedString("OpenButton"));
		cancelButton = new JButton(localeService.getTranslatedString("CancelButton"));
		
		openButton.setEnabled(false);
		getRootPane().setDefaultButton(openButton);
		
		buttonsPanel.add(openButton);
		buttonsPanel.add(cancelButton);
		
		openPanel.add(loaderPanelContainer);
		openPanel.add(buttonsPanel);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScrollPane, openPanel);
		splitPane.setDividerLocation(150);
		splitPane.setEnabled(false);
		add(splitPane);*/
	}
	
	private void addTable(){
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

		analysisTableModel.addRow(new Object[]{"Applicatie", "Pad", "Datum", "Packages", "Classes", "Interfaces", "Dependencies", "Violations"});
		analysisTableModel.addRow(new Object[]{"Applicatie", "Pad", "Datum", "Packages", "Classes", "Interfaces", "Dependencies", "Violations"});
		analysisTableModel.addRow(new Object[]{"Applicatie", "Pad", "Datum", "Packages", "Classes", "Interfaces", "Dependencies", "Violations"});
		analysisTableModel.addRow(new Object[]{"Applicatie", "Pad", "Datum", "Packages", "Classes", "Interfaces", "Dependencies", "Violations"});
		analysisTableModel.addRow(new Object[]{"Applicatie", "Pad", "Datum", "Packages", "Classes", "Interfaces", "Dependencies", "Violations"});
		analysisTableModel.addRow(new Object[]{"Applicatie", "Pad", "Datum", "Packages", "Classes", "Interfaces", "Dependencies", "Violations"});
		analysisTableModel.addRow(new Object[]{"Applicatie", "Pad", "Datum", "Packages", "Classes", "Interfaces", "Dependencies", "Violations"});
		analysisTableModel.addRow(new Object[]{"Applicatie", "Pad", "Datum", "Packages", "Classes", "Interfaces", "Dependencies", "Violations"});
		analysisTableModel.addRow(new Object[]{"Applicatie", "Pad", "Datum", "Packages", "Classes", "Interfaces", "Dependencies", "Violations"});
		analysisTableModel.addRow(new Object[]{"Applicatie", "Pad", "Datum", "Packages", "Classes", "Interfaces", "Dependencies", "Violations"});
		analysisTableModel.addRow(new Object[]{"Applicatie", "Pad", "Datum", "Packages", "Classes", "Interfaces", "Dependencies", "Violations"});
		analysisTableModel.addRow(new Object[]{"Applicatie", "Pad", "Datum", "Packages", "Classes", "Interfaces", "Dependencies", "Violations"});
		analysisTableModel.addRow(new Object[]{"Applicatie", "Pad", "Datum", "Packages", "Classes", "Interfaces", "Dependencies", "Violations"});
		analysisTableModel.addRow(new Object[]{"Applicatie", "Pad", "Datum", "Packages", "Classes", "Interfaces", "Dependencies", "Violations"});
		analysisTableModel.addRow(new Object[]{"Applicatie", "Pad", "Datum", "Packages", "Classes", "Interfaces", "Dependencies", "Violations"});
		analysisTableModel.addRow(new Object[]{"Applicatie", "Pad", "Datum", "Packages", "Classes", "Interfaces", "Dependencies", "Violations"});
		analysisTableModel.addRow(new Object[]{"Applicatie", "Pad", "Datum", "Packages", "Classes", "Interfaces", "Dependencies", "Violations"});
		analysisTableModel.addRow(new Object[]{"Applicatie", "Pad", "Datum", "Packages", "Classes", "Interfaces", "Dependencies", "Violations"});
		
		this.add(new JScrollPane(analysisTable));
	}
	
	private void setListeners(){
		/*loaderList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				loadSelectedOpenMethodPanel();
				openButton.setEnabled(true);
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		openButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(selectedLoaderPanel.validateData() && loadWorkspace()){
					dispose();
				}
			}
		});*/
	}
}