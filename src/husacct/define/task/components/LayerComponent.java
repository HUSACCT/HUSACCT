package husacct.define.task.components;

import java.awt.Component;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class LayerComponent extends AbstractDefineComponent {

	private static final long serialVersionUID = 47794294423459128L;
	private long hierarchicalLevel;
	
	public LayerComponent() {
		super();
//		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("husacct/define/presentation/resources/jframeicon.jpg"));
//		this.prepareImage(icon.getImage(), icon.getImageObserver());
//		//this.imageUpdate(, 1, 0, 0, 20, 20);
	}

	public long getHierarchicalLevel() {
		return hierarchicalLevel;
	}

	public void setHierarchicalLevel(long hierarchicalLevel) {
		this.hierarchicalLevel = hierarchicalLevel;
	}

	@Override
	public void addChild(AbstractDefineComponent child) {
		this.children.add(child);
	}
	
	@Override
	public void addChild(int index, AbstractDefineComponent child) {
		this.children.add(index, child);
	}

	@Override
	public void setChildren(ArrayList<AbstractDefineComponent> children) {
		this.children = children;
	}

	@Override
	public ArrayList<AbstractDefineComponent> getChildren() {
		return this.children;
	}

	@Override
	public void removeChild(AbstractDefineComponent child) {
		for(Component currentchild : this.children) {
			if(currentchild.equals(child)) {
				this.children.remove(currentchild);
			}
		}
	}
}
