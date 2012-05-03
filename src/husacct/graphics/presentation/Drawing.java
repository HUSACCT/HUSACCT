package husacct.graphics.presentation;

import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.presentation.figures.RelationFigure;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import org.apache.log4j.Logger;
import org.jhotdraw.draw.DefaultDrawing;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.io.ImageOutputFormat;

public class Drawing extends DefaultDrawing {
	private static final long serialVersionUID = 3212318618672284266L;
	private Logger logger = Logger.getLogger(Drawing.class);

	public Drawing() {
		super();
	}
	
	public void showExportToImagePanel(){
		File selectedFile = new File(".");
		try {
			ImageOutputFormat imageoutputformat = new ImageOutputFormat();
			JFileChooser fileChooser =  new JFileChooser();
			fileChooser.setVisible(true);
			int returnValue = fileChooser.showSaveDialog(fileChooser);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				//TODO: Move to appropriate FileManager, possible?
				selectedFile = fileChooser.getSelectedFile();
				FileOutputStream fileoutputstream = new FileOutputStream(selectedFile);
				imageoutputformat.write(fileoutputstream,this);
				fileoutputstream.close();
	        }
		} catch (IOException e) {
			logger.debug("Cannot save file to "+selectedFile.getAbsolutePath());
		}
	}

	public BaseFigure[] getShownModules() {
		ArrayList<BaseFigure> moduleFigures = new ArrayList<BaseFigure>();

		for (Figure jhotdrawfigure : this.getChildren()) {
			BaseFigure figure = (BaseFigure) jhotdrawfigure;
			if(figure.isModule()){
				moduleFigures.add(figure);
			}
		}
		return moduleFigures.toArray(new BaseFigure[] {});
	}
	
	public RelationFigure[] getShownLines() {
		ArrayList<BaseFigure> moduleFigures = new ArrayList<BaseFigure>();

		for (Figure jhotdrawfigure : this.getChildren()) {
			BaseFigure figure = (BaseFigure) jhotdrawfigure;
			if(figure.isLine()){
				moduleFigures.add(figure);
			}
		}
		return moduleFigures.toArray(new RelationFigure[] {});
	}

	@Override
	public boolean add(Figure f) {
		// this triggers at least the minimum sizes
		f.setBounds(new Point2D.Double(10, 10), new Point2D.Double(11, 11));

		// TODO implement layout mechanism here

		return super.add(f);
	}
	
	public void setFiguresNotViolated(ArrayList<BaseFigure> arrayList){
		this.willChange();
		for(BaseFigure figure : arrayList){
			figure.setViolated(false);
		}
		this.invalidate();
		this.changed();
	}

	public void clearAll() {
		this.willChange();
		this.basicRemoveAllChildren();
		this.invalidate();
		this.changed();
	}
	
	public void clearAllLines(){
		this.willChange();
		BaseFigure[] lines = getShownLines();
		for(BaseFigure line : lines){
			this.remove(line);
		}
		this.invalidate();
		this.changed();
	}
	
	public void resizeRelationFigures() {
		System.out.println(true);
		RelationFigure[] figures = getShownLines();
		// 1 relation, small
		if (1 == figures.length) {
			figures[0].setLineThickness(1);
		}
		// 2 relations; both small, or one slightly bigger
		else if (figures.length == 2) {
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
		}
		// 3 or more relations; small, big or fat, according to scale
		else if (figures.length >= 3) {
			// max amounts of dependencies
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
}
