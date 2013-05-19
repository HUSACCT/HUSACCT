package husacct.define.presentation;

import husacct.ServiceProvider;
import husacct.common.Resource;
import husacct.common.locale.ILocaleService;
import husacct.common.services.IServiceListener;
import husacct.control.ILocaleChangeListener;
import husacct.define.domain.services.WarningMessageService;
import husacct.define.presentation.jdialog.HelpDialog;
import husacct.define.presentation.jdialog.WarningDialog;
import husacct.define.presentation.jpanel.DefinitionJPanel;
import husacct.define.presentation.utils.JPanelStatus;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

public class ApplicationJInternalFrame extends JInternalFrame implements ILocaleChangeListener,Observer,IServiceListener {

	private static final long serialVersionUID = 6858870868564931134L;
	private JPanel overviewPanel;
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	private HelpDialog helpDialog;
	private WarningDialog warningDialog;
	private JButton warningButton;

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
		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerLocation(200);
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

		ImageIcon warningSign = new ImageIcon(Toolkit.getDefaultToolkit().getImage(Resource.get(Resource.ICON_VALIDATE)));
		JButton warningButton = new JButton(warningSign);
		warningButton.setText(localeService.getTranslatedString("Warnings"));
		warningButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(warningDialog == null){
					warningDialog = new WarningDialog();
				}else{
					warningDialog.setVisible(true);
				}
			}
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(questionButton);
		buttonPanel.add(warningButton);

		splitPane.add(buttonPanel, JSplitPane.LEFT);
		splitPane.add(toolBar, JSplitPane.RIGHT);
		getContentPane().add(splitPane, BorderLayout.SOUTH);
	}

	public void setContentView(JPanel jp) {
		this.overviewPanel.removeAll();
		this.overviewPanel.add(jp);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (WarningMessageService.getInstance().hasWarnings()) {
			warningButton.setText(localeService.getTranslatedString("Warnings"));
		}else{
			warningButton.setText(" ");
		}
	}

	@Override
	public void update() {}

	@Override
	public void update(Locale newLocale) {}
}
