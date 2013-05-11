package husacct.define.presentation;

import husacct.ServiceProvider;
import husacct.common.Resource;
import husacct.common.services.IServiceListener;
import husacct.control.ILocaleChangeListener;
import husacct.define.domain.services.WarningMessageService;
import husacct.define.presentation.jdialog.WarningTableJDialog;
import husacct.define.presentation.jpanel.DefinitionJPanel;
import husacct.define.presentation.utils.JPanelStatus;
import husacct.define.task.DefinitionController;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

public class ApplicationJInternalFrame extends JInternalFrame implements ILocaleChangeListener,Observer,IServiceListener {

	private static final long serialVersionUID = 6858870868564931134L;
	private JPanel overviewPanel;
	private JButton togglebutton;
	public ApplicationJInternalFrame() {
		super();
		initUi();

	
	}

	private void initUi() {
		try {
			WarningMessageService.getInstance().addObserver(this);
			setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			
			this.addDefinitionPanel();
			this.addToolBar();
			pack();
			setSize(1200, 1200);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
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
		
		togglebutton = new JButton();
		togglebutton.addActionListener(toolbarActionListener);
		togglebutton.setText("Warnings");
		
		
		Dimension d = togglebutton.getPreferredSize();
		d.width=150;
		togglebutton.setMinimumSize(new Dimension(50,50));
		togglebutton.setMaximumSize(d);
		toolBar.setMaximumSize(new Dimension(25,25));
	
		
		toolBar.add(togglebutton);
	}

	public void setContentView(JPanel jp) {
		this.overviewPanel.removeAll();
		this.overviewPanel.add(jp);
	}

	@Override
	public void update(Locale newLocale) {
	
	}
	
	private ActionListener toolbarActionListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()== togglebutton )
			{
				Icon icon = new ImageIcon(Resource.get(Resource.ICON_VALIDATE));
				togglebutton.setIcon(icon);
				WarningTableJDialog warnings = new WarningTableJDialog();
				warnings.setVisible(true);
			}
			
		}
	};
	@Override
	public void update(Observable o, Object arg) {
		System.out.println("updated??????????????????");
		if (WarningMessageService.getInstance().hasWarnings()) {
			Icon icon = new ImageIcon(Resource.get(Resource.ICON_VALIDATE));
			togglebutton.setIcon(icon);
		}else {
			System.out.println("updated??????????????????");
			togglebutton.repaint();
		}
		
	}

	@Override
	public void update() {
		System.out.println("updated??????????????????");
		
	}


	
}
