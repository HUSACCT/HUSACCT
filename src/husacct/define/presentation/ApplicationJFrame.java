package husacct.define.presentation;

import husacct.define.presentation.jpanel.DefinitionJPanel;
import husacct.define.presentation.utils.JPanelStatus;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

/**
 * 
 * @author Henk ter Harmsel
 * Define Architecture main frame
 * 
 */
public class ApplicationJFrame extends javax.swing.JFrame {

	private static final long serialVersionUID = 6858870868564931134L;
	private String defaultTitle = "HUSACCT";
	private JPanel overviewPanel;

	public ApplicationJFrame() {
		super();
		initUi();
	}

	private void initUi() {
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
			setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			
			this.setLayoutSettings();
			this.addDefinitionPanel();
			this.addToolBar();
			
			pack();
			setSize(1000, 700);
		} catch (Exception e) {
			// add your error handling code here
			e.printStackTrace();
		}
	}
	
	private void setLayoutSettings() {
		this.setTitle(" - Define Architecture");
		setIconImage(new ImageIcon(getClass().getClassLoader().getResource("husacct/define/presentation/resources/jframeicon.jpg")).getImage());
	}
	
	private void addDefinitionPanel() {
		this.overviewPanel = new JPanel();
		BorderLayout borderLayout = new BorderLayout();
		this.overviewPanel.setLayout(borderLayout);
		this.overviewPanel.add(new DefinitionJPanel());
		this.getContentPane().add(this.overviewPanel, BorderLayout.CENTER);
	}
	
	private void addToolBar() {
		JToolBar toolBar = new JToolBar();
		getContentPane().add(toolBar, BorderLayout.SOUTH);
		toolBar.setEnabled(false);
		toolBar.setBorderPainted(false);
		toolBar.add(JPanelStatus.getInstance(""));
	}

	public void setContentView(JPanel jp) {
		this.overviewPanel.removeAll();
		this.overviewPanel.add(jp);
	}

	@Override
	public void setTitle(String configuration) {
		if (configuration.trim().equals("")) {
			super.setTitle(defaultTitle);
		} else {
			super.setTitle(defaultTitle + " - " + configuration);
		}
	}

}
