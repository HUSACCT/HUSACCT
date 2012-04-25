package husacct.graphics.presentation;

import husacct.graphics.task.MouseClickListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
		JMenuItem goToParentMenu = new JMenuItem("Go level up");
		goToParentMenu.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				moduleZoomOut();
			}			
		});
		menuBar.add(goToParentMenu);
		this.add(menuBar, java.awt.BorderLayout.NORTH);
	}

	private void moduleZoomOut() {
		for (MouseClickListener l : listeners) {
			l.moduleZoomOut();
		}
	}

	public void addListener(MouseClickListener listener) {
		listeners.add(listener);
	}

	public void removeListener(MouseClickListener listener) {
		listeners.remove(listener);
	}
}
