package husacct.control.presentation.workspace.loaders;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.control.IControlService;
import husacct.control.presentation.util.FileDialog;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class XmlLoadPanel extends LoaderPanel{

	private static final long serialVersionUID = 1L;
	
	private JLabel pathLabel,descriptionLabel;
	private JTextField pathText;
	private JButton browseButton;
	
	private static File selectedFile;
	
	private GridBagConstraints constraints;
	
	private IControlService controlService = ServiceProvider.getInstance().getControlService();
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	
	public XmlLoadPanel(){
		super();
		setup();
		addComponents();
		setListeners();
	}
	
	private void setup(){
		setLayout(new GridBagLayout());
		constraints = new GridBagConstraints();
	}
	
	private void addComponents(){
		
		descriptionLabel = new JLabel(localeService.getTranslatedString("OpenFromXML"));
		pathLabel = new JLabel(localeService.getTranslatedString("PathLabel"));
		pathText = new JTextField(20);
		browseButton = new JButton(localeService.getTranslatedString("BrowseButton"));
		pathText.setEnabled(false);
		
		if(selectedFile != null){
			pathText.setText(selectedFile.getAbsolutePath());
		}
		
		JPanel hiddenPanel = new JPanel();
		hiddenPanel.setPreferredSize(new Dimension(100, 10));
		
		add(descriptionLabel, getConstraint(0, 0, 3, 1));
		add(pathLabel, getConstraint(0, 1, 1, 1));
		add(pathText, getConstraint(1, 1, 2, 1));
		add(hiddenPanel, getConstraint(1, 2, 1, 1));
		add(browseButton, getConstraint(2, 2, 1, 1));
	}
	
	private GridBagConstraints getConstraint(int gridx, int gridy, int gridwidth, int gridheight){
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(3, 3, 3, 3);
		constraints.gridx = gridx;
		constraints.gridy = gridy;
		constraints.gridwidth = gridwidth;
		constraints.gridheight = gridheight;
		return constraints;		
	}
	
	private void setListeners(){
		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showFileDialog();
			}
		});
	}
	
	protected void showFileDialog() {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("xml", "xml");
		FileDialog fileChooser = new FileDialog(JFileChooser.FILES_ONLY, localeService.getTranslatedString("OpenButton"), filter);
		
		File currentDirectory = getDirectoryFromFile(selectedFile);
		fileChooser.setCurrentDirectory(currentDirectory);
		
		int returnVal = fileChooser.showDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			setFile(fileChooser.getSelectedFile());
		}
	}
	
	private void setFile(File file) {		
		selectedFile = file;
		pathText.setText(file.getAbsolutePath());
	}
	
	@Override
	public HashMap<String, Object> getData() {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("file", selectedFile);
		return data;
	}
	
	@Override
	public boolean validateData() {
		if(selectedFile == null){
			controlService.showErrorMessage(localeService.getTranslatedString("NoFileLocationError"));
			return false;
		}
		return true;
	}

}
