package husacct.graphics.domain;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JViewport;

import org.apache.log4j.Logger;

import husacct.graphics.domain.figures.BaseFigure;
import husacct.graphics.domain.figures.RelationFigure;
import husacct.graphics.domain.figures.RelationType;
import husacct.graphics.domain.util.PanTool;
import husacct.graphics.presentation.UserInputListener;
import husacct.graphics.presentation.menubars.ContextMenu;

public class DrawingView extends DefaultDrawingView {
	
	private static final long					serialVersionUID	= 7276696509798039409L;
	private static final int					LeftMouseButton		= MouseEvent.BUTTON1;
	private static final int					RightMouseButton	= MouseEvent.BUTTON3;
	private static final int					DoubleClick			= 2;
	private static final int					ScrollSpeed			= 10;
	private Logger								logger				= Logger.getLogger(DrawingView.class);
	
	private final Drawing						drawing;
	private final DefaultDrawingEditor			editor;
	private final ContextMenu					contextMenu;
	protected AbstractTool						panTool;
	protected AbstractTool						selectTool;
	private boolean								isCtrlPressed		= false;
	
	private UserInputListener					inputListener; 		// Single value instead of ArrayListto prevent concurrency exceptions at zoomIn(). 
	private final HashSet<Figure>				previousSelection	= new HashSet<Figure>();
	
	public DrawingView(Drawing givenDrawing) {
		drawing = givenDrawing;
		setDrawing(drawing);
		
		editor = new DefaultDrawingEditor();
		editor.add(this);
		
		contextMenu = new ContextMenu();
		
		initializeSelectionTool();
		editor.setTool(selectTool);
		
		initializeKeyListener();
		initializeMouseListener();
		initializeSelectionListener();
	}
	
	public void addListener(UserInputListener listener) {
		inputListener = listener; 			// GraphicsPresentationController
		contextMenu.addListener(listener);
	}
	
	public void cannotZoomOut() {
		contextMenu.setCanZoomout(false);
	}
	
	public void canZoomOut() {
		contextMenu.setCanZoomout(true);
	}
	
	public void drawingZoomChanged(double zoomFactor) {
		inputListener.zoomSliderSetZoomFactor(zoomFactor);
		inputListener.zoomFactorChanged(zoomFactor);
	}
	
	public Drawing getDrawingHusacct() {
		return (Drawing) getDrawing();
	}
	
	private Set<Figure> getDeltaSelection() {
		HashSet<Figure> deltaSelection = new HashSet<Figure>();
		Set<Figure> selection = getSelectedFigures();
		
		for (Figure f : previousSelection)
			if (!selection.contains(f)) deltaSelection.add(f);
		return Collections.unmodifiableSet(deltaSelection);
	}
	
	private void handleDeselect() {
		Set<Figure> deselectedFigures = getDeltaSelection();
		if (deselectedFigures.size() > 0) {
			BaseFigure[] deselection = new BaseFigure[deselectedFigures.size()];
			deselection = deselectedFigures.toArray(deselection);
			for (BaseFigure figure : deselection)
				figure.resetLayer();
		}
	}
	
	private boolean hasSelection() {
		return getSelectedFigures().size() > 0;
	}
	
	public void hideSelectedFigures() {
		Set<Figure> selection = getSelectedFigures();
		drawing.hideSelectedFigures(selection);
		clearSelection();
		contextMenu.setHasHiddenFigures(drawing.hasHiddenFigures());
	}
	
