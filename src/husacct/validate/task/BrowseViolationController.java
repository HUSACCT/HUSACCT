package husacct.validate.task;

import husacct.validate.abstraction.language.ResourceBundles;
import husacct.validate.domain.violation.Violation;
import husacct.validate.task.filter.LogicalPathsViolation;

import java.util.ArrayList;
import java.util.List;

public class BrowseViolationController {
	public LogicalPathsViolation lpv;
	private ArrayList<String> ruletypes = new ArrayList<String>();
	private ArrayList<String> violationtypes = new ArrayList<String>();
	private ArrayList<String> paths = new ArrayList<String>();
	private List<Violation> violations;
	private int totalViolations;
	private boolean hidefilter = true;

	public BrowseViolationController(List<Violation> violations) {
		lpv = new LogicalPathsViolation(this);
		this.violations = violations;
		totalViolations = violations.size();
	}

	public ArrayList<Object[]> loadViolations(Boolean applyfilter) {
		ArrayList<Object[]> violationRows = new ArrayList<Object[]>();

		for (Violation violation : violations) {
			if (hidefilter) {
				if (!applyfilter || !ruletypes.contains(ResourceBundles.getValue(violation.getRuletypeKey())) && !violationtypes.contains(ResourceBundles.getValue(violation.getViolationtypeKey())) && !paths.contains(violation.getLogicalModuleFrom())) {
					violationRows.add(new Object[]{violation.getLogicalModuleFrom(), violation.getClassPathFrom(), violation.getClassPathTo(), violation.getLinenumber(), violation.getSeverityValue(), ResourceBundles.getValue(violation.getRuletypeKey()), ResourceBundles.getValue(violation.getViolationtypeKey())});
				}
			} else {
				if (!applyfilter || ruletypes.contains(ResourceBundles.getValue(violation.getRuletypeKey())) || violationtypes.contains(ResourceBundles.getValue(violation.getViolationtypeKey())) || paths.contains(violation.getLogicalModuleFrom())) {
					violationRows.add(new Object[]{violation.getLogicalModuleFrom(), violation.getClassPathFrom(), violation.getClassPathTo(), violation.getLinenumber(), violation.getSeverityValue(), ResourceBundles.getValue(violation.getRuletypeKey()), ResourceBundles.getValue(violation.getViolationtypeKey())});
				}
			}
		}

		return violationRows;
	}

	public void setPaths(ArrayList<String> paths) {
		this.paths = paths;
	}

	public void setRuletypes(ArrayList<String> ruletypes) {
		this.ruletypes = ruletypes;
	}

	public void setViolationtypes(ArrayList<String> violationtypes) {
		this.violationtypes = violationtypes;
	}

	public void setHidefilter(boolean hidefilter) {
		this.hidefilter = hidefilter;
	}

	public int getTotalViolations() {
		return totalViolations;
	}
}
