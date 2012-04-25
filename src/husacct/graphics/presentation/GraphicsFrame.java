package husacct.graphics.presentation;

import husacct.graphics.task.MouseClickListener;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class GraphicsFrame extends JInternalFrame {
	private static final long serialVersionUID = -4683140198375851034L;

	private DrawingView drawingView;
	private JMenuBar menuBar;
	private javax.swing.JScrollPane scrollPane,propertiesPane;
	
	private ArrayList<MouseClickListener> listeners = new ArrayList<MouseClickListener>();

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
	}
	
	private void createMenuBar(){
		this.menuBar = new JMenuBar();
		this.menuBar.setSize(200,20);
		int menuItemMaxWidth = 120;
		int menuItemMaxHeight = 45;
		JMenuItem goToParentMenu = new JMenuItem("Level up");
		goToParentMenu.setSize(50,20);
		goToParentMenu.setMaximumSize(new Dimension(70,menuItemMaxHeight));
		goToParentMenu.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				moduleZoomOut();
			}
		});
		menuBar.add(goToParentMenu);
		
		JMenuItem exportToImageMenu = new JMenuItem("Export to image");
		exportToImageMenu.setSize(50,20);
		exportToImageMenu.setMaximumSize(new Dimension(menuItemMaxWidth,menuItemMaxHeight));
		exportToImageMenu.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				exportToImage();
			}
		});
		menuBar.add(exportToImageMenu);
		
		JCheckBoxMenuItem showViolationsOptionMenu = new JCheckBoxMenuItem("Show violations");
		showViolationsOptionMenu.setSize(50,20);
		showViolationsOptionMenu.setMaximumSize(new Dimension(menuItemMaxWidth,menuItemMaxHeight));
		showViolationsOptionMenu.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleViolations();
			}
		});
		menuBar.add(showViolationsOptionMenu);
		
		this.add(menuBar, java.awt.BorderLayout.NORTH);
	}

	private void moduleZoomOut() {
		for (MouseClickListener l : listeners) {
			l.moduleZoomOut();
		}
	}
	
	private void exportToImage() {
		for (MouseClickListener l : listeners) {
			l.exportToImage();
		}
	}
	
	private void toggleViolations(){
		for (MouseClickListener l : listeners) {
			l.toggleViolations();
		}
	}

	public void addListener(MouseClickListener listener) {
		listeners.add(listener);
	}

	public void removeListener(MouseClickListener listener) {
		listeners.remove(listener);
	}
}
