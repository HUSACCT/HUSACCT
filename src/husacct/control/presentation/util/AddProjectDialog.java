package husacct.control.presentation.util;

import husacct.ServiceProvider;
import husacct.common.dto.ProjectDTO;
import husacct.common.locale.ILocaleService;
import husacct.common.services.IServiceListener;
import husacct.control.task.configuration.ConfigurationManager;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class AddProjectDialog extends JDialog{

	private static final long serialVersionUID = 1L;
	private JLabel pathLabel, projectNameLabel, versionLabel;
	private JTextArea descriptionText;
	private JList<String> pathList;
	private JPanel panel;
	private JTextField projectNameText, versionText;
	private JButton addButton, cancelButton, removeButton, confirmButton;
	private DefaultListModel<String> pathListModel = new DefaultListModel<String>();
	private GridBagConstraints constraint = new GridBagConstraints();
	private boolean CancelFlag = true;
	
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	
	public AddProjectDialog(JDialog dialogOwner){
		super(dialogOwner, true);
		setup();
		addComponents();
		setListeners();
		setVisible(true);
	}
	
	public AddProjectDialog(JDialog dialogOwner, ProjectDTO defaultValues) {
		super(dialogOwner, true);
		setup();
		addComponents();
		setListeners();
		setDefaultValues(defaultValues);
		setVisible(true);
	}
	
	private void setup(){
		setTitle(localeService.getTranslatedString("ManageProjectTitle"));
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLayout(new GridBagLayout());
		this.setSize(new Dimension(350, 420));
		this.setResizable(false);
		DialogUtils.alignCenter(this);
	}
	
	public void addComponents(){
		this.setLayout(new GridBagLayout());
		
		versionLabel = new JLabel(localeService.getTranslatedString("VersionLabel"));
		pathLabel = new JLabel(localeService.getTranslatedString("PathLabel"));
		projectNameLabel = new JLabel(localeService.getTranslatedString("ProjectNameLabel"));
		addButton = new JButton(localeService.getTranslatedString("AddButton"));
		removeButton = new JButton(localeService.getTranslatedString("RemoveButton"));
		confirmButton = new JButton(localeService.getTranslatedString("SaveButton"));
		cancelButton = new JButton(localeService.getTranslatedString("CancelButton"));
		
		projectNameText = new JTextField("myProject", 20);
		versionText = new JTextField(10);
		descriptionText = new JTextArea(localeService.getTranslatedString("ProjectTextSpace"), 5, 5);
		
		pathList = new JList<String>(pathListModel);
		pathList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		pathList.setLayoutOrientation(JList.VERTICAL);
		pathList.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(pathList);
		listScroller.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		removeButton.setEnabled(false);
		
		add(projectNameLabel, getConstraint(0, 0, 1, 1));
		add(projectNameText, getConstraint(1, 0, 2, 1));
		add(versionLabel, getConstraint(0, 1, 1, 1));
		add(versionText, getConstraint(1, 1, 2, 1));
		add(descriptionText, getConstraint(0, 2, 3, 1, 100, 50));
		add(pathLabel, getConstraint(0, 3, 1, 1));
		add(listScroller, getConstraint(0, 4, 2, 3, 200, 150));
		
		add(addButton, getConstraint(2, 4, 1, 1));
		add(removeButton, getConstraint(2, 5, 1, 1));
		add(confirmButton, getConstraint(0, 10, 1, 1));
		add(cancelButton, getConstraint(1, 10, 1, 1));
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
				versionLabel = new JLabel(localeService.getTranslatedString("VersionLabel"));
				pathLabel = new JLabel(localeService.getTranslatedString("PathLabel"));
				projectNameLabel = new JLabel(localeService.getTranslatedString("ProjectNameLabel"));
				addButton = new JButton(localeService.getTranslatedString("AddButton"));
				removeButton = new JButton(localeService.getTranslatedString("RemoveButton"));
				confirmButton = new JButton(localeService.getTranslatedString("OkButton"));
			}
		});
		
		confirmButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CancelFlag = false;
				dispose();
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CancelFlag = true;
				dispose();
			}
		});
	}
	
	private void showAddFileDialog() {
		FileDialog fileChooser = new FileDialog(JFileChooser.DIRECTORIES_ONLY, localeService.getTranslatedString("AddButton"));
		fileChooser.setCurrentDirectory(new File(ConfigurationManager.getProperty("LastUsedAddProjectPath")));
		int returnVal = fileChooser.showDialog(panel);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			String addedProjectPath = fileChooser.getSelectedFile().getAbsolutePath();
			ConfigurationManager.setProperty("LastUsedAddProjectPath", addedProjectPath);
			pathListModel.add(pathListModel.size(), addedProjectPath);
		}
	}
	
	private void setDefaultValues(ProjectDTO defaultValues){
		projectNameText.setText(defaultValues.name);
		versionText.setText(defaultValues.version);
		descriptionText.setText(defaultValues.description);
		for(String path : defaultValues.paths) {
			pathListModel.add(pathListModel.size(), path);
		}
	}
	
	public ProjectDTO getProjectData(){
		if(CancelFlag) {
			return null;
		}
		
		String name = projectNameText.getText();
		String version = versionText.getText();
		String description = descriptionText.getText();
		ArrayList<String> paths = new ArrayList<String>(Arrays.asList(Arrays.copyOf(pathListModel.toArray(), pathListModel.toArray().length, String[].class)));
		
		return new ProjectDTO(name, paths, "", version, description, null);
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
		/*
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
		}*/
		return true;
	}
}
