package husacct.analyse.presentation;
 
import java.awt.Dimension; 
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

public class AnalyzeGUI extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private AnalyzeTabPanel tabPanel;
	
	public AnalyzeGUI(){
		setFrameSettings();
		addMainPanel();
		this.pack();
	} 
	
	private void addMainPanel() { 
		AnalyzeTabPanel tabPanel = new AnalyzeTabPanel(); 
		JInternalFrame container = new JInternalFrame();
		container.setVisible(true);
		container.add(tabPanel);
		container.setTitle("Software Analysis Tool");
		container.setPreferredSize(new Dimension(300,300));
		this.add(container);
	}

	private void setFrameSettings(){ 
		Dimension frameSize = new Dimension(1124,800);
		this.setPreferredSize(frameSize);
		this.setVisible(true);
		this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		this.setTitle("HUSACCT Debugging Front End: Analyse Application");
	}
	

}
