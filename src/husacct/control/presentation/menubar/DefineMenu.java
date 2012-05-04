package husacct.control.presentation.menubar;

import husacct.control.task.IStateChangeListener;
import husacct.control.task.MainController;
import husacct.control.task.StateController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

@SuppressWarnings("serial")
public class DefineMenu extends JMenu{
	private JMenuItem importLogicalArchitectureItem;
	private JMenuItem defineLogicalArchitectureItem;
	private JMenuItem setApplicationDetailsItem;
	private JMenuItem showLogicalGraphicsItem;
	private JMenuItem exportLogicalArchitectureItem;

	public DefineMenu(final MainController mainController){
		super("Define");

		defineLogicalArchitectureItem = new JMenuItem("Define logical architecture");
		this.add(defineLogicalArchitectureItem);

		defineLogicalArchitectureItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getViewController().showDefineGui();
			}
		});

		setApplicationDetailsItem = new JMenuItem("Application details");
		this.add(setApplicationDetailsItem);
		setApplicationDetailsItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getApplicationController().showApplicationDetailsGui();
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
		
		//disable buttons on start
		defineLogicalArchitectureItem.setEnabled(false);
		importLogicalArchitectureItem.setEnabled(false);
		setApplicationDetailsItem.setEnabled(false);
		showLogicalGraphicsItem.setEnabled(false);
		importLogicalArchitectureItem.setEnabled(false);
		exportLogicalArchitectureItem.setEnabled(false);
		
		mainController.getStateController().addStateChangeListener(new IStateChangeListener() {

			public void changeState(int state) {
				if(state == StateController.NONE){
					defineLogicalArchitectureItem.setEnabled(false);
					importLogicalArchitectureItem.setEnabled(false);
				}else if(state == StateController.EMPTY){
					defineLogicalArchitectureItem.setEnabled(true);
					importLogicalArchitectureItem.setEnabled(true);
					importLogicalArchitectureItem.setEnabled(true);
					setApplicationDetailsItem.setEnabled(true);
				} else if(state >= StateController.DEFINED){
					defineLogicalArchitectureItem.setEnabled(true);
					importLogicalArchitectureItem.setEnabled(true);
					setApplicationDetailsItem.setEnabled(true);
					showLogicalGraphicsItem.setEnabled(true);
					importLogicalArchitectureItem.setEnabled(true);
					exportLogicalArchitectureItem.setEnabled(true);
				}
			}
		});
		
		// TODO: refactor including adapter
		this.addMenuListener(new MenuListener() {
			
			@Override
			public void menuSelected(MenuEvent arg0) {
				mainController.getStateController().checkState();
				
			}

			@Override
			public void menuCanceled(MenuEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void menuDeselected(MenuEvent e) {
				// TODO Auto-generated method stub
				
			}

		});
	}
}
