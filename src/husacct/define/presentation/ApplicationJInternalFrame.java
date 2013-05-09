	package husacct.define.presentation;

import husacct.ServiceProvider;
import husacct.common.Resource;
import husacct.common.locale.ILocaleService;
import husacct.control.ILocaleChangeListener;
import husacct.define.presentation.jdialog.HelpDialog;
import husacct.define.presentation.jpanel.DefinitionJPanel;
import husacct.define.presentation.utils.JPanelStatus;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

public class ApplicationJInternalFrame extends JInternalFrame implements ILocaleChangeListener {

	private static final long serialVersionUID = 6858870868564931134L;
	private JPanel overviewPanel;
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	private HelpDialog helpDialog;

	public ApplicationJInternalFrame() {
		super();
		initUi();
	}

	private void initUi() {
		try {
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
		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerLocation(30);
		splitPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		JToolBar toolBar = new JToolBar();
		toolBar.setEnabled(false);
		toolBar.setBorderPainted(false);
		toolBar.add(JPanelStatus.getInstance(""));
		
		ImageIcon questionMark = new ImageIcon(Toolkit.getDefaultToolkit().getImage(Resource.get(Resource.ICON_QUESTIONMARK)));
		JButton questionButton = new JButton(questionMark);
		questionButton.setToolTipText(localeService.getTranslatedString("?TooltipDefineArchitecture"));
		questionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(helpDialog == null){
					helpDialog = new HelpDialog();
				}
				else{
					helpDialog.setVisible(true);
				}
			}
		});
		
		splitPane.add(questionButton, JSplitPane.LEFT);
		splitPane.add(toolBar, JSplitPane.RIGHT);
		getContentPane().add(splitPane, BorderLayout.SOUTH);
		
	}

	public void setContentView(JPanel jp) {
		this.overviewPanel.removeAll();
		this.overviewPanel.add(jp);
	}

	@Override
	public void update(Locale newLocale) {
	
	}
	
}
