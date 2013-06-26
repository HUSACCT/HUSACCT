package husacct.graphics.task;

import husacct.ServiceProvider;
import husacct.graphics.util.DrawingDetail;
import husacct.graphics.util.UserInputListener;

public abstract class DrawingSettingsController implements UserInputListener {
	protected boolean	areSmartLinesOn	= true;
	protected boolean	areDependenciesShown, areViolationsShown,
			areExternalLibrariesShown;
	
	protected String[]	currentPaths	= new String[] {};
	
	public DrawingSettingsController() {
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
	
	protected DrawingDetail getCurrentDrawingDetail() {
		DrawingDetail detail = DrawingDetail.WITHOUT_VIOLATIONS;
		if (areViolationsShown()) detail = DrawingDetail.WITH_VIOLATIONS;
		return detail;
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
	
	@Override
	public void hideDependencies() {
		areDependenciesShown = false;
	}
	
	@Override
	public void hideSmartLines() {
		areSmartLinesOn = false;
	}
	
	@Override
	public void hideViolations() {
		areViolationsShown = false;
	}
	
	public void loadDefaultSettings() {
		showDependencies();
		hideViolations();
		showSmartLines();
	}
	
	public void notifyServiceListeners() {
		ServiceProvider.getInstance().getGraphicsService()
				.notifyServiceListeners();
	}
	
	public void resetCurrentPaths() {
		currentPaths = new String[] {};
	}
	
	public void setCurrentPaths(String[] paths) {
		currentPaths = paths;
	}
	
	@Override
	public void showDependencies() {
		areDependenciesShown = true;
	}
	
	@Override
	public void showSmartLines() {
		areSmartLinesOn = true;
	}
	
	@Override
	public void showViolations() {
		areViolationsShown = true;
	}
	
	@Override
	public void hideLibraries() {
		areExternalLibrariesShown = false;
	}
	
	@Override
	public void showLibraries() {
		areExternalLibrariesShown = true;
	}
}
