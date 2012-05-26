package husacct.control.presentation.util;

import husacct.ServiceProvider;
import husacct.control.IControlService;
import husacct.control.task.ExportController;
import husacct.control.task.MainController;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ExportViolationsReportDialog extends JDialog{
	private static final long serialVersionUID = 1L;

	private JLabel pathLabel;
	private JTextField pathText;
	private JButton browseButton, exportButton;

	private File selectedFile;

	private IControlService controlService = ServiceProvider.getInstance().getControlService();
	private ExportController exportController;
	
	public ExportViolationsReportDialog(MainController mainController) {
		super(mainController.getMainGui(), true);
		this.exportController = mainController.getExportController();
		setTitle(controlService.getTranslatedString("ExportArchitecture"));
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
		pathLabel = new JLabel(controlService.getTranslatedString("PathLabel"));
		pathText = new JTextField(20);
		browseButton = new JButton(controlService.getTranslatedString("BrowseButton"));
		exportButton = new JButton(controlService.getTranslatedString("ExportButton"));
		exportButton.setEnabled(false);
		pathText.setEnabled(false);
		
		getRootPane().setDefaultButton(exportButton);
		
		add(pathLabel);
		add(pathText);
		add(browseButton);
		add(exportButton);
	}

	private void setListeners(){
		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showFileDialog();
			}
		});
		exportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(validateData()) {
				exportController.exportViolationsReport(selectedFile);
				dispose();
				}
			}
		});
	}

	private void showFileDialog() {
		String[] fileExtensions = exportController.getExportExtensions();
		
		List<FileNameExtensionFilter> filters = new ArrayList<FileNameExtensionFilter>();
		for(String extension : fileExtensions){
			filters.add(new FileNameExtensionFilter(extension, extension));
		}
		
		FileDialog fileDialog = new FileDialog(JFileChooser.FILES_ONLY, controlService.getTranslatedString("ExportButton"), filters);

		int returnVal = fileDialog.showDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			if(fileDialog.getSelectedFile().exists()){
				setFile(fileDialog.getSelectedFile());
			} else {
				setFile(new File(fileDialog.getSelectedFile().getAbsolutePath() + "." + fileDialog.getFileFilter().getDescription()));
			}
		}
	}

	private void setFile(File file) {
		selectedFile = file;
		pathText.setText(file.getAbsolutePath());
		exportButton.setEnabled(true);
	}
	
	public boolean validateData() {
		if(selectedFile == null){
			controlService.showErrorMessage(controlService.getTranslatedString("NoFileLocationError"));
			return false;
		}		
		else if(!Regex.matchRegex(Regex.filenameRegex, selectedFile.getName())) {
			controlService.showErrorMessage(controlService.getTranslatedString("InvalidFilenameError"));
			return false;
		}
		return true;
	}
}
