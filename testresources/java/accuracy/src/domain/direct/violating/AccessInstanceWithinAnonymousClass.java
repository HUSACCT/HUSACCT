package domain.direct.violating;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

import technology.direct.dao.ProfileDAO;

public class AccessInstanceWithinAnonymousClass {
	
	private ProfileDAO profileDao;

	private JButton browseButton;
	
	public AccessInstanceWithinAnonymousClass(){
		
		browseButton = new JButton("BrowseButton");
	}
	
	private void setListeners(){
		ActionListener ac = new ActionListener() { // Declaration of new anonymous class 
			public void actionPerformed(ActionEvent arg0) {
				System.out.println(profileDao.name);				
			}
		};
		browseButton.addActionListener(ac);
	}

}