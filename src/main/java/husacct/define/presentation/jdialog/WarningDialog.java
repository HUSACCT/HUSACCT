package husacct.define.presentation.jdialog;

import husacct.ServiceProvider;
import husacct.common.Resource;
import husacct.common.locale.ILocaleService;
import husacct.define.presentation.tables.JTableWarningTable;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class WarningDialog  extends JDialog{
	private static final long serialVersionUID = 6732312133421767797L;
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();

	private JTableWarningTable warningTable;
	private JScrollPane scrollPane;
	private JPanel buttonPanel;
	private JButton closeButton;


	public WarningDialog() {
		super();
		init();
		addComponents();
		setListeners();
		this.setVisible(true);
	}

	private void init() {
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(new Dimension(600, 380));
		this.setLayout(new BorderLayout());
		this.setResizable(true);
		this.setTitle(localeService.getTranslatedString("Warnings"));
		ServiceProvider.getInstance().getControlService().centerDialog(this);
		setIconImage(new ImageIcon(Resource.get(Resource.ICON_VALIDATE)).getImage());
	}

	private void addComponents(){
		scrollPane = new JScrollPane();
		warningTable= new JTableWarningTable();
		scrollPane.setViewportView(warningTable);

		buttonPanel = new JPanel();


		closeButton = new JButton(localeService.getTranslatedString("Close"));
		buttonPanel.add(closeButton);

		getContentPane().add(scrollPane,BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		getRootPane().setDefaultButton(closeButton);
	}

	private void setListeners(){
		closeButton.addActionListener(e -> dispose());
	}
}
