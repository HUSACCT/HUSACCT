package husacct.control.presentation.menubar;

import husacct.control.task.IStateChangeListener;
import husacct.control.task.MainController;
import husacct.control.task.StateController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;

@SuppressWarnings("serial")
public class DefineMenu extends JMenu{
	private JMenuItem defineLogicalArchitectureItem;
	private JMenuItem showLogicalGraphicsItem;
	private JMenuItem exportLogicalArchitectureItem;
	private JMenuItem importLogicalArchitectureItem;

	public DefineMenu(final MainController mainController){
		super("Define");

		defineLogicalArchitectureItem = new JMenuItem("Define logical architecture");
		this.add(defineLogicalArchitectureItem);

		defineLogicalArchitectureItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getViewController().showDefineGui();
			}
		});
		
		showLogicalGraphicsItem = new JMenuItem("Show logical architecture graphics");
		this.add(showLogicalGraphicsItem);
		showLogicalGraphicsItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getViewController().showDefinedArchitectureGui();
			}
		});
		
		importLogicalArchitectureItem = new JMenuItem("Import logical architecture");
		this.add(importLogicalArchitectureItem);
		importLogicalArchitectureItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getImportExportController().showImportLogicalArchitectureGui();
			}
		});
		
		exportLogicalArchitectureItem = new JMenuItem("Export logical architecture");
		this.add(exportLogicalArchitectureItem);
		exportLogicalArchitectureItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getImportExportController().showExportLogicalArchitectureGui();
			}
		});
		
		mainController.getStateController().addStateChangeListener(new IStateChangeListener() {
			public void changeState(int state) {
				defineLogicalArchitectureItem.setEnabled(false);
				showLogicalGraphicsItem.setEnabled(false);
				importLogicalArchitectureItem.setEnabled(false);
				exportLogicalArchitectureItem.setEnabled(false);
				switch(state){
					case StateController.VALIDATED:
					case StateController.MAPPED: {
						importLogicalArchitectureItem.setEnabled(true);
					}
					case StateController.ANALYSED:
					case StateController.DEFINED: {
						exportLogicalArchitectureItem.setEnabled(true);
						showLogicalGraphicsItem.setEnabled(true);	
					}
					case StateController.EMPTY: {
						defineLogicalArchitectureItem.setEnabled(true);
						importLogicalArchitectureItem.setEnabled(true);
					}
				}
			}
		});
		
		this.addMenuListener(new MenuListenerAdapter() {
			public void menuSelected(MenuEvent e) {
				mainController.getStateController().checkState();
			}
		});
	}
}
