package husacct.graphics.task.layout;

public class NoLayoutStrategy implements LayoutStrategy {

	public NoLayoutStrategy() {
	}

	@Override
	public void doLayout() {
		// Do nothing as this layout strategy is used when we want no
		// automatic layouting done.
	}

}
