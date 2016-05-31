package husacct.graphics.task;

import husacct.common.enums.DependencyOptionType;

public class DrawingSettingsHolder {
	private boolean	areDependenciesShown = false;
	private boolean	areExternalLibrariesShown = false;
	private boolean	areLinesProportionalWide = false;
	private boolean	areSmartLinesOn	= false;
	private boolean	areViolationsShown = false;
	private boolean	isZoomWithContextOn	= false;
	private DependencyOptionType selectedDependencyOption = DependencyOptionType.ALL_DEPENDENCY;


	private String[]	currentPaths	= new String[] {};
	
	public DrawingSettingsHolder() {
	}
	
	public boolean areDependenciesShown() {
		return areDependenciesShown;
	}
	
	public boolean areSmartLinesOn() {
		return areSmartLinesOn;
	}
	
	public boolean areViolationsShown() {
		return areViolationsShown;
	}
	
	public boolean areExternalLibrariesShown() {
		return areExternalLibrariesShown;
	}
	
	public boolean areLinesProportionalWide(){
		return areLinesProportionalWide;
	}
	
	public void dependenciesHide() {
		areDependenciesShown = false;
	}
	
	public void dependenciesShow() {
		areDependenciesShown = true;
	}
	
	public String[] getCurrentPaths() {
		return currentPaths;
	}
	
	public String getCurrentPathsToString() {
		String stringPaths = "";
		for (String path : getCurrentPaths())
			stringPaths += path + " + ";
		return stringPaths;
	}
	
	public void smartLinesDisable() {
		areSmartLinesOn = false;
	}
	
	public void violationsHide() {
		areViolationsShown = false;
	}
	
	public void resetCurrentPaths() {
		currentPaths = new String[] {};
	}
	
	public void setCurrentPaths(String[] paths) {
		currentPaths = paths;
	}
	
	public void smartLinesEnable() {
		areSmartLinesOn = true;
	}
	
	public void violationsShow() {
		areViolationsShown = true;
	}
	
	public void librariesHide() {
		areExternalLibrariesShown = false;
	}
	
	public void librariesShow() {
		areExternalLibrariesShown = true;
	}
	
	public void proportionalLinesEnable(){
		areLinesProportionalWide = true;
	}
	
	public void proportionalLinesDisable(){
		areLinesProportionalWide = false;
	}
	
	public void zoomTypeChange(String zoomType){
		if(zoomType.equals("zoom"))
			isZoomWithContextOn = false;
		if(zoomType.equals("context"))
			isZoomWithContextOn = true;
	}
	
	public boolean isZoomWithContextOn(){
		return isZoomWithContextOn;
	}

	public DependencyOptionType getSelectedDependencyOption() {
		return selectedDependencyOption;
	}

	public void setSelectedDependencyOption(DependencyOptionType selectedDependencyOption) {
		this.selectedDependencyOption = selectedDependencyOption;
	}
}
