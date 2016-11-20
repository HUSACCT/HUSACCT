package husacct.analyse.presentation.reconstruct.mojo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.GroupLayout.Alignment;


import husacct.ServiceProvider;
import husacct.analyse.task.reconstruct.mojo.MoJo;
import husacct.common.help.presentation.HelpableJPanel;
import husacct.control.IControlService;

public class MojoJPanelCompare extends HelpableJPanel implements ActionListener{
	private static final long serialVersionUID = -8871162150900400867L;
	private JButton compareBrowseGold, compareBrowseToCompare, compareArchitecture;
	private JLabel comparePathGold, comparePathToCompare, compareResult;
	private JTextField compareTextGold, compareTextToCompare;
	private IControlService controlService;

	
	public MojoJPanelCompare(){
		setBorder(new TitledBorder("Compare Architectures"));
		setPageComponents();
		createLayout();
		controlService = ServiceProvider.getInstance().getControlService();
	}
	
	private void setPageComponents(){
		comparePathGold = new JLabel("Golden standard:");
		comparePathGold.setHorizontalAlignment(SwingConstants.RIGHT);
		compareTextGold = new JTextField(40);
		compareTextGold.setEnabled(false);
		compareBrowseGold = new JButton("Browse");
		compareBrowseGold.addActionListener(this);
		
		
		comparePathToCompare = new JLabel("To compare:");
		comparePathToCompare.setHorizontalAlignment(SwingConstants.RIGHT);
		compareTextToCompare = new JTextField(40);
		compareTextToCompare.setEnabled(false);
		
		compareBrowseToCompare= new JButton("Browse");
		compareBrowseToCompare.addActionListener(this);
		
		compareArchitecture= new JButton("Compare Architectures");
		compareArchitecture.addActionListener(this);
		compareArchitecture.setEnabled(false);
		
		//Result field
		compareResult = new JLabel("The calculated Mojo percentage is: ");
	}
	
	private void createLayout(){
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setAutoCreateGaps(true);
		groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setHorizontalGroup(
    		groupLayout.createParallelGroup(Alignment.LEADING)
	    		.addGroup(groupLayout.createSequentialGroup()
	                .addContainerGap()
	                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
	                	.addComponent(comparePathGold)
	                	.addComponent(comparePathToCompare))
	                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
	            		.addComponent(compareTextGold)
	            		.addComponent(compareTextToCompare)
	                	.addGroup(groupLayout.createSequentialGroup()
	                			.addComponent(compareArchitecture)
	                			.addComponent(compareResult)))
	                .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
	                	.addComponent(compareBrowseGold)
	                	.addComponent(compareBrowseToCompare))
	                .addContainerGap()));
        groupLayout.setVerticalGroup(
    		groupLayout.createParallelGroup(Alignment.LEADING)
     		.addGroup(groupLayout.createSequentialGroup()
                .addGap(5)
                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                	.addComponent(comparePathGold)
            		.addComponent(compareTextGold)
            		.addComponent(compareBrowseGold))
        		.addContainerGap()
        		.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
            		.addComponent(comparePathToCompare)
            		.addComponent(compareTextToCompare)
            		.addComponent(compareBrowseToCompare))
        		.addContainerGap()
                .addGap(25)
        		.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
        			.addComponent(compareArchitecture)
            		.addComponent(compareResult))));
        this.setLayout(groupLayout);
        
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == compareBrowseGold){
			compareTextGold.setText(controlService.showMojoExportImportDialog(false));
			enableCompareArchitecture();
		}
		if(e.getSource() == compareBrowseToCompare){
			compareTextToCompare.setText(controlService.showMojoExportImportDialog(false));
			enableCompareArchitecture();
		}
		if (e.getSource() == compareArchitecture){
			MoJo mojo = new MoJo();
	    	String[] daoArray = {compareTextGold.getText(), compareTextToCompare.getText(), "-fm"}; //"-fm is a different execution of mojo, see MoJo.java.showerrormessage() for more functions"
	    	double mojoResult = mojo.executeMojo(daoArray);
	    	compareResult.setText("The calculated Mojo percentage is: " + mojoResult + "%");
		}
		
	}
	private void enableCompareArchitecture(){
		if(!compareTextGold.getText().trim().isEmpty()&& !compareTextToCompare.getText().trim().isEmpty() ){
			compareArchitecture.setEnabled(true);
		}
		else{
			compareArchitecture.setEnabled(false);
		}
	}
}

