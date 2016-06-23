package husacct.analyse.presentation.reconstruct.mojo;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import husacct.common.help.presentation.HelpableJPanel;

public class MojoJPanel extends HelpableJPanel{
	private static final long serialVersionUID = -3586968367597151222L;

	/**
	 * @wbp.parser.entryPoint
	 */
	public JPanel createMojoPanel(){
		MojoJPanelExport exportPanel = new MojoJPanelExport();
		MojoJPanelCompare comparePanel = new MojoJPanelCompare();
		JPanel mainPanel = new JPanel();
		
		
		GroupLayout groupLayout = new GroupLayout(mainPanel);
        groupLayout.setHorizontalGroup(
    		groupLayout.createParallelGroup(Alignment.LEADING)
	    		.addGroup(groupLayout.createSequentialGroup()
	                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
	                	.addComponent(exportPanel, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
	                	.addComponent(comparePanel, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))));
        groupLayout.setVerticalGroup(
        		groupLayout.createParallelGroup(Alignment.LEADING)
     		.addGroup(groupLayout.createSequentialGroup()
				.addComponent(exportPanel, 100, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
 				.addGap(10)
                .addComponent(comparePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        
        
        mainPanel.setLayout(groupLayout);
		mainPanel.setName("Mojo");
		return mainPanel;
	
	
	}
}
