package husaccttest.analyse;

import java.util.ArrayList;
import husacct.common.dto.DependencyDTO;

public class TestObject {

	private String from;
	private ArrayList<DependencyDTO> dependencies;
	
	private String error = "";
	
	public TestObject(String from){
		this.from = from;
		this.dependencies = new ArrayList<DependencyDTO>();
	}
	
	public void addDependency(DependencyDTO dependency){
		if(dependency.from.equals("")){
			dependency.from = this.from;
		}
		
		dependencies.add(dependency);
	}
	
	public ArrayList<DependencyDTO> getDependencies(){
		return dependencies;
	}
	
	public String getFrom(){
		return this.from;
	}

	public String getLastError(){
		return this.error;
	}
	
	public void setError(String error){
		if(this.error.equals("")){
			this.error = error;
		}
	}
	
}
