package husacct.control.presentation.util;

import husacct.ServiceProvider;
import husacct.common.dto.ApplicationDTO;
import husacct.control.IControlService;
import husacct.control.task.MainController;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class SetApplicationDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JButton saveButton;
	private MainController mainController;
	private SetApplicationPanel setApplicationPanel;
	
	private IControlService controlService = ServiceProvider.getInstance().getControlService();
	
	public SetApplicationDialog(MainController mainController) {
		super(mainController.getMainGui(), true);
		this.mainController = mainController;
		setApplicationPanel = new SetApplicationPanel();
		setTitle(controlService.getTranslatedString("ApplicationProperties"));
		setup();
		addComponents();
		setListeners();
		this.setVisible(true);
	}
	
	private void setup(){
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLayout(new GridBagLayout());
		this.setSize(new Dimension(350, 380));
		this.setResizable(true);
		DialogUtils.alignCenter(this);
	}
	
	private void addComponents(){
		JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		saveButton = new JButton(controlService.getTranslatedString("SaveButton"));
		savePanel.add(saveButton);
		getRootPane().setDefaultButton(saveButton);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(setApplicationPanel);
		mainPanel.add(savePanel);
		add(mainPanel);
	}
	
	private void setListeners(){
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ApplicationDTO applicationData = setApplicationPanel.getApplicationData();
				mainController.getApplicationController().setApplicationData(applicationData);
				dispose();
			}
		});
	}

}