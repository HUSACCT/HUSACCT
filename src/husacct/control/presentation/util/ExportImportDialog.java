package husacct.control.presentation.util;

import husacct.ServiceProvider;
import husacct.analyse.presentation.reconstruct.MojoJPanel;
import husacct.common.locale.ILocaleService;
import husacct.control.IControlService;
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


public class ExportImportDialog extends JDialog {

	private MojoJPanel mojoPanel;
	private static final long serialVersionUID = 1L;

	private MainController mainController;
	private String typeOfFunction;
	
	private JLabel pathLabel;
	private JTextField pathText;
	private JButton browseButton, exportImportButton;
	private JButton mojoOrigin;

	private File selectedFile;

	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	private IControlService controlService = ServiceProvider.getInstance().getControlService();
	
	public void SARExportImportDialog(MainController mainController, String typeOfFunction, MojoJPanel mojoPanel, JButton origin) {
		//super(mainController.getMainGui(), true);
		this.mojoOrigin = origin;
		this.mojoPanel = mojoPanel;
		this.mainController = mainController;
		this.typeOfFunction = typeOfFunction;
		showFileDialog();
		
	}
	
	public ExportImportDialog(MainController mainController, String typeOfFunction) {
		super(mainController.getMainGui(), true);
		if(!typeOfFunction.equals("skipConstructor")){
			this.mainController = mainController;
			this.typeOfFunction = typeOfFunction;
			if (typeOfFunction.equals("ExportArchitecture")) {
				setTitle(localeService.getTranslatedString("ExportArchitecture"));
			} else if (typeOfFunction.equals("ExportAnalysisModel")) {
				setTitle(localeService.getTranslatedString("ExportAnalysisModel"));
			}
			
			setup();
			addComponents();
			setListeners();
			this.setVisible(true);
		}
		
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
		if (typeOfFunction.startsWith("Export") || typeOfFunction.startsWith("Report")) {
			exportImportButton = new JButton(localeService.getTranslatedString("ExportButton"));
		} else if (typeOfFunction.startsWith("Import")) {
			exportImportButton = new JButton(localeService.getTranslatedString("ImportButton"));
		}

		exportImportButton.setEnabled(false);
		pathText.setEnabled(false);
		
		getRootPane().setDefaultButton(exportImportButton);
		
		add(pathLabel);
		add(pathText);
		add(browseButton);
		add(exportImportButton);
	}

	private void setListeners(){
		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showFileDialog();				
			}
		});
		exportImportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(validateData()) {
					if (typeOfFunction.equals("ExportArchitecture")) {
						mainController.getExportImportController().exportArchitecture(selectedFile);
					} else if (typeOfFunction.equals("ImportArchitecture")) {
						mainController.getExportImportController().importArchitecture(selectedFile);
					} else if (typeOfFunction.equals("ReportArchitecture")) {
						mainController.getExportImportController().reportArchitecture(selectedFile);
					} else if (typeOfFunction.equals("ExportAnalysisModel")) {
						mainController.getExportImportController().exportAnalysisModel(selectedFile);
					} else if (typeOfFunction.equals("ImportAnalysisModel")) {
						mainController.getExportImportController().importAnalysisModel(selectedFile);
					} else if (typeOfFunction.equals("ReportDependencies")) {
						mainController.getExportImportController().reportDependencies(selectedFile);
					} else if (typeOfFunction.equals("ExportViolations")) {
						mainController.getExportImportController().exportViolationsReport(selectedFile);
					} else if (typeOfFunction.equals("ReportViolations")) {
						mainController.getExportImportController().exportViolationsReport(selectedFile);
					}

				dispose();
				}
			}
		});
	}

	private void showFileDialog() {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("xml", "XML", "xml");
		List<FileNameExtensionFilter> filters = new ArrayList<FileNameExtensionFilter>();
		if (typeOfFunction.startsWith("Report")) {
			if (typeOfFunction.equals("ReportViolations")) {
				String[] fileExtensions = mainController.getExportImportController().getExportExtensionsValidate();
				for(String extension : fileExtensions){
					filters.add(new FileNameExtensionFilter(extension, extension));
				}
			} else {
				filter = new FileNameExtensionFilter("xls", "XLS", "xls");
			}
		}
		if (typeOfFunction.toLowerCase().contains("mojo")){
			filter = new FileNameExtensionFilter("rsf", "RSF", "rsf");
		}

		FileDialog fileDialog = new FileDialog(JFileChooser.FILES_ONLY, localeService.getTranslatedString("ExportButton"), filter);
		if (typeOfFunction.startsWith("Import")) {
			fileDialog = new FileDialog(JFileChooser.FILES_ONLY, localeService.getTranslatedString("ImportButton"), filter);
		} else if (typeOfFunction.equals("ReportViolations")) {
			fileDialog = new FileDialog(JFileChooser.FILES_ONLY, localeService.getTranslatedString("ExportButton"), filters);
		}

		int returnVal = fileDialog.showDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			if (!typeOfFunction.equals("ExportMojo") && !typeOfFunction.equals("ImportMojo")){
				if(fileDialog.getSelectedFile().exists()){
					setFile(fileDialog.getSelectedFile());
				} else {
					setFile(new File(fileDialog.getSelectedFile().getAbsolutePath() + "." + fileDialog.getFileFilter().getDescription()));
				}
			}
			else {
				
				
				if(fileDialog.getSelectedFile().exists()){
					mojoPanel.setText(fileDialog.getSelectedFile(), mojoOrigin);
				}
				else{
					mojoPanel.setText(new File(fileDialog.getSelectedFile().getAbsolutePath() + "." + fileDialog.getFileFilter().getDescription()), mojoOrigin);
				}
				
			}
			
		}
	}

	private void setFile(File file) {
		selectedFile = file;
		pathText.setText(file.getAbsolutePath());
		exportImportButton.setEnabled(true);
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
