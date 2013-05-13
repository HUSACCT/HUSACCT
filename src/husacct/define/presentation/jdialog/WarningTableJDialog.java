package husacct.define.presentation.jdialog;

import husacct.ServiceProvider;
import husacct.common.Resource;
import husacct.define.presentation.tables.JTableSoftwareUnits;
import husacct.define.presentation.tables.JTableWarningTable;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;

public class WarningTableJDialog  extends JDialog implements ActionListener, KeyListener {
 private JTableWarningTable warningTable;
	
	
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WarningTableJDialog() {
		init();
	}
	
	
	
	
	
	
	
	private void init() {
		
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setSize(new Dimension(500, 380));
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(createWarningPanel(),BorderLayout.CENTER);
		setTitle(ServiceProvider.getInstance().getLocaleService().getTranslatedString("SoftwareUnitTitle"));
		setIconImage(new ImageIcon(Resource.get(Resource.HUSACCT_LOGO)).getImage());

		this.setSize(1000, 400);
		this.pack();
		
		
	}







	private JScrollPane createWarningPanel() {
		JScrollPane scrollpane= new JScrollPane();
	
	
		warningTable= new JTableWarningTable();
		
		scrollpane.setSize(1000, 400);
	
		scrollpane.setViewportView(warningTable);
	
		return scrollpane;
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
		// TODO Auto-generated method stub
		
	}








}
