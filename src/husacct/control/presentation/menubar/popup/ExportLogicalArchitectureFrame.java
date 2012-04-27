package husacct.control.presentation.menubar.popup;

import husacct.control.task.MainController;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class ExportLogicalArchitectureFrame extends JFrame {

	private MainController maincontroller;
	public ExportLogicalArchitectureFrame(MainController maincontroller){
		super("Export logical architecture");
		this.maincontroller = maincontroller;
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
