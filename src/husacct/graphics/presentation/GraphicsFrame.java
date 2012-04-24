package husacct.graphics.presentation;

import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;

public class GraphicsFrame extends JInternalFrame {
	private static final long serialVersionUID = -4683140198375851034L;

	private DrawingView drawingView;
	
	private JMenuBar menuBar;

	private javax.swing.JScrollPane scrollPane;

	public GraphicsFrame(DrawingView drawingView) {
		this.drawingView = drawingView;

		initializeComponents();
	}

	private void initializeComponents() {

		scrollPane = new JScrollPane();
		scrollPane
				.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane
				.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setViewportView(drawingView);

		this.setLayout(new java.awt.BorderLayout());
		this.add(scrollPane, java.awt.BorderLayout.CENTER);
		
		createMenuBar();
		this.add(menuBar, java.awt.BorderLayout.NORTH);
	}
	
	private void createMenuBar(){
		this.menuBar = new JMenuBar();
		
		this.menuBar = new javax.swing.JMenuBar();
		javax.swing.JMenu nmenu = new javax.swing.JMenu("North menu");
		javax.swing.JMenuItem item = new javax.swing.JMenuItem("Level up");
		nmenu.add(item);
		menuBar.add(nmenu);
	}
}
