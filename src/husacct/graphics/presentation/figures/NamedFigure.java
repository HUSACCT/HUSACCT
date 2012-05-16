package husacct.graphics.presentation.figures;

public abstract class NamedFigure extends BaseFigure {
	private static final long serialVersionUID = -2766408396802981598L;
	private String name;

	public NamedFigure(String figureName) {
		super();

		name = figureName;
	}

	public String getName() {
		return name;
	}
}
