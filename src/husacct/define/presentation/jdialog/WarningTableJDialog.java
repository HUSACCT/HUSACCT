package husacct.define.presentation.jdialog;

import husacct.ServiceProvider;
import husacct.common.Resource;
import husacct.common.locale.ILocaleService;
import husacct.control.presentation.util.DialogUtils;
import husacct.define.domain.warningmessages.WarningMessageFactory;
import husacct.define.presentation.treetable.WarningTreeTableCellrenderer;
import husacct.define.presentation.treetable.WarningTreeTableModel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import org.jdesktop.swingx.JXTreeTable;

public class WarningTableJDialog extends JDialog implements ActionListener,
KeyListener {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private	WarningMessageFactory factory = new WarningMessageFactory();
	private JXTreeTable treeTab = new JXTreeTable();
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();

	public WarningTableJDialog() {
		init();
	}

	private void init() {

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setSize(new Dimension(500, 380));
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(createTreeTable(), BorderLayout.CENTER);
		this.setTitle(localeService.getTranslatedString("Warnings"));
		DialogUtils.alignCenter(this);
		setIconImage(new ImageIcon(Resource.get(Resource.ICON_VALIDATE)).getImage());
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation( d.width / 2 - 512, d.height/2 - 384 );
		setSize( 600, 300 );
	}

	public JTabbedPane createTreeTable()
	{
		JTabbedPane tabs = new JTabbedPane();
		JPanel treeTablePanel = new JPanel( new BorderLayout() );


		WarningTreeTableModel model = new WarningTreeTableModel(factory.getsortedMessages());

		treeTab = new JXTreeTable(model);
		treeTab.setTreeCellRenderer(new WarningTreeTableCellrenderer());
		treeTablePanel.add( new JScrollPane( treeTab ) );
		tabs.addTab( "Warnings", treeTablePanel );
		return tabs;
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}

	public void refresh() {
		treeTab.setTreeTableModel(new WarningTreeTableModel(factory.getsortedMessages()));
		treeTab.repaint();
		this.repaint();
	}
}