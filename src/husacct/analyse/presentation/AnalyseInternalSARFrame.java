package husacct.analyse.presentation;

import husacct.ServiceProvider;
import husacct.common.help.presentation.HelpableJInternalFrame;
import husacct.common.services.IServiceListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AnalyseInternalSARFrame extends HelpableJInternalFrame implements ActionListener, IServiceListener {

    private static final long serialVersionUID = 1L;

    public AnalyseInternalSARFrame() {
        registerLocaleChangeListener();
        
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
}
