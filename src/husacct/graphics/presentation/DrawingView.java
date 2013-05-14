package husacct.graphics.presentation;

import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.presentation.menubars.ContextMenu;
import husacct.graphics.util.UserInputListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.swing.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.log4j.Logger;
import org.jhotdraw.draw.DefaultDrawingEditor;
import org.jhotdraw.draw.DefaultDrawingView;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.event.FigureSelectionEvent;
import org.jhotdraw.draw.event.FigureSelectionListener;
import org.jhotdraw.draw.io.ImageOutputFormat;
import org.jhotdraw.draw.tool.SelectionTool;

public class DrawingView extends DefaultDrawingView {

	private static final long serialVersionUID = 7276696509798039409L;
	private static final int LeftMouseButton = MouseEvent.BUTTON1;
	private static final int RightMouseButton = MouseEvent.BUTTON3;
	private static final int DoubleClick = 2;
	private static final int ScrollSpeed = 10;

	private final Drawing drawing;
	private final DefaultDrawingEditor editor;
	private final ContextMenu contextMenu;
	private SelectionTool selectionTool;
	private boolean isCtrlPressed = false;

	private final ArrayList<UserInputListener> listeners = new ArrayList<UserInputListener>();
	private final HashSet<Figure> previousSelection = new HashSet<Figure>();

	public DrawingView(Drawing givenDrawing) {
		this.drawing = givenDrawing;
		this.setDrawing(this.drawing);

		this.editor = new DefaultDrawingEditor();
		this.editor.add(this);

		this.contextMenu = new ContextMenu();

		this.initializeSelectionTool();
		this.initializeKeyListener();
		this.initializeMouseListener();
		this.initializeSelectionListener();
	}

	public void addListener(UserInputListener listener) {
		this.listeners.add(listener);
		this.contextMenu.addListener(listener);
	}

	public void cannotZoomOut() {
		this.contextMenu.setCanZoomout(false);
	}

	public void canZoomOut() {
		this.contextMenu.setCanZoomout(true);
	}

	public void drawingZoomChanged(double zoomFactor) {
		for (UserInputListener listener : this.listeners) {
			listener.setZoomSlider(zoomFactor);
			listener.drawingZoomChanged(zoomFactor);
		}
	}

	private void figureDeselected(BaseFigure[] figures) {
		for (UserInputListener l : this.listeners) {
			l.figureDeselected(figures);
		}
	}

	private void figureSelected(BaseFigure[] figures) {
		for (UserInputListener l : this.listeners) {
			l.figureSelected(figures);
		}
	}

	private Set<Figure> getDeltaSelection() {
		HashSet<Figure> deltaSelection = new HashSet<Figure>();
		Set<Figure> selection = this.getSelectedFigures();

		for (Figure f : this.previousSelection) {
			if (!selection.contains(f)) {
				deltaSelection.add(f);
			}
		}
		return Collections.unmodifiableSet(deltaSelection);
	}
	

	private void handleDeselect() {
		Set<Figure> deselectedFigures = this.getDeltaSelection();
		if (deselectedFigures.size() > 0) {
			BaseFigure[] deselection = new BaseFigure[deselectedFigures.size()];
			deselection = deselectedFigures.toArray(deselection);

			this.figureDeselected(deselection);
			for (BaseFigure figure : deselection) {
				figure.resetLayer();
			}
		}
	}

	private boolean hasSelection() {
		return this.getSelectedFigures().size() > 0;
	}

	public void hideSelectedFigures() {
		Set<Figure> selection = this.getSelectedFigures();
		this.drawing.hideSelectedFigures(selection);
		this.clearSelection();
		this.contextMenu.setHasHiddenFigures(this.drawing.hasHiddenFigures());
	}

	private void initializeKeyListener() {
		this.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 17) {
					DrawingView.this.isCtrlPressed = true;
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				DrawingView.this.isCtrlPressed = false;
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// Not needed
			}
		});
	}

	private void initializeMouseListener() {
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				DrawingView.this.onMouseClicked(e);
			}
		});
		this.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				DrawingView.this.onMouseScrolled(e);
			}
		});
	}

	private void initializeSelectionListener() {
		this.addFigureSelectionListener(new FigureSelectionListener() {
			@Override
			public void selectionChanged(FigureSelectionEvent evt) {
				DrawingView.this.onSelectionChanged(evt);
			}
		});
	}

	private void initializeSelectionTool() {
		this.selectionTool = new SelectionTool();
		this.editor.setTool(this.selectionTool);
	}

	private void moduleZoom(BaseFigure[] fig) {
		for (UserInputListener listener : this.listeners) {
			listener.moduleZoom(fig);
		}
		this.requestFocus();
	}

	private void onMouseClicked(MouseEvent e) {
		int mouseButton = e.getButton();
		int mouseClicks = e.getClickCount();

		this.handleDeselect();
		if (mouseButton == LeftMouseButton && this.hasSelection()) {
			BaseFigure[] selection = this.toFigureArray(this
					.getSelectedFigures());

			if (mouseClicks == DoubleClick) {
				this.moduleZoom(selection);
			} else {
				for (BaseFigure figure : selection) {
					figure.raiseLayer();
				}
			}
		} else if (mouseButton == RightMouseButton) {
			this.contextMenu.show(this, e.getX(), e.getY());
		}

		this.previousSelection.clear();
		this.previousSelection.addAll(this.getSelectedFigures());
	}

	protected void onSelectionChanged(FigureSelectionEvent evt) {
		this.handleDeselect();
		if (this.hasSelection()) {
			BaseFigure[] selection = this.toFigureArray(this
					.getSelectedFigures());
			for (BaseFigure selectedFig : selection) {
				this.drawing.bringToFront(selectedFig);
			}
			this.figureSelected(selection);
		}
		this.contextMenu.setHasSelection(this.hasSelection());
	}
	
	private void onMouseScrolled(MouseWheelEvent e) {
		if(isCtrlPressed){
			requestFocus();
			double wheelRotation = e.getWheelRotation() * -1;
			double wheelRotationFactor = wheelRotation / ScrollSpeed;
			double scaleFactor = this.getScaleFactor() + wheelRotationFactor;
			drawingZoomChanged(scaleFactor);
		}
	}

	public void removeListener(UserInputListener listener) {
		this.listeners.remove(listener);
		this.contextMenu.removeListener(listener);
	}

	public void restoreHiddenFigures() {
		this.drawing.restoreHiddenFigures();
		this.contextMenu.setHasHiddenFigures(false);
	}

	public void setHasHiddenFigures(boolean setting) {
		this.contextMenu.setHasHiddenFigures(setting);
		this.contextMenu.setHasSelection(false);
	}

	private BaseFigure[] toFigureArray(Collection<Figure> collection) {
		BaseFigure[] retVal = new BaseFigure[collection.size()];
		retVal = collection.toArray(retVal);
		return retVal;
	}
	
	public void showExternalSystems(){
		String[] columnNames ={"External systems"};
		Object[] externalSystems = this.analyseService.getExternalSystems();
			
		final JTable externalSystemsTable = new JTable(data, columnNames);
		
		externalSystemsTable.setFont(new Font("Sans-Serif", Font.PLAIN, 10));
		JScrollPane scrollPane = new JScrollPane(externalSystemsTable);		
		scrollPane.setPreferredSize(new Dimension(450, 200));
							
		JOptionPane.showMessageDialog(parent, scrollPane, "Externals systems", JOptionPane.PLAIN_MESSAGE);
	}
}
