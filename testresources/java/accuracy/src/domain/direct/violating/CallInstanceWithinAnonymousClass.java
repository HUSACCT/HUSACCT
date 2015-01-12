package domain.direct.violating;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

import domain.direct.Base;

public class CallInstanceWithinAnonymousClass extends Base{
	
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