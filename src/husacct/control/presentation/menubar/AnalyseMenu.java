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
	private JMenuItem setApplicationPropertiesItem;
	private JMenuItem analysedArchitectureDiagramItem;
	private JMenuItem analysedApplicationOverviewItem;
	
	private IControlService controlService = ServiceProvider.getInstance().getControlService();
	
	public AnalyseMenu(final MainController mainController){
		super();
		setText(controlService.getTranslatedString("Analyse"));
		
		setApplicationPropertiesItem = new JMenuItem(controlService.getTranslatedString("ApplicationProperties"));
		setApplicationPropertiesItem.setAccelerator(KeyStroke.getKeyStroke('P', KeyEvent.CTRL_DOWN_MASK));
		setApplicationPropertiesItem.setMnemonic('d');
		this.add(setApplicationPropertiesItem);
		setApplicationPropertiesItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getApplicationController().showApplicationDetailsGui();
			}
		});
		
		analysedApplicationOverviewItem = new JMenuItem(controlService.getTranslatedString("AnalysedApplicationOverview"));
		analysedApplicationOverviewItem.setAccelerator(KeyStroke.getKeyStroke('T', KeyEvent.CTRL_DOWN_MASK));
		analysedApplicationOverviewItem.setMnemonic('t');
		this.add(analysedApplicationOverviewItem);
		analysedApplicationOverviewItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getViewController().showApplicationTreeGui();
			}
		});
		
		analysedArchitectureDiagramItem = new JMenuItem(controlService.getTranslatedString("AnalysedArchitectureDiagram"));
		analysedArchitectureDiagramItem.setAccelerator(KeyStroke.getKeyStroke('A', KeyEvent.CTRL_DOWN_MASK));
		analysedArchitectureDiagramItem.setMnemonic('g');
		this.add(analysedArchitectureDiagramItem);
		analysedArchitectureDiagramItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getViewController().showAnalysedArchitectureGui();
			}
		});
		
		mainController.getStateController().addStateChangeListener(new IStateChangeListener() {
			public void changeState(List<States> states) {
				setApplicationPropertiesItem.setEnabled(false);
				analysedArchitectureDiagramItem.setEnabled(false);
				analysedApplicationOverviewItem.setEnabled(false);
				
				if(states.contains(States.OPENED)){
					setApplicationPropertiesItem.setEnabled(true);
				}
				
				if(states.contains(States.ANALYSED)){
					analysedArchitectureDiagramItem.setEnabled(true);
					analysedApplicationOverviewItem.setEnabled(true);
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
				setApplicationPropertiesItem.setText(controlService.getTranslatedString("ApplicationProperties"));
				analysedArchitectureDiagramItem.setText(controlService.getTranslatedString("AnalysedApplicationOverview"));
				analysedApplicationOverviewItem.setText(controlService.getTranslatedString("AnalysedArchitectureDiagram"));
			}
		});
	}
}
