package husacct.control.presentation.util;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.control.ControlServiceImpl;
import husacct.control.task.MainController;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

public class LoadingDialog extends JFrame implements Runnable {

	
	final WindowEvent we = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
	private static final long serialVersionUID = 1L;
	private String progressInfoText;
	private JLabel progressLabel;
	private JProgressBar progressBar;
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();

	public LoadingDialog(MainController mainController, String progressInfoText) {
		super();
		setTitle(localeService.getTranslatedString("Prepare"));
		this.progressInfoText = progressInfoText;
		setup();
		addComponents();

	}
	
	public LoadingDialog(String progressInfoText){
		ControlServiceImpl controlService = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
		new LoadingDialog(controlService.getMainController(), progressInfoText);
	}
	
	public void setProgressText(int percentage) {
		this.setTitle(percentage + "%");
		this.progressBar.setValue(percentage);
	}

	private void setup() {
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		this.setSize(new Dimension(400, 150));
		this.setResizable(false);
		//DialogUtils.alignCenter(this);
	}

	private void addComponents() {
		JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel progressPanel = new JPanel(new GridLayout(1, 1));
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		progressLabel = new JLabel(progressInfoText);
		progressBar = new JProgressBar();
		progressBar.setValue(0);
		progressPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		//progressBar.setIndeterminate(true);

		JLabel waitLabel = new JLabel(localeService.getTranslatedString("Wait"));

		JButton runInBackgroundButton = new JButton(localeService.getTranslatedString("RunInBackground"));
		runInBackgroundButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setExtendedState(JFrame.ICONIFIED);
			}
		});
		
		JButton stopButton = new JButton(localeService.getTranslatedString("Cancel"));
		
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getToolkit().getSystemEventQueue().postEvent(we);
			}
		});	
		
		labelPanel.add(progressLabel);
		progressPanel.add(progressBar);
		buttonsPanel.add(waitLabel);
		buttonPanel.add(runInBackgroundButton);
		buttonPanel.add(stopButton);
		

		add(labelPanel);
		add(progressPanel);
		add(buttonsPanel);
		add(buttonPanel);
	}

	@Override
	public void run() {
		this.setVisible(true);
		super.setAlwaysOnTop(true);
	}
}
