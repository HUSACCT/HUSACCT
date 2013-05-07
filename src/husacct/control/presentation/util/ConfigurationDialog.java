package husacct.control.presentation.util;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.common.services.IConfigurable;
import husacct.control.task.MainController;

import javax.swing.JDialog;

@SuppressWarnings("serial")
public class ConfigurationDialog extends JDialog {

	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	private ArrayList<IConfigurable> configurableServices = new ArrayList<IConfigurable>();
	
	
	public ConfigurationDialog(MainController mainController) {
		super(mainController.getMainGui(), true);
		setTitle(localeService.getTranslatedString("Configuration"));
		initiliaze();
		this.setVisible(true);
	}
	
	public void initiliaze() {
		getConfigurableServices();
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(new Dimension(420, 380));
		this.setLayout(new FlowLayout());
		this.setResizable(false);
		DialogUtils.alignCenter(this);
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
}
