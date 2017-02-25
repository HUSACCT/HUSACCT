package husacct.graphics.domain.figures;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.Collection;

import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.ConnectionFigure;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.LineConnectionFigure;
import org.jhotdraw.draw.TextFigure;
import org.jhotdraw.draw.connector.Connector;
import org.jhotdraw.draw.decoration.ArrowTip;
import org.jhotdraw.draw.event.FigureEvent;
import org.jhotdraw.draw.event.FigureListener;
import org.jhotdraw.draw.handle.BezierNodeHandle;
import org.jhotdraw.draw.handle.BezierOutlineHandle;
import org.jhotdraw.draw.handle.Handle;
import org.jhotdraw.draw.liner.Liner;
import org.jhotdraw.geom.BezierPath.Node;

public class RelationFigure extends BaseFigure implements ConnectionFigure,
		FigureListener {
	private static final long		serialVersionUID	= 1805821357919823648L;
	private LineConnectionFigure	line;
	private RelationType			relationType;
	private TextFigure				amountFigure;
	private TextFigure 				fromMultiplicity;
	private TextFigure 				toMultiplicity;
	private boolean					composite = false;
	private boolean					isUmlLink = false;

	
//	private boolean					violationRelation = false;
	
	public RelationFigure(String name, RelationType relationType, String amount) {
		super(name, name, "relation");
		this.relationType = relationType;
		
		line = new LineConnectionFigure();
		add(line);
		fromMultiplicity = new TextFigure();
		toMultiplicity = new TextFigure();
		amountFigure = new TextFigure(amount);
		add(amountFigure);
		
		line.addFigureListener(this);
	}

	public void setMultiplicity() {
		this.isUmlLink = true;
		this.fromMultiplicity.setText(composite ? "*" : "1");
	}

	public void setAmount(String amount){
		this.amountFigure.setText(amount);
	}

	@Override
	public void areaInvalidated(FigureEvent e) {
		relayout();
		relayoutMultiplicities();
	}
	
	@Override
	public void attributeChanged(FigureEvent e) {
	}
	
	@Override
	public boolean canConnect(Connector start) {
		return line.canConnect(start);
	}
	
	@Override
	public boolean canConnect(Connector start, Connector end) {
		return line.canConnect(start, end);
	}
	
	@Override
	public void changed() {
		line.changed();
		amountFigure.changed();
		super.changed();
	}
	
	@Override
	public RelationFigure clone() {
		RelationFigure other = (RelationFigure) super.clone();
		other.children = new ArrayList<Figure>();
		other.line = line.clone();
		other.children.add(other.line);
		other.amountFigure = amountFigure.clone();
		other.children.add(other.amountFigure);
		
		return other;
	}
	
	@Override
	public Collection<Handle> createHandles(int detailLevel) {
		ArrayList<Handle> handles = new ArrayList<Handle>(getNodeCount());
		switch (detailLevel) {
			case -1:
				handles.add(new BezierOutlineHandle(line, true));
				break;
			case 0:
				handles.add(new BezierOutlineHandle(line));
				if (getLiner() == null) for (int i = 1, n = getNodeCount() - 1; i < n; i++)
					handles.add(new BezierNodeHandle(line, i));
				break;
		}
		return handles;
	}
	
	@Override
	public void draw(Graphics2D graphics) {
		ArrowTipFix arrowTip = new ArrowTipFix(0.5, 7, 3.0);
		set(AttributeKeys.END_DECORATION, arrowTip);

		ModuleFigure startFigure = (ModuleFigure )line.getStartFigure();
		ModuleFigure endFigure = (ModuleFigure )line.getEndFigure();
		String startFigureType = startFigure.getType();
		String endFigureType = endFigure.getType();

		if(isUmlLink) {
			if (isPackageType(startFigureType, endFigureType)) {
				relationType = RelationType.ATTRIBUTELINK;
			} else if (relationType == RelationType.ATTRIBUTELINK){
				addMultiplicity();
				removeAmountFigure();
			} else {
				removeAmountFigure();
			}
		}

		switch(relationType) {
		case DEPENDENCY:
			double dashes = 4.0 / this.get(AttributeKeys.STROKE_WIDTH);
			set(AttributeKeys.STROKE_DASHES, new double[] { 6.0, dashes });
			break;
		case VIOLATION:
			set(AttributeKeys.STROKE_DASHES, new double[] { 2.0, 2.0 });
			break;
		case ATTRIBUTELINK:
			// default is a straight line, so do nothing
			break;
		case INHERITANCELINK:
			arrowTip = new ArrowTipFix(0.5, 13.0, 11.5, false, true, false);
			set(AttributeKeys.END_DECORATION, arrowTip);
			break;
		case IMPLEMENTSLINK:
			set(AttributeKeys.STROKE_DASHES, new double[] { 4.0, 4.0 });
			arrowTip = new ArrowTipFix(0.5, 13.0, 11.5, false, true, true);
			set(AttributeKeys.END_DECORATION, arrowTip);
			break;
		case RULELINK:
			// default is a straight line, so do nothing
			break;
		default: 
			throw new IllegalStateException("Unknown relation type");
		}
		super.draw(graphics);
	}

	private void addMultiplicity() {
		add(this.fromMultiplicity);
//		add(this.toMultiplicity);
	}

	private void removeAmountFigure() {
		remove(this.amountFigure);
	}

	private boolean isPackageType(String startFigureType, String endFigureType) {
		return endFigureType.equals("package") || startFigureType.equals("package");
	}

	@Override
	public void figureAdded(FigureEvent e) {
	}
	
	@Override
	public void figureChanged(FigureEvent e) {
	}
	
	@Override
	public void figureHandlesChanged(FigureEvent e) {
	}
	
	@Override
	public void figureRemoved(FigureEvent e) {
	}
	
	@Override
	public void figureRequestRemove(FigureEvent e) {
	}
	
	public int getAmount() {
		String amountString = amountFigure.getText();
		if (amountString.contains("/")){
			String dependencyString = amountString.substring(amountString.indexOf("/") + 1);
			return Integer.parseInt(dependencyString);

		} else {
			return Integer.parseInt(amountFigure.getText());
		}
	}
	
	@Override
	public Connector getEndConnector() {
		return line.getEndConnector();
	}
	
	@Override
	public Figure getEndFigure() {
		return line.getEndFigure();
	}
	
	@Override
	public Liner getLiner() {
		return line.getLiner();
	}
	
	@Override
	public Node getNode(int index) {
		return line.getNode(index);
	}
	
	@Override
	public int getNodeCount() {
		return line.getNodeCount();
	}
	
	@Override
	public Double getPoint(int index) {
		return line.getPoint(index);
	}
	
	@Override
	public Connector getStartConnector() {
		return line.getStartConnector();
	}
	
	@Override
	public Figure getStartFigure() {
		return line.getStartFigure();
	}
	
	@Override
	public boolean isLine() {
		return true;
	}
	
	public RelationType getRelationType() {
		return relationType;
	}

	@Override
	public void lineout() {
		line.lineout();
	}
	
	private void relayout() {
		double midX = line.getBounds().x + line.getBounds().width / 2;
		double midY = line.getBounds().y + line.getBounds().height / 2;
		
		amountFigure.willChange();
		amountFigure.setBounds(new Point2D.Double(midX, midY), null);
		amountFigure.changed();
	}
	
	public void relayoutMultiplicities(){
		double toFigureX = line.getStartPoint().x;
		double toFigureY = line.getStartPoint().y;
		
		double fromFigureX = line.getEndPoint().x;
		double fromFigureY = line.getEndPoint().y;
		
		double offsetX = toFigureX < fromFigureX ? 15 : -15;
		double offsetY = toFigureY < fromFigureY ? 15 : -15;
		
		toFigureX += offsetX;
		toFigureY += offsetY;
		fromFigureX -= offsetX;
		fromFigureY -= offsetY;
		
		fromMultiplicity.willChange();
		fromMultiplicity.setBounds(new Point2D.Double(fromFigureX, fromFigureY), null);
		fromMultiplicity.changed();
		
		toMultiplicity.willChange();
		toMultiplicity.setBounds(new Point2D.Double(toFigureX, toFigureY), null);
		toMultiplicity.changed();
	}
	
	@Override
	public void setBounds(Point2D.Double anchor, Point2D.Double lead) {
		line.updateConnection();
		relayout();
		relayoutMultiplicities();
	}
	
	@Override
	public void setEndConnector(Connector end) {
		line.setEndConnector(end);
	}
	
	@Override
	public void setEndPoint(Double p) {
		line.setEndPoint(p);
	}
	
	public void setLineColor(Color newColor) {
		set(AttributeKeys.STROKE_COLOR, newColor);
		amountFigure.set(AttributeKeys.TEXT_COLOR, newColor);
	}
	
	@Override
	public void setLiner(Liner newValue) {
		willChange();
		line.setLiner(newValue);
		changed();
	}
	
	public void setLineThickness(double thickness) {
		set(AttributeKeys.STROKE_WIDTH, thickness);
	}
	
	@Override
	public void setNode(int index, Node node) {
		line.setNode(index, node);
	}
	
	@Override
	public void setPoint(int index, Double p) {
		line.setPoint(index, p);
	}
	
	@Override
	public void setStartConnector(Connector start) {
		line.setStartConnector(start);
	}
	
	@Override
	public void setStartPoint(Double p) {
		line.setStartPoint(p);
	}
	
	@Override
	public void transform(AffineTransform tx) {
		line.updateConnection();
		relayout();
		relayoutMultiplicities();
	}
	
	@Override
	public void updateConnection() {
		line.updateConnection();
		relayout();
		relayoutMultiplicities();
	}
	
	@Override
	public void willChange() {
		line.willChange();
		amountFigure.willChange();
		super.willChange();
	}

	public void setComposite(boolean composite) {
		this.composite = composite;
	}

	public void setIsUmlLink(boolean isUmlLink) {
		this.isUmlLink = isUmlLink;
	}
}
