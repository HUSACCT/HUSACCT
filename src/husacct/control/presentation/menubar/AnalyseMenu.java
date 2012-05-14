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
public class AnalyseMenu extends JMenu{
	private JMenuItem setApplicationDetailsItem;
	private JMenuItem showAnalysedGraphicsItem;
	private JMenuItem showApplicationTreeItem;
	
	private IControlService controlService = ServiceProvider.getInstance().getControlService();
	
	public AnalyseMenu(final MainController mainController){
		super();
		setText(controlService.getTranslatedString("Analyse"));
		
		setApplicationDetailsItem = new JMenuItem(controlService.getTranslatedString("ApplicationDetails"));
		setApplicationDetailsItem.setAccelerator(KeyStroke.getKeyStroke('P', KeyEvent.CTRL_DOWN_MASK));
		setApplicationDetailsItem.setMnemonic('d');
		this.add(setApplicationDetailsItem);
		setApplicationDetailsItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getApplicationController().showApplicationDetailsGui();
			}
		});
		
		showApplicationTreeItem = new JMenuItem(controlService.getTranslatedString("ShowApplicationTree"));
		showApplicationTreeItem.setAccelerator(KeyStroke.getKeyStroke('T', KeyEvent.CTRL_DOWN_MASK));
		showApplicationTreeItem.setMnemonic('t');
		this.add(showApplicationTreeItem);
		showApplicationTreeItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getViewController().showApplicationTreeGui();
			}
		});
		
		showAnalysedGraphicsItem = new JMenuItem(controlService.getTranslatedString("ShowAnalysedArchitectureGraphics"));
		showAnalysedGraphicsItem.setAccelerator(KeyStroke.getKeyStroke('A', KeyEvent.CTRL_DOWN_MASK));
		showAnalysedGraphicsItem.setMnemonic('g');
		this.add(showAnalysedGraphicsItem);
		showAnalysedGraphicsItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getViewController().showAnalysedArchitectureGui();
			}
		});
		
		mainController.getStateController().addStateChangeListener(new IStateChangeListener() {
			public void changeState(List<States> states) {
				setApplicationDetailsItem.setEnabled(false);
				showAnalysedGraphicsItem.setEnabled(false);
				showApplicationTreeItem.setEnabled(false);
				
				if(states.contains(States.OPENED)){
					setApplicationDetailsItem.setEnabled(true);
				}
				
				if(states.contains(States.ANALYSED)){
					showAnalysedGraphicsItem.setEnabled(true);
					showApplicationTreeItem.setEnabled(true);
				}
			}
		});
		
		this.addMenuListener(new MenuListenerAdapter() {
			public void menuSelected(MenuEvent e) {
				mainController.getStateController().checkState();
			}
		});
		
		final AnalyseMenu analyseMenu = this;
		controlService.addLocaleChangeListener(new ILocaleChangeListener() {
			public void update(Locale newLocale) {
				analyseMenu.setText(controlService.getTranslatedString("Analyse"));
				setApplicationDetailsItem.setText(controlService.getTranslatedString("ApplicationDetails"));
				showAnalysedGraphicsItem.setText(controlService.getTranslatedString("ShowApplicationTree"));
				showApplicationTreeItem.setText(controlService.getTranslatedString("ShowAnalysedArchitectureGraphics"));
			}
		});
	}
}
