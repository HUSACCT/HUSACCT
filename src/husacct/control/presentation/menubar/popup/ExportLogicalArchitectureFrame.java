package husacct.control.presentation.menubar.popup;

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


public class ExportLogicalArchitectureFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JLabel pathLabel;
	private JTextField pathText;
	private JButton browseButton, saveButton;

	private File selectedFile;

	public ExportLogicalArchitectureFrame() {
		super();
		setTitle("Export Logical Architecture");
		setup();
		addComponents();
		setListeners();
	}
	
	private void setup(){
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLayout(new FlowLayout());
		this.setSize(new Dimension(350, 100));
	}

	private void addComponents(){
		pathLabel = new JLabel("Path");
		pathText = new JTextField(20);
		browseButton = new JButton("Browse");
		saveButton = new JButton("Export");
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

				//load();
			}
		});
	}

	protected void showFileDialog() {
		JFileChooser chooser = new JFileChooser();
		chooser.setApproveButtonText("Export");
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
