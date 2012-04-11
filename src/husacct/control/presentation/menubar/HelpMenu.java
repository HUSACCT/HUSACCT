package husacct.control.presentation.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class HelpMenu extends JMenu {
	public HelpMenu(){
		super("Help");
		
		JMenuItem versionItem = new JMenuItem("Version");
		this.add(versionItem);
		versionItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// TODO: Version
			}
		});
		
		JMenuItem mntmAbout = new JMenuItem("Help");
		this.add(mntmAbout);
		mntmAbout.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// TODO: help
			}
		});
	}
}
