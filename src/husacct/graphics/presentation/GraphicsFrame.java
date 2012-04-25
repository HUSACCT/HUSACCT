package husacct.graphics.presentation;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class GraphicsFrame extends JInternalFrame {
	private static final long serialVersionUID = -4683140198375851034L;

	private DrawingView drawingView;
	
	private JMenuBar menuBar;

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

		
		createMenuBar();
		this.add(menuBar, java.awt.BorderLayout.NORTH);
	}
	
	private void createMenuBar(){
		this.menuBar = new JMenuBar();
		
		this.menuBar = new javax.swing.JMenuBar();
		javax.swing.JMenu nmenu = new javax.swing.JMenu("North menu");
		nmenu.addMenuListener(new MenuListener(){
			public void menuSelected(MenuEvent e){
				controller.
			}

			@Override
			public void menuCanceled(MenuEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void menuDeselected(MenuEvent arg0) {
				// TODO Auto-generated method stub
				
			}			
		});
		menuBar.add(nmenu);
	}
}
