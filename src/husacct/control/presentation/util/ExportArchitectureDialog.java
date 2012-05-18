package husacct.control.presentation.util;

import husacct.ServiceProvider;
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


public class ExportArchitectureDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private MainController mainController;
	
	private JLabel pathLabel;
	private JTextField pathText;
	private JButton browseButton, exportButton;

	private File selectedFile;

	private IControlService controlService = ServiceProvider.getInstance().getControlService();
	
	public ExportArchitectureDialog(MainController mainController) {
		super(mainController.getMainGui(), true);
		this.mainController = mainController;
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
				mainController.getExportController().exportArchitecture(selectedFile);
				dispose();
			}
		});
	}

	protected void showFileDialog() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setApproveButtonText(controlService.getTranslatedString("ExportButton"));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("XML", "xml", "xml");
		fileChooser.setFileFilter(filter);
		int returnVal = fileChooser.showDialog(this, controlService.getTranslatedString("ExportButton"));
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			setFile(fileChooser.getSelectedFile());	            
		}
	}

	private void setFile(File file) {
		selectedFile = file;
		pathText.setText(file.getAbsolutePath());
		exportButton.setEnabled(true);
	}
}
