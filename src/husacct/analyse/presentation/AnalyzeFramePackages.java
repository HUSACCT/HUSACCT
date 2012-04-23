package husacct.analyse.presentation;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class AnalyzeFramePackages extends JFrame{ 
	public AnalyzeFramePackages(){
		setFrameSettings();
		addTextArea();
		this.pack();
	} 
	private void addTextArea() {
		JTextArea jtaFrame = new JTextArea(100,100);
		JScrollPane jScrollPaneTextArea = new JScrollPane(jtaFrame);
		jScrollPaneTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(jScrollPaneTextArea);
	} 
	private void setFrameSettings(){ 
		Dimension frameSize = new Dimension(800,600);
		this.setPreferredSize(frameSize);
		this.setVisible(true);
		this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
	    this.setTitle("Show All Packages");
	}
	
}
