package husacct.define.presentation;

import husacct.define.domain.DefineDomainService;
import husacct.define.presentation.jpanel.DefinitionJPanel;
import husacct.define.presentation.utils.JPanelStatus;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free for non-commercial use. If Jigloo is being used commercially (ie, by a corporation, company or
 * business for any purpose whatever) then you should purchase a license for each developer using Jigloo. Please visit www.cloudgarden.com for details. Use of Jigloo implies acceptance of these
 * licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class ApplicationJFrame extends javax.swing.JFrame {

	private static final long serialVersionUID = 6858870868564931134L;
	private String defaultTitle = "HUSACCT";
	private JSeparator jSeparator1;
	public JLabel jLabelStatus;
	private JToolBar jToolBar;
	public JPanel jPanelContentView;
//	public JMenuItem jMenuItemExit;
//	private JMenuBar jMenuBar;
//	public JMenuItem jMenuItemAbout;
//	public JMenuItem jMenuItemOnlineHelp;
//	public JMenuItem jMenuItemStartAnalyse;
//	public JMenuItem jMenuItemCheckDependencies;
//	public JMenuItem jMenuItemSaveArchitecture;
//	public JMenuItem jMenuItemOpenArchitecture;
//	public JMenuItem jMenuItemNewArchitecture;
//	private JMenu jMenu3;
//	private JMenu jMenu2;
//	private JMenu jMenu1;

	{
		// Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ApplicationJFrame() {
		super();
		initUi();
	}

	private void initUi() {
		try {
			setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

			setTitle("HUSACCT - Define Architecture");
			
			setIconImage(new ImageIcon(getClass().getClassLoader().getResource("husacct/define/presentation/resources/jframeicon.jpg")).getImage());
			{
				jPanelContentView = new JPanel();
				BorderLayout jPanel1Layout = new BorderLayout();
				jPanelContentView.setLayout(jPanel1Layout);
				jPanelContentView.add(new DefinitionJPanel());
				getContentPane().add(jPanelContentView, BorderLayout.CENTER);
			}
			{
				jToolBar = new JToolBar();
				getContentPane().add(jToolBar, BorderLayout.SOUTH);
				jToolBar.setEnabled(false);
				jToolBar.setBorderPainted(false);
				jToolBar.add(JPanelStatus.getInstance(""));
			}
//			{
//				jMenuBar = new JMenuBar();
//				setJMenuBar(jMenuBar);
//				{
//					jMenu1 = new JMenu();
//					jMenuBar.add(jMenu1);
//					jMenu1.setText("Architecture");
//					{
//						jMenuItemNewArchitecture = new JMenuItem();
//						jMenu1.add(jMenuItemNewArchitecture);
//						jMenuItemNewArchitecture.setText("New architecture");
//						jMenuItemNewArchitecture.setMnemonic(KeyEvent.VK_N);
//						jMenuItemNewArchitecture.setIcon(new ImageIcon(getClass().getClassLoader().getResource("husacct/define/presentation/resources/new.png")));
//						jMenuItemNewArchitecture.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
//					}
//					{
//						jMenuItemOpenArchitecture = new JMenuItem();
//						jMenu1.add(jMenuItemOpenArchitecture);
//						jMenuItemOpenArchitecture.setText("Open architecture");
//						jMenuItemOpenArchitecture.setMnemonic(KeyEvent.VK_O);
//						jMenuItemOpenArchitecture.setIcon(new ImageIcon(getClass().getClassLoader().getResource("husacct/define/presentation/resources/open.png")));
//						jMenuItemOpenArchitecture.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
//					}
//					{
//						jMenuItemSaveArchitecture = new JMenuItem();
//						jMenu1.add(jMenuItemSaveArchitecture);
//						jMenuItemSaveArchitecture.setText("Save architecture");
//						jMenuItemSaveArchitecture.setMnemonic(KeyEvent.VK_S);
//						jMenuItemSaveArchitecture.setIcon(new ImageIcon(getClass().getClassLoader().getResource("husacct/define/presentation/resources/save.png")));
//						jMenuItemSaveArchitecture.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
//					}
//					{
//						jSeparator1 = new JSeparator();
//						jMenu1.add(jSeparator1);
//					}
//					{
//						jMenuItemExit = new JMenuItem();
//						jMenu1.add(jMenuItemExit);
//						jMenuItemExit.setText("Exit");
//						jMenuItemExit.setIcon(new ImageIcon(getClass().getClassLoader().getResource("husacct/define/presentation/resources/exit.png")));
//					}
//				}
//				{
//					jMenu2 = new JMenu();
//					jMenuBar.add(jMenu2);
//					jMenu2.setText("Analyse");
//					{
//						jMenuItemStartAnalyse = new JMenuItem();
//						jMenu2.add(jMenuItemStartAnalyse);
//						jMenuItemStartAnalyse.setText("Start analyse");
//						jMenuItemStartAnalyse.setMnemonic(KeyEvent.VK_1);
//						jMenuItemStartAnalyse.setIcon(new ImageIcon(getClass().getClassLoader().getResource("husacct/define/presentation/resources/analyse.png")));
//						jMenuItemStartAnalyse.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.CTRL_MASK));
//					}
//					{
//						jMenuItemCheckDependencies = new JMenuItem();
//						jMenu2.add(jMenuItemCheckDependencies);
//						jMenuItemCheckDependencies.setText("Check dependencies");
//						jMenuItemCheckDependencies.setMnemonic(KeyEvent.VK_2);
//						jMenuItemCheckDependencies.setIcon(new ImageIcon(getClass().getClassLoader().getResource("husacct/define/presentation/resources/analyse.png")));
//						jMenuItemCheckDependencies.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.CTRL_MASK));
//					}
//				}
//				{
//					jMenu3 = new JMenu();
//					jMenuBar.add(jMenu3);
//					jMenu3.setText("Help");
//					{
//						jMenuItemAbout = new JMenuItem();
//						jMenu3.add(jMenuItemAbout);
//						jMenuItemAbout.setText("About");
//						jMenuItemAbout.setMnemonic(KeyEvent.VK_H);
//						jMenuItemAbout.setIcon(new ImageIcon(getClass().getClassLoader().getResource("husacct/define/presentation/resources/about.png")));
//						jMenuItemAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
//					}
//				}
//			}
			pack();
			setSize(1000, 700);
		} catch (Exception e) {
			// add your error handling code here
			e.printStackTrace();
		}
	}

	public void setContentView(JPanel jp) {
		jPanelContentView.removeAll();
		jPanelContentView.add(jp);
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
