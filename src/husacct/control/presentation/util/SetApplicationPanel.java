package husacct.control.presentation.util;

import husacct.ServiceProvider;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ProjectDTO;
import husacct.common.locale.ILocaleService;
import husacct.common.services.IServiceListener;
import husacct.control.IControlService;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class SetApplicationPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private JLabel pathLabel, applicationNameLabel, languageSelectLabel, versionLabel;
	private JList pathList;
	private JTextField applicationNameText, versionText;
	private JComboBox languageSelect;
	private JButton addButton, removeButton;
	private String[] languages;
	private DefaultListModel pathListModel = new DefaultListModel();
	
	private JPanel panel;
	private GridBagConstraints constraint = new GridBagConstraints();
	
	private IControlService controlService = ServiceProvider.getInstance().getControlService();
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	
	public SetApplicationPanel(){
		addComponents();
		setListeners();
		setDefaultValues();
	}
	
	public void addComponents(){
		this.setLayout(new GridBagLayout());
		this.languages = ServiceProvider.getInstance().getAnalyseService().getAvailableLanguages();
		
		applicationNameLabel = new JLabel(localeService.getTranslatedString("ApplicationNameLabel"));
		languageSelectLabel = new JLabel(localeService.getTranslatedString("LanguageSelectLabel"));
		versionLabel = new JLabel(localeService.getTranslatedString("VersionLabel"));
		pathLabel = new JLabel(localeService.getTranslatedString("PathLabel"));
		addButton = new JButton(localeService.getTranslatedString("AddButton"));
		removeButton = new JButton(localeService.getTranslatedString("RemoveButton"));
		
		applicationNameText = new JTextField("myApplication", 20);
		languageSelect = new JComboBox(languages);
		versionText = new JTextField(10);
		
		pathList = new JList(pathListModel);
		pathList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		pathList.setLayoutOrientation(JList.VERTICAL);
		pathList.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(pathList);
		listScroller.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		removeButton.setEnabled(false);
		
		add(applicationNameLabel, getConstraint(0, 0, 1, 1));
		add(applicationNameText, getConstraint(1, 0, 2, 1));
		add(languageSelectLabel, getConstraint(0, 1, 1, 1));
		add(languageSelect, getConstraint(1, 1, 2, 1));
		add(versionLabel, getConstraint(0, 2, 1, 1));
		add(versionText, getConstraint(1, 2, 2, 1));
		add(pathLabel, getConstraint(0, 3, 1, 1));
		add(listScroller, getConstraint(0, 4, 2, 3, 200, 150));
		
		add(addButton, getConstraint(2, 4, 1, 1));
		add(removeButton, getConstraint(2, 5, 1, 1));
	}
	
	private void setListeners(){
		pathList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if(pathList.getSelectedIndex() >= 0){
					removeButton.setEnabled(true);
				} else {
					removeButton.setEnabled(false);
				}
			}
		});
		
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showAddFileDialog();
			}
		});
		
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pathListModel.remove(pathList.getSelectedIndex());
			}
		});
		
		localeService.addServiceListener(new IServiceListener() {
			public void update() {
				applicationNameLabel.setText(localeService.getTranslatedString("ApplicationNameLabel"));
				languageSelectLabel.setText(localeService.getTranslatedString("LanguageSelectLabel"));
				versionLabel.setText(localeService.getTranslatedString("VersionLabel"));
				pathLabel.setText(localeService.getTranslatedString("PathLabel"));
				addButton.setText(localeService.getTranslatedString("AddButton"));
				removeButton.setText(localeService.getTranslatedString("RemoveButton"));
			}
		});
	}
	
	private void showAddFileDialog() {
		FileDialog fileChooser = new FileDialog(JFileChooser.DIRECTORIES_ONLY, localeService.getTranslatedString("AddButton"));
		int returnVal = fileChooser.showDialog(panel);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			pathListModel.add(pathListModel.size(), fileChooser.getSelectedFile().getAbsolutePath());
		}
	}
	
	private void setDefaultValues(){
		ApplicationDTO applicationData = ServiceProvider.getInstance().getDefineService().getApplicationDetails();
		applicationNameText.setText(applicationData.name);
		for(int i=0; i<languages.length; i++){
			if(applicationData.projects.size() > 0 && applicationData.projects.get(0).programmingLanguage.equals(languages[i])){
				languageSelect.setSelectedIndex(i);
			}
		}
		versionText.setText(applicationData.version);
		
		ArrayList<String> items = new ArrayList<String>();
		if(applicationData.projects.size() > 0){
			items = applicationData.projects.get(0).paths;
		}
		for (int i=0; i<items.size(); i++) {
			pathListModel.add(i, items.get(i));
		}
	}
	
	public ApplicationDTO getApplicationData(){
		String name = applicationNameText.getText();
		String language = languages[languageSelect.getSelectedIndex()];
		String version = versionText.getText();
		ArrayList<String> paths = new ArrayList<String>(Arrays.asList(Arrays.copyOf(pathListModel.toArray(), pathListModel.toArray().length, String[].class)));
		
		ArrayList<ProjectDTO> projects = new ArrayList<ProjectDTO>();
		ArrayList<AnalysedModuleDTO> analysedModules = new ArrayList<AnalysedModuleDTO>();
		ProjectDTO project = new ProjectDTO(name, paths, language, version, "", analysedModules);
		projects.add(project);
		
		ApplicationDTO applicationData = new ApplicationDTO(name, projects, version);
		return applicationData;
	}
	
	private GridBagConstraints getConstraint(int gridx, int gridy, int gridwidth, int gridheight, int ipadx, int ipady){
		constraint.fill = GridBagConstraints.BOTH;
		constraint.insets = new Insets(3, 3, 3, 3);
		constraint.ipadx = ipadx;
		constraint.ipady = ipady;
		constraint.gridx = gridx;
		constraint.gridy = gridy;
		constraint.gridwidth = gridwidth;
		constraint.gridheight = gridheight;
		return constraint;		
	}
	
	private GridBagConstraints getConstraint(int gridx, int gridy, int gridwidth, int gridheight){
		return getConstraint(gridx, gridy, gridwidth, gridheight, 0, 0);
	}
	
	public boolean dataValidated() {
		String applicationName = applicationNameText.getText();
		
		boolean showError = false;
		String errorMessage = "";
		
		
		if(applicationName == null || applicationName.length() < 1){
			errorMessage = localeService.getTranslatedString("FieldEmptyError");
			showError = true;
		}
		if (!Regex.matchRegex(Regex.nameWithSpacesRegex, applicationNameText.getText())) {
			errorMessage = localeService.getTranslatedString("MustBeAlphaNumericError");
			showError = true;
		}
		
		if(showError){
			controlService.showErrorMessage(errorMessage);
			return false;
		}
		return true;
	}
	
}
