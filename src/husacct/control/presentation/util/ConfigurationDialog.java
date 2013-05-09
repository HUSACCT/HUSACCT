package husacct.control.presentation.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.common.services.IConfigurable;
import husacct.control.ControlServiceImpl;
import husacct.control.task.MainController;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class ConfigurationDialog extends JDialog {
	
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	private ArrayList<IConfigurable> configurableServices = new ArrayList<IConfigurable>();
	
	private JPanel sidebarPanel = new JPanel();
	private JList<String> list = new JList<String>();
	
	private JPanel mainPanel = new JPanel();
	
	
	public ConfigurationDialog(MainController mainController) {
		super(mainController.getMainGui(), true);
		setTitle(localeService.getTranslatedString("Configuration"));
		initiliaze();
		this.setVisible(true);
	}
	
	public void initiliaze() {
		getConfigurableServices();
		mainPanel = configurableServices.get(0).getConfigurationPanel();
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(new Dimension(600, 400));
		this.add(sidebarPanel);
		this.add(mainPanel);
		
		DialogUtils.alignCenter(this);		
	}
	
	public void loadSidePanel() {
		for(final IConfigurable config : configurableServices) {
			JLabel label = new JLabel(config.getConfigurationName());
			list.add(label);
			label.addMouseListener(new itemSelectionListener(config.getConfigurationPanel()));
			sidebarPanel.add(label);
		}
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
	
	private class itemSelectionListener implements MouseListener {

		private JPanel configurationPanel;
		
		private itemSelectionListener(JPanel configurationPanel) {
			this.configurationPanel = configurationPanel;
		}
		
		@Override
		public void mouseClicked(MouseEvent arg0) {
			mainPanel = configurationPanel;
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {}

		@Override
		public void mouseExited(MouseEvent arg0) {}

		@Override
		public void mousePressed(MouseEvent arg0) {}

		@Override
		public void mouseReleased(MouseEvent arg0) {}
	}
}
