package husacct.define.task.components;

import java.util.ArrayList;

public abstract class AbstractDefineComponent extends AbstractCombinedComponent {

    private static final long serialVersionUID = -8598001531867139865L;
    protected long moduleId;

    public AbstractDefineComponent() {
	super();
    }

    @Override
    public abstract void addChild(AbstractCombinedComponent child);

    @Override
    public abstract void addChild(int index, AbstractCombinedComponent child);

    @Override
    public abstract ArrayList<AbstractCombinedComponent> getChildren();

    public abstract long getModuleId();

    @Override
    public abstract void removeChild(AbstractCombinedComponent child);

    @Override
    public abstract void setChildren(
	    ArrayList<AbstractCombinedComponent> children);

    public abstract void setModuleId(long moduleId);
}
