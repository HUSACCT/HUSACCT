package husacct.control.presentation.util;

import husacct.ServiceProvider;
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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
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
	private JLabel applicationNameLabel, languageSelectLabel, versionLabel, projectsLabel;
	private JTextField applicationNameText, versionText;
	private JComboBox<Object> languageSelect;
	private JButton addButton, removeButton, editButton;
	private String[] languages;
	private JList<ProjectDTO> projectList;
	private DefaultListModel<ProjectDTO> projectListModel = new DefaultListModel<ProjectDTO>();
	private boolean UpdateFlag;
	
	private JDialog dialogOwner;
	private GridBagConstraints constraint = new GridBagConstraints();
	
	private IControlService controlService = ServiceProvider.getInstance().getControlService();
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	
	public SetApplicationPanel(JDialog dialogOwner){
		this.dialogOwner = dialogOwner;
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
		projectsLabel = new JLabel(localeService.getTranslatedString("ProjectsLabel"));
		addButton = new JButton(localeService.getTranslatedString("AddButton"));
		removeButton = new JButton(localeService.getTranslatedString("RemoveButton"));
		editButton = new JButton(localeService.getTranslatedString("Edit"));
		
		applicationNameText = new JTextField("myApplication", 20);
		languageSelect = new JComboBox<Object>(languages);
		versionText = new JTextField(10);
		
		projectList = new JList<ProjectDTO>(projectListModel);
		projectList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		projectList.setLayoutOrientation(JList.VERTICAL);
		projectList.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(projectList);
		listScroller.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		removeButton.setEnabled(false);
		editButton.setEnabled(false);
		
		add(applicationNameLabel, getConstraint(0, 0, 1, 1));
		add(applicationNameText, getConstraint(1, 0, 2, 1));
		add(languageSelectLabel, getConstraint(0, 1, 1, 1));
		add(languageSelect, getConstraint(1, 1, 2, 1));
		add(versionLabel, getConstraint(0, 2, 1, 1));
		add(versionText, getConstraint(1, 2, 2, 1));
		add(projectsLabel, getConstraint(0, 3, 1, 1));
		add(listScroller, getConstraint(0, 4, 2, 3, 200, 150));
		
		add(addButton, getConstraint(2, 4, 1, 1));
		add(editButton, getConstraint(2, 5, 1, 1));
		add(removeButton, getConstraintForButtons(2, 6, 1, 1));
	}
	
	private void setListeners(){
		projectList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if(projectList.getSelectedIndex() >= 0){
					removeButton.setEnabled(true);
					editButton.setEnabled(true);
				} else {
					removeButton.setEnabled(false);
					editButton.setEnabled(false);
				}
			}
		});
		
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addProjectDialog();
			}
		});
		
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editProjectDialog((ProjectDTO)projectListModel.getElementAt(projectList.getSelectedIndex()));
			}
		});
		
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				projectListModel.remove(projectList.getSelectedIndex());
			}
		});
		
		localeService.addServiceListener(new IServiceListener() {
			public void update() {
				applicationNameLabel.setText(localeService.getTranslatedString("ApplicationNameLabel"));
				languageSelectLabel.setText(localeService.getTranslatedString("LanguageSelectLabel"));
				versionLabel.setText(localeService.getTranslatedString("VersionLabel"));
				projectsLabel.setText(localeService.getTranslatedString("ProjectsLabel"));
				addButton.setText(localeService.getTranslatedString("AddButton"));
				removeButton.setText(localeService.getTranslatedString("RemoveButton"));
			}
		});
	}
	
	private void addProjectDialog() {
		UpdateFlag = false;
		AddProjectDialog project = new AddProjectDialog(dialogOwner);
		addProjectDialogListener(project);
	}
	
	private void editProjectDialog(ProjectDTO projectToEdit) {
		UpdateFlag = true;
		AddProjectDialog project = new AddProjectDialog(dialogOwner, projectToEdit);
		addProjectDialogListener(project);
	}
	
	private void addProjectDialogListener(final AddProjectDialog projectDialog) {
		projectDialog.addWindowListener(new WindowListener() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				if(projectDialog.getProjectData() != null) {
					if(UpdateFlag) {
						projectListModel.set(projectList.getSelectedIndex(), projectDialog.getProjectData());
					} else {
						projectListModel.add(projectListModel.size(), projectDialog.getProjectData());
					}
				}
			}
			@Override
			public void windowActivated(WindowEvent e) {}
			@Override
			public void windowClosing(WindowEvent e) {}
			@Override
			public void windowDeactivated(WindowEvent e) {}
			@Override
			public void windowDeiconified(WindowEvent e) {}
			@Override
			public void windowIconified(WindowEvent e) {}
			@Override
			public void windowOpened(WindowEvent e) {}
		});
	}
	
	private void setDefaultValues(){
		ApplicationDTO applicationData = ServiceProvider.getInstance().getDefineService().getApplicationDetails();
		applicationNameText.setText(applicationData.name);
		for(int i=0; i<languages.length; i++){
			if(applicationData.projects.size() > 0 && applicationData.projects.get(0).programmingLanguage.equals(languages[i])){
				languageSelect.setSelectedIndex(i);
			}
		}		
		for(ProjectDTO project : applicationData.projects) {
			projectListModel.add(projectListModel.size(), project);
		}
		versionText.setText(applicationData.version);
	}
	
	public ApplicationDTO getApplicationData(){
		String name = applicationNameText.getText();
		String language = languages[languageSelect.getSelectedIndex()];
		String version = versionText.getText();
		
		ArrayList<ProjectDTO> projects = new ArrayList<ProjectDTO>();		
		for(int i=0; i < projectListModel.size(); i++) {
			ProjectDTO project = (ProjectDTO) projectListModel.getElementAt(i);
			project.programmingLanguage = language;
			projects.add(project);
		}
		
		ApplicationDTO applicationData = new ApplicationDTO(name, projects, version);
		return applicationData;
	}
	
	private GridBagConstraints getConstraint(int gridx, int gridy, int gridwidth, int gridheight, int ipadx, int ipady){
		constraint.fill = GridBagConstraints.BOTH;
		constraint.anchor = GridBagConstraints.CENTER;
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
	
	private GridBagConstraints getConstraintForButtons(int gridx, int gridy, int gridwidth, int gridheight){
		constraint = getConstraint(gridx,gridy,gridwidth,gridheight,0,0);
		constraint.fill = GridBagConstraints.NONE;
		constraint.anchor = GridBagConstraints.NORTH;
		return constraint;
	}
	
	public boolean dataValidated() {
		String applicationName = applicationNameText.getText();
		
		boolean showError = false;
		String errorMessage = "";
		
		
		if(applicationName == null || applicationName.trim().length() < 1){
			errorMessage = localeService.getTranslatedString("ApplicationNameEmptyError");
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
