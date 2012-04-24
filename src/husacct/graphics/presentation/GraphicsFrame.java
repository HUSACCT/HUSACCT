package husacct.graphics.presentation;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class GraphicsFrame extends JInternalFrame {
	private static final long serialVersionUID = -4683140198375851034L;

	private DrawingView drawingView;

	private javax.swing.JScrollPane scrollPane,propertiesPane;

	public GraphicsFrame(DrawingView drawingView) {
		this.drawingView = drawingView;
//		this.propertiesPane = new JScrollPane();
//		this.propertiesPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
//		this.propertiesPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
//		this.propertiesPane.setViewportView(drawingView);
//		this.propertiesPane.add(new JLabel("Properties"));

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
//		this.add(scrollPane, java.awt.BorderLayout.NORTH);
//		this.add(propertiesPane, java.awt.BorderLayout.SOUTH);
	}
}
