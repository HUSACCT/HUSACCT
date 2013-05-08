package husacct.control.presentation.util;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.control.IControlService;
import husacct.control.task.ExportController;
import husacct.control.task.MainController;
import husacct.validate.task.extensiontypes.ExtensionTypes.ExtensionType;

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
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	private ExportController exportController;
	
	public ExportViolationsReportDialog(MainController mainController) {
		super(mainController.getMainGui(), true);
		this.exportController = mainController.getExportController();
		setTitle(localeService.getTranslatedString("ExportReport"));
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
		exportButton = new JButton(localeService.getTranslatedString("ExportButton"));
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
		
		FileDialog fileDialog = new FileDialog(JFileChooser.FILES_ONLY, localeService.getTranslatedString("ExportButton"), filters);

		int returnVal = fileDialog.showDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			if(fileDialog.getSelectedFile().exists()){
				setFile(fileDialog.getSelectedFile());
			} 
			else {
				String fileExtension = "";
				
				if(fileDialog.getFileFilter() == null) {
					fileExtension = ExtensionType.PDF.getExtension();
				}
				else {
					fileExtension = fileDialog.getFileFilter().getDescription();
				}
				
				setFile(new File(fileDialog.getSelectedFile().getAbsolutePath() + "." + fileExtension));
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
			controlService.showErrorMessage(localeService.getTranslatedString("NoFileLocationError"));
			return false;
		}		
		else if(!Regex.matchRegex(Regex.filenameRegex, selectedFile.getName())) {
			controlService.showErrorMessage(localeService.getTranslatedString("InvalidFilenameError"));
			return false;
		}
		return true;
	}
}
