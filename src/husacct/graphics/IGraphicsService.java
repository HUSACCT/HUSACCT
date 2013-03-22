package husacct.graphics;

import husacct.common.services.IObservableService;

import javax.swing.JInternalFrame;

public interface IGraphicsService extends IObservableService {

    JInternalFrame getAnalysedArchitectureGUI();

    JInternalFrame getDefinedArchitectureGUI();

    void drawAnalysedArchitecture();

    void drawAnalysedArchitectureWithViolations();

    void drawDefinedArchitecture();

    void drawDefinedArchitectureWithViolations();
}
