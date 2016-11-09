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
//	private JMenu architectureDiagram;
//	private JMenuItem moduleAndRuleDiagramItem; //Disabled in vs 5.1; waiting for improvement; see Graphics system documentation
	private JMenuItem defineArchitectureItem;
	private JMenuItem definedArchitectureDiagramItem;
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
				
		definedArchitectureDiagramItem = new JMenuItem(localeService.getTranslatedString("DefinedArchitectureDiagram"));
		definedArchitectureDiagramItem.setAccelerator(KeyStroke.getKeyStroke('T', KeyEvent.CTRL_DOWN_MASK));
		definedArchitectureDiagramItem.setMnemonic(getMnemonicKeycode("DefinedArchitectureDiagramMnemonic"));
		
		exportArchitectureItem = new JMenuItem(localeService.getTranslatedString("ExportArchitecture"));
		exportArchitectureItem.setMnemonic(getMnemonicKeycode("ExportArchitectureMnemonic"));		
				
		importArchitectureItem = new JMenuItem(localeService.getTranslatedString("ImportArchitecture"));
		importArchitectureItem.setMnemonic(getMnemonicKeycode("ImportArchitectureMnemonic"));
				
		reportArchitectureItem = new JMenuItem(localeService.getTranslatedString("ReportArchitecture"));
		reportArchitectureItem.setMnemonic(getMnemonicKeycode("ReportArchitectureMnemonic"));
		
		/*
		architectureDiagram = new JMenu(localeService.getTranslatedString("DefinedArchitectureDiagram"));
		moduleAndDependenciesDiagramItem = new JMenuItem(localeService.getTranslatedString("ModuleAndDependenciesDiagram"));
		moduleAndDependenciesDiagramItem.setAccelerator(KeyStroke.getKeyStroke('T', KeyEvent.CTRL_DOWN_MASK));
		moduleAndDependenciesDiagramItem.setMnemonic(getMnemonicKeycode("DefinedArchitectureDiagramMnemonic"));
		moduleAndRuleDiagramItem = new JMenuItem(localeService.getTranslatedString("ModuleAndRuleDiagram"));
		moduleAndRuleDiagramItem.setAccelerator(KeyStroke.getKeyStroke('M', KeyEvent.CTRL_DOWN_MASK));
		moduleAndRuleDiagramItem.setMnemonic(getMnemonicKeycode("ModuleAndRuleDiagramMnemonic"));
		architectureDiagram.add(moduleAndDependenciesDiagramItem);
		architectureDiagram.add(moduleAndRuleDiagramItem);
		*/
		
		this.add(defineArchitectureItem);
		this.add(definedArchitectureDiagramItem);
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
		
		definedArchitectureDiagramItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				mainController.getViewController().showDefinedArchitectureDiagram();
			}
		});
		
		/*
		moduleAndRuleDiagramItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				mainController.getViewController().showModuleAndRuleDiagram();
			}
		});
		*/
		
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
				definedArchitectureDiagramItem.setEnabled(false);
				//moduleAndRuleDiagramItem.setEnabled(false);
				importArchitectureItem.setEnabled(false);
				exportArchitectureItem.setEnabled(false);
				reportArchitectureItem.setEnabled(false);
				
				if(states.contains(States.OPENED)){
					defineArchitectureItem.setEnabled(true);
					importArchitectureItem.setEnabled(true);
				}
				
				if(states.contains(States.DEFINED) || states.contains(States.MAPPED)){
					exportArchitectureItem.setEnabled(true);
					definedArchitectureDiagramItem.setEnabled(true);
					//moduleAndRuleDiagramItem.setEnabled(true);
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
				definedArchitectureDiagramItem.setText(localeService.getTranslatedString("DefinedArchitectureDiagram"));
				//moduleAndRuleDiagramItem.setText(localeService.getTranslatedString("ModuleAndRuleDiagram"));
				exportArchitectureItem.setText(localeService.getTranslatedString("ExportArchitecture"));
				importArchitectureItem.setText(localeService.getTranslatedString("ImportArchitecture"));
				reportArchitectureItem.setText(localeService.getTranslatedString("ReportArchitecture"));
				defineArchitectureItem.setMnemonic(getMnemonicKeycode("DefineArchitectureMnemonic"));
				definedArchitectureDiagramItem.setMnemonic(getMnemonicKeycode("DefinedArchitectureDiagramMnemonic"));
				//moduleAndRuleDiagramItem.setMnemonic(getMnemonicKeycode("ModuleAndRuleDiagramMnemonic"));
				exportArchitectureItem.setMnemonic(getMnemonicKeycode("ExportArchitectureMnemonic"));	
				importArchitectureItem.setMnemonic(getMnemonicKeycode("ImportArchitectureMnemonic"));
				reportArchitectureItem.setMnemonic(getMnemonicKeycode("ReportArchitectureMnemonic"));
			}
		});
	}
	
	public JMenuItem getDefineArchitectureItem(){
		return defineArchitectureItem;
	}
	public JMenuItem definedArchitectureDiagramItem(){
		return definedArchitectureDiagramItem;
	}
	/*
	public JMenuItem getModuleAndRuleDiagramItem(){
		return moduleAndRuleDiagramItem;
	}
	*/
	public JMenuItem getExportArchitectureItem(){
		return exportArchitectureItem;
	}
	public JMenuItem getIimportArchitectureItem(){
		return importArchitectureItem;
	}
	public JMenuItem getReportArchitectureItem(){
		return reportArchitectureItem;
	}
	private int getMnemonicKeycode(String translatedString) {
		String mnemonicString = localeService.getTranslatedString(translatedString);
		int keyCode = KeyStroke.getKeyStroke(mnemonicString).getKeyCode();
		return keyCode;
	}
}
