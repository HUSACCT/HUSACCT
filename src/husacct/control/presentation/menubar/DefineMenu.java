package husacct.control.presentation.menubar;

import husacct.ServiceProvider;
import husacct.control.IControlService;
import husacct.control.ILocaleChangeListener;
import husacct.control.task.IStateChangeListener;
import husacct.control.task.MainController;
import husacct.control.task.States;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Locale;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class DefineMenu extends JMenu{
	private MainController mainController;
	private JMenuItem defineArchitectureItem;
	private JMenuItem definedArchitectureDiagramItem;
	private JMenuItem exportArchitectureItem;
	private JMenuItem importArchitectureItem;

	private IControlService controlService = ServiceProvider.getInstance().getControlService();
	
	public DefineMenu(final MainController mainController){
		super();
		this.mainController = mainController;
		setText(controlService.getTranslatedString("Define"));
		addComponents();
		setListeners();
	}
	
	private void addComponents() {
		defineArchitectureItem = new JMenuItem(controlService.getTranslatedString("DefineArchitecture"));
		defineArchitectureItem.setAccelerator(KeyStroke.getKeyStroke('D', KeyEvent.CTRL_DOWN_MASK));
		defineArchitectureItem.setMnemonic(getMnemonicKeycode("DefineArchitectureMnemonic"));
				
		definedArchitectureDiagramItem = new JMenuItem(controlService.getTranslatedString("DefinedArchitectureDiagram"));
		definedArchitectureDiagramItem.setAccelerator(KeyStroke.getKeyStroke('L', KeyEvent.CTRL_DOWN_MASK));
		definedArchitectureDiagramItem.setMnemonic(getMnemonicKeycode("DefinedArchitectureDiagramMnemonic"));
				
		importArchitectureItem = new JMenuItem(controlService.getTranslatedString("ImportArchitecture"));
		importArchitectureItem.setMnemonic(getMnemonicKeycode("ImportArchitectureMnemonic"));
				
		exportArchitectureItem = new JMenuItem(controlService.getTranslatedString("ExportArchitecture"));
		exportArchitectureItem.setMnemonic(getMnemonicKeycode("ExportArchitectureMnemonic"));		
		
		this.add(defineArchitectureItem);
		this.add(definedArchitectureDiagramItem);
		this.add(importArchitectureItem);
		this.add(exportArchitectureItem);
	}
	
	private void setListeners() {
		defineArchitectureItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getViewController().showDefineGui();
			}
		});
		
		definedArchitectureDiagramItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getViewController().showDefinedArchitectureGui();
			}
		});
		
		importArchitectureItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getImportController().showImportArchitectureGui();
			}
		});
		
		exportArchitectureItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getExportController().showExportArchitectureGui();
			}
		});
		
		mainController.getStateController().addStateChangeListener(new IStateChangeListener() {
			public void changeState(List<States> states) {
				defineArchitectureItem.setEnabled(false);
				definedArchitectureDiagramItem.setEnabled(false);
				importArchitectureItem.setEnabled(false);
				exportArchitectureItem.setEnabled(false);
				
				if(states.contains(States.OPENED)){
					defineArchitectureItem.setEnabled(true);
					importArchitectureItem.setEnabled(true);
				}
				
				if(states.contains(States.DEFINED) || states.contains(States.MAPPED)){
					exportArchitectureItem.setEnabled(true);
					definedArchitectureDiagramItem.setEnabled(true);
				}
			}
		});
		
		final DefineMenu defineMenu = this;
		controlService.addLocaleChangeListener(new ILocaleChangeListener() {
			public void update(Locale newLocale) {
				defineMenu.setText(controlService.getTranslatedString("Define"));
				defineArchitectureItem.setText(controlService.getTranslatedString("DefineArchitecture"));
				definedArchitectureDiagramItem.setText(controlService.getTranslatedString("DefinedArchitectureDiagram"));
				exportArchitectureItem.setText(controlService.getTranslatedString("ExportArchitecture"));
				importArchitectureItem.setText(controlService.getTranslatedString("ImportArchitecture"));
				defineArchitectureItem.setMnemonic(getMnemonicKeycode("DefineArchitectureMnemonic"));
				definedArchitectureDiagramItem.setMnemonic(getMnemonicKeycode("DefinedArchitectureDiagramMnemonic"));
				importArchitectureItem.setMnemonic(getMnemonicKeycode("ImportArchitectureMnemonic"));
				exportArchitectureItem.setMnemonic(getMnemonicKeycode("ExportArchitectureMnemonic"));	
			}
		});
	}
	
	public JMenuItem getDefineArchitectureItem(){
		return defineArchitectureItem;
	}
	public JMenuItem getDefinedArchitectureDiagramItem(){
		return definedArchitectureDiagramItem;
	}
	public JMenuItem getExportArchitectureItem(){
		return exportArchitectureItem;
	}
	public JMenuItem getIimportArchitectureItem(){
		return importArchitectureItem;
	}
	
	private int getMnemonicKeycode(String translatedString) {
		String mnemonicString = controlService.getTranslatedString(translatedString);
		int keyCode = KeyStroke.getKeyStroke(mnemonicString).getKeyCode();
		return keyCode;
	}
}
