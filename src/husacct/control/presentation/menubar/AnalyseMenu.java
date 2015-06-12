package husacct.control.presentation.menubar;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.common.services.IServiceListener;
import husacct.control.task.IStateChangeListener;
import husacct.control.task.MainController;
import husacct.control.task.States;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class AnalyseMenu extends JMenu{
	private MainController mainController;
	private JMenuItem setApplicationPropertiesItem;
	private JMenuItem analyseNowItem;
	private JMenuItem analysedArchitectureDiagramItem;
	private JMenuItem analysedApplicationOverviewItem;
	private JMenuItem analysisHistoryItem;
	private JMenuItem reportDependenciesItem;
	private JMenuItem reconstructArchitectureItem;
	private JMenuItem exportAnalysisModelItem;
	private JMenuItem importAnalysisModelItem;
	
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	
	public AnalyseMenu(final MainController mainController){
		super();
		this.mainController = mainController;
		setText(localeService.getTranslatedString("Analyse"));
		addComponents();
		setListeners();	
	}
	
	private void addComponents(){
		setApplicationPropertiesItem = new JMenuItem(localeService.getTranslatedString("ApplicationProperties"));
		setApplicationPropertiesItem.setAccelerator(KeyStroke.getKeyStroke('P', KeyEvent.CTRL_DOWN_MASK));
		setApplicationPropertiesItem.setMnemonic(getMnemonicKeycode("ApplicationPropertiesMnemonic"));
		
		analyseNowItem = new JMenuItem(localeService.getTranslatedString("AnalyseNow"));
		analyseNowItem.setMnemonic(getMnemonicKeycode("AnalyseNowMnemonic"));
		
		analysedApplicationOverviewItem = new JMenuItem(localeService.getTranslatedString("AnalysedApplicationOverview"));
		analysedApplicationOverviewItem.setAccelerator(KeyStroke.getKeyStroke('T', KeyEvent.CTRL_DOWN_MASK));
		analysedApplicationOverviewItem.setMnemonic(getMnemonicKeycode("AnalysedApplicationOverviewMnemonic"));
		
		analysedArchitectureDiagramItem = new JMenuItem(localeService.getTranslatedString("AnalysedArchitectureDiagram"));
		analysedArchitectureDiagramItem.setAccelerator(KeyStroke.getKeyStroke('A', KeyEvent.CTRL_DOWN_MASK));
		analysedArchitectureDiagramItem.setMnemonic(getMnemonicKeycode("AnalysedArchitectureDiagramMnemonic"));
		
		analysisHistoryItem = new JMenuItem(localeService.getTranslatedString("AnalysisHistory"));
		
		reconstructArchitectureItem = new JMenuItem(localeService.getTranslatedString("ReconstructArchitecture"));
		reconstructArchitectureItem.setMnemonic(getMnemonicKeycode("ReconstructArchitectureNowMnemonic"));
		
		exportAnalysisModelItem = new JMenuItem(localeService.getTranslatedString("ExportAnalysisModel"));
		exportAnalysisModelItem.setMnemonic(getMnemonicKeycode("ExportAnalysisModelMnemonic"));
		
		importAnalysisModelItem = new JMenuItem(localeService.getTranslatedString("ImportAnalysisModel"));
		importAnalysisModelItem.setMnemonic(getMnemonicKeycode("ImportAnalysisModelMnemonic"));
		
		reportDependenciesItem = new JMenuItem(localeService.getTranslatedString("ExportDependencies"));
		reportDependenciesItem.setAccelerator(KeyStroke.getKeyStroke('E', KeyEvent.CTRL_DOWN_MASK));
		reportDependenciesItem.setMnemonic(getMnemonicKeycode("ReportDependenciesMnemonic"));
		
		this.add(setApplicationPropertiesItem);
		this.add(analyseNowItem);
		this.add(analysedApplicationOverviewItem);
		this.add(analysedArchitectureDiagramItem);
		this.add(analysisHistoryItem);
		//this.add(reconstructArchitectureItem);
		this.add(exportAnalysisModelItem);
		this.add(importAnalysisModelItem);
		this.add(reportDependenciesItem);
	}
	
	private void setListeners() {
		setApplicationPropertiesItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getApplicationController().showApplicationDetailsGui();
			}
		});
		
		analyseNowItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getApplicationController().analyseApplication();
			}
		});
		
		analysedApplicationOverviewItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getViewController().showApplicationOverviewGui();
			}
		});
		
		analysedArchitectureDiagramItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getViewController().showAnalysedArchitectureDiagram();
			}
		});
		
		analysisHistoryItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getApplicationAnalysisHistoryLogController().showApplicationAnalysisHistoryOverview();
			}
		});
		
		reportDependenciesItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getExportImportController().showReportDependenciesGui();
			}
		});
		
		reconstructArchitectureItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getApplicationController().reconstructArchitecture();
			}
		});
		
		exportAnalysisModelItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getExportImportController().showExportAnalysisModelGui();
			}
		});
		
		importAnalysisModelItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getExportImportController().showImportAnalyseModelGui();
			}
		});

		mainController.getStateController().addStateChangeListener(new IStateChangeListener() {
			public void changeState(List<States> states) {
				setApplicationPropertiesItem.setEnabled(false);
				analyseNowItem.setEnabled(false);
				analysedArchitectureDiagramItem.setEnabled(false);
				analysedApplicationOverviewItem.setEnabled(false);
				analysisHistoryItem.setEnabled(false);
				reportDependenciesItem.setEnabled(false);
				reconstructArchitectureItem.setEnabled(false);
				exportAnalysisModelItem.setEnabled(false);
				importAnalysisModelItem.setEnabled(false);
				
				if(states.contains(States.OPENED)){
					setApplicationPropertiesItem.setEnabled(true);
					analysisHistoryItem.setEnabled(true);
				}
				if(states.contains(States.APPSET) && (!states.contains(States.VALIDATING))){
					analyseNowItem.setEnabled(true);
					importAnalysisModelItem.setEnabled(true);
				}
				if(states.contains(States.ANALYSED)){
					analysedArchitectureDiagramItem.setEnabled(true);
					analysedApplicationOverviewItem.setEnabled(true);
					reportDependenciesItem.setEnabled(true);
					analysisHistoryItem.setEnabled(true);
					reconstructArchitectureItem.setEnabled(true);
					exportAnalysisModelItem.setEnabled(true);
				}
			}
		});
		
		final AnalyseMenu analyseMenu = this;
		localeService.addServiceListener(new IServiceListener() {
			public void update() {
				analyseMenu.setText(localeService.getTranslatedString("Analyse"));
				setApplicationPropertiesItem.setText(localeService.getTranslatedString("ApplicationProperties"));
				analyseNowItem.setText(localeService.getTranslatedString("AnalyseNow"));
				analysedArchitectureDiagramItem.setText(localeService.getTranslatedString("AnalysedArchitectureDiagram"));
				analysedApplicationOverviewItem.setText(localeService.getTranslatedString("AnalysedApplicationOverview"));
				reportDependenciesItem.setText(localeService.getTranslatedString("ExportDependencies"));
				reconstructArchitectureItem.setText(localeService.getTranslatedString("ReconstructArchitecture"));
				setApplicationPropertiesItem.setMnemonic(getMnemonicKeycode("ApplicationPropertiesMnemonic"));
				setApplicationPropertiesItem.setMnemonic(getMnemonicKeycode("AnalyseNowMnemonic"));
				analysedApplicationOverviewItem.setMnemonic(getMnemonicKeycode("AnalysedApplicationOverviewMnemonic"));
				analysedArchitectureDiagramItem.setMnemonic(getMnemonicKeycode("AnalysedArchitectureDiagramMnemonic"));
				reportDependenciesItem.setMnemonic(getMnemonicKeycode("ReportDependenciesMnemonic"));
				reconstructArchitectureItem.setMnemonic(getMnemonicKeycode("ReconstructArchitectureNowMnemonic"));
			}	
		});
	}
	
	public JMenuItem getSetApplicationPropertiesItem(){
		return setApplicationPropertiesItem;
	}
	
	public JMenuItem getAnalysedArchitectureDiagramItem(){
		return analysedArchitectureDiagramItem;
	}
	
	public JMenuItem getAnalysedApplicationOverviewItem(){
		return analysedApplicationOverviewItem;
	}
	
	private int getMnemonicKeycode(String translatedString) {
		String mnemonicString = localeService.getTranslatedString(translatedString);
		int keyCode = KeyStroke.getKeyStroke(mnemonicString).getKeyCode();
		return keyCode;
	}
}
