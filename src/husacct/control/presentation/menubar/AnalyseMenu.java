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
public class AnalyseMenu extends JMenu{
	private JMenuItem setApplicationDetailsItem;
	private JMenuItem showAnalysedGraphicsItem;

	public AnalyseMenu(final MainController mainController){
		super("Analyse");

		setApplicationDetailsItem = new JMenuItem("Application details");
		this.add(setApplicationDetailsItem);
		setApplicationDetailsItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getApplicationController().showApplicationDetailsGui();
			}
		});
		
		showAnalysedGraphicsItem = new JMenuItem("Show analysed architecture graphics");
		this.add(showAnalysedGraphicsItem);
		showAnalysedGraphicsItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getViewController().showAnalysedArchitectureGui();
			}
		});
		
		mainController.getStateController().addStateChangeListener(new IStateChangeListener() {
			public void changeState(int state) {
				setApplicationDetailsItem.setEnabled(false);
				showAnalysedGraphicsItem.setEnabled(false);
				switch(state){
					case StateController.VALIDATED:
					case StateController.MAPPED:
					case StateController.ANALYSED: {
						showAnalysedGraphicsItem.setEnabled(true);
					}
					case StateController.DEFINED: {
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
