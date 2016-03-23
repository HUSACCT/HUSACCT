package husacct.analyse.presentation;

import husacct.ServiceProvider;
import husacct.analyse.presentation.jpanel.DefinitionJPanel;
import husacct.common.help.presentation.HelpableJInternalFrame;
import husacct.common.services.IServiceListener;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

public class AnalyseInternalSARFrame extends HelpableJInternalFrame implements ActionListener, IServiceListener {

    private static final long serialVersionUID = 1L;
	private JPanel overviewPanel;

    public AnalyseInternalSARFrame() {
        registerLocaleChangeListener();
        initUI();
    }

    private void registerLocaleChangeListener() {
        ServiceProvider.getInstance().getLocaleService().addServiceListener(this);
    }

    public void reloadText() {
        this.setTitle("Software Architectur Reconstruction"); // Translate, original: controller.translate("AnalysedWindowTitle")
        this.invalidate();
        this.revalidate();
        this.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent clickEvent) {
    }

    @Override
    public void update() {
        reloadText();
    }
    
	private void addDefinitionPanel() {
		this.overviewPanel = new JPanel();
		BorderLayout borderLayout = new BorderLayout();
		this.overviewPanel.setLayout(borderLayout);
		this.overviewPanel.add(new DefinitionJPanel());
		this.overviewPanel.setSize(20, 20);
		this.getContentPane().add(this.overviewPanel, BorderLayout.CENTER);
	}
    
    public void initUI(){
    	this.addDefinitionPanel();
    }
}
