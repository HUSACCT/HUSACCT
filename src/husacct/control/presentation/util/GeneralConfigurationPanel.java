package husacct.control.presentation.util;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.common.services.IServiceListener;
import husacct.control.IControlService;
import husacct.control.task.configuration.NonExistingSettingException;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
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
public class GeneralConfigurationPanel extends JPanel {
	
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	private IControlService controlService = ServiceProvider.getInstance().getControlService();
	
	private Logger logger = Logger.getLogger(GeneralConfigurationPanel.class);
	
	ButtonGroup languageGroup = new ButtonGroup();
	
	GridBagConstraints constraints = new GridBagConstraints();
	
	public GeneralConfigurationPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		initialiseLanguage();
		initialiseCodeviewer();
	}
	
	private void initialiseLanguage() {
		
		JPanel languagePanel = new JPanel();
		languagePanel.setBorder(BorderFactory.createTitledBorder(localeService.getTranslatedString("ConfigGeneralLanguage")));
		
		for(final Locale locale : localeService.getAvailableLocales()){
			String language = locale.getDisplayLanguage();
			final JRadioButton languageItem = new JRadioButton(language);
			
			
			if(language.equals(localeService.getLocale().getDisplayLanguage())){
				languageItem.setSelected(true);
			}
			
			languageItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setLocaleFromString(locale.getLanguage());
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
		final JTextField location = new JTextField();
		final JFileChooser fileChooser = new JFileChooser();
		final JCheckBox enable = new JCheckBox(localeService.getTranslatedString("ConfigGeneralCodeviewerEnable"));
		final JButton selectFile = new JButton("A Button"); //TODO Language String
		
		JPanel codeviewerPanel = new JPanel(new GridBagLayout());
		
		
		codeviewerPanel.setBorder(BorderFactory.createTitledBorder(localeService.getTranslatedString("ConfigGeneralCodeviewer")));
		
		
		boolean external = false;
		try {
			external = controlService.getPropertyAsBoolean("ExternalCodeviewer");
		} catch (NonExistingSettingException e) { }
		
		enable.setSelected(external);
		location.setEnabled(external);
		selectFile.setEnabled(external);
		
		enable.addItemListener(new ItemListener() {
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
		
		selectFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int result = fileChooser.showOpenDialog(GeneralConfigurationPanel.this);
				if(result == JFileChooser.APPROVE_OPTION) {
					location.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		
		
		
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 0;
		codeviewerPanel.add(enable, constraints);
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
	
	private void setLocaleFromString(String locale){
		logger.debug("User sets language to: " + locale);
		localeService.setLocale(new Locale(locale, locale));
	}
	
	public JPanel getGUI() {
		return this;
	}
}
