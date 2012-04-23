package husacct.analyse.presentation;
 
import java.awt.Dimension; 
import javax.swing.JFrame;

public class AnalyzeGUI extends JFrame {
	
	public static void main(String[] args) {
		   try {  
	        	javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");      
	        } catch (Exception ex) {
	        	System.out.println(ex.getStackTrace());
	        }
		AnalyzeGUI testGUI = new AnalyzeGUI();
		
	}
	
	private AnalyzeTabPanel tabPanel;
	
	public AnalyzeGUI(){
		setFrameSettings();
		addMainPanel();
		this.pack();
	} 
	
	private void addMainPanel() { 
		
		AnalyzeTabPanel tabPanel = new AnalyzeTabPanel(); 
		this.add(tabPanel);
 
	}

	private void setFrameSettings(){ 
		Dimension frameSize = new Dimension(1024,800);
		this.setPreferredSize(frameSize);
		this.setVisible(true);
		this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
	    this.setTitle("Software analysis tool");
	}
	

}