	private void initializeKeyListener() {
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 17) isCtrlPressed = true;
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				isCtrlPressed = false;
			}
		});
	}
	
	private void initializeMouseListener() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				DrawingView.this.onMouseClicked(e);
			}
		});
		
		addMouseWheelListener((MouseWheelListener) e -> DrawingView.this.onMouseScrolled(e));
	}
	
	public void initializePanTool(JViewport viewport, JComponent comp) {
		panTool = new PanTool(viewport, comp);
	}
	
	private void initializeSelectionListener() {
		addFigureSelectionListener(new FigureSelectionListener() {
			@Override
			public void selectionChanged(FigureSelectionEvent evt) {
				DrawingView.this.onSelectionChanged(evt);
			}
		});
	}
	
	private void initializeSelectionTool() {
		selectTool = new SelectionTool();
	}
	
	private void zoomIn() {
		try{
			inputListener.zoomIn();
		}
		catch (Exception e){
			logger.error(" Exception: " + e);
		}
	}
	
	private void onMouseClicked(MouseEvent e) {
		int mouseButton = e.getButton();
		int mouseClicks = e.getClickCount();
		try{
			handleDeselect();
			if (mouseButton == LeftMouseButton && hasSelection()) {
				BaseFigure[] selection = toFigureArray(getSelectedFigures());
				if (mouseClicks == DoubleClick) 
					zoomIn();
				else
					for (BaseFigure figure : selection)
						figure.raiseLayer();
			} else if (mouseButton == RightMouseButton) 
				contextMenu.show(this, e.getX(), e.getY());
			previousSelection.clear();
			previousSelection.addAll(getSelectedFigures());
		} catch (Exception exc){
			logger.error(" Exception " + exc +" In AWT on button: " + mouseButton + "or click: " + mouseClicks);
			//exc.printStackTrace();
		}
	}
	
	private void onMouseScrolled(MouseWheelEvent e) {
		try{
			if (isCtrlPressed) {
				requestFocus();
				double wheelRotation = e.getWheelRotation() * -1;
				double wheelRotationFactor = wheelRotation / ScrollSpeed;
				double scaleFactor = getScaleFactor() + wheelRotationFactor;
				drawingZoomChanged(scaleFactor);
			}
		} catch (Exception exc){
			logger.error(" Exception in handling mouse scroll: " + exc);
		}
	}
	
	protected void onSelectionChanged(FigureSelectionEvent evt) {
		handleDeselect();
		if (hasSelection()) {
			BaseFigure[] selection = toFigureArray(getSelectedFigures());
			for (BaseFigure selectedFig : selection){
				drawing.bringToFront(selectedFig);
			}
			// Determine if properties should be displayed or hidden.
			BaseFigure selectedFigure = selection[0];
			if (selectedFigure.isLine()) {
				RelationFigure line = (RelationFigure) selectedFigure;
				RelationType relationType = line.getRelationType();
				if (relationType == RelationType.VIOLATION){
					inputListener.propertiesPaneShowViolations(selectedFigure);
				} else if (relationType == RelationType.DEPENDENCY) {
					inputListener.propertiesPaneShowDependencies(selectedFigure);
				} else if(relationType == RelationType.RULELINK){
					inputListener.propertiesPaneShowRules(selectedFigure);
				} else if(RelationType.isUmlLink(relationType.get())){
					inputListener.propertiesPaneShowUmlLinks(selectedFigure);
				}else{
					// TODO handle other cases of relation type
				}
			} else if(selectedFigure.isModule() || selectedFigure.isParent()) {
				inputListener.propertiesPaneShowRules(selectedFigure);
			}else {
			
				inputListener.propertiesPaneHide();
			}
		}
		contextMenu.setHasSelection(hasSelection());
	}
	
	public void removeListeners() {
		inputListener = null;
		contextMenu.removeListener();
	}
	
	public void removePanTool() {
		panTool = null;
	}
	
	public void restoreHiddenFigures() {
		drawing.restoreHiddenFigures();
		contextMenu.setHasHiddenFigures(false);
	}
	
	public void setHasHiddenFigures(boolean setting) {
		contextMenu.setHasHiddenFigures(setting);
		contextMenu.setHasSelection(false);
	}
	
	public BaseFigure[] toFigureArray(Collection<Figure> collection) {
		return collection.toArray(new BaseFigure[collection.size()]);
	}
	
	public void usePanTool() {
		if (panTool != null) {
			getEditor().setTool(panTool);
		}
	}
	
	public void useSelectTool() {
		getEditor().setTool(selectTool);
	}
}
