package husacct.validate;

import javax.swing.JInternalFrame;

import husacct.common.dto.CategoryDTO;
import husacct.common.dto.ViolationDTO;

public interface IValidateService 
{
	public CategoryDTO[] getCategories();
	public ViolationDTO[] getViolations(String logicalpathFrom, String logicalpathTo);
	public String[] getExportExtentions();
	public void checkConformance();
	public void exportViolations(String name, String fileType, String path);
	public JInternalFrame getBrowseViolationsGUI();
}