package husacct.analyse.task.analyser.csharp;

import java.util.ArrayList;

public class CSharpData {
	private String className;
	private String uniqueName;
	private int intentLevel;
	private boolean closed = false;
	private String parentClass;
	private boolean hasParent;
	private String belongsToPackage;
	private boolean isAbstract;
	private String inheritanceTo;
	public String inheritanceFrom;
	private ArrayList<String> moreInherit = new ArrayList<String>();
	
	public CSharpData(String className, int intentLevel, String belongsToPackage) {
		this.className = className;
		this.intentLevel = intentLevel;
		this.belongsToPackage = belongsToPackage;
	}
	
	public CSharpData() {
		super();
	}

	public CSharpData(String tempNamespaceName, int indentLevel) {
		this.className = tempNamespaceName;
		this.intentLevel = indentLevel;
	}

	public ArrayList<String> getInherits()
	{
		return moreInherit;
	}
	
	public void addInherit(String to)
	{
		moreInherit.add(to);
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

	public String getInheritanceFrom() {
		return inheritanceFrom;
	}

	public void setInheritanceFrom(String inheritanceClass) {
		this.inheritanceFrom = inheritanceClass;
	}

	public String getInheritanceTo() {
		return inheritanceTo;
	}

	public void setInheritanceTo(String inheritanceTo) {
		this.inheritanceTo = inheritanceTo;
	}
	
}
