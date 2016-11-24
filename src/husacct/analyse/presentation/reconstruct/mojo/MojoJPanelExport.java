package husacct.analyse.presentation.reconstruct.mojo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.GroupLayout.Alignment;

import org.apache.log4j.Logger;

import husacct.ServiceProvider;
import husacct.analyse.task.reconstruct.ReconstructArchitecture;
import husacct.common.dto.ModuleDTO;
import husacct.common.help.presentation.HelpableJPanel;
import husacct.control.IControlService;
import husacct.define.IDefineService;

import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class MojoJPanelExport extends HelpableJPanel implements ActionListener {
	private static final long serialVersionUID = 2911110095301402204L;
	private JLabel exportPath;
	private JButton exportBrowse, exportArchitecture;
	private JTextField exportText;
	private final Logger logger = Logger.getLogger(ReconstructArchitecture.class);
	private IControlService controlService;
	
	public MojoJPanelExport(){
		setBorder(new TitledBorder("Export current Intended Architecture"));
		setPageComponents();
		createLayout();
		controlService = ServiceProvider.getInstance().getControlService();
	}
	
	private void setPageComponents(){
		exportPath = new JLabel("Export path:");
		exportText = new JTextField(40);
		exportText.setEnabled(false);
		
		exportBrowse = new JButton("Browse");
		exportBrowse.addActionListener(this);
		exportArchitecture = new JButton("Export");
		exportArchitecture.addActionListener(this);
		exportArchitecture.setEnabled(false);
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
	                	.addComponent(exportPath))
	                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
	            		.addComponent(exportText)
	            		.addComponent(exportArchitecture))
	                .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
	                	.addComponent(exportBrowse))
	                .addContainerGap()));
        groupLayout.setVerticalGroup(
    		groupLayout.createParallelGroup(Alignment.LEADING)
     		.addGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                	.addComponent(exportPath)
            		.addComponent(exportText)
            		.addComponent(exportBrowse))
        		.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
            		.addComponent(exportArchitecture))));
        this.setLayout(groupLayout);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == exportBrowse){
			exportText.setText(controlService.showMojoExportImportDialog(true));
			enableExportArchitecture();
		}
		if(e.getSource() == exportArchitecture){
			exportFile(exportText.getText());
		}
	}
	
	private void enableExportArchitecture() {
		if(!exportText.getText().trim().isEmpty()){
			exportArchitecture.setEnabled(true);
		}
		else{
			exportArchitecture.setEnabled(false);
		}
		
	}

	private void exportFile(String path){
		IDefineService defineService = ServiceProvider.getInstance().getDefineService();
		try {
			File exportFile = new File(path);
			exportFile.createNewFile();
			FileWriter writer = new FileWriter(exportFile);
			String toWrite = "";
			for(ModuleDTO moduleDTO : defineService.getAllModules()){
				for(String softwareDTOPath : defineService.getAssignedSoftwareUnitsOfModule(moduleDTO.logicalPath)){
					toWrite = "contain " + moduleDTO.logicalPath.replaceAll(" ", "") + " " + softwareDTOPath + "\n";
					writer.write(toWrite);
				}
			}
			writer.flush();
			writer.close();
		} catch (IOException e1) {
			logger.info("Error occured while writing the file");
		}
	}
	
}
