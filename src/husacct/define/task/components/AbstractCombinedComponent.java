package husacct.define.task.components;

import java.awt.Component;
import java.util.ArrayList;

public abstract class AbstractCombinedComponent extends Component {

    private static final long serialVersionUID = 1L;
    protected ArrayList<AbstractCombinedComponent> children;

    public AbstractCombinedComponent() {
        super();
        this.children = new ArrayList<AbstractCombinedComponent>();
    }

    public abstract void addChild(AbstractCombinedComponent child);

    public abstract void addChild(int index, AbstractCombinedComponent child);

    public abstract void setChildren(ArrayList<AbstractCombinedComponent> children);

    public abstract ArrayList<AbstractCombinedComponent> getChildren();

    public abstract void removeChild(AbstractCombinedComponent child);
}
