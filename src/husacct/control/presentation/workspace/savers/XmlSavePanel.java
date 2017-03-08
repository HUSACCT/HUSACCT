	package husacct.control.presentation.workspace.savers;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.control.IControlService;
import husacct.control.presentation.util.FileDialog;
import husacct.control.presentation.util.Regex;
import husacct.control.task.configuration.ConfigurationManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class XmlSavePanel extends SaverPanel{

	private static final long serialVersionUID = 1L;
	
	private JLabel pathLabel,descriptionLabel;
	private JTextField pathText;
	private JButton browseButton;
	
	private JCheckBox doCompress;
	
	private JLabel spaceRequiredDescription;
	private JLabel spaceRequired;
	
	private JLabel spaceAvailableDescription;
	private JLabel spaceAvailable;
	
	private File selectedFile = new File(ConfigurationManager.getProperty("LastUsedLoadXMLWorkspacePath"));
	
	private GridBagConstraints constraints;
	
	private IControlService controlService = ServiceProvider.getInstance().getControlService();
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	
	public XmlSavePanel(){
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
		
		descriptionLabel = new JLabel(localeService.getTranslatedString("SaveToXML"));
		pathLabel = new JLabel(localeService.getTranslatedString("PathLabelShort"));
		pathText = new JTextField(20);
		
		
		doCompress = new JCheckBox();
		doCompress.setText("Compress");

		spaceAvailableDescription = new JLabel(localeService.getTranslatedString("spaceAvailable"));
		spaceAvailable = new JLabel(localeService.getTranslatedString("calculating"));
		
		spaceRequiredDescription = new JLabel(localeService.getTranslatedString("spaceRequried"));
		spaceRequired = new JLabel();
		
		
		browseButton = new JButton(localeService.getTranslatedString("BrowseButton"));
		pathText.setEnabled(false);
		
		if(selectedFile != null){
			pathText.setText(selectedFile.getAbsolutePath());
			pathText.setDisabledTextColor(Color.BLUE);
		}
		
		JPanel hiddenPanel = new JPanel();
		hiddenPanel.setPreferredSize(new Dimension(100, 10));
		
		add(descriptionLabel, getConstraint(0, 0, 3, 1));
		add(pathLabel, getConstraint(0, 1, 1, 1));
		add(pathText, getConstraint(1, 1, 2, 1));
		add(hiddenPanel, getConstraint(1, 2, 1, 1));
	
		add(browseButton, getConstraint(2, 2, 1, 1));
		add(doCompress, getConstraint(0,3,1,1));
	
		add(spaceAvailableDescription, getConstraint(0,7,1,1));
		add(spaceAvailable, getConstraint(1,7,1,1));
		
		add(spaceRequiredDescription, getConstraint(0,8,1,1));
		add(spaceRequired, getConstraint(1,8,1,1));
		calculateAvailableSpaces();
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
		
		doCompress.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				calculateAvailableSpaces();
			}
		});
	}
	
	protected void showFileDialog() {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("xml", "xml");
		FileDialog fileDialog = new FileDialog(JFileChooser.FILES_ONLY, localeService.getTranslatedString("SaveButton"), filter);
		
		File currentDirectory = getDirectoryFromFile(selectedFile);
		fileDialog.setCurrentDirectory(currentDirectory);
		
		int returnVal = fileDialog.showDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			if(fileDialog.getSelectedFile().exists()){
				File selectedFile = fileDialog.getSelectedFile();
				setFile(selectedFile);
			} else {
				setFile(new File(fileDialog.getSelectedFile().getAbsolutePath() + "." + fileDialog.getFileFilter().getDescription()));
			}
		}
	}
	
	private void setFile(File file) {
		selectedFile = file;
		ConfigurationManager.setProperty("LastUsedSaveXMLWorkspacePath", file.getAbsolutePath());
		ConfigurationManager.storeProperties();
		pathText.setText(file.getAbsolutePath());
	}
	
	@Override
	public HashMap<String, Object> getData() {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("file", selectedFile);
		return data;
	}
	
	private void calculateAvailableSpaces() {
		File f = new File("C:/");
		this.spaceAvailable.setText(f.getFreeSpace()/1073741824 + " GB of " + f.getTotalSpace()/1073741824 + " GB");
		resourceGatherer rg = new resourceGatherer(this);
		rg.run();		
	}
	
	public void setRequiredSpace(int bytes) {
		if(bytes < 1024) {
			this.spaceRequired.setText((bytes + " byte"));
		}
		else if(bytes < 1048576) {
			this.spaceRequired.setText((bytes/1024 + " KB"));
		}
		else if(bytes < 1073741824){
			this.spaceRequired.setText((bytes/1048576 + " MB"));
		}
		else {
			this.spaceRequired.setText((bytes/1073741824 + " GB"));
		}
	}
	
	@Override
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
	
	@Override
	public HashMap<String, Object> getConfig() {
		HashMap<String, Object> config = new HashMap<String, Object>();
		config.put("doCompress", this.doCompress.isSelected());
		return config;
	}

}
