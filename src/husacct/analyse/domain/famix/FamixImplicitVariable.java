package husacct.analyse.domain.famix;

public class FamixImplicitVariable extends FamixStructuralEntity
{
	
	private String belongsToContext;

	public String getBelongsToContext()
	{
		return belongsToContext;
	}

	public void setBelongsToContext(String belongsToContext)
	{
		this.belongsToContext = belongsToContext;
	}

}
