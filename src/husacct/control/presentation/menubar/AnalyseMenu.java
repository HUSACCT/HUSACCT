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
	private MainController mainController;
	private JMenuItem setApplicationPropertiesItem;
	private JMenuItem analysedArchitectureDiagramItem;
	private JMenuItem analysedApplicationOverviewItem;
	
	private IControlService controlService = ServiceProvider.getInstance().getControlService();
	
	public AnalyseMenu(final MainController mainController){
		super();
		this.mainController = mainController;
		setText(controlService.getTranslatedString("Analyse"));
		addComponents();
		setListeners();	
	}
	
	private void addComponents(){
		setApplicationPropertiesItem = new JMenuItem(controlService.getTranslatedString("ApplicationProperties"));
		setApplicationPropertiesItem.setAccelerator(KeyStroke.getKeyStroke('P', KeyEvent.CTRL_DOWN_MASK));
		setApplicationPropertiesItem.setMnemonic(getMnemonicKeycode("ApplicationPropertiesMnemonic"));
		
		analysedApplicationOverviewItem = new JMenuItem(controlService.getTranslatedString("AnalysedApplicationOverview"));
		analysedApplicationOverviewItem.setAccelerator(KeyStroke.getKeyStroke('T', KeyEvent.CTRL_DOWN_MASK));
		analysedApplicationOverviewItem.setMnemonic(getMnemonicKeycode("AnalysedApplicationOverviewMnemonic"));
			
		analysedArchitectureDiagramItem = new JMenuItem(controlService.getTranslatedString("AnalysedArchitectureDiagram"));
		analysedArchitectureDiagramItem.setAccelerator(KeyStroke.getKeyStroke('A', KeyEvent.CTRL_DOWN_MASK));
		analysedArchitectureDiagramItem.setMnemonic(getMnemonicKeycode("AnalysedArchitectureDiagramMnemonic"));
		
		this.add(setApplicationPropertiesItem);
		this.add(analysedApplicationOverviewItem);
		this.add(analysedArchitectureDiagramItem);
		
	}
	
	private void setListeners() {
		setApplicationPropertiesItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getApplicationController().showApplicationDetailsGui();
			}
		});
		
		analysedApplicationOverviewItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getViewController().showApplicationOverviewGui();
			}
		});
		
		analysedArchitectureDiagramItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getViewController().showAnalysedArchitectureGui();
			}
		});
		
		this.addMenuListener(new MenuAdapter() {
			public void menuSelected(MenuEvent e) {
				mainController.getStateController().checkState();
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
		
		this.addMenuListener(new MenuAdapter() {
			public void menuSelected(MenuEvent e) {
				mainController.getStateController().checkState();
			}
		});
		
		final AnalyseMenu analyseMenu = this;
		controlService.addLocaleChangeListener(new ILocaleChangeListener() {
			public void update(Locale newLocale) {
				analyseMenu.setText(controlService.getTranslatedString("Analyse"));
				setApplicationPropertiesItem.setText(controlService.getTranslatedString("ApplicationProperties"));
				analysedArchitectureDiagramItem.setText(controlService.getTranslatedString("AnalysedArchitectureDiagram"));
				analysedApplicationOverviewItem.setText(controlService.getTranslatedString("AnalysedApplicationOverview"));
				setApplicationPropertiesItem.setMnemonic(getMnemonicKeycode("ApplicationPropertiesMnemonic"));
				analysedApplicationOverviewItem.setMnemonic(getMnemonicKeycode("AnalysedApplicationOverviewMnemonic"));
				analysedArchitectureDiagramItem.setMnemonic(getMnemonicKeycode("AnalysedArchitectureDiagramMnemonic"));
			}
		});
	}
	
	private int getMnemonicKeycode(String translatedString) {
		String mnemonicString = controlService.getTranslatedString(translatedString);
		int keyCode = KeyStroke.getKeyStroke(mnemonicString).getKeyCode();
		return keyCode;
	}
}
