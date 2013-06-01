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

	private int amountOfProcesses = 1;
	private int currentProcessNumber = 1;
	
	final WindowEvent we = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
	private static final long serialVersionUID = 1L;
	private String progressInfoText;
	private String projectProgressInfoText;
	private String processInfoText;
	private JLabel progressLabel;
	private JLabel projectProgressLabel;
	
	private MainController mainController;
	private JProgressBar progressBar;	
	private JProgressBar projectProgressBar;
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	private JLabel processLabel;

	public LoadingDialog(MainController mainController, String processInfoText) {
		super();
		this.mainController = mainController;
		setTitle(localeService.getTranslatedString("Prepare"));
		this.processInfoText = processInfoText;
		setup();
		addComponents();
	}
	
	public LoadingDialog(String processInfoText){
		ControlServiceImpl controlService = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
		new LoadingDialog(controlService.getMainController(), processInfoText);
	}
	
	public void setProgressText(int percentage) {
		//percentage = (percentage/amountOfProcesses) + (currentProcessNumber*(100/amountOfProcesses));
		progressBar.setIndeterminate(false);
		this.progressLabel.setText(percentage + "%");
		this.setTitle((percentage/amountOfProcesses) + (currentProcessNumber*(100/amountOfProcesses)) + "%");
		this.progressBar.setValue(percentage);
	}
	
	public void setAmountOfProcesses(int amount) {
		this.amountOfProcesses = amount;
		this.projectProgressBar.setMaximum(amount);
	}

	public void setCurrentProcess(int number) {
		if(this.currentProcessNumber != number) {
			this.progressLabel.setText("0%");
			this.progressBar.setIndeterminate(true);
		}
		this.currentProcessNumber = number;
		this.projectProgressLabel.setText(number+1 + " / " + this.amountOfProcesses);
	
		this.projectProgressBar.setValue(number+1);
	}
	
	private void setup() {
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		this.setSize(new Dimension(400, 250));
		this.setResizable(false);
		//DialogUtils.alignCenter(this);
	}

	private void addComponents() {
		JPanel processPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		JPanel projectLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		JPanel projectProgressPanel = new JPanel(new GridLayout(1, 1));
		JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		JPanel progressPanel = new JPanel(new GridLayout(1, 1));
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		

		processLabel = new JLabel(processInfoText);
		
		progressLabel = new JLabel(progressInfoText);
		progressBar = new JProgressBar();
		progressBar.setValue(0);
		progressPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		progressBar.setIndeterminate(true);

		projectProgressLabel = new JLabel(projectProgressInfoText);
		projectProgressBar = new JProgressBar();
		projectProgressBar.setValue(0);
		projectProgressPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		
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
		
		processPanel.add(processLabel);
		
		labelPanel.add(progressLabel);

		progressPanel.add(progressBar);
		
		projectLabelPanel.add(projectProgressLabel);
		projectProgressPanel.add(projectProgressBar);
		
		buttonsPanel.add(waitLabel);
		buttonPanel.add(runInBackgroundButton);
		buttonPanel.add(stopButton);
		
		add(processPanel);
		add(projectLabelPanel);
		add(projectProgressPanel);
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
