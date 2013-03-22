package husacct.define.task.components;

import java.util.ArrayList;

public abstract class AbstractDefineComponent extends AbstractCombinedComponent {

    private static final long serialVersionUID = -8598001531867139865L;
    protected long moduleId;

    public AbstractDefineComponent() {
        super();
    }

    public abstract void addChild(AbstractCombinedComponent child);

    public abstract void addChild(int index, AbstractCombinedComponent child);

    public abstract void setChildren(ArrayList<AbstractCombinedComponent> children);

    public abstract ArrayList<AbstractCombinedComponent> getChildren();

    public abstract void removeChild(AbstractCombinedComponent child);

    public abstract void setModuleId(long moduleId);

    public abstract long getModuleId();
}
