package husacct.analyse.presentation;
import java.awt.Dimension;  
import javax.swing.JPanel; 
import javax.swing.JScrollPane;
import javax.swing.JTree; 

public class AnalyzePanelNavigator extends JPanel  { 
  	private static final long serialVersionUID = 1L;
	public JTree analyzedCodeTree;
	public JScrollPane jScrollPaneTree;
	
	public AnalyzePanelNavigator(){
		setContainerPanelSettings();
		addNavigatorComponents();
	} 
	
	public void setContainerPanelSettings(){
		Dimension containerPanelSize = new Dimension(900,900);
		this.setPreferredSize(containerPanelSize);
		this.setVisible(true);  
	}
	
	private void addNavigatorComponents() {  
		analyzedCodeTree = new JTree(); 
		analyzedCodeTree.setPreferredSize(new Dimension(850, 700)); 
		
		jScrollPaneTree = new JScrollPane(analyzedCodeTree);
		jScrollPaneTree.setPreferredSize(new Dimension(850, 700));
		jScrollPaneTree.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);		  
		this.add(jScrollPaneTree); 
	} 
}