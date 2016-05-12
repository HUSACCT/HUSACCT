package husacct.analyse.presentation.reconstruct;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;

import org.apache.log4j.Logger;

import husacct.ServiceProvider;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants;
import husacct.analyse.task.reconstruct.ReconstructArchitecture;
import husacct.analyse.task.reconstruct.mojo.MoJo;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ReconstructArchitectureDTO;
import husacct.control.task.MainController;
import husacct.define.IDefineSarService;
import husacct.define.IDefineService;

public class MojoJPanel implements ActionListener{
	private final Logger logger = Logger.getLogger(ReconstructArchitecture.class);
	private JButton browseGold, browseTC, store, calculate;
	private JTextField textPath1, textPath2;
	private JPanel compareMojoPanel;
	private JComboBox<String> approaches;
	private JLabel result;
	
	//private AnalyseTaskControl analyseTaskControl;
	
	public MojoJPanel(/*AnalyseTaskControl analyseTaskControl*/){
		/*this.analyseTaskControl = analyseTaskControl;*/
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public JPanel createMojoPanel(){
		JLabel path1 = new JLabel("Golden path:");
		JLabel path2 = new JLabel("To Compare path:");
		
		textPath1 = new JTextField(40);
		textPath1.setEnabled(false);
		textPath2 = new JTextField(40);
		textPath2.setEnabled(false);
		
		approaches = new JComboBox<>();
		approaches.addItem("hoi");
		approaches.setMaximumSize(new Dimension(textPath2.getWidth(), textPath2.getHeight()));
		
		browseGold = new JButton("Browse");
		browseGold.addActionListener(this);
		
		browseTC = new JButton("Browse");
		browseTC.addActionListener(this);
		
		store = new JButton("Store");	
		store.addActionListener(this);
		store.setEnabled(false);
		
		calculate = new JButton("Calculate");	
		calculate.addActionListener(this);
		calculate.setEnabled(false);
		
		JPanel resultPanel = new JPanel();		
		result = new JLabel("");
		result.setVisible(false);
		result.setHorizontalAlignment(SwingConstants.LEFT);
		resultPanel.add(result);
		
		
		JPanel goldenMojoPanel = new JPanel();
		GroupLayout layout = new GroupLayout(goldenMojoPanel);
		goldenMojoPanel.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addComponent(path1)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(textPath1)
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(store)))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(browseGold))
		);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(path1)
						.addComponent(textPath1)
						.addComponent(browseGold))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(store))
		);
		
		
		compareMojoPanel = new JPanel();
		compareMojoPanel.setVisible(false);
		
		GroupLayout layoutC = new GroupLayout(compareMojoPanel);
		compareMojoPanel.setLayout(layoutC);
		
		layoutC.setAutoCreateGaps(true);
		layoutC.setAutoCreateContainerGaps(true);
		
		layoutC.setHorizontalGroup(layoutC.createSequentialGroup()
				.addComponent(path2)
				.addGroup(layoutC.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(textPath2)
					.addGroup(layoutC.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(approaches)
						.addComponent(calculate)))
				.addGroup(layoutC.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(browseTC))
		);
		
		layoutC.setVerticalGroup(layoutC.createSequentialGroup()
				.addGroup(layoutC.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(path2)
						.addComponent(textPath2)
						.addComponent(browseTC))
				.addGroup(layoutC.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(approaches))
				.addGroup(layoutC.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(calculate))
		);
		
		path1.setPreferredSize(new Dimension(path2.getWidth(), path2.getHeight()));
		
		JPanel mojoPanel2 = new JPanel();
		mojoPanel2.setLayout(new GridLayout(4,2));
		mojoPanel2.add(goldenMojoPanel);
		mojoPanel2.add(new JPanel());//empty grid cell
		mojoPanel2.add(compareMojoPanel);
		mojoPanel2.add(new JPanel()); //empty grid cell
		
		mojoPanel2.add(resultPanel);
		mojoPanel2.add(new JPanel());//empty grid cell
		mojoPanel2.add(new JPanel());//empty grid cell
		mojoPanel2.add(new JPanel());//empty grid cell
		mojoPanel2.setName("Mojo");
		
		return mojoPanel2;
	}
	
	MainController mainController = new MainController();
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == browseGold){
			mainController.getExportImportController().showExportMojoGui(this, browseGold);
		}
		
		if(e.getSource() == store){
			exportFile(textPath1);
			
			if (!textPath2.getText().trim().equals("")){
				calculate.setEnabled(true);
			}
			compareMojoPanel.setVisible(true);
		}
		if(e.getSource() == browseTC){
			mainController.getExportImportController().showExportMojoGui(this, browseTC);
			calculate.setEnabled(true);
		}
		if (e.getSource() == calculate){
			IDefineService defineService = ServiceProvider.getInstance().getDefineService();
			IDefineSarService defineSarService = defineService.getSarService();
			ReconstructArchitectureDTO dto = new ReconstructArchitectureDTO();
			String approach = (String) approaches.getSelectedItem();
			int threshold = 10;
			String relationType = AnalyseReconstructConstants.RelationTypes.allDependencies;

			ModuleDTO selectedModule = defineSarService.getModule_SelectedInGUI();
			dto.setSelectedModule(selectedModule);
			dto.setApproach(approach);
			dto.setThreshold(threshold);
			dto.setRelationType(relationType);
			
			/*analyseTaskControl.reconstructArchitecture_ClearAll();
			analyseTaskControl.reconstructArchitecture_Execute(dto);*/
			
			exportFile(textPath2);
			
			MoJo mojoTest = new MoJo();
	    	String[] daoArray = {textPath1.getText(), textPath2.getText(), "-fm"}; //"-fm is a different execution of mojo, see MoJo.java.showerrormessage() for more functions"
	    	result.setText(mojoTest.executeMojo(daoArray, approaches.getSelectedItem().toString()));
	    	result.setVisible(true);
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
		if(origin == browseGold){
			textPath1.setText(replacedPath);
			store.setEnabled(true);
			calculate.setEnabled(false);
		}
		else if(origin == browseTC){
			textPath2.setText(replacedPath);
			calculate.setEnabled(true);
		}
		
	}
}
