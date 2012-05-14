package husacct.graphics.presentation.figures;

public abstract class NamedFigure extends BaseFigure {
	private static final long serialVersionUID = -2766408396802981598L;
	private String name;

	public NamedFigure(String name) {
		super();

		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}
