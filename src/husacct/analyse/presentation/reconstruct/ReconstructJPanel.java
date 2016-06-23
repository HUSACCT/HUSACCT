package husacct.analyse.presentation.reconstruct;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JTabbedPane;
import javax.swing.border.LineBorder;

import org.apache.log4j.Logger;

import husacct.ServiceProvider;
import husacct.analyse.presentation.reconstruct.approaches.AllApproachesJPanel;
import husacct.analyse.presentation.reconstruct.approaches.DistinctApproachesPanel;
import husacct.analyse.presentation.reconstruct.mojo.MojoJPanel;
import husacct.analyse.task.AnalyseTaskControl;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants;
import husacct.common.help.presentation.HelpableJPanel;
import husacct.common.locale.ILocaleService;

import java.io.IOException;


public class ReconstructJPanel extends HelpableJPanel{
	private final Logger logger = Logger.getLogger(ReconstructJPanel.class);
	private static final long serialVersionUID = 1L;
	private AnalyseTaskControl analyseTaskControl;
	public JTabbedPane tabbedPane;
	
	/**
	 * Create the panel.
	 */
	public ReconstructJPanel(AnalyseTaskControl atc) {
		super();
		analyseTaskControl = atc;
		try{
			initUI();
		}catch(Exception e){
			logger.error("initUI() failt: " + e);
		}
	}
	
	public final void initUI() throws IOException{
		setLayout(new BorderLayout(0, 10));
		
		tabbedPane = setupTabbedPane();
		add(tabbedPane);
				
		String allApprTranslation = getTranslation(AnalyseReconstructConstants.ApproachesTable.PanelAllApproaches);
		tabbedPane.addTab(allApprTranslation, null, new AllApproachesJPanel(analyseTaskControl), null);
		
		String distinctApprTranslation = getTranslation(AnalyseReconstructConstants.ApproachesTable.PanelDistinctApproaches);
		tabbedPane.addTab(distinctApprTranslation, null, new DistinctApproachesPanel(analyseTaskControl), null);
	
		MojoJPanel mojoPanel = new MojoJPanel(/*analyseTaskControl*/);
		tabbedPane.addTab("Mojo",null, mojoPanel.createMojoPanel(),null);
	}
	
	private JTabbedPane setupTabbedPane(){
		JTabbedPane tPane = new JTabbedPane(JTabbedPane.TOP);
		tPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		return tPane;
	}
	
	private String getTranslation(String translationKey){
		ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
		return localeService.getTranslatedString(translationKey);
	}
}