package husacct.control.presentation.workspace;

import husacct.control.task.MainController;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class CreateWorkspaceFrame extends JFrame{
	
	private MainController mainController;
	
	public CreateWorkspaceFrame(MainController mainController){
		// TODO: create workspace gui + controller.createWorkspace
		super("Create workspace");
		this.mainController = mainController;
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLayout(new FlowLayout());
		JButton createButton = new JButton("Create workspace");
		JLabel label = new JLabel("TODO create workspace");
		this.setSize(new Dimension(300, 500));
		this.getContentPane().add(label);
		this.getContentPane().add(createButton);
		this.setVisible(true);
	}
}
