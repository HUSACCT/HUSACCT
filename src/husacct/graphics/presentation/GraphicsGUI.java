//package husacct.graphics.presentation;
//
//import java.util.ArrayList;
//
//import husacct.common.dto.ViolationDTO;
//import husacct.graphics.task.MouseClickListener;
//import husacct.graphics.task.figures.AbstractFigure;
//import husacct.graphics.task.figures.IViolatedFigure;
//import husacct.graphics.task.figures.ModuleFigure;
//import husacct.graphics.task.figures.RelationFigure;
//
//import javax.swing.JInternalFrame;
//
//public abstract class GraphicsGUI {
//	private ArrayList<MouseClickListener> guiListeners;
//
//	public GraphicsGUI() {
//		this.guiListeners = new ArrayList<MouseClickListener>();
//	}
//
//	public final void addGUIListener(MouseClickListener listener) {
//		this.guiListeners.add(listener);
//	}
//
//	public final void fireModuleFigureZoom(ModuleFigure moduleFigure) {
//		for (MouseClickListener l : guiListeners) {
//			l.onModuleFigureZoom(moduleFigure);
//		}
//	}
//
//	public final void fireViolatedFigureSelect(IViolatedFigure violatedFigure) {
//		for (MouseClickListener l : guiListeners) {
//			l.onViolatedFigureSelect(violatedFigure);
//		}
//	}
//
//	public abstract JInternalFrame getGUI();
//
//	public abstract ModuleFigure[] getShownModuleFigures();
//
//	public abstract java.awt.Image getImage(String type);
//
//	public abstract void add(AbstractFigure figure);
//
//	public abstract void addRelation(RelationFigure relation,
//			ModuleFigure from, ModuleFigure to);
//
//	public abstract void showViolations(ViolationDTO[] violations);
//}
