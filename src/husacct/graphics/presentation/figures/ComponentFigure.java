package husacct.graphics.presentation.figures;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.RectangleFigure;
import org.jhotdraw.draw.TextFigure;

public class ComponentFigure extends ModuleFigure
{

	private static final long serialVersionUID = -344477017055924400L;
	
	public static final int SPACE_LEFT = 34;
	public static final int COMP_HEIGHT = 24;
	public static final int COMP_DISTANCE = 8;
	
	private RectangleFigure mainBox;
	private RectangleFigure box1;
	private RectangleFigure box2;
	private TextFigure textBox;
	
	public ComponentFigure(Rectangle2D.Double rect, String name)
	{		
		super(rect, name);
		
	}
	
	@Override
	protected void initializeComponents() {
		this.mainBox = new RectangleFigure();
		this.box1 = new RectangleFigure();
		this.box2 = new RectangleFigure();
		this.textBox = new TextFigure(name);
		
		this.children.add(mainBox);
		this.children.add(box1);
		this.children.add(box2);
		this.children.add(textBox);
		
		this.sizeChildBoxes(anchor, lead);		
	}
	
	private void sizeChildBoxes(Point2D.Double anchor, Point2D.Double lead)
	{
        if((lead.x - anchor.x) < getMinimumWidth())
        {
        	lead.x = anchor.x + getMinimumWidth(); 
        }
        
        if((lead.y - anchor.y) < getMinimumHeight())
        {
        	lead.y = anchor.y + getMinimumHeight();
        }
        
        Point2D.Double newMainBoxAnchor = (Point2D.Double)anchor.clone();
        newMainBoxAnchor.x += SPACE_LEFT;
		this.mainBox.setBounds(newMainBoxAnchor, lead);
		
		Point2D.Double newBox1Anchor = (Point2D.Double)anchor.clone();
		newBox1Anchor.y += COMP_DISTANCE;
		Point2D.Double newBox1Lead = new Point2D.Double(newBox1Anchor.x+SPACE_LEFT*2, newBox1Anchor.y+COMP_HEIGHT);
		this.box1.setBounds(newBox1Anchor, newBox1Lead);
		
		Point2D.Double newBox2Anchor = (Point2D.Double)newBox1Anchor.clone();
		newBox2Anchor.y += COMP_HEIGHT+COMP_DISTANCE;
		Point2D.Double newBox2Lead = new Point2D.Double(newBox2Anchor.x+SPACE_LEFT*2, newBox2Anchor.y+COMP_HEIGHT);
		this.box2.setBounds(newBox2Anchor, newBox2Lead);
		
		Point2D.Double textBoxOrigin = (Point2D.Double) newBox1Anchor.clone();
		textBoxOrigin.x += SPACE_LEFT*2+8;
		this.textBox.setBounds(textBoxOrigin, null);
	}
	
	public static int getMinimumHeight()
	{
		return COMP_DISTANCE+COMP_HEIGHT+COMP_DISTANCE+COMP_HEIGHT+COMP_DISTANCE;
	}
	
	public static int getMinimumWidth()
	{
		return SPACE_LEFT*4;
	}
    
    @Override
    public void setBounds(Point2D.Double anchor, Point2D.Double lead)
    {    	
        this.sizeChildBoxes(anchor, lead);
        
        invalidate();
    }
       
    @Override
    public ComponentFigure clone()
    {
    	ComponentFigure that = (ComponentFigure) super.clone();
        that.mainBox = this.mainBox.clone();
        that.box1 = this.box1.clone();
        that.box2 = this.box2.clone();
        that.textBox = this.textBox.clone();
        that.children = new ArrayList<Figure>();
        that.children.add(that.mainBox);
        that.children.add(that.box1);
        that.children.add(that.box2);
        that.children.add(that.textBox);
        return that;
    }
}


