package husacct.control.presentation.util;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.common.services.IServiceListener;
import husacct.control.task.MainController;
import husacct.control.task.configuration.ConfigPanel;
import husacct.control.task.configuration.ConfigurationManager;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class GeneralConfigurationPanel extends ConfigPanel {
	
	final JTextField location = new JTextField();
	final JFileChooser fileChooser = new JFileChooser();
	
	private JCheckBox enableExternalCodeviewer, enableActionLogger;
	private JButton selectFile = new JButton();
	private JPanel languagePanel, codeviewerPanel, actionLoggerPanel;
	
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	
	private Logger logger = Logger.getLogger(GeneralConfigurationPanel.class);
	
	private ButtonGroup languageGroup = new ButtonGroup();
	private GridBagConstraints constraints = new GridBagConstraints();
	
	private String language;
	
	public GeneralConfigurationPanel(MainController mainController) {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		initialiseLanguage();
		initialiseCodeviewer();
		initialiseActionLogger();
		
		setComponentText();
		setListeners();
		
		loadDefaults();
	}

	private void initialiseLanguage() {
		language = localeService.getLocale().getLanguage();
		languagePanel = new JPanel();

		for(final Locale locale : localeService.getAvailableLocales()){
			String language = locale.getDisplayLanguage();
			final JRadioButton languageItem = new JRadioButton(language);
			languageItem.setName(locale.getLanguage());
			if(language.equals(localeService.getLocale().getDisplayLanguage())){
				languageItem.setSelected(true);
			}
			
			languageItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setLanguage(locale.getLanguage());					
				}
			});
			
			localeService.addServiceListener(new IServiceListener() {
				@Override
				public void update() {
					if(localeService.getLocale().getDisplayLanguage().equals(languageItem.getText())){
						languageItem.setSelected(true);
					} else {
						languageItem.setSelected(false);
					}
				}
			});
			languageGroup.add(languageItem);
			languagePanel.add(languageItem);
		}
		this.add(languagePanel);
	}
	
	private void initialiseCodeviewer() {
		codeviewerPanel = new JPanel(new GridBagLayout());
		enableExternalCodeviewer = new JCheckBox();
		enableExternalCodeviewer.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if(event.getStateChange() == ItemEvent.SELECTED) {
					location.setEnabled(true);
					selectFile.setEnabled(true);
				} else {
					location.setEnabled(false);
					selectFile.setEnabled(false);
				}
			}
		});
		
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 0;
		codeviewerPanel.add(enableExternalCodeviewer, constraints);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 1;
		location.setPreferredSize(new Dimension(260, 20));
		codeviewerPanel.add(location, constraints);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 1;
		constraints.gridy = 1;
		codeviewerPanel.add(selectFile, constraints);
		
		this.add(codeviewerPanel);
	}
	
	private void initialiseActionLogger() {
		actionLoggerPanel = new JPanel();
		enableActionLogger = new JCheckBox();
		actionLoggerPanel.add(enableActionLogger);
		this.add(actionLoggerPanel);
	}
	
	private void setListeners() {
		localeService.addServiceListener(new IServiceListener() {
			public void update() {
				setComponentText();
			}
		});
		
		selectFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int result = fileChooser.showOpenDialog(GeneralConfigurationPanel.this);
				if(result == JFileChooser.APPROVE_OPTION) {
					location.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
	}
	
	private void setLocaleFromString(String locale){
		logger.info("User sets language to: " + locale);
		localeService.setLocale(new Locale(locale, locale));
	}
	
	public JPanel getGUI() {
		return this;
	}
	
	protected void setComponentText() {
		languagePanel.setBorder(BorderFactory.createTitledBorder(localeService.getTranslatedString("ConfigGeneralLanguage")));
		codeviewerPanel.setBorder(BorderFactory.createTitledBorder(localeService.getTranslatedString("ConfigGeneralCodeviewer")));
		actionLoggerPanel.setBorder(BorderFactory.createTitledBorder(localeService.getTranslatedString("ConfigGeneralActionLogger")));
		
		enableExternalCodeviewer.setText(localeService.getTranslatedString("ConfigGeneralCodeviewerEnable"));
		enableActionLogger.setText(localeService.getTranslatedString("ConfigGeneralActionLoggerEnable"));
		selectFile.setText(localeService.getTranslatedString("ConfigGeneralCodeviewerBrowse"));
	}

	public void loadDefaults() {
		String ExternalCodeviewer = ConfigurationManager.getProperty("ExternalCodeviewer");
		String ActionLogger = ConfigurationManager.getProperty("ActionLogger");
		boolean external = false;
		boolean logger = false;
		external = Boolean.valueOf(ExternalCodeviewer);
		logger = Boolean.valueOf(ActionLogger);
		enableExternalCodeviewer.setSelected(external);
		enableActionLogger.setSelected(logger);
		location.setEnabled(external);
		selectFile.setEnabled(external);

		location.setText(ConfigurationManager.getProperty("IDELocation"));
	}
	
	private void setLanguage(String language) {
		this.language = language;
	}
	
	@Override
	public void SaveSettings() {
		ConfigurationManager.setProperty("ExternalCodeviewer", String.valueOf(enableExternalCodeviewer.isSelected()));
		ConfigurationManager.setProperty("IDELocation", location.getText());
		ConfigurationManager.setProperty("Language", language);
		
		boolean showActionLogger = enableActionLogger.isSelected();
		ConfigurationManager.setProperty("ActionLogger", String.valueOf(showActionLogger));
		
		setLocaleFromString(language);
	}

	@Override
	public void ResetSettings() {
		loadDefaults();
	}
}