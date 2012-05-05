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
	private JMenuItem setApplicationDetailsItem;
	private JMenuItem showLogicalGraphicsItem;
	private JMenuItem showAnalysedGraphicsItem;
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
		
		showAnalysedGraphicsItem = new JMenuItem("Show analysed architecture graphics");
		this.add(showAnalysedGraphicsItem);
		showAnalysedGraphicsItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getViewController().showAnalysedArchitectureGui();
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
				setApplicationDetailsItem.setEnabled(false);
				showLogicalGraphicsItem.setEnabled(false);
				showAnalysedGraphicsItem.setEnabled(false);
				importLogicalArchitectureItem.setEnabled(false);
				exportLogicalArchitectureItem.setEnabled(false);
				switch(state){
					case StateController.VALIDATED:
					case StateController.MAPPED: {
						importLogicalArchitectureItem.setEnabled(true);
					}
					case StateController.ANALYSED: {
						showAnalysedGraphicsItem.setEnabled(true);
					}
					case StateController.DEFINED: {
						exportLogicalArchitectureItem.setEnabled(true);
						showLogicalGraphicsItem.setEnabled(true);	
					}
					case StateController.EMPTY: {
						defineLogicalArchitectureItem.setEnabled(true);
						importLogicalArchitectureItem.setEnabled(true);
						setApplicationDetailsItem.setEnabled(true);	
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
