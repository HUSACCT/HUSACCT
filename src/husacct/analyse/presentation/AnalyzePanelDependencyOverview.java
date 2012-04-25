package husacct.analyse.presentation;
import java.awt.Dimension;
import java.awt.GridBagLayout; 
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel; 

public class AnalyzePanelDependencyOverview extends JInternalFrame { 
	private static final long serialVersionUID = 1L;
	private JPanel selectionPanel;
	public JButton getRootDependButton = new JButton("getRootModules()");
	public JButton getDependencyFrom = new JButton("getDependencyFrom(from)");
	public JButton getDependencyFromTo = new JButton("getDependency(from, to)");
	public JButton getDependencyTo = new JButton("getDependencyTo(to)");
	public JButton showAttributes = new JButton("showAttributes");
	public JButton showInvocation = new JButton("showInvocation");
	public JButton showAssociation = new JButton("showAssociation");
	 
	public AnalyzePanelDependencyOverview(){
		setContainerPanelSettings();
		addStartComponents();
	} 
	
	public void setContainerPanelSettings(){
		Dimension containerPanelSize = new Dimension(900,700);
 		this.setPreferredSize(containerPanelSize);
 		this.setTitle("Analyse Services");
		this.setVisible(true);  
	}
	
	private void addStartComponents() { 
 		selectionPanel = new JPanel();
		selectionPanel.setLayout(new GridBagLayout());
		selectionPanel.add(getRootDependButton);
		selectionPanel.add(getDependencyFrom);
		selectionPanel.add(getDependencyFromTo);
		selectionPanel.add(getDependencyTo);
//		selectionPanel.add(showAttributes);
//		selectionPanel.add(showInvocation);
//		selectionPanel.add(showAssociation); 
//		
		this.add(selectionPanel);
	}
	
	

}