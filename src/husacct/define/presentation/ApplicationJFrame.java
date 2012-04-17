package husacct.define.presentation;

import husacct.define.presentation.jpanel.DefinitionJPanel;
import husacct.define.presentation.utils.JPanelStatus;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
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
			this.addMenuBar();
			
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
	
	private void addMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		menuBar.add(this.createArchitectureMenu());
		menuBar.add(this.createAnalyseMenu());
		menuBar.add(this.createAboutMenu());
	}
	
	private JMenu createArchitectureMenu() {
		JMenu architectureMenu = new JMenu();
		architectureMenu.setText("Architecture");
		
		architectureMenu.add(this.createNewArchitectureMenuItem());
		architectureMenu.add(this.createOpenArchitectureMenuItem());
		architectureMenu.add(this.createSaveArchitectureMenuItem());
		architectureMenu.add(this.createSeperator());
		architectureMenu.add(this.createExitMenuItem());
		return architectureMenu;
	}
	
	private JMenuItem createNewArchitectureMenuItem() {
		JMenuItem newArchitecture = new JMenuItem();
		newArchitecture.setText("New architecture");
		newArchitecture.setMnemonic(KeyEvent.VK_N);
		newArchitecture.setIcon(new ImageIcon(getClass().getClassLoader().getResource("husacct/define/presentation/resources/new.png")));
		newArchitecture.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		return newArchitecture;
	}
	
	private JMenuItem createOpenArchitectureMenuItem() {
		JMenuItem openArchitecture = new JMenuItem();
		openArchitecture.setText("Open architecture");
		openArchitecture.setMnemonic(KeyEvent.VK_O);
		openArchitecture.setIcon(new ImageIcon(getClass().getClassLoader().getResource("husacct/define/presentation/resources/open.png")));
		openArchitecture.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		return openArchitecture;
	}
	
	private JMenuItem createSaveArchitectureMenuItem() {
		JMenuItem saveArchitecture = new JMenuItem();
		saveArchitecture.setText("Save architecture");
		saveArchitecture.setMnemonic(KeyEvent.VK_S);
		saveArchitecture.setIcon(new ImageIcon(getClass().getClassLoader().getResource("husacct/define/presentation/resources/save.png")));
		saveArchitecture.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		return saveArchitecture;
	}
	
	private JSeparator createSeperator() {
		JSeparator separator = new JSeparator();
		return separator;
	}
	
	private JMenuItem createExitMenuItem() {
		JMenuItem exit = new JMenuItem();
		exit.setText("Exit");
		exit.setIcon(new ImageIcon(getClass().getClassLoader().getResource("husacct/define/presentation/resources/exit.png")));
		return exit;
	}
	
	@Deprecated
	private JMenu createAnalyseMenu() {
		JMenu analyseMenu = new JMenu();
		analyseMenu.setText("Analyse");
		analyseMenu.add(this.createStartAnalyseMenuItem());
		analyseMenu.add(this.createCheckDependenciesMenuItem());
		return analyseMenu;
	}
	
	@Deprecated
	private JMenuItem createStartAnalyseMenuItem() {
		JMenuItem startAnalyse = new JMenuItem();
		startAnalyse.setText("Start analyse");
		startAnalyse.setMnemonic(KeyEvent.VK_1);
		startAnalyse.setIcon(new ImageIcon(getClass().getClassLoader().getResource("husacct/define/presentation/resources/analyse.png")));
		startAnalyse.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.CTRL_MASK));
		return startAnalyse;
	}
	
	@Deprecated
	private JMenuItem createCheckDependenciesMenuItem() {
		JMenuItem checkDependencies = new JMenuItem();
		checkDependencies.setText("Check dependencies");
		checkDependencies.setMnemonic(KeyEvent.VK_2);
		checkDependencies.setIcon(new ImageIcon(getClass().getClassLoader().getResource("husacct/define/presentation/resources/analyse.png")));
		checkDependencies.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.CTRL_MASK));
		return checkDependencies;
	}
	
	private JMenu createAboutMenu() {
		JMenu aboutMenu = new JMenu();
		aboutMenu.setText("Help");
		aboutMenu.add(this.createAboutMenuItem());
		return aboutMenu;
	}
	
	private JMenuItem createAboutMenuItem() {
		JMenuItem aboutMenuItem = new JMenuItem();
		aboutMenuItem.setText("About");
		aboutMenuItem.setMnemonic(KeyEvent.VK_H);
		aboutMenuItem.setIcon(new ImageIcon(getClass().getClassLoader().getResource("husacct/define/presentation/resources/about.png")));
		aboutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
		return aboutMenuItem;
		
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
