package husacct.control.presentation.menubar;

import husacct.control.presentation.menubar.popup.AnalyseApplicationFrame;
import husacct.control.task.IStateChangeListener;
import husacct.control.task.MainController;
import husacct.control.task.StateController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

@SuppressWarnings("serial")
public class DefineMenu extends JMenu{
	private JMenuItem importLogicalArchitectureItem;
	private JMenuItem defineLogicalArchitectureItem;
	private JMenuItem setApplicationItem;
	private JMenuItem showLogicalGraphicsItem;
	private JFrame analyseApplicationFrame = new AnalyseApplicationFrame();


	public DefineMenu(final MainController mainController){
		super("Define");

		defineLogicalArchitectureItem = new JMenuItem("Define logical architecture");
		this.add(defineLogicalArchitectureItem);

		defineLogicalArchitectureItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getViewController().showDefineGui();
			}
		});

		setApplicationItem = new JMenuItem("Analyse application");
		this.add(setApplicationItem);
		setApplicationItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				analyseApplicationFrame.setVisible(true);
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
				// TODO: import gui
			}
		});

		//disable buttons on start
		defineLogicalArchitectureItem.setEnabled(false);
		importLogicalArchitectureItem.setEnabled(false);
		setApplicationItem.setEnabled(false);
		showLogicalGraphicsItem.setEnabled(false);
		
		mainController.getStateController().addStateChangeListener(new IStateChangeListener() {

			public void changeState(int state) {
				if(state == StateController.NONE){
					defineLogicalArchitectureItem.setEnabled(false);
					importLogicalArchitectureItem.setEnabled(false);
				}else if(state == StateController.EMPTY){
					defineLogicalArchitectureItem.setEnabled(true);
					importLogicalArchitectureItem.setEnabled(true);
					setApplicationItem.setEnabled(true);
				} else if(state >= StateController.DEFINED){
					defineLogicalArchitectureItem.setEnabled(true);
					importLogicalArchitectureItem.setEnabled(true);
					setApplicationItem.setEnabled(true);
					showLogicalGraphicsItem.setEnabled(true);
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
