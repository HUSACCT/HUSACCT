package husacct.analyse.task.analyser.csharp;

public class CSharpInstanceData {
	private String instanceName;
	private String belongsToClass;
	private String to;
	private boolean hasClassScope;
	
	public void setInstanceName(String instanceName){
		this.instanceName = instanceName;
	}
	
	public String getInstanceName(){
		return instanceName;
	}
	
	public void setBelongsToClass(String belongsToClass){
		this.belongsToClass = belongsToClass;
	}
	
	public String getBelongsToClass(){
		return belongsToClass;
	}
	
	public void setTo(String to){
		this.to = to;
	}
	
	public String getTo(){
		return to;
	}
	
	public void setClassScope(boolean hasClassScope){
		this.hasClassScope = hasClassScope;
	}
	
	public boolean getHasClassScope(){
		return hasClassScope;
	}
	
	public boolean instanceEquals(String instanceName){
		if(this.instanceName.equals(instanceName)){
			return true;
		}
	return false;
	}
}
