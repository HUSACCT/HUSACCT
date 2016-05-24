package husacct.analyse.presentation.reconstruct;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;

import org.apache.log4j.Logger;

import husacct.ServiceProvider;
import husacct.analyse.task.reconstruct.ReconstructArchitecture;
import husacct.analyse.task.reconstruct.mojo.MoJo;
import husacct.common.dto.ModuleDTO;
import husacct.control.task.MainController;
import husacct.define.IDefineService;
import net.miginfocom.swing.MigLayout;

public class MojoJPanel implements ActionListener{
	private final Logger logger = Logger.getLogger(ReconstructArchitecture.class);
	private JButton exportBrowse, exportArchitecture;
	private JButton compareBrowseGold, compareBrowseToCompare, compareArchitecture;
	private JLabel exportTitle, exportPath;
	private JLabel compareTitle, comparePathGold, comparePathToCompare, compareResult;
	private JTextField exportText;
	private JTextField compareTextGold, compareTextToCompare;
	
	
	//private AnalyseTaskControl analyseTaskControl;
	
	public MojoJPanel(/*AnalyseTaskControl analyseTaskControl*/){
		/*this.analyseTaskControl = analyseTaskControl;*/
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public JPanel createMojoPanel(){
		//export side
		exportTitle = new JLabel("Export current Intended Architecture");
		exportTitle.setFont (exportTitle.getFont().deriveFont (14.0f));
		exportPath = new JLabel("Export path:");
		exportText = new JTextField(40);
		exportText.setEnabled(false);
		
		exportBrowse = new JButton("Browse");
		exportBrowse.addActionListener(this);
		exportArchitecture = new JButton("Export");
		exportArchitecture.addActionListener(this);
		exportArchitecture.setEnabled(false);
		
		//compare side
		compareTitle = new JLabel("Compare Architectures");
		compareTitle.setFont (compareTitle.getFont().deriveFont (14.0f));
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
		
		//result
		compareResult = new JLabel();
		compareResult.setVisible(false);
		
		
		JPanel exportPanel = new JPanel();
		exportPanel.setLayout(new MigLayout("wrap 3", "[left]", "[top]"));
		exportPanel.add(exportTitle, "span 3");
		exportPanel.add(exportPath, "skip 6");
		exportPanel.add(exportText);
		exportPanel.add(exportBrowse);
		exportPanel.add(exportArchitecture, "skip 1");
		
		JPanel comparePanel = new JPanel();
		comparePanel.setLayout(new MigLayout("wrap 3", "[left]", "[top]"));
		comparePanel.add(compareTitle, "span 3");
		comparePanel.add(comparePathGold, "skip 6");
		comparePanel.add(compareTextGold);
		comparePanel.add(compareBrowseGold);
		comparePanel.add(comparePathToCompare, "skip 3");
		comparePanel.add(compareTextToCompare);
		comparePanel.add(compareBrowseToCompare);
		comparePanel.add(compareArchitecture, "skip 1");
		
		JPanel resultPanel = new JPanel();		
		resultPanel.add(compareResult);
		
		JPanel mainPanel = new JPanel(new MigLayout("", "[0:0,grow 50,left][0:0,grow 50, left]", "[top]15[]"));
		mainPanel.add(exportPanel);
		mainPanel.add(comparePanel, "wrap");
		mainPanel.add(resultPanel, "skip 1");
		mainPanel.setName("Mojo");
		
		return mainPanel;
	}
	
	MainController mainController = new MainController();
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == exportBrowse){
			mainController.getExportImportController().showExportMojoGui(this, exportBrowse, true);
		}
		if(e.getSource() == exportArchitecture){
			exportFile(exportText);
		}
		if(e.getSource() == compareBrowseGold){
			mainController.getExportImportController().showExportMojoGui(this, compareBrowseGold, false);
		}
		if(e.getSource() == compareBrowseToCompare){
			mainController.getExportImportController().showExportMojoGui(this, compareBrowseToCompare, false);
		}
		if (e.getSource() == compareArchitecture){
			MoJo mojo = new MoJo();
	    	String[] daoArray = {compareTextGold.getText(), compareTextToCompare.getText(), "-fm"}; //"-fm is a different execution of mojo, see MoJo.java.showerrormessage() for more functions"
	    	double mojoResult = mojo.executeMojo(daoArray);
	    	compareResult.setText("The compared architecture has a mojo percentage of " + mojoResult + "% compared to the selected golden standard.");
	    	compareResult.setVisible(true);
		}
		
	}
	
	private void exportFile(JTextField textField){
		IDefineService defineService = ServiceProvider.getInstance().getDefineService();
		try {
			File exportFile = new File(textField.getText());
			exportFile.createNewFile();
			FileWriter writer = new FileWriter(exportFile);
			String toWrite = "";
			for(ModuleDTO moduleDTO : defineService.getAllModules()){
				for(String softwareDTOPath : defineService.getAssignedSoftwareUnitsOfModule(moduleDTO.logicalPath)){
					toWrite = "contain " + moduleDTO.logicalPath + " " + softwareDTOPath + "\n";
					writer.write(toWrite);
				}
			}
			
			writer.flush();
			writer.close();
		} catch (IOException e1) {
			logger.info("Error occured while writing the file");
		}
	}
	
	public void setText(File file, JButton origin){
		String replacedPath = file.getPath().replaceFirst(".xml", ".rsf");
		if(origin == exportBrowse){
			exportText.setText(replacedPath);
			exportArchitecture.setEnabled(true);
		}
		else if(origin == compareBrowseGold){
			compareTextGold.setText(replacedPath);
			if(!compareTextToCompare.getText().trim().equals("")){
				compareArchitecture.setEnabled(true);
			}
		}
		else if(origin == compareBrowseToCompare){
			compareTextToCompare.setText(replacedPath);
			if(!compareTextGold.getText().trim().equals("")){
				compareArchitecture.setEnabled(true);
			}
		}
	}
}
