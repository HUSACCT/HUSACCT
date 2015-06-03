package husacct.control.presentation.util;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.control.IControlService;
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


public class ExportDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private MainController mainController;
	private String typeOfExport;
	
	private JLabel pathLabel;
	private JTextField pathText;
	private JButton browseButton, exportButton;

	private File selectedFile;

	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	private IControlService controlService = ServiceProvider.getInstance().getControlService();
	
	public ExportDialog(MainController mainController, String typeOfExport) {
		super(mainController.getMainGui(), true);
		this.mainController = mainController;
		this.typeOfExport = typeOfExport;
		if (typeOfExport.equals("ExportArchitecture")) {
			setTitle(localeService.getTranslatedString("ExportArchitecture"));
		} else if (typeOfExport.equals("ExportAnalysisModel")) {
			setTitle(localeService.getTranslatedString("ExportAnalysisModel"));
		}
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
		pathLabel = new JLabel(localeService.getTranslatedString("PathLabelShort"));
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
					if (typeOfExport.equals("ExportArchitecture")) {
						mainController.getExportController().exportArchitecture(selectedFile);
					} else if (typeOfExport.equals("ExportAnalysisModel")) {
						mainController.getExportController().exportAnalysisModel(selectedFile);
					}
				dispose();
				}
			}
		});
	}

	private void showFileDialog() {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("XML", "xml", "xml");
		FileDialog fileDialog = new FileDialog(JFileChooser.FILES_ONLY, localeService.getTranslatedString("ExportButton"), filter);
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
