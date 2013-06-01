package husacct.control.presentation.util;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.control.task.MainController;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;


public class ImportArchitectureDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private MainController mainController;
	
	private JLabel pathLabel;
	private JTextField pathText;
	private JButton browseButton, importButton;

	private File selectedFile;
	
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	
	public ImportArchitectureDialog(MainController mainController) {
		super(mainController.getMainGui(), true);
		this.mainController = mainController;
		setTitle(localeService.getTranslatedString("ImportArchitecture"));
		setup();
		addComponents();
		setListeners();
		this.setVisible(true);
	}
	
	private void setup(){
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLayout(new FlowLayout());
		this.setSize(new Dimension(350, 100));
		this.setResizable(false);
		DialogUtils.alignCenter(this);
	}

	private void addComponents(){
		pathLabel = new JLabel(localeService.getTranslatedString("PathLabel"));
		pathText = new JTextField(20);
		browseButton = new JButton(localeService.getTranslatedString("BrowseButton"));
		importButton = new JButton(localeService.getTranslatedString("ImportButton"));
		importButton.setEnabled(false);
		pathText.setEnabled(false);

		getRootPane().setDefaultButton(importButton);
		
		add(pathLabel);
		add(pathText);
		add(browseButton);
		add(importButton);
	}

	private void setListeners(){
		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showFileDialog();				
			}
		});
		importButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mainController.getImportController().importArchitecture(selectedFile);
				dispose();
			}
		});
	}

	private void showFileDialog() {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("XML", "xml", "xml");
		FileDialog fileDialog = new FileDialog(JFileChooser.FILES_ONLY, localeService.getTranslatedString("ImportButton"), filter);
		int returnVal = fileDialog.showDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			setFile(fileDialog.getSelectedFile());
		}
	}

	private void setFile(File file) {
		selectedFile = file;
		pathText.setText(file.getAbsolutePath());
		importButton.setEnabled(true);
	}
}
