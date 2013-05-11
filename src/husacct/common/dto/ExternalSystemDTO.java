package husacct.common.dto;

import java.util.ArrayList;

public class ExternalSystemDTO extends AbstractDTO {
	public String systemName;
	public String systemPackage;
	public String description;
	public ArrayList<DependencyDTO> fromDependencies;
	
	public ExternalSystemDTO(){
		fromDependencies = new ArrayList<DependencyDTO>();
	}
	
	public ExternalSystemDTO(String name, String pckage){
		this.systemName = name;
		this.systemPackage = pckage;
		fromDependencies = new ArrayList<DependencyDTO>();
	}
	
	public boolean addDependency(DependencyDTO dependency){
		if(!fromDependencies.contains(dependency)){
			fromDependencies.add(dependency);
			return true;
		}
		return false;
	}
	
	public int getDependencyCount(){
		return fromDependencies.size();
	}
	
	public boolean equals(ExternalSystemDTO other){
		if(this.systemName.equals(other.systemName) && this.systemPackage.equals(other.systemPackage))
			return true;
		return false;
	}
	
	public String toString(){		
		return this.systemName + " from package (" + this.systemPackage + ") was found in " + getDependencyCount() + " dependencies.";
	}
}
