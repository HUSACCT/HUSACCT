package husacct.analyse.task.analyser.csharp;

public class CSharpInstanceData {
	private String instanceName;
	private String belongsToClass;
	private String to;
	private boolean hasMethodScope;
	
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
	
	public void setHasMethodScope(boolean hasMethodScope){
		this.hasMethodScope = hasMethodScope;
	}
	
	public boolean getHasMethodScope(){
		return hasMethodScope;
	}
	
	public boolean instanceNameEquals(String instanceName){
		if(this.instanceName.equals(instanceName)){
			return true;
		}
	return false;
	}
}
