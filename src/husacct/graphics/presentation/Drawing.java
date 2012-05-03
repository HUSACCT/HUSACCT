package husacct.graphics.presentation;

import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.presentation.figures.RelationFigure;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	
	public void clearViolationLines(){
		this.willChange();
		BaseFigure[] lines = getShownLines();
		for(BaseFigure line : lines){
			if(line.isViolated()){
				this.remove(line);
			}
		}
		this.invalidate();
		this.changed();
	}
}
