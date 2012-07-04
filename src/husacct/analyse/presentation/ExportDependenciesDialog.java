package husacct.analyse.presentation;


//TODO Due to time issues this was done here. THis class needs to move to the control service..
import husacct.control.task.threading.ThreadWithLoader;
import husacct.ServiceProvider;
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

class ExportDependenciesDialog extends JDialog{

	private static final long serialVersionUID = -6928586336028017253L;
	
	private JLabel pathLabel;
	private JTextField pathField;
	private JButton browseButton, exportButton;
	
	private File selectedFile;
	private AnalyseUIController uiController;
	
	public ExportDependenciesDialog(AnalyseUIController uiController){
		this.uiController = uiController;
		setTitle(uiController.translate("ExportDependencies"));
		setup();
		addComponents();
		setListeners();
		super.setVisible(true);
	}
	
	private void setup(){
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLayout(new FlowLayout());
		this.setSize(new Dimension(350, 100));
		this.setResizable(false);
		ServiceProvider.getInstance().getControlService().centerDialog(this);
	}
	
	private void addComponents(){
		pathLabel = new JLabel(uiController.translate("PathLabel"));
		pathField = new JTextField(20);
		browseButton = new JButton(uiController.translate("BrowseButton"));
		exportButton = new JButton(uiController.translate("ExportButton"));
		exportButton.setEnabled(false);
		pathField.setEnabled(false);
		
		getRootPane().setDefaultButton(exportButton);
		
		add(pathLabel);
		add(pathField);
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
					dispose();
					ThreadedDependencyExport dependencyExport = new ThreadedDependencyExport(uiController, selectedFile.getAbsolutePath());
					ThreadWithLoader analyseExportThread = ServiceProvider.getInstance().getControlService().getThreadWithLoader(ServiceProvider.getInstance().getLocaleService().getTranslatedString("ExportingDependencies"), dependencyExport);
					analyseExportThread.run();
				}
			}
		});
	}

	private void showFileDialog() {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("xls", "xls", "xls" );
		FileDialog fileDialog = new FileDialog(JFileChooser.FILES_ONLY, uiController.translate("Open"), filter);
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
		pathField.setText(file.getAbsolutePath());
		exportButton.setEnabled(true);
	}
	
	public boolean validateData() {
		if(selectedFile == null){
			ServiceProvider.getInstance().getControlService().showErrorMessage(uiController.translate("NoFileLocationError"));
			return false;
		}		
		else if(!Regex.matchRegex(Regex.filenameRegex, selectedFile.getName())) {
			ServiceProvider.getInstance().getControlService().showErrorMessage(uiController.translate("InvalidFilenameError"));
			return false;
		}
		return true;
	}	
	
}
