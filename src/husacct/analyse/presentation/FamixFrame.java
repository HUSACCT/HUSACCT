package husacct.analyse.presentation;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class FamixFrame extends JFrame {

 
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		FamixFrame ff = new FamixFrame();
		
		ff.setContentArea("SET DISZ");
	}
	
	
	private JTextArea taFamixOutput;
	
	public FamixFrame (){
		 maakGUI();
	}


	private void maakGUI() {
		
		taFamixOutput = new JTextArea();
		//taFamixOutput.setSize(new Dimension(1000,700));
		taFamixOutput.setVisible(true);
		
		JScrollPane sbrText2 = new JScrollPane(taFamixOutput);
		sbrText2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		
		 
		this.add(sbrText2);
		this.setSize(1200,800);
		this.setVisible(true);
		
		
	}
	
	
	public void setContentArea(String inputTekst){
		
		  
		taFamixOutput.setText(inputTekst);
	}
	
	
	
	

}
