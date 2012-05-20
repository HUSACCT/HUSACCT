package husacct.control.presentation.workspace;

import husacct.ServiceProvider;
import husacct.common.dto.ApplicationDTO;
import husacct.control.IControlService;
import husacct.control.presentation.util.DialogUtils;
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
	private JCheckBox setApplicationCheckbox;
	private JButton okButton, cancelButton;
	private JTextField workspaceNameText;

	private IControlService controlService = ServiceProvider.getInstance().getControlService();

	public CreateWorkspaceDialog(MainController mainController){
		super(mainController.getMainGui(), true);
		this.mainController = mainController;
		setApplicationPanel = new SetApplicationPanel();
		setApplicationPanel.setVisible(false);
		this.setTitle(controlService.getTranslatedString("CreateWorkspaceTitle"));		
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

		JLabel workspaceNameLabel = new JLabel(controlService.getTranslatedString("WorkspaceNameLabel"));
		setApplicationCheckbox = new JCheckBox(controlService.getTranslatedString("SetApplicationCheckbox"), false);
		okButton = new JButton(controlService.getTranslatedString("OkButton"));
		cancelButton = new JButton(controlService.getTranslatedString("CancelButton"));
		workspaceNameText = new JTextField(20);
		workspaceNameText.setText("myHusacctWorkspace"); 
		
		getRootPane().setDefaultButton(okButton);
		
		workspacePanel.add(workspaceNameLabel);
		workspacePanel.add(workspaceNameText);
		workspacePanel.add(setApplicationCheckbox);
		workspacePanel.add(new JSeparator(SwingConstants.HORIZONTAL));
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		mainPanel.add(workspacePanel);
		mainPanel.add(setApplicationPanel);
		mainPanel.add(buttonPanel);
		add(mainPanel);
	}

	private void setListeners(){

		setApplicationCheckbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toggleSetApplicationPanel(setApplicationCheckbox.isSelected());
			}
		});

		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(setApplicationCheckbox.isSelected()){
					ApplicationDTO applicationData = setApplicationPanel.getApplicationData();
					mainController.getApplicationController().setApplicationData(applicationData);
				}
				createWorkspace();
				dispose();
				mainController.getViewController().showDefineGui();
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
		if ((workspaceName != null) && (workspaceName.length() > 0)) {
			mainController.getWorkspaceController().createWorkspace(workspaceName);
		}	
	}
}
