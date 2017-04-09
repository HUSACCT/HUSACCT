package husacct.externalinterface;

import java.util.ArrayList;

import husacct.common.dto.AbstractDTO;

/**
 *	Data transfer object to pass the arguments to perform a Software Architecture Compliance Check (SACC) with HUSACCT.
 *	Arguments:
 *  - husacctWorkspaceFile: Refers to a file that contains the definition of the intended architecture (modules, rules, assigned software units, ...) of the project.
 *	- sourceCodePaths: (optional) Overwrites the source code paths in husacctWorkspaceFile 
 *  - importFilePreviousViolations: (Optional) Path of a previous exportFileAllCurrentViolations. 
 * 	  	Based on this input, new violations can be determined.
 *  - exportAllViolations: (Optional) Indicates if an XML document with all current violations should be created.
 *  - exportNewViolations: (Optional) Indicates if an XML document with only the new current violations should be created.
 */
public class SaccCommandDTO extends AbstractDTO {
	private String husacctWorkspaceFile = "";			
	private ArrayList<String> sourceCodePaths = new ArrayList<String>();
	private String importFilePreviousViolations = "";				
	private boolean exportAllViolations = false;
	private boolean exportNewViolations = false;

	public SaccCommandDTO() {
	}
	
    public String getHusacctWorkspaceFile() {
		return husacctWorkspaceFile;
	}

	public void setHusacctWorkspaceFile(String husacctWorkspaceFile) {
		this.husacctWorkspaceFile = husacctWorkspaceFile;
	}

	public ArrayList<String> getSourceCodePaths() {
		return sourceCodePaths;
	}

	public void setSourceCodePaths(ArrayList<String> sourceCodePaths) {
		this.sourceCodePaths = sourceCodePaths;
	}

	public String getImportFilePreviousViolations() {
		return importFilePreviousViolations;
	}

	public void setImportFilePreviousViolations(String importFilePreviousViolations) {
		this.importFilePreviousViolations = importFilePreviousViolations;
	}

	public boolean getExportAllViolations() {
		return exportAllViolations;
	}

	public void setExportAllViolations(boolean exportAllViolations) {
		this.exportAllViolations = exportAllViolations;
	}

	public boolean getExportNewViolations() {
		return exportNewViolations;
	}

	public void setExportNewViolations(boolean exportNewViolations) {
		this.exportNewViolations = exportNewViolations;
	}

	@Override
	public String toString() {
    	
        String representation = "";
        representation += "\n husacctWorkspaceFile: " + husacctWorkspaceFile;
        for (String path : sourceCodePaths) {
        	representation += "\n sourceCodePath: " + path;
        }
        representation += "\n importFilePreviousViolations: " + importFilePreviousViolations;
        representation += "\n exportAllViolations: " + Boolean.toString(exportAllViolations);
        representation += "\n exportNewViolations: " + Boolean.toString(exportNewViolations);
        representation += "\n";
        return representation;
    }
}