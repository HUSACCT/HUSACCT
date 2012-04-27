package husacct.define.task.components;

import java.awt.Component;
import java.util.ArrayList;

public abstract class AbstractDefineComponent extends Component {

	private static final long serialVersionUID = -8598001531867139865L;
	protected ArrayList<AbstractDefineComponent> children;
	
	public AbstractDefineComponent() {
		super();
		this.children = new ArrayList<AbstractDefineComponent>();
	}
	
	public abstract void addChild(AbstractDefineComponent child);
	public abstract void addChild(int index, AbstractDefineComponent child);
	public abstract void setChildren(ArrayList<AbstractDefineComponent> children);
	public abstract ArrayList<AbstractDefineComponent> getChildren();
	public abstract void removeChild(AbstractDefineComponent child);
}
