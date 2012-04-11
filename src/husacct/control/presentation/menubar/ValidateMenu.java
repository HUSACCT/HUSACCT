package husacct.control.presentation.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class ValidateMenu extends JMenu{
	public ValidateMenu(){
		super("Validate");
		
		JMenuItem mntmConfigure = new JMenuItem("Validate now");
		this.add(mntmConfigure);
		mntmConfigure.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// TODO: Validate now
			}
		});
		
		JMenuItem mntmValidateNow = new JMenuItem("Configuration");
		this.add(mntmValidateNow);
		mntmValidateNow.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// TODO: Configuration
			}
		});
	}
}
