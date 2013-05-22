package husacct.graphics.presentation;

import husacct.graphics.abstraction.FileManager;
import husacct.graphics.presentation.decorators.ViolationsDecorator;
import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.presentation.figures.RelationFigure;
import husacct.graphics.presentation.linelayoutstrategies.ConnectorLineSeparationStrategy;
import husacct.graphics.presentation.linelayoutstrategies.ILineSeparationStrategy;
import husacct.graphics.util.FigureMap;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.log4j.Logger;
import org.jhotdraw.draw.ConnectionFigure;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.QuadTreeDrawing;
import org.jhotdraw.draw.io.ImageOutputFormat;

public class Drawing extends QuadTreeDrawing {
	private static final long serialVersionUID = 3212318618672284266L;
	private final ArrayList<BaseFigure> hiddenFigures;
	private final Logger logger = Logger.getLogger(Drawing.class);
	private final FileManager filemanager = new FileManager();
	private File selectedFile = filemanager.getFile();
	private FigureMap figureMap;

	public Drawing() {
		super();
		hiddenFigures = new ArrayList<BaseFigure>();
	}

	@Override
	public boolean add(Figure figure) {
		// This triggers the minimum sizes
		figure.setBounds(new Point2D.Double(10, 10), new Point2D.Double(11, 11));
		return super.add(figure);
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
		for (BaseFigure line : lines)
			remove(line);
		invalidate();
		changed();
	}

	public RelationFigure[] getShownLines() {
		ArrayList<BaseFigure> moduleFigures = new ArrayList<BaseFigure>();
		for (Figure jhotdrawfigure : this.getChildren()) {
			BaseFigure figure = (BaseFigure) jhotdrawfigure;
			if (figure.isLine()) moduleFigures.add(figure);
		}
		return moduleFigures.toArray(new RelationFigure[] {});
	}

	public BaseFigure[] getShownModules() {
		ArrayList<BaseFigure> moduleFigures = new ArrayList<BaseFigure>();
		for (Figure jhotdrawfigure : this.getChildren()) {
			BaseFigure figure = (BaseFigure) jhotdrawfigure;
			if (!figure.isLine() && !figure.isParent()) moduleFigures.add(figure);
		}
		return moduleFigures.toArray(new BaseFigure[] {});
	}

	public boolean hasHiddenFigures() {
		return hiddenFigures.size() > 0;
	}

	public void hideSelectedFigures(Set<Figure> selection) {
		List<Figure> figures = this.getChildren();
		for (Figure figure : figures) {
			BaseFigure bf = (BaseFigure) figure;
			if (!bf.isLine()) {
				if (selection.contains(bf)) {
					bf.setEnabled(false);
					hiddenFigures.add(bf);
				}
			} else {
				ConnectionFigure cf = (ConnectionFigure) figure;
				if (selection.contains(cf.getStartFigure())
						|| selection.contains(cf.getEndFigure())) {
					bf.setEnabled(false);
					hiddenFigures.add(bf);
				}
			}
		}
	}

	public void restoreHiddenFigures() {
		for (BaseFigure figure : hiddenFigures)
			figure.setEnabled(true);
		hiddenFigures.clear();
	}

	private void seperateOverlappingLineFigures(
			ILineSeparationStrategy strategy, RelationFigure[] figures) {
		HashMap<RelationFigure, Set<RelationFigure>> overlappingFigureSets = new HashMap<RelationFigure, Set<RelationFigure>>();

		for (RelationFigure figure1 : figures) {
			Figure figure1start = figure1.getStartConnector().getOwner();
			Figure figure1end = figure1.getEndConnector().getOwner();

			for (RelationFigure figure2 : figures) {
				if (figure1 == figure2) continue;

				Figure figure2start = figure2.getStartConnector().getOwner();
				Figure figure2end = figure2.getEndConnector().getOwner();

				if (figure1start == figure2start && figure1end == figure2end
						|| figure1start == figure2end
						&& figure1end == figure2start) break;

				Set<RelationFigure> addTo;
				if (overlappingFigureSets.containsKey(figure1)) addTo = overlappingFigureSets.get(figure1);
				else if (overlappingFigureSets.containsKey(figure2)) addTo = overlappingFigureSets.get(figure2);
				else {
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

			strategy.separateLines(overlappingFigures);
		}

	}

	public void setFigureMap(FigureMap map) {
		figureMap = map;
	}

	public void setFiguresNotViolated(ArrayList<BaseFigure> arrayList) {
		willChange();
		for (BaseFigure figure : arrayList)
			figure.removeDecoratorByType(ViolationsDecorator.class);
		invalidate();
		changed();
	}

	public void showExportToImagePanel() {
		try {
			ImageOutputFormat imageoutputformat = new ImageOutputFormat();
			JFileChooser fileChooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG",
					"png", "png");
			fileChooser.setFileFilter(filter);
			fileChooser.setVisible(true);
			int returnValue = fileChooser.showSaveDialog(fileChooser);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				selectedFile = fileChooser.getSelectedFile();
				filemanager.setFile(selectedFile);
				filemanager.createOutputStream();
				QuadTreeDrawing cloneDrawing = clone();
				for (BaseFigure bf : hiddenFigures)
					cloneDrawing.remove(bf);
				imageoutputformat.write(filemanager.getOutputStream(),
						cloneDrawing);
				filemanager.closeOutputStream();

			}
		} catch (IOException e) {
			logger.debug("Cannot save file to "
					+ selectedFile.getAbsolutePath());
		}

	}

	private void updateLineFigureThicknesses(RelationFigure[] figures) {
		int maxAmount = figureMap.getMaxAll();
		for (RelationFigure figure : figures) {
			double weight = (double) figure.getAmount() / maxAmount;
			if (weight < 0.25) figure.setLineThickness(1);
			else if (weight < 0.50) figure.setLineThickness(2);
			else if (weight < 0.75) figure.setLineThickness(4);
			else
				figure.setLineThickness(5);
		}
	}

	public void updateLineFigureToContext() {
		RelationFigure[] figures = getShownLines();
		updateLineFigureThicknesses(figures);
		seperateOverlappingLineFigures(
				new ConnectorLineSeparationStrategy(), figures);
	}

	public void updateLines() {
		RelationFigure[] lines = getShownLines();
		for (RelationFigure line : lines)
			line.updateConnection();
	}
}
