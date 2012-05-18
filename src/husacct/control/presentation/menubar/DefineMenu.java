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
import javax.swing.event.MenuEvent;

@SuppressWarnings("serial")
public class DefineMenu extends JMenu{
	private JMenuItem defineLogicalArchitectureItem;
	private JMenuItem showLogicalGraphicsItem;
	private JMenuItem exportLogicalArchitectureItem;
	private JMenuItem importLogicalArchitectureItem;

	private IControlService controlService = ServiceProvider.getInstance().getControlService();
	
	public DefineMenu(final MainController mainController){
		super();
		setText(controlService.getTranslatedString("Define"));
		
		defineLogicalArchitectureItem = new JMenuItem(controlService.getTranslatedString("DefineLogicalArchitecture"));
		defineLogicalArchitectureItem.setAccelerator(KeyStroke.getKeyStroke('D', KeyEvent.CTRL_DOWN_MASK));
		defineLogicalArchitectureItem.setMnemonic('d');
		this.add(defineLogicalArchitectureItem);

		defineLogicalArchitectureItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getViewController().showDefineGui();
			}
		});
		
		showLogicalGraphicsItem = new JMenuItem(controlService.getTranslatedString("ShowLogicalArchitectureGraphics"));
		showLogicalGraphicsItem.setAccelerator(KeyStroke.getKeyStroke('L', KeyEvent.CTRL_DOWN_MASK));
		showLogicalGraphicsItem.setMnemonic('s');
		this.add(showLogicalGraphicsItem);
		showLogicalGraphicsItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getViewController().showDefinedArchitectureGui();
			}
		});
		
		importLogicalArchitectureItem = new JMenuItem(controlService.getTranslatedString("ImportLogicalArchitecture"));
		importLogicalArchitectureItem.setMnemonic('i');
		this.add(importLogicalArchitectureItem);
		importLogicalArchitectureItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getImportController().showImportLogicalArchitectureGui();
			}
		});
		
		exportLogicalArchitectureItem = new JMenuItem(controlService.getTranslatedString("ExportLogicalArchitecture"));
		exportLogicalArchitectureItem.setMnemonic('e');
		this.add(exportLogicalArchitectureItem);
		exportLogicalArchitectureItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getExportController().showExportLogicalArchitectureGui();
			}
		});
		
		mainController.getStateController().addStateChangeListener(new IStateChangeListener() {
			public void changeState(List<States> states) {
				defineLogicalArchitectureItem.setEnabled(false);
				showLogicalGraphicsItem.setEnabled(false);
				importLogicalArchitectureItem.setEnabled(false);
				exportLogicalArchitectureItem.setEnabled(false);
				
				if(states.contains(States.OPENED)){
					defineLogicalArchitectureItem.setEnabled(true);
					importLogicalArchitectureItem.setEnabled(true);
				}
				
				if(states.contains(States.DEFINED) || states.contains(States.MAPPED)){
					exportLogicalArchitectureItem.setEnabled(true);
					showLogicalGraphicsItem.setEnabled(true);
				}
			}
		});
		
		this.addMenuListener(new MenuListenerAdapter() {
			public void menuSelected(MenuEvent e) {
				mainController.getStateController().checkState();
			}
		});
		
		final DefineMenu defineMenu = this;
		controlService.addLocaleChangeListener(new ILocaleChangeListener() {
			public void update(Locale newLocale) {
				defineMenu.setText(controlService.getTranslatedString("Define"));
				defineLogicalArchitectureItem.setText(controlService.getTranslatedString("DefineLogicalArchitecture"));
				showLogicalGraphicsItem.setText(controlService.getTranslatedString("ShowLogicalArchitectureGraphics"));
				exportLogicalArchitectureItem.setText(controlService.getTranslatedString("ExportLogicalArchitecture"));
				importLogicalArchitectureItem.setText(controlService.getTranslatedString("ImportLogicalArchitecture"));
			}
		});
	}
}
