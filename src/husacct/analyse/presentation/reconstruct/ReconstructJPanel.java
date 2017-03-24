package husacct.analyse.presentation.reconstruct;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.apache.log4j.Logger;

import husacct.ServiceProvider;
import husacct.analyse.presentation.reconstruct.approaches.PracticalApproachesJPanel;
import husacct.analyse.presentation.reconstruct.approaches.ResearchApproachesHUPanel;
import husacct.analyse.presentation.reconstruct.mojo.MojoJPanel;
import husacct.analyse.task.AnalyseTaskControl;
import husacct.common.help.presentation.HelpableJPanel;
import husacct.common.locale.ILocaleService;


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
		initUI();
	}
	
	public final void initUI() {
		setLayout(new BorderLayout(0, 10));
		tabbedPane = new JTabbedPane(SwingConstants.TOP);
		tabbedPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		add(tabbedPane);
		addTabs();
	}
	
	private void addTabs(){
		tabbedPane.removeAll();
		try{
			String allApprTranslation = getTranslation("PracticalApproaches");
			tabbedPane.addTab(allApprTranslation, null, new PracticalApproachesJPanel(analyseTaskControl), null);
			
			String distinctApprTranslation = getTranslation("ResearchApproachesHU");
			tabbedPane.addTab(distinctApprTranslation, null, new ResearchApproachesHUPanel(analyseTaskControl), null);
		
			MojoJPanel mojoPanel = new MojoJPanel();
			tabbedPane.addTab("MoJo",null, mojoPanel.createMojoPanel(),null);
		}catch(Exception e){
			logger.error("initUI() failt: " + e);
		}
	}
	
	private String getTranslation(String translationKey){
		ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
		return localeService.getTranslatedString(translationKey);
	}
	
	public void reload() {
		addTabs();
		this.invalidate();
        this.revalidate();
        this.repaint();
	}
}