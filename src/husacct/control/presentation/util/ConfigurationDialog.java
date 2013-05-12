package husacct.control.presentation.util;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.common.services.IConfigurable;
import husacct.common.services.IServiceListener;
import husacct.control.task.MainController;
import husacct.control.task.configuration.ConfigPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class ConfigurationDialog extends JDialog {
	
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	private ArrayList<IConfigurable> configurableServices = new ArrayList<IConfigurable>();
	private ArrayList<ConfigPanel> configPanels = new ArrayList<ConfigPanel>();
	
	private JButton save = new JButton(), reset = new JButton(), cancel = new JButton();
	private JPanel sidebarPanel = new JPanel(new BorderLayout()), mainPanel= new JPanel(), buttonPanel = new JPanel();
	private JList<String> list;
	
	private HashMap<String, JPanel> configPanelMap = new HashMap<String, JPanel>();
	
	
	public ConfigurationDialog(MainController mainController) {
		super(mainController.getMainGui(), true);
		this.setLayout(new BorderLayout());
		initiliaze();
		setComponentText();
		this.setVisible(true);
	}
	
	public void initiliaze() {
		getConfigurableServices();
		getConfigPanels();
		mainPanel = configurableServices.get(0).getConfigurationPanel();
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(new Dimension(800, 600));
		loadSidePanel();
		loadButtons();
		
		list.setBackground(Color.WHITE);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		
		sidebarPanel.setPreferredSize(new Dimension(160, 0));
		this.add(sidebarPanel, BorderLayout.WEST);
		this.add(mainPanel, BorderLayout.CENTER);
		DialogUtils.alignCenter(this);	
		
		localeService.addServiceListener(new IServiceListener() {
			public void update() {
				setComponentText();
				loadSidePanel();
			}
		});
	}
	
	public void loadSidePanel() {
		sidebarPanel.removeAll();
		
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		for(final IConfigurable config : configurableServices) {
			listModel.addElement(config.getConfigurationName());
			configPanelMap.put(config.getConfigurationName(), config.getConfigurationPanel());
		}
		list = new JList<String>(listModel);
		list.setSelectedIndex(0);
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				if(event.getValueIsAdjusting()) {
					mainPanel = configPanelMap.get(list.getSelectedValue());
				}
			}
		});
		
		JScrollPane itemScroll = new JScrollPane(list);
		sidebarPanel.add(itemScroll, BorderLayout.CENTER);
	}
	
	public void loadButtons() {
		buttonPanel.add(save);
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				for(ConfigPanel panel : configPanels) {
					panel.SaveSettings();
				}
			}	
		});
		
		buttonPanel.add(reset);
		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				for(ConfigPanel panel : configPanels) {
					panel.ResetSettings();
				}
			}	
		});
		buttonPanel.add(cancel);
		
		this.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	public void getConfigurableServices() {
		if(ServiceProvider.getInstance().getControlService() instanceof IConfigurable){
			configurableServices.add((IConfigurable) ServiceProvider.getInstance().getControlService());
		}
		
		if(ServiceProvider.getInstance().getDefineService() instanceof IConfigurable){
			configurableServices.add((IConfigurable) ServiceProvider.getInstance().getDefineService());
		}
		
		if(ServiceProvider.getInstance().getAnalyseService() instanceof IConfigurable){
			configurableServices.add((IConfigurable) ServiceProvider.getInstance().getAnalyseService());
		}
		
		if(ServiceProvider.getInstance().getValidateService() instanceof IConfigurable){
			configurableServices.add((IConfigurable) ServiceProvider.getInstance().getValidateService());
		}
		
		if(ServiceProvider.getInstance().getGraphicsService() instanceof IConfigurable){
			configurableServices.add((IConfigurable) ServiceProvider.getInstance().getGraphicsService());
		}
	}
	
	public void getConfigPanels() {
		for(IConfigurable config : configurableServices) {
			if(config.getConfigurationPanel() instanceof ConfigPanel)
				configPanels.add((ConfigPanel) config.getConfigurationPanel());
		}
	}
	
	private void setComponentText() {
		this.setTitle(localeService.getTranslatedString("ToolsOptions"));
		
		save.setText(localeService.getTranslatedString("SaveButton"));
		reset.setText(localeService.getTranslatedString("ResetButton"));
		cancel.setText(localeService.getTranslatedString("CancelButton"));
	}
}
