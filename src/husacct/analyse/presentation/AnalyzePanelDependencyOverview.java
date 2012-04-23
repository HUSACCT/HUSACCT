package husacct.analyse.presentation;
import java.awt.Dimension;
import java.awt.GridBagLayout; 
import javax.swing.JButton;
import javax.swing.JPanel; 

public class AnalyzePanelDependencyOverview extends JPanel { 
	private static final long serialVersionUID = 1L;
	private JPanel selectionPanel;
	public JButton showPackages = new JButton("showPackages");
	public JButton showClasses = new JButton("showClasses");
	public JButton showVariables = new JButton("showVariables");
	public JButton showMethods = new JButton("showMethods");
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
		this.setVisible(true);  
	}
	
	private void addStartComponents() { 
 		selectionPanel = new JPanel();
		selectionPanel.setLayout(new GridBagLayout());
		selectionPanel.add(showPackages);
		selectionPanel.add(showClasses);
		selectionPanel.add(showVariables);
		selectionPanel.add(showMethods);
		selectionPanel.add(showAttributes);
		selectionPanel.add(showInvocation);
		selectionPanel.add(showAssociation); 
		this.add(selectionPanel);
	}
	
	

}