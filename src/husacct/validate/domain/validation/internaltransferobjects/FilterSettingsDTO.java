package husacct.validate.domain.validation.internaltransferobjects;

import java.util.ArrayList;

public class FilterSettingsDTO {

	private ArrayList<String> ruletypes;
	private ArrayList<String> violationtypes;
	private ArrayList<String> paths;

	public FilterSettingsDTO(ArrayList<String> ruletypes, ArrayList<String> violationtypes, ArrayList<String> paths) {
		this.ruletypes = ruletypes;
		this.violationtypes = violationtypes;
		this.paths = paths;
	}

	public ArrayList<String> getRuletypes() {
		return ruletypes;
	}

	public void setRuletypes(ArrayList<String> ruletypes) {
		this.ruletypes = ruletypes;
	}

	public ArrayList<String> getViolationtypes() {
		return violationtypes;
	}

	public void setViolationtypes(ArrayList<String> violationtypes) {
		this.violationtypes = violationtypes;
	}

	public ArrayList<String> getPaths() {
		return paths;
	}

	public void setPaths(ArrayList<String> paths) {
		this.paths = paths;
	}
}
