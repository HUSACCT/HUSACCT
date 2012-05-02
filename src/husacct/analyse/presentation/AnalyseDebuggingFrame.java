package husacct.analyse.presentation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.UIManager;

public class AnalyseDebuggingFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JDesktopPane desktop;
	
	public AnalyseDebuggingFrame() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int inset = 20; 
		setBounds(inset, inset, screenSize.width - (inset *2), screenSize.height - (inset * 2));
		
		JInternalFrame theAnalyseFrame = new AnalyseInternalFrame();
		desktop = new JDesktopPane();
		desktop.add(theAnalyseFrame);
		desktop.setDragMode(JDesktopPane.LIVE_DRAG_MODE);
		desktop.setBackground(UIManager.getColor("Panel.background"));
		setContentPane(desktop);
		
		Image icon = Toolkit.getDefaultToolkit().getImage("img/husacct.png");
		setIconImage(icon);	
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout(0, 0));
		setTitle("Husacct - Analyse Application");
		setVisible(true);
	}

	public static void main(String[] args){
		new AnalyseDebuggingFrame();
	}
}
