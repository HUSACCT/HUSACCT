package husacct.graphics.presentation;

import husacct.common.dto.AbstractDTO;
import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.presentation.figures.RelationFigure;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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
	
	// TODO: This doesn't belong here
	// Presentation logic
	public void sizeRelationFigures(HashMap<RelationFigure, ? extends AbstractDTO[]> figures) {
		// 1 relation, small
		if (figures.size() == 1) {
			figures.keySet().iterator().next().setLineThickness(1);
		}
		// 2 relations; both small, or one slightly bigger
		else if (figures.size() == 2) {
			Iterator<RelationFigure> iterator = figures.keySet().iterator();
			RelationFigure figure1 = iterator.next();
			RelationFigure figure2 = iterator.next();
			int length1 = figures.get(figure1).length;
			int length2 = figures.get(figure2).length;

			if (length1 == length2) {
				figure1.setLineThickness(1);
				figure2.setLineThickness(1);
			} else if (length1 < length2) {
				figure1.setLineThickness(1);
				figure2.setLineThickness(2);
			} else { // length1 > length2
				figure1.setLineThickness(2);
				figure2.setLineThickness(1);
			}
		}
		// 3 ore more relations; small, big or fat, according to scale
		else if (figures.size() >= 3) {
			// max amounts of dependencies
			int maxAmount = -1;
			for (RelationFigure fig : figures.keySet()) {
				int length = figures.get(fig).length;

				if (maxAmount == -1 || maxAmount < length) {
					maxAmount = length;
				}
			}

			// set line thickness according to scale
			for (RelationFigure fig : figures.keySet()) {
				double weight = (double) figures.get(fig).length / maxAmount;
				if (weight < 0.33) {
					fig.setLineThickness(1);
				} else if (weight < 0.66) {
					fig.setLineThickness(3);
				} else {
					fig.setLineThickness(4);
				}
			}
		}
	}
}
