package husacct.analyse.task.analyse.csharp;

public class CSharpClassData {

    private String className;
    private String uniqueName;
    private int intentLevel;
    private boolean closed = false;
    private String parentClass;
    private boolean hasParent;
    private String belongsToPackage;
    private boolean isAbstract;

    public CSharpClassData(String className, int intentLevel) {
        this.className = className;
        this.intentLevel = intentLevel;
    }

    public CSharpClassData() {
        super();
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public void setIntentLevel(int intentLevel) {
        this.intentLevel = intentLevel;
    }

    public int getIntentLevel() {
        return intentLevel;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public boolean getClosed() {
        return closed;
    }

    public void setParentClass(String parentClass) {
        this.parentClass = parentClass;
    }

    public String getParentClass() {
        return parentClass;
    }

    public void setHasParent(boolean hasParent) {
        this.hasParent = hasParent;
    }

    public boolean isHasParent() {
        return hasParent;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setBelongsToPackage(String belongsToPackage) {
        this.belongsToPackage = belongsToPackage;
    }

    public String getBelongsToPackage() {
        return belongsToPackage;
    }

    public void setAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

    public boolean isAbstract() {
        return isAbstract;
    }
}
