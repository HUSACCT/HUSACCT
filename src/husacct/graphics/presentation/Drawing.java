package husacct.graphics.presentation;

import husacct.graphics.abstraction.FileManager;
import husacct.graphics.presentation.decorators.ViolationsDecorator;
import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.presentation.figures.RelationFigure;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.log4j.Logger;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.QuadTreeDrawing;
import org.jhotdraw.draw.io.ImageOutputFormat;

public class Drawing extends QuadTreeDrawing {
	private static final long serialVersionUID = 3212318618672284266L;
	private Logger logger = Logger.getLogger(Drawing.class);
	public final int RELATIONS_DISTANCE = 30;
	FileManager filemanager = new FileManager();
	File selectedFile = filemanager.getFile();

	public Drawing() {
		super();
	}
	
	public void showExportToImagePanel() {
		try {
			ImageOutputFormat imageoutputformat = new ImageOutputFormat();
			JFileChooser fileChooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG", "png", "png");
			fileChooser.setFileFilter(filter);
			fileChooser.setVisible(true);
			int returnValue = fileChooser.showSaveDialog(fileChooser);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				selectedFile = fileChooser.getSelectedFile();
				filemanager.setFile(selectedFile);
				filemanager.createOutputStream();
				imageoutputformat.write(filemanager.getOutputStream(), this);
				filemanager.closeOutputStream();
			}
		} catch (IOException e) {
			logger.debug("Cannot save file to " + selectedFile.getAbsolutePath());
		}
	}

	public BaseFigure[] getShownModules() {
		ArrayList<BaseFigure> moduleFigures = new ArrayList<BaseFigure>();
		for (Figure jhotdrawfigure : getChildren()) {
			BaseFigure figure = (BaseFigure) jhotdrawfigure;
			if (figure.isModule() && !figure.getClass().getName().equals("ParentFigure")) {
				moduleFigures.add(figure);
			}
		}
		return moduleFigures.toArray(new BaseFigure[] {});
	}

	public RelationFigure[] getShownLines() {
		ArrayList<BaseFigure> moduleFigures = new ArrayList<BaseFigure>();
		for (Figure jhotdrawfigure : getChildren()) {
			BaseFigure figure = (BaseFigure) jhotdrawfigure;
			if (figure.isLine()) {
				moduleFigures.add(figure);
			}
		}
		return moduleFigures.toArray(new RelationFigure[] {});
	}

	@Override
	public boolean add(Figure figure) {
		// this triggers at least the minimum sizes
		figure.setBounds(new Point2D.Double(10, 10), new Point2D.Double(11, 11));
		return super.add(figure);
	}

	public void setFiguresNotViolated(ArrayList<BaseFigure> arrayList) {
		willChange();
		for (BaseFigure figure : arrayList) {
			figure.removeDecoratorByType(ViolationsDecorator.class);
		}
		invalidate();
		changed();
	}

	public void clearAll() {
		willChange();
		basicRemoveAllChildren();
		invalidate();
		changed();
	}

	public void clearAllLines() {
		willChange();
		BaseFigure[] lines = getShownLines();
		for (BaseFigure line : lines) {
			remove(line);
		}
		invalidate();
		changed();
	}

	public void updateLineFigureToContext() {
		RelationFigure[] figures = getShownLines();
		updateLineFigureThicknesses(figures);
		seperateOverlappingLineFigures(figures);
	}

	private void updateLineFigureThicknesses(RelationFigure[] figures) {
		if (1 == figures.length) {
			// 1 relation, small
			figures[0].setLineThickness(1);
		} else if (figures.length == 2) {
			// 2 relations; both small, or one slightly bigger
			int length1 = figures[0].getAmount();
			int length2 = figures[1].getAmount();

			if (length1 == length2) {
				figures[0].setLineThickness(1);
				figures[1].setLineThickness(1);
			} else if (length1 < length2) {
				figures[0].setLineThickness(1);
				figures[1].setLineThickness(2);
			} else { // length1 > length2
				figures[0].setLineThickness(2);
				figures[1].setLineThickness(1);
			}
		} else if (figures.length >= 3) {
			// 3 or more relations; small, big or  fat, according to scale max amounts of dependencies
			int maxAmount = -1;
			for (RelationFigure figure : figures) {
				int length = figure.getAmount();
				if (maxAmount == -1 || maxAmount < length) {
					maxAmount = length;
				}
			}

			// set line thickness according to scale
			for (RelationFigure figure : figures) {
				double weight = (double) figure.getAmount() / maxAmount;
				if (weight < 0.33) {
					figure.setLineThickness(1);
				} else if (weight < 0.66) {
					figure.setLineThickness(3);
				} else {
					figure.setLineThickness(4);
				}
			}
		}
	}

	private void seperateOverlappingLineFigures(RelationFigure[] figures) {
		HashMap<RelationFigure, Set<RelationFigure>> overlappingFigureSets = new HashMap<RelationFigure, Set<RelationFigure>>();

		for (RelationFigure figure1 : figures) {
			Figure figure1start = figure1.getStartConnector().getOwner();
			Figure figure1end = figure1.getEndConnector().getOwner();

			for (RelationFigure figure2 : figures) {
				if (figure1 == figure2) {
					continue;
				}

				Figure figure2start = figure2.getStartConnector().getOwner();
				Figure figure2end = figure2.getEndConnector().getOwner();

				if (!((figure1start == figure2start && figure1end == figure2end) || (figure1start == figure2end && figure1end == figure2start))) {
					continue;
				}

				Set<RelationFigure> addTo;
				if (overlappingFigureSets.containsKey(figure1)) {
					addTo = overlappingFigureSets.get(figure1);
				} else if (overlappingFigureSets.containsKey(figure2)) {
					addTo = overlappingFigureSets.get(figure2);
				} else {
					addTo = new HashSet<RelationFigure>();
					overlappingFigureSets.put(figure1, addTo);
				}

				addTo.add(figure1);
				addTo.add(figure2);
			}
		}

		for (RelationFigure keyFigure : overlappingFigureSets.keySet()) {
			HashSet<RelationFigure> overlappingFigures = new HashSet<RelationFigure>();
			overlappingFigures.add(keyFigure);
			overlappingFigures.addAll(overlappingFigureSets.get(keyFigure));

			double start = (0 - ((overlappingFigures.size() / 2) * this.RELATIONS_DISTANCE)) / 2;

			for (RelationFigure figure : overlappingFigures) {
				figure.setDistance(start);
				start += this.RELATIONS_DISTANCE;
			}
		}

	}
}
