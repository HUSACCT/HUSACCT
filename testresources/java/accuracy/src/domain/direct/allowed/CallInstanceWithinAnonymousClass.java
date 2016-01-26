package domain.direct.allowed;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

import technology.direct.dao.ProfileDAO;

public class CallInstanceWithinAnonymousClass{
	
	private ProfileDAO profileDao;
	private JButton browseButton;
	
	public CallInstanceWithinAnonymousClass(){
		
		browseButton = new JButton("BrowseButton");
	}
	
	private void setListeners(){
		browseButton.addActionListener(new ActionListener() { // Creation of new anonymous class as argument.
			public void actionPerformed(ActionEvent arg0) {
				System.out.println(profileDao.getCampaignType());				
			}
		});
	}

}