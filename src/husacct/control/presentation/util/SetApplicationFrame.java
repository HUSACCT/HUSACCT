package husacct.control.presentation.util;

import husacct.ServiceProvider;
import husacct.common.dto.ApplicationDTO;
import husacct.control.task.MainController;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class SetApplicationFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JLabel pathLabel, applicationNameLabel, languageSelectLabel, versionLabel;
	private JList pathList;
	private JTextField applicationNameText, versionText;
	private JComboBox languageSelect;
	private JButton addButton, removeButton, saveButton;
	private String[] languages;
	private DefaultListModel pathListModel = new DefaultListModel();
	private MainController mainController;
	private ApplicationDTO applicationData;
	
	private GridBagConstraints constraint = new GridBagConstraints();
	
	public SetApplicationFrame(MainController mainController) {
		super();
		this.mainController = mainController;
		this.applicationData = ServiceProvider.getInstance().getDefineService().getApplicationDetails();
		this.languages = ServiceProvider.getInstance().getAnalyseService().getAvailableLanguages();
		setTitle("Application details");
		setup();
		addComponents();
		setDefaultValues();
		setListeners();
	}

	private void setup(){
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLayout(new GridBagLayout());
		this.setSize(new Dimension(350, 380));
		this.setResizable(true);
		this.setVisible(true);
	}

	private void addComponents(){
		applicationNameLabel = new JLabel("Application name");
		languageSelectLabel = new JLabel("Select language");
		versionLabel = new JLabel("Version");
		pathLabel = new JLabel("Path");
		
		applicationNameText = new JTextField(20);
		languageSelect = new JComboBox(languages);
		versionText = new JTextField(10);
		
		pathList = new JList(pathListModel);
		pathList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		pathList.setLayoutOrientation(JList.VERTICAL);
		pathList.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(pathList);
		listScroller.setAlignmentX(LEFT_ALIGNMENT);
		
		addButton = new JButton("Add");
		removeButton = new JButton("Remove");
		saveButton = new JButton("Save");
		
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
		add(saveButton, getConstraint(2, 7, 1, 1));
		
	}

	private void setDefaultValues(){
		applicationNameText.setText(applicationData.name);
		for(int i=0; i<languages.length; i++){
			if(applicationData.programmingLanguage.equals(languages[i])){
				languageSelect.setSelectedIndex(i);
			}
		}
		versionText.setText(applicationData.version);
		String[] items = applicationData.paths;
		for (int i=0; i<items.length; i++) {
			pathListModel.add(i, items[i]);
		}
		
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
		
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setApplicationData();
				dispose();
			}
		});
	}

	private void showAddFileDialog() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = chooser.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			pathListModel.add(pathListModel.size(), chooser.getSelectedFile().getAbsolutePath());
		}
	}

	private void setApplicationData(){
		applicationData.name = applicationNameText.getText();
		applicationData.programmingLanguage = languages[languageSelect.getSelectedIndex()];
		applicationData.version = versionText.getText();
		applicationData.paths = Arrays.copyOf(pathListModel.toArray(), pathListModel.toArray().length, String[].class);
		mainController.getApplicationController().setApplicationData(applicationData);
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
}