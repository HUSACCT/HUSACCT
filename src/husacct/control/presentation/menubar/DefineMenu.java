package husacct.control.presentation.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.common.services.IServiceListener;
import husacct.control.task.IStateChangeListener;
import husacct.control.task.MainController;
import husacct.control.task.States;

@SuppressWarnings("serial")
public class DefineMenu extends JMenu{
	private MainController mainController;
	private JMenu architectureDiagram;
	private JMenuItem defineArchitectureItem;
	private JMenuItem moduleAndDependenciesDiagramItem;
	private JMenuItem moduleAndRuleDiagramItem;
	private JMenuItem reportArchitectureItem;
	private JMenuItem exportArchitectureItem;
	private JMenuItem importArchitectureItem;

	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	
	public DefineMenu(final MainController mainController){
		super();
		this.mainController = mainController;
		setText(localeService.getTranslatedString("Define"));
		addComponents();
		setListeners();
	}
	
	private void addComponents() {
		defineArchitectureItem = new JMenuItem(localeService.getTranslatedString("DefineArchitecture"));
		defineArchitectureItem.setAccelerator(KeyStroke.getKeyStroke('D', KeyEvent.CTRL_DOWN_MASK));
		defineArchitectureItem.setMnemonic(getMnemonicKeycode("DefineArchitectureMnemonic"));
				
		moduleAndDependenciesDiagramItem = new JMenuItem(localeService.getTranslatedString("ModuleAndDependenciesDiagram"));
		moduleAndDependenciesDiagramItem.setAccelerator(KeyStroke.getKeyStroke('T', KeyEvent.CTRL_DOWN_MASK));
		moduleAndDependenciesDiagramItem.setMnemonic(getMnemonicKeycode("DefinedArchitectureDiagramMnemonic"));
		
		moduleAndRuleDiagramItem = new JMenuItem(localeService.getTranslatedString("ModuleAndRuleDiagram"));
		moduleAndRuleDiagramItem.setAccelerator(KeyStroke.getKeyStroke('M', KeyEvent.CTRL_DOWN_MASK));
		moduleAndRuleDiagramItem.setMnemonic(getMnemonicKeycode("ModuleAndRuleDiagramMnemonic"));
				
		exportArchitectureItem = new JMenuItem(localeService.getTranslatedString("ExportArchitecture"));
		exportArchitectureItem.setMnemonic(getMnemonicKeycode("ExportArchitectureMnemonic"));		
				
		importArchitectureItem = new JMenuItem(localeService.getTranslatedString("ImportArchitecture"));
		importArchitectureItem.setMnemonic(getMnemonicKeycode("ImportArchitectureMnemonic"));
				
		reportArchitectureItem = new JMenuItem(localeService.getTranslatedString("ReportArchitecture"));
		reportArchitectureItem.setMnemonic(getMnemonicKeycode("ReportArchitectureMnemonic"));
		
		architectureDiagram = new JMenu(localeService.getTranslatedString("DefinedArchitectureDiagram"));
		architectureDiagram.add(moduleAndRuleDiagramItem);
		architectureDiagram.add(moduleAndDependenciesDiagramItem);
		
		this.add(defineArchitectureItem);
		this.add(architectureDiagram);
		this.add(exportArchitectureItem);
		this.add(importArchitectureItem);
		this.add(reportArchitectureItem);
	}
	
	private void setListeners() {
		defineArchitectureItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				mainController.getViewController().showDefineArchitecture();
			}
		});
		
		moduleAndDependenciesDiagramItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				mainController.getViewController().showDefinedArchitectureDiagram();
			}
		});
		
		moduleAndRuleDiagramItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				mainController.getViewController().showModuleAndRuleDiagram();
			}
		});
		
		exportArchitectureItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				mainController.getExportImportController().showExportArchitectureGui();
			}
		});
		
		importArchitectureItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				mainController.getExportImportController().showImportArchitectureGui();
				mainController.getViewController().showDefineArchitecture();
			}
		});
		
		reportArchitectureItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				mainController.getExportImportController().showReportArchitectureGui();
			}
		});
		
		mainController.getStateController().addStateChangeListener(new IStateChangeListener() {
			@Override
			public void changeState(List<States> states) {
				defineArchitectureItem.setEnabled(false);
				moduleAndDependenciesDiagramItem.setEnabled(false);
				moduleAndRuleDiagramItem.setEnabled(false);
				importArchitectureItem.setEnabled(false);
				exportArchitectureItem.setEnabled(false);
				reportArchitectureItem.setEnabled(false);
				
				if(states.contains(States.OPENED)){
					defineArchitectureItem.setEnabled(true);
					importArchitectureItem.setEnabled(true);
				}
				
				if(states.contains(States.DEFINED) || states.contains(States.MAPPED)){
					exportArchitectureItem.setEnabled(true);
					moduleAndDependenciesDiagramItem.setEnabled(true);
					moduleAndRuleDiagramItem.setEnabled(true);
					reportArchitectureItem.setEnabled(true);
				}
			}
		});
		
		final DefineMenu defineMenu = this;
		localeService.addServiceListener(new IServiceListener() {
			@Override
			public void update() {
				defineMenu.setText(localeService.getTranslatedString("Define"));
				defineArchitectureItem.setText(localeService.getTranslatedString("DefineArchitecture"));
				moduleAndDependenciesDiagramItem.setText(localeService.getTranslatedString("ModuleAndDependenciesDiagram"));
				moduleAndRuleDiagramItem.setText(localeService.getTranslatedString("ModuleAndRuleDiagram"));
				moduleAndRuleDiagramItem.setMnemonic(getMnemonicKeycode("ModuleAndRuleDiagramMnemonic"));
				exportArchitectureItem.setText(localeService.getTranslatedString("ExportArchitecture"));
				importArchitectureItem.setText(localeService.getTranslatedString("ImportArchitecture"));
				reportArchitectureItem.setText(localeService.getTranslatedString("ReportArchitecture"));
				defineArchitectureItem.setMnemonic(getMnemonicKeycode("DefineArchitectureMnemonic"));
				moduleAndDependenciesDiagramItem.setMnemonic(getMnemonicKeycode("DefinedArchitectureDiagramMnemonic"));
				exportArchitectureItem.setMnemonic(getMnemonicKeycode("ExportArchitectureMnemonic"));	
				importArchitectureItem.setMnemonic(getMnemonicKeycode("ImportArchitectureMnemonic"));
				reportArchitectureItem.setMnemonic(getMnemonicKeycode("ReportArchitectureMnemonic"));
			}
		});
	}
	
	public JMenuItem getDefineArchitectureItem(){
		return defineArchitectureItem;
	}
	public JMenuItem getModuleAndDependenciesDiagramItem(){
		return moduleAndDependenciesDiagramItem;
	}
	public JMenuItem getModuleAndRuleDiagramItem(){
		return moduleAndRuleDiagramItem;
	}
	public JMenuItem getExportArchitectureItem(){
		return exportArchitectureItem;
	}
	public JMenuItem getIimportArchitectureItem(){
		return importArchitectureItem;
	}
	public JMenuItem getReportArchitectureItem(){
		return reportArchitectureItem;
	}
	public JMenu getArchitectureDiagram(){
		return architectureDiagram;
	}
	private int getMnemonicKeycode(String translatedString) {
		String mnemonicString = localeService.getTranslatedString(translatedString);
		int keyCode = KeyStroke.getKeyStroke(mnemonicString).getKeyCode();
		return keyCode;
	}
}
