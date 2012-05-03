package husacct.control.presentation.menubar.popup;

import husacct.control.task.MainController;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class AnalyseApplicationFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JLabel pathLabel, applicationNameLabel, languageSelectLabel, versionLabel;
	private JTextField pathText, applicationNameText, versionText;
	private JComboBox languageSelect;
	private JButton browseButton, analyseButton;
	private String[] languages = { "Java", "C#" };
	private String[] selectedPaths;
	private MainController mainController;

	
	public AnalyseApplicationFrame() {
		super();
		setTitle("Analyse application");
		setup();
		addComponents();
		setListeners();
	}

	private void setup(){
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLayout(new GridLayout(6,3));
		this.setSize(new Dimension(300, 100));
	}

	private void addComponents(){
		applicationNameLabel = new JLabel("Application name");
		applicationNameText = new JTextField(20);
		languageSelectLabel = new JLabel("Select language");
		languageSelect = new JComboBox(languages);
		versionLabel = new JLabel("Version");
		versionText = new JTextField(10);
		pathLabel = new JLabel("Path");
		pathText = new JTextField(20);
		browseButton = new JButton("Browse");
		analyseButton = new JButton("Analyse application");
		analyseButton.setEnabled(false);
		pathText.setEnabled(false);

		add(applicationNameLabel);
		add(applicationNameText);
		add(languageSelectLabel);
		add(languageSelect);
		add(pathLabel);
		add(pathText);
		add(browseButton);
		add(versionLabel);
		add(versionText);
		add(analyseButton);
	}

	private void setListeners(){
		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showFileDialog();				
			}
		});
		analyseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				analyseApplication();
			}
		});
	}

	protected void showFileDialog() {
		JFileChooser chooser = new JFileChooser();
		int returnVal = chooser.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			setFile(chooser.getSelectedFile());	            
		}
	}

	private void setFile(File file) {
		pathText.setText(file.getAbsolutePath());
		analyseButton.setEnabled(true);
	}
	
	private void analyseApplication(){
		String name = applicationNameText.getText();
		String selectedLanguage = languageSelect.getSelectedItem().toString();
		String version = versionText.getText();
		mainController.getApplicationController().analyseApplication(name, selectedPaths, selectedLanguage, version);
	}
	
}
