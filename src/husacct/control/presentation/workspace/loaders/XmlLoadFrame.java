package husacct.control.presentation.workspace.loaders;

import husacct.control.task.WorkspaceController;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class XmlLoadFrame extends JFrame implements ILoaderFrame{

	private WorkspaceController workspaceController;
	
	private JLabel pathLabel;
	private JTextField pathText;
	private JButton browseButton, openButton;
	
	private File selectedFile;
	
	public XmlLoadFrame(){
		super();
		setTitle("Load XML");
	}
	
	public void setWorkspaceController(WorkspaceController workspaceController) {
		this.workspaceController = workspaceController;
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
		openButton = new JButton("Open");
		openButton.setEnabled(false);
		pathText.setEnabled(false);
		
		add(pathLabel);
		add(pathText);
		add(browseButton);
		add(openButton);
	}
	
	private void setListeners(){
		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showFileDialog();				
			}
		});
		openButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				load();
			}
		});
	}
	
	protected void showFileDialog() {
		JFileChooser chooser = new JFileChooser();
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
		openButton.setEnabled(true);
	}
	
	protected void load() {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("file", selectedFile);
		workspaceController.loadWorkspace("xml", data);
	}	

}
