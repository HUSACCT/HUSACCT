package husacct.analyse.presentation.reconstruct;

import husacct.ServiceProvider;
import husacct.analyse.task.AnalyseTaskControl;
import husacct.common.help.presentation.HelpableJInternalFrame;
import husacct.common.locale.ILocaleService;
import husacct.common.services.IServiceListener;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

public class AnalyseInternalSARFrame extends HelpableJInternalFrame implements ActionListener, IServiceListener {

    private static final long serialVersionUID = 1L;
	private final ILocaleService localService = ServiceProvider.getInstance().getLocaleService();
	protected AnalyseTaskControl analyseTaskControl;
	private JPanel overviewPanel;
	private ReconstructJPanel reconstructJPanel;

    public AnalyseInternalSARFrame(AnalyseTaskControl atc) {
    	analyseTaskControl = atc;
        registerLocaleChangeListener();
        initUI();
    }

    private void registerLocaleChangeListener() {
    	localService.addServiceListener(this);
    }

    public void initUI(){
    	Dimension minimumSize = new Dimension(800, 400); // The technical minimumSize = (740, 350)
		setMinimumSize(minimumSize);
    	addReconstructPanel();
    }

	private void addReconstructPanel() {
		this.overviewPanel = new JPanel();
		BorderLayout borderLayout = new BorderLayout();
		this.overviewPanel.setLayout(borderLayout);
		reconstructJPanel = new ReconstructJPanel(analyseTaskControl);
		this.overviewPanel.add(reconstructJPanel);
		this.overviewPanel.setSize(20, 20);
		this.getContentPane().add(this.overviewPanel, BorderLayout.CENTER);
	}
    
    @Override
    public void actionPerformed(ActionEvent clickEvent) {
    }
    
    @Override
    public void setBounds(int x, int y, int width, int height) {
    	Dimension sarSize = getMinimumSize();
    	Container c = getParent();
    	if (c != null) {
	    	int paneWidth = getParent().getWidth();
	    	int paneHeight = getParent().getHeight();
	    	super.setBounds(paneWidth - sarSize.width -1, paneHeight - sarSize.height -1, sarSize.width, sarSize.height);
    	} else {
        	super.setBounds(0, 0, sarSize.width, sarSize.height);
    	}
    }

    @Override
    public void update() {
        reloadText();
    }
    
    public void reloadText() {
        this.setTitle(localService.getTranslatedString("SoftwareArchitectureReconstruction"));
        reconstructJPanel.reload();
        this.invalidate();
        this.revalidate();
        this.repaint();
    }

}
