package husacct.common.dto;


//Owner: Analyse

public class AnalysisStatisticsDTO extends AbstractDTO {
	public final int totalNrOfPackages;
	public final int totalNrOfClasses;
	public final int totalNrOfLinesOfCode;
	public final int totalNrOfDependencies;
	public final int totalNrOfUmlLinks;
	
	public final int selectionNrOfPackages;
	public final int selectionNrOfClasses;
	public final int selectionNrOfLinesOfCode;
	
	
	public AnalysisStatisticsDTO(int totalNrOfPackages, int totalNrOfClasses, int totalNrOfLinesOfCode
			, int totalNrOfDependencies, int totalNrOfUmlLinks, int selectionNrOfPackages, int selectionNrOfClasses, int selectionNrOfLinesOfCode) {
		super();
		this.totalNrOfPackages = totalNrOfPackages;
		this.totalNrOfClasses = totalNrOfClasses;
		this.totalNrOfLinesOfCode = totalNrOfLinesOfCode;
		this.totalNrOfDependencies = totalNrOfDependencies;
		this.totalNrOfUmlLinks = totalNrOfUmlLinks;
		this.selectionNrOfPackages = selectionNrOfPackages;
		this.selectionNrOfClasses = selectionNrOfClasses;
		this.selectionNrOfLinesOfCode = selectionNrOfLinesOfCode;
	}
	
	public String toString(){
		String result = "";
		result += "totalNrOfPackages: " + totalNrOfPackages + "\n";
		result += "totalNrOfClasses: " + totalNrOfClasses + "\n";
		result += "totalNrOfLinesOfCode: " + totalNrOfLinesOfCode + "\n";
		result += "totalNrOfUmlLinks: " + totalNrOfUmlLinks + "\n";
		result += "totalNrOfDependencies: " + totalNrOfDependencies + "\n";
		result += "selectionNrOfPackages: " + selectionNrOfPackages + "\n";
		result += "selectionNrOfClasses: " + selectionNrOfClasses + "\n";
		result += "selectionNrOfLinesOfCode: " + selectionNrOfLinesOfCode + "\n";
		return result;
	}
}
