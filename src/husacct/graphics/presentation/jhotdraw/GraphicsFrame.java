package husacct.graphics.presentation.jhotdraw;


import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;

public class GraphicsFrame extends JInternalFrame {	
	private static final long serialVersionUID = -4683140198375851034L;

	private DrawingView drawingView;
	
	private javax.swing.JScrollPane scrollPane;
	
	public GraphicsFrame(DrawingView drawingView)
	{
		this.drawingView = drawingView;
		
		initializeComponents();
	}

	private void initializeComponents() {
		
        scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setViewportView(drawingView);
        
        this.setLayout(new java.awt.BorderLayout());
        this.add(scrollPane, java.awt.BorderLayout.CENTER);
	}
}
