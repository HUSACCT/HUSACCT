package husacct.control.presentation.workspace;

import husacct.control.task.WorkspaceController;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class OpenWorkspaceFrame extends JFrame{
	
	private WorkspaceController controller;
	
	public OpenWorkspaceFrame(WorkspaceController controller){
		// TODO: open workspace gui + controller.createWorkspace
		// misschien iets van util.browsefiledialog
		super("Open workspace");
		this.controller = controller;
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLayout(new FlowLayout());
		JButton createButton = new JButton("Open workspace");
		JLabel label = new JLabel("TODO open workspace");
		this.setSize(new Dimension(300, 500));
		this.getContentPane().add(label);
		this.getContentPane().add(createButton);
		this.setVisible(true);
	}

}
