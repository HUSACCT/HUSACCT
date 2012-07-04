package husacct.control.presentation.workspace;

import husacct.ServiceProvider;
import husacct.common.dto.ApplicationDTO;
import husacct.common.locale.ILocaleService;
import husacct.control.IControlService;
import husacct.control.presentation.util.DialogUtils;
import husacct.control.presentation.util.Regex;
import husacct.control.presentation.util.SetApplicationPanel;
import husacct.control.task.MainController;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class CreateWorkspaceDialog extends JDialog{

	private static final long serialVersionUID = 1L;
	private MainController mainController;
	private SetApplicationPanel setApplicationPanel;
	private JCheckBox analyseApplicationCheckbox;
	private JButton okButton, cancelButton;
	private JTextField workspaceNameText;

	private IControlService controlService = ServiceProvider.getInstance().getControlService();
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	
	public CreateWorkspaceDialog(MainController mainController){
		super(mainController.getMainGui(), true);
		this.mainController = mainController;
		setApplicationPanel = new SetApplicationPanel();
		setApplicationPanel.setVisible(false);
		this.setTitle(localeService.getTranslatedString("CreateWorkspaceTitle"));		
		setup();
		addComponents();
		setListeners();
		this.setVisible(true);
	}

	private void setup(){
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLayout(new GridBagLayout());
		this.setSize(new Dimension(350, 150));
		this.setResizable(false);
		DialogUtils.alignCenter(this);
	}

	private void addComponents(){
		JPanel workspacePanel = new JPanel();
		workspacePanel.setLayout(new BoxLayout(workspacePanel, BoxLayout.Y_AXIS));
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		JLabel workspaceNameLabel = new JLabel(localeService.getTranslatedString("WorkspaceNameLabel"));
		analyseApplicationCheckbox = new JCheckBox(localeService.getTranslatedString("AnalyseApplicationCheckBox"), false);
		okButton = new JButton(localeService.getTranslatedString("OkButton"));
		cancelButton = new JButton(localeService.getTranslatedString("CancelButton"));
		workspaceNameText = new JTextField(20);
		workspaceNameText.setText("myHusacctWorkspace"); 

		getRootPane().setDefaultButton(okButton);

		workspacePanel.add(workspaceNameLabel);
		workspacePanel.add(workspaceNameText);
		workspacePanel.add(analyseApplicationCheckbox);
		workspacePanel.add(new JSeparator(SwingConstants.HORIZONTAL));
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		mainPanel.add(workspacePanel);
		mainPanel.add(setApplicationPanel);
		mainPanel.add(buttonPanel);
		add(mainPanel);
	}

	private void setListeners(){
		
		final CreateWorkspaceDialog createWorkspaceDialog = this;
		
		analyseApplicationCheckbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toggleSetApplicationPanel(analyseApplicationCheckbox.isSelected());
				DialogUtils.alignCenter(createWorkspaceDialog);
			}
		});

		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(analyseApplicationCheckbox.isSelected()){
					if(setApplicationPanel.dataValidated() && workspaceNameValidated()) {
						createWorkspace();
						ApplicationDTO applicationData = setApplicationPanel.getApplicationData();
						mainController.getApplicationController().setAndAnalyseApplicationData(applicationData);
						dispose();	
						mainController.getViewController().showDefineArchitecture();
					}
				} else {
					if(workspaceNameValidated()) {
						createWorkspace();			
						dispose();	
						mainController.getViewController().showDefineArchitecture();		
					}
				}	
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}

	private void toggleSetApplicationPanel(boolean checked){
		if(checked){
			this.setSize(new Dimension(350, 420));
			setApplicationPanel.setVisible(true);
		} else {
			this.setSize(new Dimension(350, 150));
			setApplicationPanel.setVisible(false);
		}
	}

	private void createWorkspace(){  		
		String workspaceName = workspaceNameText.getText();
		mainController.getWorkspaceController().createWorkspace(workspaceName);
	}	

	private boolean workspaceNameValidated() {
		String workspaceName = workspaceNameText.getText();
		if (workspaceName == null || workspaceName.length() < 1) {
			controlService.showErrorMessage(localeService.getTranslatedString("FieldEmptyError"));
			return false;
		}
		else if(!Regex.matchRegex(Regex.nameRegex, workspaceName)) {
			controlService.showErrorMessage(localeService.getTranslatedString("MustBeAlphaNumericError"));
			return false;
		}
		return true;
	}
}
