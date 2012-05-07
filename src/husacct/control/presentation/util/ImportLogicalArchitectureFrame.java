package husacct.control.presentation.util;

import husacct.control.task.MainController;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;


public class ImportLogicalArchitectureFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private MainController mainController;
	
	private JLabel pathLabel;
	private JTextField pathText;
	private JButton browseButton, saveButton;

	private File selectedFile;

	public ImportLogicalArchitectureFrame(MainController mainController) {
		super();
		this.mainController = mainController;
		setTitle("Import Logical Architecture");
		setup();
		addComponents();
		setListeners();
	}
	
	private void setup(){
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLayout(new FlowLayout());
		this.setSize(new Dimension(350, 100));
		this.setVisible(true);
	}

	private void addComponents(){
		pathLabel = new JLabel("Path");
		pathText = new JTextField(20);
		browseButton = new JButton("Browse");
		saveButton = new JButton("Import");
		saveButton.setEnabled(false);
		pathText.setEnabled(false);

		add(pathLabel);
		add(pathText);
		add(browseButton);
		add(saveButton);
	}

	private void setListeners(){
		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showFileDialog();				
			}
		});
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mainController.getImportExportController().importLogicalArchitecture(selectedFile);
				dispose();
			}
		});
	}

	protected void showFileDialog() {
		JFileChooser chooser = new JFileChooser();
		chooser.setApproveButtonText("Import");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("XML", "xml", "xml");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			setFile(chooser.getSelectedFile());	            
		}
	}

	private void setFile(File file) {
		selectedFile = file;
		pathText.setText(file.getAbsolutePath());
		saveButton.setEnabled(true);
	}
}
