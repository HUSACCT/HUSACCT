package husacct.define.task.components;

import java.util.ArrayList;

public class AnalyzedModuleComponent extends AbstractCombinedComponent {

    private static final long serialVersionUID = 1713515026043620607L;
    private String uniqueName = "";
    private String type = "";
    private String visibility;

    public AnalyzedModuleComponent() {
        super();
    }

    public AnalyzedModuleComponent(String uniqueName, String name, String type, String visibility) {
        this();
        this.setUniqueName(uniqueName);
        this.setName(name);
        this.setType(type);
        this.setVisibility(visibility);
    }

    public void addChild(AbstractCombinedComponent child) {
        this.children.add(child);
    }

    public void addChild(int index, AbstractCombinedComponent child) {
        this.children.add(index, child);
    }

    public void setChildren(ArrayList<AbstractCombinedComponent> children) {
        this.children = children;
    }

    public ArrayList<AbstractCombinedComponent> getChildren() {
        return this.children;
    }

    public void removeChild(AbstractCombinedComponent child) {
        for (AbstractCombinedComponent currentchild : this.children) {
            if (currentchild.equals(child)) {
                this.children.remove(currentchild);
            }
        }
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public String getUniqueName() {
        return this.uniqueName;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type.toUpperCase();
    }

    public String getVisibility() {
        return this.visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }
}
