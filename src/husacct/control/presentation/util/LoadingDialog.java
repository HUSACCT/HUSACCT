package husacct.control.presentation.util;

import husacct.control.task.MainController;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

public class LoadingDialog extends JDialog implements Runnable{

	private static final long serialVersionUID = 1L;
	
	private String progressInfoText;
	
	public LoadingDialog(MainController mainController, String progressInfoText){
		super(mainController.getMainGui(), true);
		setTitle("Loading");
		this.progressInfoText = progressInfoText;
		setup();
		addComponents();
	}
	
	private void setup(){
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		this.setSize(new Dimension(400, 130));
		this.setResizable(false);
		DialogUtils.alignCenter(this);
	}
	
	private void addComponents(){
		JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel progressPanel = new JPanel(new GridLayout(1, 1));
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		JLabel progressLabel = new JLabel(progressInfoText);
		JProgressBar progressBar = new JProgressBar();
		
		progressPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		progressBar.setIndeterminate(true);
		
		JLabel waitLabel = new JLabel("Please wait...");
		
		labelPanel.add(progressLabel);
		progressPanel.add(progressBar);
		buttonsPanel.add(waitLabel);
		
		add(labelPanel);
		add(progressPanel);
		add(buttonsPanel);
	}

	@Override
	public void run() {
		this.setVisible(true);
	}	
}
