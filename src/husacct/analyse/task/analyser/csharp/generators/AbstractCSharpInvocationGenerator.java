package husacct.analyse.task.analyser.csharp.generators;


public abstract class AbstractCSharpInvocationGenerator extends CSharpGenerator {
	protected String from = "";
	protected String to = "";
	protected int lineNumber;
	protected String invocationName = "";
	protected String belongsToMethod = "";
	protected String nameOfInstance = "";
	
	public AbstractCSharpInvocationGenerator(String packageAndClassName) {
		from = packageAndClassName;
	}
	
	abstract void saveInvocationToDomain();
}
